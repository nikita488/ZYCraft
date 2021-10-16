package nikita488.zycraft.client.model;

import com.google.gson.*;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.model.*;
import net.minecraftforge.client.model.geometry.IModelGeometry;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

public class FluidContainerModel implements IModelGeometry<FluidContainerModel>
{
    private final BlockModel model;
    private final ObjectList<String> fluidTextures;
    @Nullable
    private final ResourceLocation replacementTexture;

    public FluidContainerModel(BlockModel model, ObjectList<String> fluidTextures)
    {
        this(model, fluidTextures, null);
    }

    public FluidContainerModel(BlockModel model, ObjectList<String> fluidTextures, @Nullable ResourceLocation replacementTexture)
    {
        this.model = model;
        this.fluidTextures = fluidTextures;
        this.replacementTexture = replacementTexture;
    }

    public FluidContainerModel wrapModel(BlockModel model, ResourceLocation replacementTexture)
    {
        return new FluidContainerModel(model, fluidTextures, replacementTexture);
    }

    @Override
    public BakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState transform, ItemOverrides overrides, ResourceLocation location)
    {
        if (replacementTexture == null)
            return new Baked(model.bake(bakery, model, spriteGetter, transform, location, true), new ItemOverrides(this, bakery, model, model.getOverrides()));

        Map<String, Either<Material, String>> textures = new HashMap<>(model.textureMap);

        for (String textureName : fluidTextures)
            if (model.hasTexture(textureName))
                textures.put(textureName, Either.left(ModelLoaderRegistry.blockMaterial(replacementTexture)));

        BlockModel wrappedModel = new BlockModel(null, Collections.emptyList(), textures, false, null, ItemTransforms.NO_TRANSFORMS, Collections.emptyList());

        wrappedModel.parent = model;
        return wrappedModel.bake(bakery, wrappedModel, spriteGetter, transform, location, true);
    }

    @Override
    public Collection<Material> getTextures(IModelConfiguration owner, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors)
    {
        return model.getMaterials(modelGetter, missingTextureErrors);
    }

    private static class Baked extends BakedModelWrapper<BakedModel>
    {
        private final ItemOverrides overrides;

        public Baked(BakedModel model, ItemOverrides overrides)
        {
            super(model);
            this.overrides = overrides;
        }

        @Override
        public ItemOverrides getOverrides()
        {
            return overrides;
        }
    }

    public static class Loader implements IModelLoader<FluidContainerModel>
    {
        public static final Loader INSTANCE = new Loader();

        @Override
        public void onResourceManagerReload(ResourceManager manager) {}

        @Override
        public FluidContainerModel read(JsonDeserializationContext ctx, JsonObject data)
        {
            BlockModel model = GsonHelper.getAsObject(data, "model", ctx, BlockModel.class);
            JsonArray array = GsonHelper.getAsJsonArray(data, "fluid_textures");

            if (array.size() == 0)
                throw new JsonParseException("Expected at least 1 fluid texture, got 0");

            ObjectList<String> fluidTextures = new ObjectArrayList<>();

            for (JsonElement element : array)
                fluidTextures.add(GsonHelper.convertToString(element, "texture"));

            return new FluidContainerModel(model, fluidTextures);
        }
    }

    private static class ItemOverrides extends ItemOverrides
    {
        private final FluidContainerModel parent;
        private final ModelBakery bakery;
        private final UnbakedModel emptyModel;
        private final ItemOverride[] overrides;
        private final Object2ObjectMap<ResourceLocation, BakedModel[]> overrideModels = new Object2ObjectOpenHashMap<>();

        public ItemOverrides(FluidContainerModel parent, ModelBakery bakery, UnbakedModel emptyModel, List<ItemOverride> overrides)
        {
            this.parent = parent;
            this.bakery = bakery;
            this.emptyModel = emptyModel;
            this.overrides = new ItemOverride[overrides.size()];

            for (int i = 0, j = overrides.size() - 1; i < overrides.size(); i++, j--)
                this.overrides[i] = overrides.get(j);
        }

        @Nullable
        @Override
        public BakedModel resolve(BakedModel emptyModel, ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity entity)
        {
            if (overrides.length == 0)
                return emptyModel;

            FluidStack fluidStack = FluidUtil.getFluidContained(stack).orElse(FluidStack.EMPTY);
            ResourceLocation id = fluidStack.getFluid().getRegistryName();

            if (fluidStack.isEmpty() || id == null)
                return emptyModel;

            FluidAttributes attributes = fluidStack.getFluid().getAttributes();
            ResourceLocation texture = attributes.getStillTexture(fluidStack);
            BakedModel[] models = overrideModels.computeIfAbsent(id, key ->
            {
                BakedModel[] wrappedModels = new BakedModel[overrides.length];
                BlockModelRotation rotation = attributes.isLighterThanAir() ? BlockModelRotation.X180_Y0 : BlockModelRotation.X0_Y0;

                for (int i = 0; i < overrides.length; i++)
                {
                    ItemOverride override = overrides[i];
                    UnbakedModel model = bakery.getModel(override.getModel());

                    if (!Objects.equals(model, this.emptyModel) && model instanceof BlockModel)
                        wrappedModels[i] = parent.wrapModel((BlockModel)model, texture)
                                .bake(null, bakery, ModelLoader.defaultTextureGetter(), rotation, this, override.getModel());
                }

                return wrappedModels;
            });

            for (int i = 0; i < overrides.length; i++)
            {
                if (!overrides[i].test(stack, world, entity))
                    continue;

                BakedModel model = models[i];
                return model != null ? model : emptyModel;
            }

            return emptyModel;
        }
    }
}

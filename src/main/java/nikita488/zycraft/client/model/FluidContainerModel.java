package nikita488.zycraft.client.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IFluidTypeRenderProperties;
import net.minecraftforge.client.RenderProperties;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.geometry.IModelGeometry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
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
            return new Baked(model.bake(bakery, model, spriteGetter, transform, location, true), new Overrides(this, bakery, model, model.getOverrides()));

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
        private final Overrides overrides;

        public Baked(BakedModel model, Overrides overrides)
        {
            super(model);
            this.overrides = overrides;
        }

        @Override
        public Overrides getOverrides()
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

    private static class Overrides extends ItemOverrides
    {
        private final FluidContainerModel parent;
        private final ModelBakery bakery;
        private final UnbakedModel emptyModel;
        private final ItemOverride[] overrides;
        private final Object2ObjectMap<ResourceLocation, BakedModel[]> overrideModels = new Object2ObjectOpenHashMap<>();

        public Overrides(FluidContainerModel parent, ModelBakery bakery, UnbakedModel emptyModel, List<ItemOverride> overrides)
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
        public BakedModel resolve(BakedModel emptyModel, ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int hashCode)
        {
            //TODO: Fix Aluminium Foil rendering
            if (overrides.length == 0)
                return emptyModel;

            FluidStack fluidStack = FluidUtil.getFluidContained(stack).orElse(FluidStack.EMPTY);
            ResourceLocation id = ForgeRegistries.FLUIDS.getKey(fluidStack.getFluid());

            if (fluidStack.isEmpty() || id == null)
                return emptyModel;

            FluidType type = fluidStack.getFluid().getFluidType();
            IFluidTypeRenderProperties properties = RenderProperties.get(type);
            ResourceLocation texture = properties.getStillTexture(fluidStack);
            BakedModel[] models = overrideModels.computeIfAbsent(id, key ->
            {
                BakedModel[] wrappedModels = new BakedModel[overrides.length];
                BlockModelRotation rotation = type.isLighterThanAir() ? BlockModelRotation.X180_Y0 : BlockModelRotation.X0_Y0;

                for (int i = 0; i < overrides.length; i++)
                {
                    ItemOverride override = overrides[i];
                    UnbakedModel model = bakery.getModel(override.getModel());

                    if (!Objects.equals(model, this.emptyModel) && model instanceof BlockModel)
                        wrappedModels[i] = parent.wrapModel((BlockModel)model, texture)
                                .bake(null, bakery, ForgeModelBakery.defaultTextureGetter(), rotation, this, override.getModel());
                }

                return wrappedModels;
            });

            /*for (int i = 0; i < overrides.length; i++)
            {
                if (!overrides[i].test(stack, level, entity))
                    continue;

                BakedModel model = models[i];
                return model != null ? model : emptyModel;
            }*/

            return emptyModel;
        }
    }
}

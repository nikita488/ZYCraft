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
import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IModelTransform;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverride;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.ModelRotation;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.geometry.IModelGeometry;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

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
    public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<RenderMaterial, TextureAtlasSprite> spriteGetter, IModelTransform transform, ItemOverrideList overrides, ResourceLocation location)
    {
        if (replacementTexture == null)
            return new Baked(model.bake(bakery, model, spriteGetter, transform, location, true), new ItemOverrides(this, bakery, model, model.getOverrides()));

        Map<String, Either<RenderMaterial, String>> textures = new HashMap<>(model.textureMap);

        for (String textureName : fluidTextures)
            if (model.hasTexture(textureName))
                textures.put(textureName, Either.left(ModelLoaderRegistry.blockMaterial(replacementTexture)));

        BlockModel wrappedModel = new BlockModel(null, Collections.emptyList(), textures, false, null, ItemCameraTransforms.NO_TRANSFORMS, Collections.emptyList());

        wrappedModel.parent = model;
        return wrappedModel.bake(bakery, wrappedModel, spriteGetter, transform, location, true);
    }

    @Override
    public Collection<RenderMaterial> getTextures(IModelConfiguration owner, Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors)
    {
        return model.getMaterials(modelGetter, missingTextureErrors);
    }

    private static class Baked extends BakedModelWrapper<IBakedModel>
    {
        private final ItemOverrideList overrides;

        public Baked(IBakedModel model, ItemOverrideList overrides)
        {
            super(model);
            this.overrides = overrides;
        }

        @Override
        public ItemOverrideList getOverrides()
        {
            return overrides;
        }
    }

    public static class Loader implements IModelLoader<FluidContainerModel>
    {
        public static final Loader INSTANCE = new Loader();

        @Override
        public void onResourceManagerReload(IResourceManager manager) {}

        @Override
        public FluidContainerModel read(JsonDeserializationContext ctx, JsonObject data)
        {
            BlockModel model = JSONUtils.getAsObject(data, "model", ctx, BlockModel.class);
            JsonArray array = JSONUtils.getAsJsonArray(data, "fluid_textures");

            if (array.size() == 0)
                throw new JsonParseException("Expected at least 1 fluid texture, got 0");

            ObjectList<String> fluidTextures = new ObjectArrayList<>();

            for (JsonElement element : array)
                fluidTextures.add(JSONUtils.convertToString(element, "texture"));

            return new FluidContainerModel(model, fluidTextures);
        }
    }

    private static class ItemOverrides extends ItemOverrideList
    {
        private final FluidContainerModel parent;
        private final ModelBakery bakery;
        private final IUnbakedModel emptyModel;
        private final ItemOverride[] overrides;
        private final Object2ObjectMap<ResourceLocation, IBakedModel[]> overrideModels = new Object2ObjectOpenHashMap<>();

        public ItemOverrides(FluidContainerModel parent, ModelBakery bakery, IUnbakedModel emptyModel, List<ItemOverride> overrides)
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
        public IBakedModel resolve(IBakedModel emptyModel, ItemStack stack, @Nullable ClientWorld level, @Nullable LivingEntity entity)
        {
            if (overrides.length == 0)
                return emptyModel;

            FluidStack fluidStack = FluidUtil.getFluidContained(stack).orElse(FluidStack.EMPTY);
            ResourceLocation id = fluidStack.getFluid().getRegistryName();

            if (fluidStack.isEmpty() || id == null)
                return emptyModel;

            FluidAttributes attributes = fluidStack.getFluid().getAttributes();
            ResourceLocation texture = attributes.getStillTexture(fluidStack);
            IBakedModel[] models = overrideModels.computeIfAbsent(id, key ->
            {
                IBakedModel[] wrappedModels = new IBakedModel[overrides.length];
                ModelRotation rotation = attributes.isLighterThanAir() ? ModelRotation.X180_Y0 : ModelRotation.X0_Y0;

                for (int i = 0; i < overrides.length; i++)
                {
                    ItemOverride override = overrides[i];
                    IUnbakedModel model = bakery.getModel(override.getModel());

                    if (!Objects.equals(model, this.emptyModel) && model instanceof BlockModel)
                        wrappedModels[i] = parent.wrapModel((BlockModel)model, texture)
                                .bake(null, bakery, ModelLoader.defaultTextureGetter(), rotation, this, override.getModel());
                }

                return wrappedModels;
            });

            for (int i = 0; i < overrides.length; i++)
            {
                if (!overrides[i].test(stack, level, entity))
                    continue;

                IBakedModel model = models[i];
                return model != null ? model : emptyModel;
            }

            return emptyModel;
        }
    }
}

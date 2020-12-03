package nikita488.zycraft.multiblock.client;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.geometry.IModelGeometry;
import nikita488.zycraft.multiblock.tile.DefaultMultiChildTile;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

public class MultiChildModel implements IModelGeometry<MultiChildModel>
{
    @Override
    public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<RenderMaterial, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation)
    {
        return new Baked(spriteGetter.apply(ModelLoaderRegistry.blockMaterial(MissingTextureSprite.getLocation())));
    }

    @Override
    public Collection<RenderMaterial> getTextures(IModelConfiguration owner, Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors)
    {
        return Collections.emptyList();
    }

    public static class Baked implements IDynamicBakedModel
    {
        private final TextureAtlasSprite missingSprite;

        public Baked(TextureAtlasSprite missingSprite)
        {
            this.missingSprite = missingSprite;
        }

        @Override
        public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand, IModelData modelData)
        {
            if (state == null || side == null)
                return Collections.emptyList();

            Minecraft mc = Minecraft.getInstance();
            RenderType layer = MinecraftForgeClient.getRenderLayer();

            if (layer == null)
                return mc.getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getMissingModel()
                        .getQuads(state, side, rand, modelData);

            state = modelData.getData(DefaultMultiChildTile.STATE);

            if (!RenderTypeLookup.canRenderInLayer(state, layer))
                return Collections.emptyList();

            IBakedModel model = mc.getBlockRendererDispatcher().getModelForState(state);
            modelData = model.getModelData(new MultiChildWorld(mc.world), modelData.getData(DefaultMultiChildTile.POS), state, modelData);

            return model.getQuads(state, side, rand, modelData);
        }

        @Override
        public boolean isAmbientOcclusion()
        {
            return true;
        }

        @Override
        public boolean isGui3d()
        {
            return false;
        }

        @Override
        public boolean isSideLit()
        {
            return false;
        }

        @Override
        public boolean isBuiltInRenderer()
        {
            return false;
        }

        @Override
        public TextureAtlasSprite getParticleTexture()
        {
            return missingSprite;
        }

        @Override
        public TextureAtlasSprite getParticleTexture(IModelData modelData)
        {
            Minecraft mc = Minecraft.getInstance();
            BlockState state = modelData.getData(DefaultMultiChildTile.STATE);
            IBakedModel model = mc.getBlockRendererDispatcher().getModelForState(state);
            modelData = model.getModelData(new MultiChildWorld(mc.world), modelData.getData(DefaultMultiChildTile.POS), state, modelData);

            return model.getParticleTexture(modelData);
        }

        @Override
        public ItemOverrideList getOverrides()
        {
            return ItemOverrideList.EMPTY;
        }
    }

    public static class Loader implements IModelLoader<MultiChildModel>
    {
        public static final Loader INSTANCE = new Loader();

        @Override
        public void onResourceManagerReload(IResourceManager manager) {}

        @Override
        public MultiChildModel read(JsonDeserializationContext ctx, JsonObject modelContents)
        {
            return new MultiChildModel();
        }
    }
}

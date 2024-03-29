package nikita488.zycraft.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IModelTransform;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.geometry.IModelGeometry;
import nikita488.zycraft.multiblock.child.tile.ConvertedMultiChildTile;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public class ConvertedMultiChildModel implements IModelGeometry<ConvertedMultiChildModel>
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

    private static class Baked implements IDynamicBakedModel
    {
        private static final BlockPos.Mutable RELATIVE_POS = new BlockPos.Mutable();
        private final TextureAtlasSprite missingSprite;

        public Baked(TextureAtlasSprite missingSprite)
        {
            this.missingSprite = missingSprite;
        }

        @Override
        public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random random, IModelData modelData)
        {
            if (state == null || side == null)
                return Collections.emptyList();

            Minecraft mc = Minecraft.getInstance();
            RenderType layer = MinecraftForgeClient.getRenderLayer();

            if (layer == null)
                return mc.getModelManager().getMissingModel().getQuads(state, side, random, modelData);

            BlockState initialState = modelData.getData(ConvertedMultiChildTile.INITIAL_STATE);
            IBlockDisplayReader getter = modelData.getData(ConvertedMultiChildTile.BLOCK_GETTER);
            BlockPos pos = modelData.getData(ConvertedMultiChildTile.POS);

            if (initialState == null || getter == null || pos == null || !RenderTypeLookup.canRenderInLayer(initialState, layer))
                return Collections.emptyList();

            BlockState relativeState = getter.getBlockState(RELATIVE_POS.setWithOffset(pos, side));
            IBakedModel model = mc.getBlockRenderer().getBlockModel(initialState);

            return !initialState.skipRendering(relativeState, side) ?
                    model.getQuads(initialState, side, random, model.getModelData(getter, pos, initialState, modelData)) :
                    Collections.emptyList();
        }

        @Override
        public boolean useAmbientOcclusion()
        {
            return true;
        }

        @Override
        public boolean isGui3d()
        {
            return false;
        }

        @Override
        public boolean usesBlockLight()
        {
            return false;
        }

        @Override
        public boolean isCustomRenderer()
        {
            return false;
        }

        @Override
        public TextureAtlasSprite getParticleIcon()
        {
            return missingSprite;
        }

        @Override
        public TextureAtlasSprite getParticleTexture(IModelData modelData)
        {
            BlockState initialState = modelData.getData(ConvertedMultiChildTile.INITIAL_STATE);
            IBlockDisplayReader getter = modelData.getData(ConvertedMultiChildTile.BLOCK_GETTER);
            BlockPos pos = modelData.getData(ConvertedMultiChildTile.POS);

            if (initialState == null || getter == null || pos == null)
                return missingSprite;

            IBakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(initialState);
            return model.getParticleTexture(model.getModelData(getter, pos, initialState, modelData));
        }

        @Override
        public ItemOverrideList getOverrides()
        {
            return ItemOverrideList.EMPTY;
        }
    }

    public static class Loader implements IModelLoader<ConvertedMultiChildModel>
    {
        public static final Loader INSTANCE = new Loader();

        @Override
        public void onResourceManagerReload(IResourceManager manager) {}

        @Override
        public ConvertedMultiChildModel read(JsonDeserializationContext ctx, JsonObject modelContents)
        {
            return new ConvertedMultiChildModel();
        }
    }
}

package nikita488.zycraft.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.geometry.IModelGeometry;
import nikita488.zycraft.multiblock.child.tile.ConvertedMultiChildTile;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

public class ConvertedMultiChildModel implements IModelGeometry<ConvertedMultiChildModel>
{
    @Override
    public BakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ItemOverrides overrides, ResourceLocation modelLocation)
    {
        return new Baked(spriteGetter.apply(ModelLoaderRegistry.blockMaterial(MissingTextureAtlasSprite.getLocation())));
    }

    @Override
    public Collection<Material> getTextures(IModelConfiguration owner, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors)
    {
        return Collections.emptyList();
    }

    private static class Baked implements IDynamicBakedModel
    {
        private static final BlockPos.MutableBlockPos RELATIVE_POS = new BlockPos.MutableBlockPos();
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
            BlockAndTintGetter getter = modelData.getData(ConvertedMultiChildTile.BLOCK_GETTER);
            BlockPos pos = modelData.getData(ConvertedMultiChildTile.POS);

            if (initialState == null || getter == null || pos == null || !ItemBlockRenderTypes.canRenderInLayer(initialState, layer))
                return Collections.emptyList();

            BlockState relativeState = getter.getBlockState(RELATIVE_POS.setWithOffset(pos, side));
            BakedModel model = mc.getBlockRenderer().getBlockModel(initialState);

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
            BlockAndTintGetter getter = modelData.getData(ConvertedMultiChildTile.BLOCK_GETTER);
            BlockPos pos = modelData.getData(ConvertedMultiChildTile.POS);

            if (initialState == null || getter == null || pos == null)
                return missingSprite;

            BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(initialState);
            return model.getParticleTexture(model.getModelData(getter, pos, initialState, modelData));
        }

        @Override
        public ItemOverrides getOverrides()
        {
            return ItemOverrides.EMPTY;
        }
    }

    public static class Loader implements IModelLoader<ConvertedMultiChildModel>
    {
        public static final Loader INSTANCE = new Loader();

        @Override
        public void onResourceManagerReload(ResourceManager manager) {}

        @Override
        public ConvertedMultiChildModel read(JsonDeserializationContext ctx, JsonObject modelContents)
        {
            return new ConvertedMultiChildModel();
        }
    }
}

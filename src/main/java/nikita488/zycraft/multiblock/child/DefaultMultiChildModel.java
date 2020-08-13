package nikita488.zycraft.multiblock.child;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.IResourceManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.geometry.IModelGeometry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public class DefaultMultiChildModel implements IModelGeometry<DefaultMultiChildModel>
{
    @Override
    public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation)
    {
        return new Baked(spriteGetter.apply(new Material(AtlasTexture.LOCATION_BLOCKS_TEXTURE, MissingTextureSprite.getLocation())));
    }

    @Override
    public Collection<Material> getTextures(IModelConfiguration owner, Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors)
    {
        return ImmutableList.of();
    }

    public static class Baked implements IDynamicBakedModel
    {
        private final TextureAtlasSprite missingSprite;

        public Baked(TextureAtlasSprite missingSprite)
        {
            this.missingSprite = missingSprite;
        }

        @Override
        public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random rand, IModelData data)
        {
            RenderType layer = MinecraftForgeClient.getRenderLayer();

            if (layer == null || face == null)
                return Minecraft.getInstance().getBlockRendererDispatcher()
                        .getBlockModelShapes()
                        .getModelManager()
                        .getMissingModel().getQuads(state, face, rand, data);

            if (!data.hasProperty(DefaultMultiChildTile.STATE))
                return ImmutableList.of();

            BlockState childState = data.getData(DefaultMultiChildTile.STATE);

            if (childState == null || !RenderTypeLookup.canRenderInLayer(childState, layer))
                return ImmutableList.of();

            return Minecraft.getInstance().getBlockRendererDispatcher()
                    .getModelForState(childState).getQuads(childState, face, rand, data);
        }

        @Nonnull
        @Override
        public IModelData getModelData(@Nonnull ILightReader world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData)
        {
            TileEntity tile = world.getTileEntity(pos);

            if (!(tile instanceof DefaultMultiChildTile))
                return tileData;

            tileData.setData(DefaultMultiChildTile.STATE, ((DefaultMultiChildTile)tile).state());
            return tileData;
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
        public boolean func_230044_c_()
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
        public TextureAtlasSprite getParticleTexture(IModelData data)
        {
            if (!data.hasProperty(DefaultMultiChildTile.STATE))
                return missingSprite;

            BlockState state = data.getData(DefaultMultiChildTile.STATE);

            if (state == null)
                return missingSprite;

            return Minecraft.getInstance().getBlockRendererDispatcher().getModelForState(state).getParticleTexture(data);
        }

        @Override
        public ItemOverrideList getOverrides()
        {
            return ItemOverrideList.EMPTY;
        }
    }

    public static class Loader implements IModelLoader<DefaultMultiChildModel>
    {
        @Override
        public void onResourceManagerReload(IResourceManager manager) {}

        @Override
        public DefaultMultiChildModel read(JsonDeserializationContext ctx, JsonObject modelContents)
        {
            return new DefaultMultiChildModel();
        }
    }
}

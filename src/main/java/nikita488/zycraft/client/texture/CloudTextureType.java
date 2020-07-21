package nikita488.zycraft.client.texture;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import nikita488.zycraft.ZYCraft;
import team.chisel.ctm.api.texture.ICTMTexture;
import team.chisel.ctm.api.texture.ITextureContext;
import team.chisel.ctm.api.texture.ITextureType;
import team.chisel.ctm.api.texture.TextureType;
import team.chisel.ctm.api.util.TextureInfo;

@TextureType(ZYCraft.MOD_ID + ":cloud")
public class CloudTextureType implements ITextureType
{
    private static final ITextureContext EMPTY_CONTEXT = () -> 0L;

    @Override
    public ICTMTexture<CloudTextureType> makeTexture(TextureInfo info)
    {
        info.getSprites()[0] = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(CloudSprite.name());
        return new CloudTexture(this, info);
    }

    @Override
    public ITextureContext getBlockRenderContext(BlockState state, IBlockReader world, BlockPos pos, ICTMTexture<?> texture)
    {
        return EMPTY_CONTEXT;
    }

    @Override
    public ITextureContext getContextFromData(long l)
    {
        return EMPTY_CONTEXT;
    }
}

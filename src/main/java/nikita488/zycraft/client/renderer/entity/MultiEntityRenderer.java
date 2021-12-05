package nikita488.zycraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import nikita488.zycraft.entity.MultiEntity;
import nikita488.zycraft.multiblock.IDynamicMultiBlock;

public class MultiEntityRenderer extends EntityRenderer<MultiEntity>
{
    public MultiEntityRenderer(EntityRendererProvider.Context context)
    {
        super(context);
    }

    @Override
    public void render(MultiEntity entity, float yRot, float partialTicks, PoseStack stack, MultiBufferSource source, int lightMap)
    {
        IDynamicMultiBlock multiBlock = entity.parentMultiBlock();

        if (multiBlock != null)
            multiBlock.render(stack, source, lightMap, partialTicks);
    }

    @Override
    public ResourceLocation getTextureLocation(MultiEntity entity)
    {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}

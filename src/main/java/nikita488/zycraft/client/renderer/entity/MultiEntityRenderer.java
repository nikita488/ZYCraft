package nikita488.zycraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.resources.ResourceLocation;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.entity.MultiEntity;
import nikita488.zycraft.multiblock.IDynamicMultiBlock;

public class MultiEntityRenderer extends EntityRenderer<MultiEntity>
{
    private static final ResourceLocation TEXTURE = ZYCraft.id("");

    public MultiEntityRenderer(EntityRenderDispatcher manager)
    {
        super(manager);
    }

    @Override
    public void render(MultiEntity entity, float rotationYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int lightMap)
    {
        IDynamicMultiBlock multiBlock = entity.parentMultiBlock();

        if (multiBlock != null)
            multiBlock.render(stack, buffer, lightMap, partialTicks);
    }

    @Override
    public ResourceLocation getTextureLocation(MultiEntity entity)
    {
        return TEXTURE;
    }
}

package nikita488.zycraft.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.entity.MultiEntity;
import nikita488.zycraft.multiblock.IDynamicMultiBlock;

public class MultiEntityRenderer extends EntityRenderer<MultiEntity>
{
    private static final ResourceLocation TEXTURE = ZYCraft.id("");

    public MultiEntityRenderer(EntityRendererManager manager)
    {
        super(manager);
    }

    @Override
    public void render(MultiEntity entity, float rotationYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int lightMap)
    {
        IDynamicMultiBlock multiBlock = entity.getMultiBlock();

        if (multiBlock != null)
            multiBlock.render(stack, buffer, lightMap, partialTicks);
    }

    @Override
    public ResourceLocation getTextureLocation(MultiEntity entity)
    {
        return TEXTURE;
    }
}

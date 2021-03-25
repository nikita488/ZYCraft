package nikita488.zycraft.multiblock.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import nikita488.zycraft.multiblock.MultiBlock;
import nikita488.zycraft.multiblock.MultiEntity;

public class MultiEntityRenderer extends EntityRenderer<MultiEntity>
{
    public MultiEntityRenderer(EntityRendererManager manager)
    {
        super(manager);
    }

    @Override
    public void render(MultiEntity entity, float rotationYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int lightMap)
    {
        MultiBlock multiBlock = entity.getMultiBlock();

        if (multiBlock != null)
            multiBlock.render(stack, buffer, lightMap, partialTicks);
    }

    @Override
    public ResourceLocation getEntityTexture(MultiEntity entity)
    {
        return null;
    }
}

package nikita488.zycraft.client.renderer.multiblock;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.DrawHighlightEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.WorldEvent;
import nikita488.zycraft.client.ZYRenderTypes;
import nikita488.zycraft.multiblock.MultiBlock;
import nikita488.zycraft.multiblock.child.IMultiChild;
import nikita488.zycraft.util.ZYConstants;

public class MultiHighlightRenderer
{
    private static final float MIN = -0.009F;
    private static final float MAX = 1.009F;
    private static final BlockPos.MutableBlockPos LAST_POS = new BlockPos.MutableBlockPos();
    private static final BlockPos.MutableBlockPos RELATIVE_POS = new BlockPos.MutableBlockPos();
    private static long startTime, lastTime;

    public static void init()
    {
        MinecraftForge.EVENT_BUS.addListener(MultiHighlightRenderer::onClientTick);
        MinecraftForge.EVENT_BUS.addListener(MultiHighlightRenderer::onWorldUnload);
        MinecraftForge.EVENT_BUS.addListener(MultiHighlightRenderer::onDrawBlockHighlight);
    }

    private static void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END)
            return;

        Minecraft mc = Minecraft.getInstance();
        HitResult hitResult = mc.hitResult;

        if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK)
            return;

        Level level = mc.level;

        if (level != null && (level.getGameTime() - lastTime) > 10)
            LAST_POS.set(BlockPos.ZERO);
    }

    private static void onWorldUnload(WorldEvent.Unload event)
    {
        if (event.getWorld().isClientSide())
            LAST_POS.set(BlockPos.ZERO);
    }

    private static void onDrawBlockHighlight(DrawHighlightEvent.HighlightBlock event)
    {
        Level level = event.getInfo().getEntity().getCommandSenderWorld();
        BlockPos highlightPos = event.getTarget().getBlockPos();
        BlockEntity blockEntity = level.getBlockEntity(highlightPos);
        long time = level.getGameTime();

        if (!LAST_POS.equals(highlightPos))
        {
            LAST_POS.set(highlightPos);
            startTime = time;
        }

        lastTime = time;

        if (blockEntity instanceof IMultiChild)
        {
            IMultiChild child = (IMultiChild)blockEntity;

            if (!child.hasParents())
                return;

            float ticksElapsed = (time - startTime) + event.getPartialTicks();
            float strength = Math.max(0F, 1F - ticksElapsed / 100);

            if (strength <= 0F)
                return;

            ObjectSet<BlockPos> highlightBlocks = new ObjectOpenHashSet<>();

            for (MultiBlock multiBlock : child.parentMultiBlocks())
                highlightBlocks.addAll(multiBlock.childBlocks());

            PoseStack stack = event.getMatrix();
            Vec3 cameraPos = event.getInfo().getPosition();

            stack.pushPose();
            stack.translate(-cameraPos.x(), -cameraPos.y(), -cameraPos.z());

            VertexConsumer buffer = event.getBuffers().getBuffer(ZYRenderTypes.MULTI_HIGHLIGHT);

            for (BlockPos pos : highlightBlocks)
                for (Direction side : ZYConstants.DIRECTIONS)
                    if (!highlightBlocks.contains(RELATIVE_POS.setWithOffset(pos, side)))
                        renderHighlightQuad(stack, buffer, pos, side, 0.3F * strength);

            stack.popPose();
        }
    }

    private static void renderHighlightQuad(PoseStack stack, VertexConsumer buffer, BlockPos pos, Direction side, float alpha)
    {
        stack.pushPose();
        stack.translate(pos.getX(), pos.getY(), pos.getZ());

        Matrix4f matrix = stack.last().pose();

        switch (side)
        {
            case DOWN:
                buffer.vertex(matrix, MIN, MIN, MIN).color(1F, 1F, 1F, alpha).uv(0F, 0F).endVertex();
                buffer.vertex(matrix, MAX, MIN, MIN).color(1F, 1F, 1F, alpha).uv(1F, 0F).endVertex();
                buffer.vertex(matrix, MAX, MIN, MAX).color(1F, 1F, 1F, alpha).uv(1F, 1F).endVertex();
                buffer.vertex(matrix, MIN, MIN, MAX).color(1F, 1F, 1F, alpha).uv(0F, 1F).endVertex();
                break;
            case UP:
                buffer.vertex(matrix, MAX, MAX, MIN).color(1F, 1F, 1F, alpha).uv(1F, 0F).endVertex();
                buffer.vertex(matrix, MIN, MAX, MIN).color(1F, 1F, 1F, alpha).uv(0F, 0F).endVertex();
                buffer.vertex(matrix, MIN, MAX, MAX).color(1F, 1F, 1F, alpha).uv(0F, 1F).endVertex();
                buffer.vertex(matrix, MAX, MAX, MAX).color(1F, 1F, 1F, alpha).uv(1F, 1F).endVertex();
                break;
            case NORTH:
                buffer.vertex(matrix, MIN, MAX, MIN).color(1F, 1F, 1F, alpha).uv(1F, 0F).endVertex();
                buffer.vertex(matrix, MAX, MAX, MIN).color(1F, 1F, 1F, alpha).uv(0F, 0F).endVertex();
                buffer.vertex(matrix, MAX, MIN, MIN).color(1F, 1F, 1F, alpha).uv(0F, 1F).endVertex();
                buffer.vertex(matrix, MIN, MIN, MIN).color(1F, 1F, 1F, alpha).uv(1F, 1F).endVertex();
                break;
            case SOUTH:
                buffer.vertex(matrix, MAX, MAX, MAX).color(1F, 1F, 1F, alpha).uv(1F, 0F).endVertex();
                buffer.vertex(matrix, MIN, MAX, MAX).color(1F, 1F, 1F, alpha).uv(0F, 0F).endVertex();
                buffer.vertex(matrix, MIN, MIN, MAX).color(1F, 1F, 1F, alpha).uv(0F, 1F).endVertex();
                buffer.vertex(matrix, MAX, MIN, MAX).color(1F, 1F, 1F, alpha).uv(1F, 1F).endVertex();
                break;
            case WEST:
                buffer.vertex(matrix, MIN, MAX, MAX).color(1F, 1F, 1F, alpha).uv(1F, 0F).endVertex();
                buffer.vertex(matrix, MIN, MAX, MIN).color(1F, 1F, 1F, alpha).uv(0F, 0F).endVertex();
                buffer.vertex(matrix, MIN, MIN, MIN).color(1F, 1F, 1F, alpha).uv(0F, 1F).endVertex();
                buffer.vertex(matrix, MIN, MIN, MAX).color(1F, 1F, 1F, alpha).uv(1F, 1F).endVertex();
                break;
            case EAST:
                buffer.vertex(matrix, MAX, MAX, MIN).color(1F, 1F, 1F, alpha).uv(1F, 0F).endVertex();
                buffer.vertex(matrix, MAX, MAX, MAX).color(1F, 1F, 1F, alpha).uv(0F, 0F).endVertex();
                buffer.vertex(matrix, MAX, MIN, MAX).color(1F, 1F, 1F, alpha).uv(0F, 1F).endVertex();
                buffer.vertex(matrix, MAX, MIN, MIN).color(1F, 1F, 1F, alpha).uv(1F, 1F).endVertex();
                break;
        }

        stack.popPose();
    }
}

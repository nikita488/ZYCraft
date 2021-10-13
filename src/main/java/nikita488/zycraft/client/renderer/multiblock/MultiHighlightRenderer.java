package nikita488.zycraft.client.renderer.multiblock;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
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
    private static final BlockPos.Mutable LAST_POS = new BlockPos.Mutable();
    private static final BlockPos.Mutable ADJACENT_POS = new BlockPos.Mutable();
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
        RayTraceResult hitResult = mc.hitResult;

        if (hitResult != null && hitResult.getType() == RayTraceResult.Type.BLOCK)
            return;

        World world = mc.level;

        if (world != null && (world.getGameTime() - lastTime) > 10)
            LAST_POS.set(BlockPos.ZERO);
    }

    private static void onWorldUnload(WorldEvent.Unload event)
    {
        if (event.getWorld().isClientSide())
            LAST_POS.set(BlockPos.ZERO);
    }

    private static void onDrawBlockHighlight(DrawHighlightEvent.HighlightBlock event)
    {
        World world = event.getInfo().getEntity().getCommandSenderWorld();
        BlockPos highlightPos = event.getTarget().getBlockPos();
        TileEntity tile = world.getBlockEntity(highlightPos);
        long time = world.getGameTime();

        if (!LAST_POS.equals(highlightPos))
        {
            LAST_POS.set(highlightPos);
            startTime = time;
        }

        lastTime = time;

        if (tile instanceof IMultiChild)
        {
            IMultiChild child = (IMultiChild)tile;

            if (!child.hasParents())
                return;

            float ticksElapsed = (time - startTime) + event.getPartialTicks();
            float strength = Math.max(0F, 1F - ticksElapsed / 100);

            if (strength <= 0F)
                return;

            ObjectSet<BlockPos> highlightBlocks = new ObjectOpenHashSet<>();

            for (MultiBlock multiBlock : child.parentMultiBlocks())
                highlightBlocks.addAll(multiBlock.childBlocks());

            MatrixStack stack = event.getMatrix();
            Vector3d cameraPos = event.getInfo().getPosition();

            stack.pushPose();
            stack.translate(-cameraPos.x(), -cameraPos.y(), -cameraPos.z());

            IVertexBuilder buffer = event.getBuffers().getBuffer(ZYRenderTypes.MULTI_HIGHLIGHT);

            for (BlockPos pos : highlightBlocks)
                for (Direction side : ZYConstants.DIRECTIONS)
                    if (!highlightBlocks.contains(ADJACENT_POS.setWithOffset(pos, side)))
                        renderHighlightQuad(stack, buffer, pos, side, 0.3F * strength);

            stack.popPose();
        }
    }

    private static void renderHighlightQuad(MatrixStack stack, IVertexBuilder buffer, BlockPos pos, Direction side, float alpha)
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

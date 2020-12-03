package nikita488.zycraft.multiblock;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.DrawHighlightEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.client.ZYRenderTypes;
import nikita488.zycraft.multiblock.tile.MultiChildTile;

@Mod.EventBusSubscriber(modid = ZYCraft.MOD_ID, value = Dist.CLIENT)
public class MultiClientHandler
{
    private static final Direction[] VALUES = Direction.values();
    private static final BlockPos.Mutable prevHighlightPos = new BlockPos.Mutable();
    private static final BlockPos.Mutable adjPos = new BlockPos.Mutable();
    private static long highlightStartTime;

    @SubscribeEvent
    public static void tick(TickEvent.ClientTickEvent event)
    {
        if (Minecraft.getInstance().world != null && event.phase == TickEvent.Phase.END)
            MultiManager.CLIENT_INSTANCE.tick();
    }

    @SubscribeEvent
    public static void renderMultiHighlight(DrawHighlightEvent.HighlightBlock event)
    {
        World world = Minecraft.getInstance().world;
        BlockPos highlightPos = event.getTarget().getPos();
        TileEntity tile = world.getTileEntity(highlightPos);

        if (!(tile instanceof MultiChildTile))
            return;

        MultiChildTile child = (MultiChildTile)tile;

        if (!child.hasParent())
            return;

        long time = world.getGameTime();

        if (!prevHighlightPos.equals(highlightPos))
        {
            prevHighlightPos.setPos(highlightPos);
            highlightStartTime = time;
        }

        float ticksElapsed = (time - highlightStartTime) + event.getPartialTicks();
        float strength = Math.max(0, 1 - ticksElapsed / 100);

        if (strength == 0)
            return;

        ObjectSet<BlockPos> highlightBlocks = new ObjectOpenHashSet<>();

        for (MultiBlock multiBlock : child.parentMultiBlocks())
            highlightBlocks.addAll(multiBlock.childBlocks());

        MatrixStack stack = event.getMatrix();
        Vector3d cameraPos = event.getInfo().getProjectedView();

        stack.push();
        stack.translate(-cameraPos.getX(), -cameraPos.getY(), -cameraPos.getZ());

        IVertexBuilder builder = event.getBuffers().getBuffer(ZYRenderTypes.MULTI_HIGHLIGHT);

        for (BlockPos pos : highlightBlocks)
            for (Direction side : VALUES)
                if (!highlightBlocks.contains(adjPos.setAndMove(pos, side)))
                    fillHighlightQuad(stack, builder, pos, side, strength);

        stack.pop();
    }

    private static void fillHighlightQuad(MatrixStack stack, IVertexBuilder builder, BlockPos highlightPos, Direction side, float strength)
    {
        float min = -0.009F;
        float max = 1.009F;
        float alpha = 0.3F * strength;

        stack.push();
        stack.translate(highlightPos.getX(), highlightPos.getY(), highlightPos.getZ());

        Matrix4f matrix = stack.getLast().getMatrix();

        switch (side)
        {
            case DOWN:
                builder.pos(matrix, min, min, min).color(1, 1, 1, alpha).tex(0, 0).endVertex();
                builder.pos(matrix, max, min, min).color(1, 1, 1, alpha).tex(1, 0).endVertex();
                builder.pos(matrix, max, min, max).color(1, 1, 1, alpha).tex(1, 1).endVertex();
                builder.pos(matrix, min, min, max).color(1, 1, 1, alpha).tex(0, 1).endVertex();
                break;
            case UP:
                builder.pos(matrix, max, max, min).color(1, 1, 1, alpha).tex(1, 0).endVertex();
                builder.pos(matrix, min, max, min).color(1, 1, 1, alpha).tex(0, 0).endVertex();
                builder.pos(matrix, min, max, max).color(1, 1, 1, alpha).tex(0, 1).endVertex();
                builder.pos(matrix, max, max, max).color(1, 1, 1, alpha).tex(1, 1).endVertex();
                break;
            case NORTH:
                builder.pos(matrix, min, max, min).color(1, 1, 1, alpha).tex(1, 0).endVertex();
                builder.pos(matrix, max, max, min).color(1, 1, 1, alpha).tex(0, 0).endVertex();
                builder.pos(matrix, max, min, min).color(1, 1, 1, alpha).tex(0, 1).endVertex();
                builder.pos(matrix, min, min, min).color(1, 1, 1, alpha).tex(1, 1).endVertex();
                break;
            case SOUTH:
                builder.pos(matrix, max, max, max).color(1, 1, 1, alpha).tex(1, 0).endVertex();
                builder.pos(matrix, min, max, max).color(1, 1, 1, alpha).tex(0, 0).endVertex();
                builder.pos(matrix, min, min, max).color(1, 1, 1, alpha).tex(0, 1).endVertex();
                builder.pos(matrix, max, min, max).color(1, 1, 1, alpha).tex(1, 1).endVertex();
                break;
            case WEST:
                builder.pos(matrix, min, max, max).color(1, 1, 1, alpha).tex(1, 0).endVertex();
                builder.pos(matrix, min, max, min).color(1, 1, 1, alpha).tex(0, 0).endVertex();
                builder.pos(matrix, min, min, min).color(1, 1, 1, alpha).tex(0, 1).endVertex();
                builder.pos(matrix, min, min, max).color(1, 1, 1, alpha).tex(1, 1).endVertex();
                break;
            case EAST:
                builder.pos(matrix, max, max, min).color(1, 1, 1, alpha).tex(1, 0).endVertex();
                builder.pos(matrix, max, max, max).color(1, 1, 1, alpha).tex(0, 0).endVertex();
                builder.pos(matrix, max, min, max).color(1, 1, 1, alpha).tex(0, 1).endVertex();
                builder.pos(matrix, max, min, min).color(1, 1, 1, alpha).tex(1, 1).endVertex();
                break;
        }

        stack.pop();
    }
}

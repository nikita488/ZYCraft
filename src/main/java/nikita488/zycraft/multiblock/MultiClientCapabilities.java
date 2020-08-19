package nikita488.zycraft.multiblock;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.DrawHighlightEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.api.multiblock.MultiBlock;
import nikita488.zycraft.api.multiblock.capability.IMultiWorld;
import nikita488.zycraft.api.multiblock.child.MultiChildTile;
import nikita488.zycraft.client.ZYRenderTypes;
import nikita488.zycraft.multiblock.capability.MultiWorldCapability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = ZYCraft.MOD_ID, value = Dist.CLIENT)
public class MultiClientCapabilities
{
    private static LazyOptional<IMultiWorld> capability = LazyOptional.empty();

    public static LazyOptional<IMultiWorld> getMultiWorld()
    {
        return getMultiWorld(Minecraft.getInstance().world);
    }

    @Nonnull
    public static LazyOptional<IMultiWorld> getMultiWorld(@Nullable IWorld world)
    {
        if (world == null || !world.isRemote())
            return LazyOptional.empty();

        if (capability.isPresent())
            return capability;

        ZYCraft.LOGGER.info("Cache ClientWorld {} capability.", world.getDimension().getType().getId());
        capability = world.getWorld().getCapability(MultiWorldCapability.INSTANCE);
        capability.addListener(cap -> capability = LazyOptional.empty());
        return capability;
    }

    @SubscribeEvent
    public static void tickMultiWorld(TickEvent.ClientTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END) return;
        getMultiWorld().ifPresent(IMultiWorld::tick);
    }

    private static final Direction[] VALUES = Direction.values();
    private static final BlockPos.Mutable prevHighlightPos = new BlockPos.Mutable();
    private static long highlightStartTime;

    @SubscribeEvent
    public static void renderMultiHighlight(DrawHighlightEvent.HighlightBlock event)
    {
        World world = Minecraft.getInstance().world;
        BlockPos highlightPos = event.getTarget().getPos();

        TileEntity tile = world.getTileEntity(highlightPos);

        if (!(tile instanceof MultiChildTile) || !((MultiChildTile)tile).hasParent())
            return;

        long time = world.getGameTime();

        if (!prevHighlightPos.equals(highlightPos))
        {
            prevHighlightPos.setPos(highlightPos);
            highlightStartTime = time;
        }

        float highlightTime = (time - highlightStartTime) + event.getPartialTicks();
        float strength = Math.max(0.0F, 1.0F - highlightTime / 100.0F);

        if (strength == 0.0F)
            return;

        ObjectOpenHashSet<BlockPos> highlightBlocks = new ObjectOpenHashSet<>();

        for (MultiBlock multiBlock : ((MultiChildTile)tile).parentMultiBlocks())
            highlightBlocks.addAll(multiBlock.childBlocks());

        MatrixStack stack = event.getMatrix();
        Vec3d cameraPos = event.getInfo().getProjectedView();

        stack.push();
        stack.translate(-cameraPos.getX(), -cameraPos.getY(), -cameraPos.getZ());

        Matrix4f matrix = stack.getLast().getMatrix();
        IVertexBuilder builder = event.getBuffers().getBuffer(ZYRenderTypes.MULTI_HIGHLIGHT);
        BlockPos.Mutable adjacentPos = new BlockPos.Mutable();

        for (BlockPos pos : highlightBlocks)
            for (Direction side : VALUES)
                if (!highlightBlocks.contains(adjacentPos.setPos(pos).move(side)))
                    fillHighlightQuad(matrix, builder, pos, side, strength);

        stack.pop();
        ((IRenderTypeBuffer.Impl)event.getBuffers()).finish(ZYRenderTypes.MULTI_HIGHLIGHT);
    }

    private static void fillHighlightQuad(Matrix4f matrix, IVertexBuilder builder, BlockPos highlightPos, Direction side, float strength)
    {
        float offset = 0.009F;
        float x1 = highlightPos.getX() - offset;
        float y1 = highlightPos.getY() - offset;
        float z1 = highlightPos.getZ() - offset;
        float x2 = highlightPos.getX() + 1.0F + offset;
        float y2 = highlightPos.getY() + 1.0F + offset;
        float z2 = highlightPos.getZ() + 1.0F + offset;
        float alpha = 0.3F * strength;

        switch (side)
        {
            case DOWN:
                builder.pos(matrix, x1, y1, z1).color(1.0F, 1.0F, 1.0F, alpha).tex(0, 0).endVertex();
                builder.pos(matrix, x2, y1, z1).color(1.0F, 1.0F, 1.0F, alpha).tex(1, 0).endVertex();
                builder.pos(matrix, x2, y1, z2).color(1.0F, 1.0F, 1.0F, alpha).tex(1, 1).endVertex();
                builder.pos(matrix, x1, y1, z2).color(1.0F, 1.0F, 1.0F, alpha).tex(0, 1).endVertex();
                break;
            case UP:
                builder.pos(matrix, x2, y2, z1).color(1.0F, 1.0F, 1.0F, alpha).tex(1, 0).endVertex();
                builder.pos(matrix, x1, y2, z1).color(1.0F, 1.0F, 1.0F, alpha).tex(0, 0).endVertex();
                builder.pos(matrix, x1, y2, z2).color(1.0F, 1.0F, 1.0F, alpha).tex(0, 1).endVertex();
                builder.pos(matrix, x2, y2, z2).color(1.0F, 1.0F, 1.0F, alpha).tex(1, 1).endVertex();
                break;
            case NORTH:
                builder.pos(matrix, x1, y2, z1).color(1.0F, 1.0F, 1.0F, alpha).tex(1, 0).endVertex();
                builder.pos(matrix, x2, y2, z1).color(1.0F, 1.0F, 1.0F, alpha).tex(0, 0).endVertex();
                builder.pos(matrix, x2, y1, z1).color(1.0F, 1.0F, 1.0F, alpha).tex(0, 1).endVertex();
                builder.pos(matrix, x1, y1, z1).color(1.0F, 1.0F, 1.0F, alpha).tex(1, 1).endVertex();
                break;
            case SOUTH:
                builder.pos(matrix, x2, y2, z2).color(1.0F, 1.0F, 1.0F, alpha).tex(1, 0).endVertex();
                builder.pos(matrix, x1, y2, z2).color(1.0F, 1.0F, 1.0F, alpha).tex(0, 0).endVertex();
                builder.pos(matrix, x1, y1, z2).color(1.0F, 1.0F, 1.0F, alpha).tex(0, 1).endVertex();
                builder.pos(matrix, x2, y1, z2).color(1.0F, 1.0F, 1.0F, alpha).tex(1, 1).endVertex();
                break;
            case WEST:
                builder.pos(matrix, x1, y2, z2).color(1.0F, 1.0F, 1.0F, alpha).tex(1, 0).endVertex();
                builder.pos(matrix, x1, y2, z1).color(1.0F, 1.0F, 1.0F, alpha).tex(0, 0).endVertex();
                builder.pos(matrix, x1, y1, z1).color(1.0F, 1.0F, 1.0F, alpha).tex(0, 1).endVertex();
                builder.pos(matrix, x1, y1, z2).color(1.0F, 1.0F, 1.0F, alpha).tex(1, 1).endVertex();
                break;
            case EAST:
                builder.pos(matrix, x2, y2, z1).color(1.0F, 1.0F, 1.0F, alpha).tex(1, 0).endVertex();
                builder.pos(matrix, x2, y2, z2).color(1.0F, 1.0F, 1.0F, alpha).tex(0, 0).endVertex();
                builder.pos(matrix, x2, y1, z2).color(1.0F, 1.0F, 1.0F, alpha).tex(0, 1).endVertex();
                builder.pos(matrix, x2, y1, z1).color(1.0F, 1.0F, 1.0F, alpha).tex(1, 1).endVertex();
                break;
        }
    }
}

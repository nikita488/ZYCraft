package nikita488.zycraft.util;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nikita488.zycraft.api.colorable.IColorable;
import nikita488.zycraft.block.QuartzCrystalClusterBlock;
import nikita488.zycraft.particle.SparkleParticleData;

import java.util.Random;

public class ParticleUtils
{
    public static void glowingBlock(BlockState state, World world, BlockPos pos, Random rand)
    {
        glowingBlock(state, world, pos, rand, 0xFFFFFF7F);
    }

    public static void glowingColorableBlock(BlockState state, World world, BlockPos pos, Random rand)
    {
        TileEntity tile = world.getBlockEntity(pos);

        if (tile instanceof IColorable)
            glowingBlock(state, world, pos, rand, Color.rgba(((IColorable)tile).getColor(state, world, pos), 192));
    }

    public static void glowingBlock(BlockState state, World world, BlockPos pos, Random rand, int rgba)
    {
        SparkleParticleData data = SparkleParticleData.builder()
                .color(rgba)
                .lifetimeFactor(6)
                .sizeFactor(0.5F)
                .build();

        double offset = 9 / 16F;

        for (Direction dir : ZYConstants.DIRECTIONS)
        {
            if (rand.nextFloat() > 0.15F)
                continue;

            BlockPos adjPos = pos.relative(dir);
            BlockState adjState = world.getBlockState(adjPos);
            if (state == adjState || adjState.isCollisionShapeFullBlock(world, adjPos))
                continue;

            Direction.Axis axis = dir.getAxis();
            double xOffset = axis == Direction.Axis.X ? 0.5D + offset * dir.getStepX() : rand.nextFloat();
            double yOffset = axis == Direction.Axis.Y ? 0.5D + offset * dir.getStepY() : rand.nextFloat();
            double zOffset = axis == Direction.Axis.Z ? 0.5D + offset * dir.getStepZ() : rand.nextFloat();

            world.addParticle(data, pos.getX() + xOffset, pos.getY() + yOffset, pos.getZ() + zOffset, 0D, 0D, 0D);
        }
    }

    public static void quartzCrystalCluster(BlockState state, World world, BlockPos pos, Random rand)
    {
        SparkleParticleData data = SparkleParticleData.builder()
                .color(0xFFFFFF80)
                .lifetimeFactor(6)
                .sizeFactor(0.5F)
                .build();

        float size = 6 / 16F;
        float height = 11 / 16F;
        Direction dir = state.getValue(QuartzCrystalClusterBlock.FACING);
        double xOffset = getOffset(rand, dir, Direction.Axis.X, size, height);
        double yOffset = getOffset(rand, dir, Direction.Axis.Y, size, height);
        double zOffset = getOffset(rand, dir, Direction.Axis.Z, size, height);

        world.addParticle(data, pos.getX() + xOffset, pos.getY() + yOffset, pos.getZ() + zOffset, 0D, 0D, 0D);
    }

    private static double getOffset(Random rand, Direction dir, Direction.Axis axis, float size, float height)
    {
        if (dir.getAxis() == axis)
            if (dir.getAxisDirection() == Direction.AxisDirection.POSITIVE)
                return rand.nextFloat() * height;
            else
                return 1F - rand.nextFloat() * height;
        else
            return 0.5D + (rand.nextFloat() - 0.5D) * size;
    }
}

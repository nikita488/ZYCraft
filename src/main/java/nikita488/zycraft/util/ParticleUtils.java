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
    public static void glowingBlock(BlockState state, World level, BlockPos pos, Random random)
    {
        glowingBlock(state, level, pos, random, 0xFFFFFF7F);
    }

    public static void glowingColorableBlock(BlockState state, World level, BlockPos pos, Random random)
    {
        TileEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof IColorable)
            glowingBlock(state, level, pos, random, Color.rgba(((IColorable)blockEntity).getColor(state, level, pos), 192));
    }

    public static void glowingBlock(BlockState state, World level, BlockPos pos, Random random, int rgba)
    {
        SparkleParticleData data = SparkleParticleData.builder()
                .color(rgba)
                .lifetimeFactor(6)
                .sizeFactor(0.5F)
                .build();

        double offset = 9 / 16F;

        for (Direction side : ZYConstants.DIRECTIONS)
        {
            if (random.nextFloat() > 0.15F)
                continue;

            BlockPos relativePos = pos.relative(side);
            BlockState relativeState = level.getBlockState(relativePos);

            if (state == relativeState || relativeState.isCollisionShapeFullBlock(level, relativePos))
                continue;

            Direction.Axis axis = side.getAxis();
            double xOffset = axis == Direction.Axis.X ? 0.5D + offset * side.getStepX() : random.nextFloat();
            double yOffset = axis == Direction.Axis.Y ? 0.5D + offset * side.getStepY() : random.nextFloat();
            double zOffset = axis == Direction.Axis.Z ? 0.5D + offset * side.getStepZ() : random.nextFloat();

            level.addParticle(data, pos.getX() + xOffset, pos.getY() + yOffset, pos.getZ() + zOffset, 0D, 0D, 0D);
        }
    }

    public static void quartzCrystalCluster(BlockState state, World level, BlockPos pos, Random random)
    {
        SparkleParticleData data = SparkleParticleData.builder()
                .color(0xFFFFFF80)
                .lifetimeFactor(6)
                .sizeFactor(0.5F)
                .build();

        float size = 6 / 16F;
        float height = 11 / 16F;
        Direction facing = state.getValue(QuartzCrystalClusterBlock.FACING);
        double xOffset = getOffset(random, facing, Direction.Axis.X, size, height);
        double yOffset = getOffset(random, facing, Direction.Axis.Y, size, height);
        double zOffset = getOffset(random, facing, Direction.Axis.Z, size, height);

        level.addParticle(data, pos.getX() + xOffset, pos.getY() + yOffset, pos.getZ() + zOffset, 0D, 0D, 0D);
    }

    private static double getOffset(Random random, Direction facing, Direction.Axis axis, float size, float height)
    {
        if (facing.getAxis() == axis)
            if (facing.getAxisDirection() == Direction.AxisDirection.POSITIVE)
                return random.nextFloat() * height;
            else
                return 1D - random.nextFloat() * height;
        else
            return 0.5D + (random.nextFloat() - 0.5D) * size;
    }
}

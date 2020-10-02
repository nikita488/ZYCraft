package nikita488.zycraft.util;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nikita488.zycraft.block.QuartzCrystalClusterBlock;
import nikita488.zycraft.particle.SparkleParticleData;
import nikita488.zycraft.tile.ColorableTile;

import java.util.Random;

public class ParticleSpawn
{
    private static final Direction[] VALUES = Direction.values();

    public static void glowingBlock(BlockState state, World world, BlockPos pos, Random rand)
    {
        glowingBlock(state, world, pos, rand, 0xFFFFFF7F);
    }

    public static void glowingColorableBlock(BlockState state, World world, BlockPos pos, Random rand)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof ColorableTile)
            glowingBlock(state, world, pos, rand, Color4b.rgba(((ColorableTile)tile).rgb(), 192));
    }

    public static void glowingBlock(BlockState state, World world, BlockPos pos, Random rand, int rgba)
    {
        SparkleParticleData data = SparkleParticleData.builder()
                .color(rgba)
                .ageFactor(6)
                .scaleFactor(0.5F)
                .noClip()
                .motionless()
                .build();

        double offset = 9 / 16F;

        for (Direction dir : VALUES)
        {
            if (rand.nextFloat() > 0.15F)
                continue;

            BlockPos adjPos = pos.offset(dir);
            BlockState adjState = world.getBlockState(adjPos);
            if (state == adjState || adjState.hasOpaqueCollisionShape(world, adjPos))
                continue;

            Direction.Axis axis = dir.getAxis();
            double xOffset = axis == Direction.Axis.X ? 0.5D + offset * dir.getXOffset() : rand.nextFloat();
            double yOffset = axis == Direction.Axis.Y ? 0.5D + offset * dir.getYOffset() : rand.nextFloat();
            double zOffset = axis == Direction.Axis.Z ? 0.5D + offset * dir.getZOffset() : rand.nextFloat();

            world.addParticle(data, pos.getX() + xOffset, pos.getY() + yOffset, pos.getZ() + zOffset, 0, 0, 0);
        }
    }

    public static void quartzCrystalCluster(BlockState state, World world, BlockPos pos, Random rand)
    {
        SparkleParticleData data = SparkleParticleData.builder()
                .color(0xFFFFFF80)
                .ageFactor(6)
                .scaleFactor(0.5F)
                .noClip()
                .motionless()
                .build();

        float size = 6 / 16F;
        float height = 11 / 16F;
        Direction dir = state.get(QuartzCrystalClusterBlock.FACING);
        double xOffset = getOffset(rand, dir, Direction.Axis.X, size, height);
        double yOffset = getOffset(rand, dir, Direction.Axis.Y, size, height);
        double zOffset = getOffset(rand, dir, Direction.Axis.Z, size, height);

        world.addParticle(data, pos.getX() + xOffset, pos.getY() + yOffset, pos.getZ() + zOffset, 0, 0, 0);
    }

    private static double getOffset(Random rand, Direction dir, Direction.Axis axis, float size, float height)
    {
        if (dir.getAxis() == axis)
            if (dir.getAxisDirection() == Direction.AxisDirection.POSITIVE)
                return rand.nextFloat() * height;
            else
                return 1.0F - rand.nextFloat() * height;
        else
            return 0.5D + (rand.nextFloat() - 0.5D) * size;
    }
}

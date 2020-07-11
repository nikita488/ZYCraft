package com.nikita488.zycraft.util;

import com.nikita488.zycraft.block.QuartzCrystalBlock;
import com.nikita488.zycraft.particle.SparkleParticleData;
import com.nikita488.zycraft.tile.ColorableTile;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.particle.DiggingParticle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import java.util.Random;

public class ParticleSpawn
{
    public static void glowingBlock(World world, BlockPos pos, Random rand)
    {
        glowingBlock(world, pos, rand, 0xFFFFFF7F);
    }

    public static void glowingColorableBlock(World world, BlockPos pos, Random rand)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof ColorableTile)
            glowingBlock(world, pos, rand, Color4b.rgba(((ColorableTile)tile).color().rgb(), 192));
    }

    public static void glowingBlock(World world, BlockPos pos, Random rand, int rgba)
    {
        SparkleParticleData data = SparkleParticleData.builder()
                .color(rgba)
                .ageFactor(6)
                .scaleFactor(0.5F)
                .noClip()
                .zeroMotion()
                .build();

        double offset = 9 / 16F;

        for (Direction dir : Direction.values())
        {
            BlockPos offsetPos = pos.offset(dir);
            if (world.getBlockState(offsetPos).isOpaqueCube(world, offsetPos))
                continue;

            Direction.Axis axis = dir.getAxis();
            double xOffset = axis == Direction.Axis.X ? 0.5D + offset * dir.getXOffset() : rand.nextFloat();
            double yOffset = axis == Direction.Axis.Y ? 0.5D + offset * dir.getYOffset() : rand.nextFloat();
            double zOffset = axis == Direction.Axis.Z ? 0.5D + offset * dir.getZOffset() : rand.nextFloat();
            //if (rand.nextInt(16) == 0)
                world.addParticle(data, pos.getX() + xOffset, pos.getY() + yOffset, pos.getZ() + zOffset, 0, 0, 0);
        }

        //onAttachWorldCap.addParticle(data, pos.getX() + 0.03125F, pos.getY() - 0.03125F, pos.getZ()  + 0.03125F, 0, 0, 0);
    }

    public static void quartzCrystal(BlockState state, World world, BlockPos pos, Random rand)
    {
        SparkleParticleData data = SparkleParticleData.builder()
                .color(0xFFFFFF80)
                .ageFactor(6)
                .scaleFactor(0.5F)
                .noClip()
                .zeroMotion()
                .build();

        float size = 6 / 16F;
        float height = 11 / 16F;
        Direction dir = state.get(QuartzCrystalBlock.FACING);
        double xOffset = getOffset(rand, dir, Direction.Axis.X, size, height);
        double yOffset = getOffset(rand, dir, Direction.Axis.Y, size, height);
        double zOffset = getOffset(rand, dir, Direction.Axis.Z, size, height);
        //if (rand.nextInt(5) == 0)
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

    public static void blockHitEffects(BlockState state, World world, BlockRayTraceResult target, ParticleManager manager)
    {
        if (state.getRenderType() == BlockRenderType.INVISIBLE)
            return;

        BlockPos pos = target.getPos();
        Direction side = target.getFace();
        int blockX = pos.getX();
        int blockY = pos.getY();
        int blockZ = pos.getZ();
        Random rand = world.getRandom();
        AxisAlignedBB aabb = state.getShape(world, pos).getBoundingBox();
        double x = (double)blockX + rand.nextDouble() * (aabb.maxX - aabb.minX - (double)0.2F) + (double)0.1F + aabb.minX;
        double y = (double)blockY + rand.nextDouble() * (aabb.maxY - aabb.minY - (double)0.2F) + (double)0.1F + aabb.minY;
        double z = (double)blockZ + rand.nextDouble() * (aabb.maxZ - aabb.minZ - (double)0.2F) + (double)0.1F + aabb.minZ;

        switch (side)
        {
            case DOWN:
                y = (double)blockY + aabb.minY - (double)0.1F;
                break;
            case UP:
                y = (double)blockY + aabb.maxY + (double)0.1F;
                break;
            case NORTH:
                z = (double)blockZ + aabb.minZ - (double)0.1F;
                break;
            case SOUTH:
                z = (double)blockZ + aabb.maxZ + (double)0.1F;
                break;
            case WEST:
                x = (double)blockX + aabb.minX - (double)0.1F;
                break;
            case EAST:
                x = (double)blockX + aabb.maxX + (double)0.1F;
                break;
        }

        manager.addEffect(new DiggingParticle(world, x, y, z, 0.0D, 0.0D, 0.0D, state)
                .setBlockPos(pos)
                .multiplyVelocity(0.2F)
                .multiplyParticleScaleBy(0.6F));
    }
}

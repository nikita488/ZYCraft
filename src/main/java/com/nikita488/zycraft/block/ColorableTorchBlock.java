package com.nikita488.zycraft.block;

import com.nikita488.zycraft.particle.SparkleParticleData;
import com.nikita488.zycraft.util.Color4b;
import net.minecraft.block.BlockState;
import net.minecraft.block.TorchBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class ColorableTorchBlock extends TorchBlock
{
    public ColorableTorchBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand)
    {
        double x = (double)pos.getX() + 0.5D;
        double y = (double)pos.getY() + 0.7D;
        double z = (double)pos.getZ() + 0.5D;
        SparkleParticleData data = SparkleParticleData.builder()
                .color(Color4b.rgba(0, 255, 255, 255))
                .ageFactor(6)
                .scaleFactor(0.5F)
                .noClip()
                .zeroMotion()
                .gravity(-0.1F)
                .build();
        world.addParticle(data, x, y, z, 0, 0, 0);
        world.addParticle(data, x, y, z, 0, 0, 0);
    }
}

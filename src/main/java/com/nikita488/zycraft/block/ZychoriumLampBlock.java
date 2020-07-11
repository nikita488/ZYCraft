package com.nikita488.zycraft.block;

import com.nikita488.zycraft.tile.ColorableTile;
import com.nikita488.zycraft.util.Color4b;
import com.nikita488.zycraft.util.ParticleSpawn;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.Random;

public class ZychoriumLampBlock extends ColorableBlock
{
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    private final boolean inverted;

    public ZychoriumLampBlock(boolean inverted, Properties properties)
    {
        super(properties);
        this.inverted = inverted;
        setDefaultState(getDefaultState().with(LIT, inverted));
    }

    @Override
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand)
    {
        if (state.get(LIT))
            ParticleSpawn.glowingColorableBlock(world, pos, rand);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getDefaultState().with(LIT, inverted != context.getWorld().isBlockPowered(context.getPos()));
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving)
    {
        if (world.isRemote)
            return;

        boolean lit = isLit(state);
        if (lit == world.isBlockPowered(pos))
            return;
        if (lit)
            world.getPendingBlockTicks().scheduleTick(pos, this, 4);
        else
            world.setBlockState(pos, state.cycle(LIT), Constants.BlockFlags.BLOCK_UPDATE);
    }

    @Override
    public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random)
    {
        if (isLit(state) && !world.isBlockPowered(pos))
            world.setBlockState(pos, state.cycle(LIT), Constants.BlockFlags.BLOCK_UPDATE);
    }

    private boolean isLit(BlockState state)
    {
        return inverted != state.get(LIT);
    }

    @Override
    public int getLightValue(BlockState state)
    {
        return state.get(LIT) ? super.getLightValue(state) : 0;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(LIT);
    }

    @Override
    public int getColor(BlockState state, ColorableTile tile)
    {
        return litColor(tile.color().rgb(), litAmount(tile.getWorld(), tile.getPos()));
    }

    private float litAmount(World world, BlockPos pos)
    {
        int strength = world.getRedstonePowerFromNeighbors(pos);
        return 0.25F + 0.05F * (inverted ? 15 - strength : strength);
    }

    private int litColor(int baseColor, float litAmount)
    {
        litAmount = MathHelper.clamp(litAmount, 0, 1);
        int r = (int)(Color4b.component(baseColor, 1) * litAmount);
        int g = (int)(Color4b.component(baseColor, 2) * litAmount);
        int b = (int)(Color4b.component(baseColor, 3) * litAmount);
        return (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
    }
}

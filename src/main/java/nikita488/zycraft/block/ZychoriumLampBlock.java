package nikita488.zycraft.block;

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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import nikita488.zycraft.tile.ColorableTile;
import nikita488.zycraft.util.Color4b;
import nikita488.zycraft.util.ParticleSpawn;

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
        registerDefaultState(defaultBlockState().setValue(LIT, inverted));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return defaultBlockState().setValue(LIT, inverted != context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving)
    {
        if (world.isClientSide)
            return;

        boolean lit = isLit(state);

        if (lit != world.hasNeighborSignal(pos))
            if (lit)
                world.getBlockTicks().scheduleTick(pos, this, 4);
            else
                world.setBlock(pos, state.cycle(LIT), Constants.BlockFlags.BLOCK_UPDATE);
    }

    @Override
    public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random)
    {
        if (isLit(state) && !world.hasNeighborSignal(pos))
            world.setBlock(pos, state.cycle(LIT), Constants.BlockFlags.BLOCK_UPDATE);
    }

    private boolean isLit(BlockState state)
    {
        return inverted != state.getValue(LIT);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(LIT);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand)
    {
        if (state.getValue(LIT))
            ParticleSpawn.glowingColorableBlock(state, world, pos, rand);
    }

    @Override
    public int getColor(BlockState state, ColorableTile colorable, int tintIndex)
    {
        return litColor(colorable.color(), litAmount(colorable.getLevel(), colorable.getBlockPos()));
    }

    private float litAmount(World world, BlockPos pos)
    {
        int strength = world.getBestNeighborSignal(pos);
        return 0.25F + 0.05F * (inverted ? 15 - strength : strength);
    }

    private int litColor(Color4b baseColor, float litAmount)
    {
        litAmount = MathHelper.clamp(litAmount, 0, 1);
        int r = (int)(baseColor.r() * litAmount);
        int g = (int)(baseColor.g() * litAmount);
        int b = (int)(baseColor.b() * litAmount);
        return Color4b.rgb(r, g, b);
    }
}

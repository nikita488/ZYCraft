package nikita488.zycraft.multiblock.child.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.ToolType;
import nikita488.zycraft.block.state.properties.ZYBlockStateProperties;
import nikita488.zycraft.init.ZYTiles;
import nikita488.zycraft.multiblock.child.tile.ConvertedMultiChildTile;
import team.chisel.ctm.api.IFacade;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

@SuppressWarnings("deprecation")
public class ConvertedMultiChildBlock extends MultiChildBlock implements IFacade
{
    public static final BooleanProperty USE_SHAPE_FOR_LIGHT_OCCLUSION = ZYBlockStateProperties.USE_SHAPE_FOR_LIGHT_OCCLUSION;
    public static final BooleanProperty SIGNAL_SOURCE = ZYBlockStateProperties.SIGNAL_SOURCE;
    public static final BooleanProperty HAS_ANALOG_OUTPUT_SIGNAL = ZYBlockStateProperties.HAS_ANALOG_OUTPUT_SIGNAL;

    public ConvertedMultiChildBlock(Properties properties)
    {
        super(properties);
        setDefaultState(getDefaultState().with(USE_SHAPE_FOR_LIGHT_OCCLUSION, false).with(SIGNAL_SOURCE, false).with(HAS_ANALOG_OUTPUT_SIGNAL, false));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return ZYTiles.CONVERTED_MULTI_CHILD.create();
    }

    private static BlockState getState(IBlockReader world, BlockPos pos)
    {
        TileEntity tile = world.getTileEntity(pos);
        return tile instanceof ConvertedMultiChildTile ? ((ConvertedMultiChildTile)tile).initialState() : Blocks.AIR.getDefaultState();
    }
    //AbstractBlock.Properties methods
    public static boolean isValidSpawn(BlockState state, IBlockReader world, BlockPos pos, EntityType<?> type)
    {
        return getState(world, pos).canEntitySpawn(world, pos, type);
    }

    public static boolean isRedstoneConductor(BlockState state, IBlockReader world, BlockPos pos)
    {
        return getState(world, pos).isNormalCube(world, pos);
    }

    public static boolean isSuffocating(BlockState state, IBlockReader world, BlockPos pos)
    {
        return getState(world, pos).isSuffocating(world, pos);
    }

    public static boolean isViewBlocking(BlockState state, IBlockReader world, BlockPos pos)
    {
        return getState(world, pos).shouldBlockVision(world, pos);
    }

    public static boolean emissiveRendering(BlockState state, IBlockReader world, BlockPos pos)
    {
        return getState(world, pos).isEmissiveRendering(world, pos);
    }
    //AbstactBlock methods
/*    //TODO: Probably remove
    @Override
    public void updateDiagonalNeighbors(BlockState state, IWorld world, BlockPos pos, int flags, int recursionLeft)
    {
        getState(world, pos).updateDiagonalNeighbors(world, pos, flags, recursionLeft);
    }

    //TODO: Probably false
    @Override
    public boolean allowsMovement(BlockState state, IBlockReader world, BlockPos pos, PathType type)
    {
        return getState(world, pos).allowsMovement(world, pos, type);
    }

    //TODO: Probably remove
    @Override
    public BlockState updatePostPlacement(BlockState state, Direction side, BlockState adjState, IWorld world, BlockPos pos, BlockPos adjPos)
    {
        TileEntity tile = world.getTileEntity(pos);

        if (!(tile instanceof DefaultMultiChildTile))
            return state;

        DefaultMultiChildTile child = (DefaultMultiChildTile)tile;
        BlockState oldState = child.state();
        BlockState newState = oldState.updatePostPlacement(side, adjState, world, pos, adjPos);

        if (oldState != newState && !newState.isAir())
            child.setState(newState);

        return state;
    }

    //TODO: Probably remove
    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos adjPos, boolean isMoving)
    {
        getState(world, pos).neighborChanged(world, pos, block, adjPos, isMoving);
    }*/

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving)
    {
        getState(world, pos).onReplaced(world, pos, newState, isMoving);
        super.onReplaced(state, world, pos, newState, isMoving);
    }

/*    //TODO: Probably remove
    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        return super.onBlockActivated(state, world, pos, player, hand, hit);
    }*/

/*    //TODO: Probably remove
    @Override
    public boolean eventReceived(BlockState state, World world, BlockPos pos, int id, int param)
    {
        return getState(world, pos).receiveBlockEvent(world, pos, id, param);
    }*/

    @Override
    public boolean isTransparent(BlockState state)
    {
        return state.get(USE_SHAPE_FOR_LIGHT_OCCLUSION);
    }

    @Override
    public boolean canProvidePower(BlockState state)
    {
        return state.get(SIGNAL_SOURCE);
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState state)
    {
        return state.get(HAS_ANALOG_OUTPUT_SIGNAL);
    }

    @Override
    public boolean isReplaceable(BlockState state, BlockItemUseContext ctx)
    {
        return false;
    }

    @Override
    public boolean isReplaceable(BlockState state, Fluid fluid)
    {
        return false;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder)
    {
        TileEntity tile = builder.get(LootParameters.BLOCK_ENTITY);
        return tile instanceof ConvertedMultiChildTile ? ((ConvertedMultiChildTile)tile).initialState().getDrops(builder) : super.getDrops(state, builder);
    }

    @Override
    public int getOpacity(BlockState state, IBlockReader world, BlockPos pos)
    {
        return getState(world, pos).getOpacity(world, pos);
    }

/*    //TODO: Probably remove
    @Nullable
    @Override
    public INamedContainerProvider getContainer(BlockState state, World world, BlockPos pos)
    {
        return getState(world, pos).getContainer(world, pos);
    }

    //TODO: Probably remove
    @Override
    public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos)
    {
        return getState(world, pos).isValidPosition(world, pos);
    }*/

    @Override
    @OnlyIn(Dist.CLIENT)
    public float getAmbientOcclusionLightValue(BlockState state, IBlockReader world, BlockPos pos)
    {
        return getState(world, pos).getAmbientOcclusionLightValue(world, pos);
    }

    @Override
    public int getComparatorInputOverride(BlockState state, World world, BlockPos pos)
    {
        return getState(world, pos).getComparatorInputOverride(world, pos);
    }

/*    //TODO: Probably remove
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
    {
        getState(world, pos).randomTick(world, pos, random);
    }

    //TODO: Probably remove
    @Override
    public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand)
    {
        getState(world, pos).tick(world, pos, rand);
    }*/

    @Override
    public float getPlayerRelativeBlockHardness(BlockState state, PlayerEntity player, IBlockReader world, BlockPos pos)
    {
        return getState(world, pos).getPlayerRelativeBlockHardness(player, world, pos);
    }

    @Override
    public void spawnAdditionalDrops(BlockState state, ServerWorld world, BlockPos pos, ItemStack stack)
    {
        getState(world, pos).spawnAdditionalDrops(world, pos, stack);
    }

/*    //TODO: Probably remove?
    @Override
    public void onBlockClicked(BlockState state, World world, BlockPos pos, PlayerEntity player)
    {
        getState(world, pos).onBlockClicked(world, pos, player);
    }*/

    @Override
    public int getWeakPower(BlockState state, IBlockReader world, BlockPos pos, Direction side)
    {
        return getState(world, pos).getWeakPower(world, pos, side);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity)
    {
        getState(world, pos).onEntityCollision(world, pos, entity);
    }

    @Override
    public int getStrongPower(BlockState state, IBlockReader world, BlockPos pos, Direction side)
    {
        return getState(world, pos).getStrongPower(world, pos, side);
    }

    @Override
    public void onProjectileCollision(World world, BlockState state, BlockRayTraceResult hit, ProjectileEntity projectile)
    {
        getState(world, hit.getPos()).onProjectileCollision(world, state, hit, projectile);
    }
    //Block methods
/*    //TODO: Probably remove
    @Override
    public boolean ticksRandomly(BlockState state) 
    {
        return state.get(RANDOMLY_TICKING);
    }*/

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader world, BlockPos pos) 
    {
        return getState(world, pos).propagatesSkylightDown(world, pos);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand) 
    {
        getState(world, pos).getBlock().animateTick(state, world, pos, rand);
    }

    @Override
    public void onPlayerDestroy(IWorld world, BlockPos pos, BlockState state) 
    {
        getState(world, pos).getBlock().onPlayerDestroy(world, pos, state);
    }

    @Override
    public void dropXpOnBlockBreak(ServerWorld world, BlockPos pos, int amount) 
    {
        getState(world, pos).getBlock().dropXpOnBlockBreak(world, pos, amount);
    }

    @Override
    public void onExplosionDestroy(World world, BlockPos pos, Explosion explosion) 
    {
        getState(world, pos).getBlock().onExplosionDestroy(world, pos, explosion);
    }

    @Override
    public void onEntityWalk(World world, BlockPos pos, Entity entity) 
    {
        getState(world, pos).getBlock().onEntityWalk(world, pos, entity);
    }

    @Override
    public void harvestBlock(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity tile, ItemStack stack)
    {
        getState(world, pos).getBlock().harvestBlock(world, player, pos, state, tile, stack);
    }

    @Override
    public void onFallenUpon(World world, BlockPos pos, Entity entity, float fallDistance) 
    {
        getState(world, pos).getBlock().onFallenUpon(world, pos, entity, fallDistance);
    }

    @Override
    public void onLanded(IBlockReader world, Entity entity) 
    {
        getState(world, entity.getPosition()).getBlock().onLanded(world, entity);
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) 
    {
        getState(world, pos).getBlock().onBlockHarvested(world, pos, state, player);
    }

/*    //TODO: Probably remove
    @Override
    public void fillWithRain(World world, BlockPos pos) 
    {
        getState(world, pos).getBlock().fillWithRain(world, pos);
    }*/

    @Override
    public float getSlipperiness(BlockState state, IWorldReader world, BlockPos pos, @Nullable Entity entity) 
    {
        return getState(world, pos).getSlipperiness(world, pos, entity);
    }

    @Override
    public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, IPlantable plantable) 
    {
        return getState(world, pos).canSustainPlant(world, pos, facing, plantable);
    }
    //IForgeBlock methods
    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos)
    {
        return getState(world, pos).getLightValue(world, pos);
    }

    @Override
    public boolean isLadder(BlockState state, IWorldReader world, BlockPos pos, LivingEntity entity) 
    {
        return getState(world, pos).isLadder(world, pos, entity);
    }

    @Override
    public boolean isBurning(BlockState state, IBlockReader world, BlockPos pos) 
    {
        return getState(world, pos).isBurning(world, pos);
    }

    @Override
    public boolean canHarvestBlock(BlockState state, IBlockReader world, BlockPos pos, PlayerEntity player) 
    {
        return getState(world, pos).canHarvestBlock(world, pos, player);
    }

    @Override
    public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, FluidState fluid) 
    {
        return getState(world, pos).removedByPlayer(world, pos, player, willHarvest, fluid);
    }

    @Override
    public boolean canCreatureSpawn(BlockState state, IBlockReader world, BlockPos pos, EntitySpawnPlacementRegistry.PlacementType type, @Nullable EntityType<?> entityType)
    {
        return getState(world, pos).canCreatureSpawn((IWorldReader)world, pos, type, entityType);
    }

    @Override
    public boolean canBeReplacedByLeaves(BlockState state, IWorldReader world, BlockPos pos) 
    {
        return false;
    }

    @Override
    public boolean canBeReplacedByLogs(BlockState state, IWorldReader world, BlockPos pos) 
    {
        return false;
    }

    @Override
    public float getExplosionResistance(BlockState state, IBlockReader world, BlockPos pos, Explosion explosion) 
    {
        return getState(world, pos).getExplosionResistance(world, pos, explosion);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side)
    {
        return getState(world, pos).canConnectRedstone(world, pos, side);
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) 
    {
        return getState(world, pos).getPickBlock(target, world, pos, player);
    }

    @Override
    public boolean addLandingEffects(BlockState state, ServerWorld world, BlockPos pos, BlockState unused, LivingEntity entity, int numberOfParticles)
    {
        return getState(world, pos).addLandingEffects(world, pos, state, entity, numberOfParticles);
    }

    @Override
    public boolean addRunningEffects(BlockState state, World world, BlockPos pos, Entity entity) 
    {
        return getState(world, pos).addRunningEffects(world, pos, entity);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean addHitEffects(BlockState state, World world, RayTraceResult target, ParticleManager manager)
    {
        return getState(world, ((BlockRayTraceResult)target).getPos()).addHitEffects(world, target, manager);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean addDestroyEffects(BlockState state, World world, BlockPos pos, ParticleManager manager) 
    {
        return getState(world, pos).addDestroyEffects(world, pos, manager);
    }

    @Override
    public boolean isFertile(BlockState state, IBlockReader world, BlockPos pos) 
    {
        return getState(world, pos).isFertile(world, pos);
    }

    @Override
    public boolean isConduitFrame(BlockState state, IWorldReader world, BlockPos pos, BlockPos conduit) 
    {
        return getState(world, pos).isConduitFrame(world, pos, conduit);
    }

    @Override
    public boolean isPortalFrame(BlockState state, IBlockReader world, BlockPos pos) 
    {
        return getState(world, pos).isPortalFrame(world, pos);
    }

    @Override
    public int getExpDrop(BlockState state, IWorldReader world, BlockPos pos, int fortuneLevel, int silkTouch)
    {
        return getState(world, pos).getExpDrop(world, pos, fortuneLevel, silkTouch);
    }

    @Override
    public float getEnchantPowerBonus(BlockState state, IWorldReader world, BlockPos pos) 
    {
        return getState(world, pos).getEnchantPowerBonus(world, pos);
    }

/*    //TODO: Probably remove
    @Override
    public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos adjPos)
    {
        getState(world, pos).onNeighborChange(world, pos, adjPos);
    }*/

    @Override
    public boolean shouldCheckWeakPower(BlockState state, IWorldReader world, BlockPos pos, Direction side) 
    {
        return getState(world, pos).shouldCheckWeakPower(world, pos, side);
    }

    @Override
    public boolean getWeakChanges(BlockState state, IWorldReader world, BlockPos pos) 
    {
        return getState(world, pos).getWeakChanges(world, pos);
    }

    @Override
    public SoundType getSoundType(BlockState state, IWorldReader world, BlockPos pos, @Nullable Entity entity)
    {
        return getState(world, pos).getSoundType(world, pos, entity);
    }

    @Nullable
    @Override
    public float[] getBeaconColorMultiplier(BlockState state, IWorldReader world, BlockPos pos, BlockPos beaconPos) 
    {
        return getState(world, pos).getBeaconColorMultiplier(world, pos, beaconPos);
    }

    @Override
    public BlockState getStateAtViewpoint(BlockState state, IBlockReader world, BlockPos pos, Vector3d viewpoint) 
    {
        return getState(world, pos).getStateAtViewpoint(world, pos, viewpoint);
    }

    @Nullable
    @Override
    public PathNodeType getAiPathNodeType(BlockState state, IBlockReader world, BlockPos pos, @Nullable MobEntity entity)
    {
        return getState(world, pos).getAiPathNodeType(world, pos, entity);
    }
    //FLAMMABLE START
    @Override
    public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction side)
    {
        return getState(world, pos).getFlammability(world, pos, side);
    }

    @Override
    public boolean isFlammable(BlockState state, IBlockReader world, BlockPos pos, Direction face) 
    {
        return getState(world, pos).isFlammable(world, pos, face);
    }

/*    //TODO: Probably remove
    @Override
    public void catchFire(BlockState state, World world, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter)
    {
        getState(world, pos).catchFire(world, pos, face, igniter);
    }*/

    @Override
    public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction side)
    {
        return getState(world, pos).getFireSpreadSpeed(world, pos, side);
    }

    @Override
    public boolean isFireSource(BlockState state, IWorldReader world, BlockPos pos, Direction side) 
    {
        return getState(world, pos).isFireSource(world, pos, side);
    }
    //FLAMMABLE END
    @Override
    public boolean canEntityDestroy(BlockState state, IBlockReader world, BlockPos pos, Entity entity) 
    {
        return getState(world, pos).canEntityDestroy(world, pos, entity);
    }

    @Override
    public boolean canDropFromExplosion(BlockState state, IBlockReader world, BlockPos pos, Explosion explosion) 
    {
        return getState(world, pos).canDropFromExplosion(world, pos, explosion);
    }

    @Override
    public void onBlockExploded(BlockState state, World world, BlockPos pos, Explosion explosion) 
    {
        getState(world, pos).onBlockExploded(world, pos, explosion);
    }

    @Override
    public boolean collisionExtendsVertically(BlockState state, IBlockReader world, BlockPos pos, Entity collidingEntity) 
    {
        return getState(world, pos).collisionExtendsVertically(world, pos, collidingEntity);
    }

    @Override
    public boolean shouldDisplayFluidOverlay(BlockState state, IBlockDisplayReader world, BlockPos pos, FluidState fluidState) 
    {
        return getState(world, pos).shouldDisplayFluidOverlay(world, pos, fluidState);
    }

    @Nullable
    @Override
    public BlockState getToolModifiedState(BlockState state, World world, BlockPos pos, PlayerEntity player, ItemStack stack, ToolType toolType) 
    {
        return null;
    }

    @Override
    public boolean isScaffolding(BlockState state, IWorldReader world, BlockPos pos, LivingEntity entity) 
    {
        return getState(world, pos).isScaffolding(entity);
    }

    @Override
    public Vector3d getFogColor(BlockState state, IWorldReader world, BlockPos pos, Entity entity, Vector3d originalColor, float partialTicks)
    {
        return getState(world, pos).getFogColor(world, pos, entity, originalColor, partialTicks);
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation rotation)
    {
        return getState(world, pos).rotate(world, pos, rotation);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(USE_SHAPE_FOR_LIGHT_OCCLUSION, SIGNAL_SOURCE, HAS_ANALOG_OUTPUT_SIGNAL);
    }

    @Nonnull
    @Override
    public BlockState getFacade(@Nonnull IBlockReader world, @Nonnull BlockPos pos, @Nullable Direction side)
    {
        return getState(world, pos);
    }
}

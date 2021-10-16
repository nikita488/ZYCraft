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
        registerDefaultState(defaultBlockState().setValue(USE_SHAPE_FOR_LIGHT_OCCLUSION, false).setValue(SIGNAL_SOURCE, false).setValue(HAS_ANALOG_OUTPUT_SIGNAL, false));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader getter)
    {
        return ZYTiles.CONVERTED_MULTI_CHILD.create();
    }

    private static BlockState getState(IBlockReader getter, BlockPos pos)
    {
        TileEntity blockEntity = getter.getBlockEntity(pos);
        return blockEntity instanceof ConvertedMultiChildTile ? ((ConvertedMultiChildTile)blockEntity).initialState() : Blocks.AIR.defaultBlockState();
    }
    //AbstractBlock.Properties methods
    public static boolean isValidSpawn(BlockState state, IBlockReader getter, BlockPos pos, EntityType<?> type)
    {
        return getState(getter, pos).isValidSpawn(getter, pos, type);
    }

    public static boolean isRedstoneConductor(BlockState state, IBlockReader getter, BlockPos pos)
    {
        return getState(getter, pos).isRedstoneConductor(getter, pos);
    }

    public static boolean isSuffocating(BlockState state, IBlockReader getter, BlockPos pos)
    {
        return getState(getter, pos).isSuffocating(getter, pos);
    }

    public static boolean isViewBlocking(BlockState state, IBlockReader getter, BlockPos pos)
    {
        return getState(getter, pos).isViewBlocking(getter, pos);
    }

    public static boolean emissiveRendering(BlockState state, IBlockReader getter, BlockPos pos)
    {
        return getState(getter, pos).emissiveRendering(getter, pos);
    }
    //AbstactBlock methods
/*    //TODO: Probably remove
    @Override
    public void updateDiagonalNeighbors(BlockState state, IWorld accessor, BlockPos pos, int flags, int recursionLeft)
    {
        getState(world, pos).updateDiagonalNeighbors(world, pos, flags, recursionLeft);
    }

    //TODO: Probably false
    @Override
    public boolean allowsMovement(BlockState state, IBlockReader getter, BlockPos pos, PathType type)
    {
        return getState(world, pos).allowsMovement(world, pos, type);
    }

    //TODO: Probably remove
    @Override
    public BlockState updatePostPlacement(BlockState state, Direction side, BlockState adjState, IWorld accessor, BlockPos pos, BlockPos adjPos)
    {
        return state;
    }

    //TODO: Probably remove
    @Override
    public void neighborChanged(BlockState state, World level, BlockPos pos, Block block, BlockPos adjPos, boolean isMoving)
    {
        getState(world, pos).neighborChanged(world, pos, block, adjPos, isMoving);
    }*/

    @Override
    public void onRemove(BlockState state, World level, BlockPos pos, BlockState newState, boolean isMoving)
    {
        getState(level, pos).onRemove(level, pos, newState, isMoving);
        super.onRemove(state, level, pos, newState, isMoving);
    }

/*    //TODO: Probably remove
    @Override
    public ActionResultType onBlockActivated(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hitResult)
    {
        return super.onBlockActivated(state, world, pos, player, hand, hit);
    }*/

/*    //TODO: Probably remove
    @Override
    public boolean eventReceived(BlockState state, World level, BlockPos pos, int id, int param)
    {
        return getState(world, pos).receiveBlockEvent(world, pos, id, param);
    }*/

    @Override
    public boolean useShapeForLightOcclusion(BlockState state)
    {
        return state.getValue(USE_SHAPE_FOR_LIGHT_OCCLUSION);
    }

    @Override
    public boolean isSignalSource(BlockState state)
    {
        return state.getValue(SIGNAL_SOURCE);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state)
    {
        return state.getValue(HAS_ANALOG_OUTPUT_SIGNAL);
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockItemUseContext ctx)
    {
        return false;
    }

    @Override
    public boolean canBeReplaced(BlockState state, Fluid fluid)
    {
        return false;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder)
    {
        TileEntity blockEntity = builder.getOptionalParameter(LootParameters.BLOCK_ENTITY);
        return blockEntity instanceof ConvertedMultiChildTile ? ((ConvertedMultiChildTile)blockEntity).initialState().getDrops(builder) : super.getDrops(state, builder);
    }

    @Override
    public int getLightBlock(BlockState state, IBlockReader getter, BlockPos pos)
    {
        return getState(getter, pos).getLightBlock(getter, pos);
    }

/*    //TODO: Probably remove
    @Nullable
    @Override
    public INamedContainerProvider getContainer(BlockState state, World level, BlockPos pos)
    {
        return getState(world, pos).getContainer(world, pos);
    }

    //TODO: Probably remove
    @Override
    public boolean isValidPosition(BlockState state, IWorldReader reader, BlockPos pos)
    {
        return getState(world, pos).isValidPosition(world, pos);
    }*/

    @Override
    @OnlyIn(Dist.CLIENT)
    public float getShadeBrightness(BlockState state, IBlockReader getter, BlockPos pos)
    {
        return getState(getter, pos).getShadeBrightness(getter, pos);
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, World level, BlockPos pos)
    {
        return getState(level, pos).getAnalogOutputSignal(level, pos);
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
    public float getDestroyProgress(BlockState state, PlayerEntity player, IBlockReader getter, BlockPos pos)
    {
        return getState(getter, pos).getDestroyProgress(player, getter, pos);
    }

    @Override
    public void spawnAfterBreak(BlockState state, ServerWorld world, BlockPos pos, ItemStack stack)
    {
        getState(world, pos).spawnAfterBreak(world, pos, stack);
    }

/*    //TODO: Probably remove?
    @Override
    public void onBlockClicked(BlockState state, World level, BlockPos pos, PlayerEntity player)
    {
        getState(world, pos).onBlockClicked(world, pos, player);
    }*/

    @Override
    public int getSignal(BlockState state, IBlockReader getter, BlockPos pos, Direction side)
    {
        return getState(getter, pos).getSignal(getter, pos, side);
    }

    @Override
    public void entityInside(BlockState state, World level, BlockPos pos, Entity entity)
    {
        getState(level, pos).entityInside(level, pos, entity);
    }

    @Override
    public int getDirectSignal(BlockState state, IBlockReader getter, BlockPos pos, Direction side)
    {
        return getState(getter, pos).getDirectSignal(getter, pos, side);
    }

    @Override
    public void onProjectileHit(World level, BlockState state, BlockRayTraceResult hitResult, ProjectileEntity projectile)
    {
        getState(level, hitResult.getBlockPos()).onProjectileHit(level, state, hitResult, projectile);
    }
    //Block methods
/*    //TODO: Probably remove
    @Override
    public boolean ticksRandomly(BlockState state) 
    {
        return state.get(RANDOMLY_TICKING);
    }*/

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader getter, BlockPos pos)
    {
        return getState(getter, pos).propagatesSkylightDown(getter, pos);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, World level, BlockPos pos, Random random)
    {
        getState(level, pos).getBlock().animateTick(state, level, pos, random);
    }

    @Override
    public void destroy(IWorld accessor, BlockPos pos, BlockState state) 
    {
        getState(accessor, pos).getBlock().destroy(accessor, pos, state);
    }

    @Override
    public void popExperience(ServerWorld world, BlockPos pos, int amount) 
    {
        getState(world, pos).getBlock().popExperience(world, pos, amount);
    }

    @Override
    public void wasExploded(World level, BlockPos pos, Explosion explosion) 
    {
        getState(level, pos).getBlock().wasExploded(level, pos, explosion);
    }

    @Override
    public void stepOn(World level, BlockPos pos, Entity entity) 
    {
        getState(level, pos).getBlock().stepOn(level, pos, entity);
    }

    @Override
    public void playerDestroy(World level, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity blockEntity, ItemStack stack)
    {
        getState(level, pos).getBlock().playerDestroy(level, player, pos, state, blockEntity, stack);
    }

    @Override
    public void fallOn(World level, BlockPos pos, Entity entity, float fallDistance) 
    {
        getState(level, pos).getBlock().fallOn(level, pos, entity, fallDistance);
    }

    @Override
    public void updateEntityAfterFallOn(IBlockReader getter, Entity entity)
    {
        getState(getter, entity.blockPosition()).getBlock().updateEntityAfterFallOn(getter, entity);
    }

    @Override
    public void playerWillDestroy(World level, BlockPos pos, BlockState state, PlayerEntity player) 
    {
        getState(level, pos).getBlock().playerWillDestroy(level, pos, state, player);
    }

/*    //TODO: Probably remove
    @Override
    public void fillWithRain(World level, BlockPos pos) 
    {
        getState(world, pos).getBlock().fillWithRain(world, pos);
    }*/

    @Override
    public float getSlipperiness(BlockState state, IWorldReader reader, BlockPos pos, @Nullable Entity entity) 
    {
        return getState(reader, pos).getSlipperiness(reader, pos, entity);
    }

    @Override
    public boolean canSustainPlant(BlockState state, IBlockReader getter, BlockPos pos, Direction facing, IPlantable plantable)
    {
        return getState(getter, pos).canSustainPlant(getter, pos, facing, plantable);
    }
    //IForgeBlock methods
    @Override
    public int getLightValue(BlockState state, IBlockReader getter, BlockPos pos)
    {
        return getState(getter, pos).getLightValue(getter, pos);
    }

    @Override
    public boolean isLadder(BlockState state, IWorldReader reader, BlockPos pos, LivingEntity entity) 
    {
        return getState(reader, pos).isLadder(reader, pos, entity);
    }

    @Override
    public boolean isBurning(BlockState state, IBlockReader getter, BlockPos pos)
    {
        return getState(getter, pos).isBurning(getter, pos);
    }

    @Override
    public boolean canHarvestBlock(BlockState state, IBlockReader getter, BlockPos pos, PlayerEntity player)
    {
        return getState(getter, pos).canHarvestBlock(getter, pos, player);
    }

    @Override
    public boolean removedByPlayer(BlockState state, World level, BlockPos pos, PlayerEntity player, boolean willHarvest, FluidState fluid) 
    {
        return getState(level, pos).removedByPlayer(level, pos, player, willHarvest, fluid);
    }

    @Override
    public boolean canCreatureSpawn(BlockState state, IBlockReader getter, BlockPos pos, EntitySpawnPlacementRegistry.PlacementType type, @Nullable EntityType<?> entityType)
    {
        return getState(getter, pos).canCreatureSpawn((IWorldReader)getter, pos, type, entityType);
    }

    @Override
    public boolean canBeReplacedByLeaves(BlockState state, IWorldReader reader, BlockPos pos) 
    {
        return false;
    }

    @Override
    public boolean canBeReplacedByLogs(BlockState state, IWorldReader reader, BlockPos pos) 
    {
        return false;
    }

    @Override
    public float getExplosionResistance(BlockState state, IBlockReader getter, BlockPos pos, Explosion explosion)
    {
        return getState(getter, pos).getExplosionResistance(getter, pos, explosion);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockReader getter, BlockPos pos, @Nullable Direction side)
    {
        return getState(getter, pos).canConnectRedstone(getter, pos, side);
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader getter, BlockPos pos, PlayerEntity player)
    {
        return getState(getter, pos).getPickBlock(target, getter, pos, player);
    }

    @Override
    public boolean addLandingEffects(BlockState state, ServerWorld world, BlockPos pos, BlockState unused, LivingEntity entity, int numberOfParticles)
    {
        return getState(world, pos).addLandingEffects(world, pos, state, entity, numberOfParticles);
    }

    @Override
    public boolean addRunningEffects(BlockState state, World level, BlockPos pos, Entity entity) 
    {
        return getState(level, pos).addRunningEffects(level, pos, entity);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean addHitEffects(BlockState state, World level, RayTraceResult target, ParticleManager manager)
    {
        return getState(level, ((BlockRayTraceResult)target).getBlockPos()).addHitEffects(level, target, manager);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean addDestroyEffects(BlockState state, World level, BlockPos pos, ParticleManager manager) 
    {
        return getState(level, pos).addDestroyEffects(level, pos, manager);
    }

    @Override
    public boolean isFertile(BlockState state, IBlockReader getter, BlockPos pos)
    {
        return getState(getter, pos).isFertile(getter, pos);
    }

    @Override
    public boolean isConduitFrame(BlockState state, IWorldReader reader, BlockPos pos, BlockPos conduit) 
    {
        return getState(reader, pos).isConduitFrame(reader, pos, conduit);
    }

    @Override
    public boolean isPortalFrame(BlockState state, IBlockReader getter, BlockPos pos)
    {
        return getState(getter, pos).isPortalFrame(getter, pos);
    }

    @Override
    public int getExpDrop(BlockState state, IWorldReader reader, BlockPos pos, int fortuneLevel, int silkTouch)
    {
        return getState(reader, pos).getExpDrop(reader, pos, fortuneLevel, silkTouch);
    }

    @Override
    public float getEnchantPowerBonus(BlockState state, IWorldReader reader, BlockPos pos) 
    {
        return getState(reader, pos).getEnchantPowerBonus(reader, pos);
    }

/*    //TODO: Probably remove
    @Override
    public void onNeighborChange(BlockState state, IWorldReader reader, BlockPos pos, BlockPos adjPos)
    {
        getState(world, pos).onNeighborChange(world, pos, adjPos);
    }*/

    @Override
    public boolean shouldCheckWeakPower(BlockState state, IWorldReader reader, BlockPos pos, Direction side) 
    {
        return getState(reader, pos).shouldCheckWeakPower(reader, pos, side);
    }

    @Override
    public boolean getWeakChanges(BlockState state, IWorldReader reader, BlockPos pos) 
    {
        return getState(reader, pos).getWeakChanges(reader, pos);
    }

    @Override
    public SoundType getSoundType(BlockState state, IWorldReader reader, BlockPos pos, @Nullable Entity entity)
    {
        return getState(reader, pos).getSoundType(reader, pos, entity);
    }

    @Nullable
    @Override
    public float[] getBeaconColorMultiplier(BlockState state, IWorldReader reader, BlockPos pos, BlockPos beaconPos) 
    {
        return getState(reader, pos).getBeaconColorMultiplier(reader, pos, beaconPos);
    }

    @Override
    public BlockState getStateAtViewpoint(BlockState state, IBlockReader getter, BlockPos pos, Vector3d viewpoint)
    {
        return getState(getter, pos).getStateAtViewpoint(getter, pos, viewpoint);
    }

    @Nullable
    @Override
    public PathNodeType getAiPathNodeType(BlockState state, IBlockReader getter, BlockPos pos, @Nullable MobEntity entity)
    {
        return getState(getter, pos).getAiPathNodeType(getter, pos, entity);
    }
    //FLAMMABLE START
    @Override
    public int getFlammability(BlockState state, IBlockReader getter, BlockPos pos, Direction side)
    {
        return getState(getter, pos).getFlammability(getter, pos, side);
    }

    @Override
    public boolean isFlammable(BlockState state, IBlockReader getter, BlockPos pos, Direction face)
    {
        return getState(getter, pos).isFlammable(getter, pos, face);
    }

/*    //TODO: Probably remove
    @Override
    public void catchFire(BlockState state, World level, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter)
    {
        getState(world, pos).catchFire(world, pos, face, igniter);
    }*/

    @Override
    public int getFireSpreadSpeed(BlockState state, IBlockReader getter, BlockPos pos, Direction side)
    {
        return getState(getter, pos).getFireSpreadSpeed(getter, pos, side);
    }

    @Override
    public boolean isFireSource(BlockState state, IWorldReader reader, BlockPos pos, Direction side) 
    {
        return getState(reader, pos).isFireSource(reader, pos, side);
    }
    //FLAMMABLE END
    @Override
    public boolean canEntityDestroy(BlockState state, IBlockReader getter, BlockPos pos, Entity entity)
    {
        return getState(getter, pos).canEntityDestroy(getter, pos, entity);
    }

    @Override
    public boolean canDropFromExplosion(BlockState state, IBlockReader getter, BlockPos pos, Explosion explosion)
    {
        return getState(getter, pos).canDropFromExplosion(getter, pos, explosion);
    }

    @Override
    public void onBlockExploded(BlockState state, World level, BlockPos pos, Explosion explosion) 
    {
        getState(level, pos).onBlockExploded(level, pos, explosion);
    }

    @Override
    public boolean collisionExtendsVertically(BlockState state, IBlockReader getter, BlockPos pos, Entity collidingEntity)
    {
        return getState(getter, pos).collisionExtendsVertically(getter, pos, collidingEntity);
    }

    @Override
    public boolean shouldDisplayFluidOverlay(BlockState state, IBlockDisplayReader getter, BlockPos pos, FluidState fluidState) 
    {
        return getState(getter, pos).shouldDisplayFluidOverlay(getter, pos, fluidState);
    }

    @Nullable
    @Override
    public BlockState getToolModifiedState(BlockState state, World level, BlockPos pos, PlayerEntity player, ItemStack stack, ToolType toolType) 
    {
        return null;
    }

    @Override
    public boolean isScaffolding(BlockState state, IWorldReader reader, BlockPos pos, LivingEntity entity) 
    {
        return getState(reader, pos).isScaffolding(entity);
    }

    @Override
    public Vector3d getFogColor(BlockState state, IWorldReader reader, BlockPos pos, Entity entity, Vector3d originalColor, float partialTicks)
    {
        return getState(reader, pos).getFogColor(reader, pos, entity, originalColor, partialTicks);
    }

    @Override
    public BlockState rotate(BlockState state, IWorld accessor, BlockPos pos, Rotation rotation)
    {
        return getState(accessor, pos).rotate(accessor, pos, rotation);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(USE_SHAPE_FOR_LIGHT_OCCLUSION, SIGNAL_SOURCE, HAS_ANALOG_OUTPUT_SIGNAL);
    }

    @Nonnull
    @Override
    public BlockState getFacade(@Nonnull IBlockReader getter, @Nonnull BlockPos pos, @Nullable Direction side)
    {
        return getState(getter, pos);
    }
}

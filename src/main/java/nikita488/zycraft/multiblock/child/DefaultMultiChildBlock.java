package nikita488.zycraft.multiblock.child;

import net.minecraft.block.*;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.Constants;
import nikita488.zycraft.api.multiblock.child.MultiChildBlock;
import nikita488.zycraft.api.multiblock.child.MultiChildMaterial;
import nikita488.zycraft.init.ZYBlocks;
import nikita488.zycraft.init.ZYTiles;
import team.chisel.ctm.api.IFacade;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class DefaultMultiChildBlock extends MultiChildBlock implements IFacade
{
    public static final BooleanProperty TICKABLE = BooleanProperty.create("tickable");
    public static final BooleanProperty EMISSIVE = BooleanProperty.create("emissive");
    public static final BooleanProperty TRANSPARENT = BooleanProperty.create("transparent");
    public static final BooleanProperty PROVIDE_POWER = BooleanProperty.create("provide_power");
    public static final BooleanProperty COMPARABLE = BooleanProperty.create("comparable");

    public DefaultMultiChildBlock(Properties properties)
    {
        super(properties);
        setDefaultState(getDefaultState()
                .with(TICKABLE, false)
                .with(EMISSIVE, false)
                .with(TRANSPARENT, false)
                .with(PROVIDE_POWER, false)
                .with(COMPARABLE, false));
    }

    public static boolean tryConvertAt(IWorld world, BlockPos pos)
    {
        BlockState state = world.getBlockState(pos);
        MultiChildMaterial material = MultiChildMaterial.fromState(state, world, pos);

        if (material == null)
            return false;

        BlockState childState = ZYBlocks.DEFAULT_MULTI_CHILD.get(material).getDefaultState()
                .with(DefaultMultiChildBlock.TICKABLE, state.ticksRandomly())
                .with(DefaultMultiChildBlock.EMISSIVE, state.isEmissiveRendering())
                .with(DefaultMultiChildBlock.TRANSPARENT, state.isTransparent())
                .with(DefaultMultiChildBlock.PROVIDE_POWER, state.canProvidePower())
                .with(DefaultMultiChildBlock.COMPARABLE, state.hasComparatorInputOverride());

        world.setBlockState(pos, childState, Constants.BlockFlags.BLOCK_UPDATE);

        TileEntity tile = world.getTileEntity(pos);

        if (!(tile instanceof DefaultMultiChildTile))
            return false;

        ((DefaultMultiChildTile)tile).setState(state);
        return true;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        if (ZYBlocks.DEFAULT_MULTI_CHILD.get(MultiChildMaterial.AIR).has(state))
            return BlockRenderType.INVISIBLE;
        return BlockRenderType.MODEL;
    }

    @Nonnull
    @Override
    public BlockState getFacade(@Nonnull IBlockReader world, @Nonnull BlockPos pos, @Nullable Direction side)
    {
        return getState(world, pos);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return ZYTiles.DEFAULT_MULTI_CHILD.create();
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(TICKABLE, EMISSIVE, TRANSPARENT, PROVIDE_POWER, COMPARABLE);
    }

    private BlockState getState(IBlockReader world, BlockPos pos)
    {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof DefaultMultiChildTile)
            return ((DefaultMultiChildTile)tile).state();
        return Blocks.AIR.getDefaultState();
    }

    @Override
    public boolean canEntitySpawn(BlockState state, IBlockReader world, BlockPos pos, EntityType<?> type)
    {
        return getState(world, pos).canEntitySpawn(world, pos, type);
    }

    @Override
    public MaterialColor getMaterialColor(BlockState state, IBlockReader world, BlockPos pos)
    {
        return getState(world, pos).getMaterialColor(world, pos);
    }

    @Override
    public void updateNeighbors(BlockState state, IWorld world, BlockPos pos, int flags)
    {
        getState(world, pos).updateNeighbors(world, pos, flags);
    }

    @Override
    public void updateDiagonalNeighbors(BlockState state, IWorld world, BlockPos pos, int flags)
    {
        getState(world, pos).updateDiagonalNeighbors(world, pos, flags);
    }

    //TODO: Needed?
    @Override
    public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos)
    {
        //return getState(world, currentPos).updatePostPlacement(facing, facingState, world, currentPos, facingPos);
        return state;
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader world, BlockPos pos)
    {
        return getState(world, pos).isNormalCube(world, pos);
    }

    @Override
    public boolean causesSuffocation(BlockState state, IBlockReader world, BlockPos pos)
    {
        return getState(world, pos).isSuffocating(world, pos);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isViewBlocking(BlockState state, IBlockReader world, BlockPos pos)
    {
        return getState(world, pos).causesSuffocation(world, pos);
    }

    @Override
    public boolean allowsMovement(BlockState state, IBlockReader world, BlockPos pos, PathType type)
    {
        return getState(world, pos).allowsMovement(world, pos, type);
    }

    @Override
    public boolean isReplaceable(BlockState state, BlockItemUseContext context)
    {
        return false;
    }

    @Override
    public boolean isReplaceable(BlockState state, Fluid fluid)
    {
        return false;
    }

    @Override
    public float getBlockHardness(BlockState state, IBlockReader world, BlockPos pos)
    {
        return getState(world, pos).getBlockHardness(world, pos);
    }

    @Override
    public boolean ticksRandomly(BlockState state)
    {
        return state.get(TICKABLE);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isEmissiveRendering(BlockState state)
    {
        return state.get(EMISSIVE);
    }

    //TODO: Needed?
    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isSideInvisible(BlockState state, BlockState adjacentState, Direction side)
    {
        return adjacentState.getBlock() == this;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader world, BlockPos pos)
    {
        return getState(world, pos).propagatesSkylightDown(world, pos);
    }

    @Override
    public int getOpacity(BlockState state, IBlockReader world, BlockPos pos)
    {
        return getState(world, pos).getOpacity(world, pos);
    }

    @Override
    public boolean isTransparent(BlockState state)
    {
        return state.get(TRANSPARENT);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
    {
        getState(world, pos).randomTick(world, pos, random);
    }

    @Override
    public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand)
    {
        getState(world, pos).tick(world, pos, rand);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand)
    {
        BlockState actualState = getState(world, pos);
        actualState.getBlock().animateTick(actualState, world, pos, rand);
    }

    @Override
    public void onPlayerDestroy(IWorld world, BlockPos pos, BlockState state)
    {
        BlockState actualState = getState(world, pos);
        actualState.getBlock().onPlayerDestroy(world, pos, state);
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving)
    {
        getState(world, pos).neighborChanged(world, pos, block, fromPos, isMoving);
    }

    @Nullable
    @Override
    public INamedContainerProvider getContainer(BlockState state, World world, BlockPos pos)
    {
        return getState(world, pos).getContainer(world, pos);
    }

    @Override
    public float getPlayerRelativeBlockHardness(BlockState state, PlayerEntity player, IBlockReader world, BlockPos pos)
    {
        return getState(world, pos).getPlayerRelativeBlockHardness(player, world, pos);
    }

    @Override
    public void spawnAdditionalDrops(BlockState state, World world, BlockPos pos, ItemStack stack)
    {
        getState(world, pos).spawnAdditionalDrops(world, pos, stack);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder)
    {
        TileEntity tile = builder.get(LootParameters.BLOCK_ENTITY);

        if (tile instanceof DefaultMultiChildTile)
            return ((DefaultMultiChildTile)tile).state().getDrops(builder);

        return super.getDrops(state, builder);
    }

    @Override
    public void dropXpOnBlockBreak(World world, BlockPos pos, int amount)
    {
        BlockState actualState = getState(world, pos);
        actualState.getBlock().dropXpOnBlockBreak(world, pos, amount);
    }

    @Override
    public void onExplosionDestroy(World world, BlockPos pos, Explosion explosion)
    {
        BlockState actualState = getState(world, pos);
        actualState.getBlock().onExplosionDestroy(world, pos, explosion);
    }

    //TODO: Needed?
    @Override
    public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos)
    {
        //return getState(world, pos).isValidPosition(world, pos);
        return true;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        ActionResultType type = getState(world, pos).onBlockActivated(world, player, hand, hit);

        if (type.isSuccessOrConsume())
            return type;

        return super.onBlockActivated(state, world, pos, player, hand, hit);
    }

    @Override
    public void onEntityWalk(World world, BlockPos pos, Entity entity)
    {
        BlockState actualState = getState(world, pos);
        actualState.getBlock().onEntityWalk(world, pos, entity);
    }

    @Override
    public void onBlockClicked(BlockState state, World world, BlockPos pos, PlayerEntity player)
    {
        getState(world, pos).onBlockClicked(world, pos, player);
    }

    @Override
    public int getWeakPower(BlockState state, IBlockReader world, BlockPos pos, Direction side)
    {
        return getState(world, pos).getWeakPower(world, pos, side);
    }

    @Override
    public boolean canProvidePower(BlockState state)
    {
        return state.get(PROVIDE_POWER);
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
    public void harvestBlock(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity tile, ItemStack stack)
    {
        BlockState actualState = getState(world, pos);
        actualState.getBlock().harvestBlock(world, player, pos, state, tile, stack);
    }

    @Override
    public boolean eventReceived(BlockState state, World world, BlockPos pos, int id, int param)
    {
        return getState(world, pos).onBlockEventReceived(world, pos, id, param);
    }

    @Override
    public float getAmbientOcclusionLightValue(BlockState state, IBlockReader world, BlockPos pos)
    {
        return getState(world, pos).getAmbientOcclusionLightValue(world, pos);
    }

    @Override
    public void onFallenUpon(World world, BlockPos pos, Entity entity, float fallDistance)
    {
        BlockState actualState = getState(world, pos);
        actualState.getBlock().onFallenUpon(world, pos, entity, fallDistance);
    }

    @Override
    public void onLanded(IBlockReader world, Entity entity)
    {
        BlockState actualState = getState(world, new BlockPos(entity));
        actualState.getBlock().onLanded(world, entity);
    }

    @Override
    public void onProjectileCollision(World world, BlockState state, BlockRayTraceResult hit, Entity projectile)
    {
        BlockState actualState = getState(world, hit.getPos());
        actualState.onProjectileCollision(world, actualState, hit, projectile);
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player)
    {
        BlockState actualState = getState(world, pos);
        actualState.getBlock().onBlockHarvested(world, pos, state, player);
    }

    @Override
    public void fillWithRain(World world, BlockPos pos)
    {
        BlockState actualState = getState(world, pos);
        actualState.getBlock().fillWithRain(world, pos);
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState state)
    {
        return state.get(COMPARABLE);
    }

    @Override
    public int getComparatorInputOverride(BlockState state, World world, BlockPos pos)
    {
        return getState(world, pos).getComparatorInputOverride(world, pos);
    }

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

    //TODO: Implement light value logic
    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos)
    {
        return super.getLightValue(getState(world, pos), world, pos);
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
    public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, IFluidState fluid)
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
    public float getExplosionResistance(BlockState state, IWorldReader world, BlockPos pos, @Nullable Entity exploder, Explosion explosion)
    {
        return getState(world, pos).getExplosionResistance(world, pos, exploder, explosion);
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
    public boolean addLandingEffects(BlockState state, ServerWorld world, BlockPos pos, BlockState unusedState, LivingEntity entity, int numberOfParticles)
    {
        BlockState childState = getState(world, pos);

        if (childState.addLandingEffects(world, pos, childState, entity, numberOfParticles))
            return true;

        world.spawnParticle(new BlockParticleData(ParticleTypes.BLOCK, childState),
                entity.getPosX(), entity.getPosY(), entity.getPosZ(),
                numberOfParticles,
                0, 0, 0,
                0.15D);
        return true;
    }

    @Override
    public boolean addRunningEffects(BlockState state, World world, BlockPos pos, Entity entity)
    {
        //TODO: Fix particles tint
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
    public void onPlantGrow(BlockState state, IWorld world, BlockPos pos, BlockPos source)
    {
        getState(world, pos).onPlantGrow(world, pos, source);
    }

    @Override
    public boolean isFertile(BlockState state, IBlockReader world, BlockPos pos)
    {
        return getState(world, pos).isFertile(world, pos);
    }

    @Override
    public boolean isBeaconBase(BlockState state, IWorldReader world, BlockPos pos, BlockPos beacon)
    {
        return getState(world, pos).isBeaconBase(world, pos, beacon);
    }

    @Override
    public boolean isConduitFrame(BlockState state, IWorldReader world, BlockPos pos, BlockPos conduit)
    {
        return getState(world, pos).isConduitFrame(world, pos, conduit);
    }

    @Override
    public boolean isPortalFrame(BlockState state, IWorldReader world, BlockPos pos)
    {
        return getState(world, pos).isPortalFrame(world, pos);
    }

    @Override
    public int getExpDrop(BlockState state, IWorldReader world, BlockPos pos, int fortuneLevel, int silkTouch)
    {
        return getState(world, pos).getExpDrop(world, pos, fortuneLevel, silkTouch);
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction)
    {
        return getState(world, pos).rotate(world, pos, direction);
    }

    @Override
    public float getEnchantPowerBonus(BlockState state, IWorldReader world, BlockPos pos)
    {
        return getState(world, pos).getEnchantPowerBonus(world, pos);
    }

    @Override
    public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor)
    {
        getState(world, pos).onNeighborChange(world, pos, neighbor);
    }

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
    public BlockState getStateAtViewpoint(BlockState state, IBlockReader world, BlockPos pos, Vec3d viewpoint)
    {
        return getState(world, pos).getStateAtViewpoint(world, pos, viewpoint);
    }

    @Nullable
    @Override
    public PathNodeType getAiPathNodeType(BlockState state, IBlockReader world, BlockPos pos, @Nullable MobEntity entity)
    {
        return getState(world, pos).getAiPathNodeType(world, pos, entity);
    }

    @Override
    public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face)
    {
        return getState(world, pos).getFlammability(world, pos, face);
    }

    @Override
    public boolean isFlammable(BlockState state, IBlockReader world, BlockPos pos, Direction face)
    {
        return getState(world, pos).isFlammable(world, pos, face);
    }

    @Override
    public void catchFire(BlockState state, World world, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter)
    {
        getState(world, pos).catchFire(world, pos, face, igniter);
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face)
    {
        return getState(world, pos).getFireSpreadSpeed(world, pos, face);
    }

    @Override
    public boolean isFireSource(BlockState state, IBlockReader world, BlockPos pos, Direction side)
    {
        return getState(world, pos).isFireSource(world, pos, side);
    }

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
    public boolean shouldDisplayFluidOverlay(BlockState state, ILightReader world, BlockPos pos, IFluidState fluidState)
    {
        return getState(world, pos).shouldDisplayFluidOverlay(world, pos, fluidState);
    }
}

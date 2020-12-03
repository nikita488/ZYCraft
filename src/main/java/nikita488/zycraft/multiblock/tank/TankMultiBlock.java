package nikita488.zycraft.multiblock.tank;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import nikita488.zycraft.init.ZYContainers;
import nikita488.zycraft.init.ZYMultiTypes;
import nikita488.zycraft.multiblock.Cuboid6i;
import nikita488.zycraft.multiblock.IterableItemHandler;
import nikita488.zycraft.multiblock.MultiBlock;
import nikita488.zycraft.multiblock.client.FluidCuboidRenderer;
import nikita488.zycraft.multiblock.tile.MultiChildTile;
import nikita488.zycraft.util.FluidUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class TankMultiBlock extends MultiBlock implements IContainerProvider
{
    private Cuboid6i cuboid;
    private final IterableItemHandler inventory = new IterableItemHandler(2);
    private final FluidTank fluidTank = new FluidTank(16000)
    {
        @Override
        protected void onContentsChanged()
        {
            markDirty();
            scheduleUpdate();
        }
    };
    private final LazyOptional<IItemHandler> itemCap = LazyOptional.of(() -> inventory);
    private final LazyOptional<IFluidHandler> fluidCap = LazyOptional.of(() -> fluidTank);

    public TankMultiBlock(World world, Cuboid6i cuboid)
    {
        this(world, new ChunkPos(cuboid.center()));
        this.cuboid = cuboid;
    }

    public TankMultiBlock(World world, ChunkPos mainChunk)
    {
        super(ZYMultiTypes.TANK.get(), world, mainChunk);
    }

    @Override
    public void tick()
    {
        if (world.isRemote)
            return;

        Optional<IFluidHandlerItem> capability = FluidUtils.getItemFluidHandler(inventory.getStackInSlot(0));

        if (!capability.isPresent())
            return;

        IFluidHandlerItem handler = capability.get();
        FluidStack containerFluid = handler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);

        if (!containerFluid.isEmpty())
        {
            int filled = fluidTank.fill(containerFluid, IFluidHandler.FluidAction.SIMULATE);
            FluidStack drained;

            if (filled <= 0 || (drained = handler.drain(filled, IFluidHandler.FluidAction.EXECUTE)).isEmpty())
                return;

            ItemStack container = handler.getContainer();

            if (!inventory.insertItem(1, container, true).isEmpty())
                return;

            inventory.extractItem(0, 1, false);
            inventory.insertItem(1, container, false);
            fluidTank.fill(drained, IFluidHandler.FluidAction.EXECUTE);
        }
        else
        {
            FluidStack tankFluid = fluidTank.getFluidInTank(0);

            if (tankFluid.isEmpty())
                return;

            int filled = handler.fill(tankFluid, IFluidHandler.FluidAction.EXECUTE);
            ItemStack container = handler.getContainer();

            if (filled <= 0 || !inventory.insertItem(1, container, true).isEmpty())
                return;

            inventory.extractItem(0, 1, false);
            inventory.insertItem(1, container, false);
            fluidTank.drain(filled, IFluidHandler.FluidAction.EXECUTE);
        }
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox()
    {
        return new AxisAlignedBB(cuboid.minX(), cuboid.minY(), cuboid.minZ(), cuboid.maxX(), cuboid.maxY(), cuboid.maxZ());
    }

    @Override
    public void render(MatrixStack stack, IRenderTypeBuffer buffer, int lightMap, float partialTicks)
    {
        stack.push();
        stack.translate(-cuboid.minX() + 1, -cuboid.minY() + 1, -cuboid.minZ() + 1);
        FluidCuboidRenderer.render(fluidTank.getFluid(), fluidTank.getCapacity(), cuboid, 1, stack, buffer, lightMap);
        stack.pop();
    }

    @Override
    public void invalidate()
    {
        super.invalidate();
        itemCap.invalidate();
        fluidCap.invalidate();
    }

    @Override
    public ActionResultType onChildInteraction(MultiChildTile child, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        if (!world.isRemote)
            ZYContainers.TANK.open((ServerPlayerEntity)player, new StringTextComponent("Multi-Tank"), this, buffer -> buffer.writeVarInt(fluidTank.getCapacity()));

        return ActionResultType.SUCCESS;
    }

    @Override
    public int getChildLightValue(MultiChildTile child)
    {
        return 0;
    }

    @Override
    public void initChildBlocks()
    {
        addChildBlocks(cuboid.expand(1));
    }

    @Override
    public void load(CompoundNBT tag)
    {
        this.cuboid = Cuboid6i.load(tag.getCompound("Cuboid"));
        fluidTank.readFromNBT(tag.getCompound("Tank"));
    }

    @Override
    public void save(CompoundNBT tag)
    {
        tag.put("Cuboid", cuboid.save());
        tag.put("Tank", fluidTank.writeToNBT(new CompoundNBT()));
    }

    @Override
    public void decode(PacketBuffer buffer)
    {
        this.cuboid = Cuboid6i.decode(buffer);
        fluidTank.setFluid(buffer.readFluidStack());
    }

    @Override
    public void encode(PacketBuffer buffer)
    {
        cuboid.encode(buffer);
        buffer.writeFluidStack(fluidTank.getFluid());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nonnull MultiChildTile child, @Nullable Direction side)
    {
        if (!valid)
            return LazyOptional.empty();

        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return itemCap.cast();
        else if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return fluidCap.cast();

        return LazyOptional.empty();
    }

    public IterableItemHandler getInventory()
    {
        return inventory;
    }

    public FluidTank getFluidTank()
    {
        return fluidTank;
    }

    @Nullable
    @Override
    public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity player)
    {
        return new TankContainer(windowID, playerInventory, this);
    }
}

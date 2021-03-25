package nikita488.zycraft.multiblock.tank;

import com.mojang.blaze3d.matrix.MatrixStack;
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
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import nikita488.zycraft.init.ZYContainers;
import nikita488.zycraft.init.ZYMultiTypes;
import nikita488.zycraft.multiblock.Cuboid6i;
import nikita488.zycraft.multiblock.IterableItemHandler;
import nikita488.zycraft.multiblock.MultiBlock;
import nikita488.zycraft.multiblock.tile.MultiChildTile;
import nikita488.zycraft.util.FluidUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class TankMultiBlock extends MultiBlock implements IContainerProvider
{
    private Cuboid6i insideArea;
    private final IterableItemHandler inventory = new IterableItemHandler(2);
    private final NonNullLazy<MultiFluidTank> fluidTank = NonNullLazy.of(() -> new MultiFluidTank(insideArea)
    {
        @Override
        protected void onContentsChanged()
        {
            markDirty();
            scheduleUpdate();
            updateLight();
        }
    });
    private final LazyOptional<IFluidHandler> fluidCap = Util.make(LazyOptional.of(fluidTank::get), capability ->
            capability.addListener(instance -> fluidTank.get().invalidate()));
    private final LazyOptional<IItemHandler> itemCap = LazyOptional.of(() -> inventory);

    public TankMultiBlock(World world, Cuboid6i insideArea)
    {
        this(world, new ChunkPos(insideArea.center()));
        this.insideArea = insideArea;
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
            int filled = fluidTank.get().fill(containerFluid, IFluidHandler.FluidAction.SIMULATE);
            FluidStack drained;

            if (filled <= 0 || (drained = handler.drain(filled, IFluidHandler.FluidAction.EXECUTE)).isEmpty())
                return;

            ItemStack container = handler.getContainer();

            if (!inventory.insertItem(1, container, true).isEmpty())
                return;

            inventory.extractItem(0, 1, false);
            inventory.insertItem(1, container, false);
            fluidTank.get().fill(drained, IFluidHandler.FluidAction.EXECUTE);
        }
        else
        {
            FluidStack tankFluid = fluidTank.get().getFluidInTank(0);

            if (tankFluid.isEmpty())
                return;

            int filled = handler.fill(tankFluid, IFluidHandler.FluidAction.EXECUTE);
            ItemStack container = handler.getContainer();

            if (filled <= 0 || !inventory.insertItem(1, container, true).isEmpty())
                return;

            inventory.extractItem(0, 1, false);
            inventory.insertItem(1, container, false);
            fluidTank.get().drain(filled, IFluidHandler.FluidAction.EXECUTE);
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox()
    {
        return new AxisAlignedBB(insideArea.minX(), insideArea.minY(), insideArea.minZ(), insideArea.maxX() + 1, insideArea.maxY() + 1, insideArea.maxZ() + 1);
    }

    @Override
    public void render(MatrixStack stack, IRenderTypeBuffer buffer, int lightMap, float partialTicks)
    {
        stack.push();
        stack.translate(-insideArea.minX() + 1, -insideArea.minY() + 1, -insideArea.minZ() + 1);
        fluidTank.get().render(stack, buffer, lightMap, partialTicks);
        stack.pop();
    }

    @Override
    public void invalidate(boolean destroy)
    {
        super.invalidate(destroy);

        if (!destroy)//TODO: I think we need this check to invalidate caps only when multiblock is destroyed and not unloaded
            return;

        itemCap.invalidate();
        fluidCap.invalidate();
    }

    @Override
    public ActionResultType onChildInteraction(MultiChildTile child, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        if (child.parentMultiBlocks().size() > 1)
            return ActionResultType.CONSUME;

        if (!world.isRemote)
            ZYContainers.TANK.open((ServerPlayerEntity)player, new StringTextComponent("Multi-Tank"), this, buffer -> buffer.writeVarInt(fluidTank.get().getCapacity()));

        return ActionResultType.SUCCESS;
    }

    @Override
    public int getChildLightValue(MultiChildTile child)
    {
        return fluidTank.get().getLightValue(child.getPos());//TODO: Why light on client is 0 here?!
    }

    @Override
    public void initChildBlocks()
    {
        addChildBlocks(insideArea.expand(1));
    }

    @Override
    public void load(CompoundNBT tag)
    {
        this.insideArea = Cuboid6i.load(tag.getCompound("InsideArea"));
        fluidTank.get().readFromNBT(tag.getCompound("FluidTank"));
    }

    @Override
    public void save(CompoundNBT tag)
    {
        tag.put("InsideArea", insideArea.save(new CompoundNBT()));
        tag.put("FluidTank", fluidTank.get().writeToNBT(new CompoundNBT()));
    }

    @Override
    public void decode(PacketBuffer buffer)
    {
        this.insideArea = Cuboid6i.decode(buffer);
    }

    @Override
    public void decodeUpdate(PacketBuffer buffer)
    {
        fluidTank.get().decode(buffer);
        updateLight();
    }

    @Override
    public void encode(PacketBuffer buffer)
    {
        insideArea.encode(buffer);
    }

    @Override
    public void encodeUpdate(PacketBuffer buffer)
    {
        fluidTank.get().encode(buffer);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> type, @Nonnull MultiChildTile child)
    {
        if (!valid)
            return LazyOptional.empty();

        if (type == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return fluidTank.get().getLevel(child.getPos().getY()).cast();
        if (type == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return itemCap.cast();

        return LazyOptional.empty();
    }

    public MultiFluidTank getFluidTank()
    {
        return fluidTank.get();
    }

    public IterableItemHandler getInventory()
    {
        return inventory;
    }

    @Nullable
    @Override
    public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity player)
    {
        return new TankContainer(windowID, playerInventory, this);
    }
}

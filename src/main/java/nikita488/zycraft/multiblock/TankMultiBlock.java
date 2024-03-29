package nikita488.zycraft.multiblock;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import nikita488.zycraft.init.ZYLang;
import nikita488.zycraft.init.ZYMultiTypes;
import nikita488.zycraft.menu.TankContainer;
import nikita488.zycraft.multiblock.fluid.MultiFluidTank;
import nikita488.zycraft.multiblock.inventory.MultiItemIOHandler;
import nikita488.zycraft.util.Cuboid6i;
import nikita488.zycraft.util.FluidUtils;
import nikita488.zycraft.util.InventoryUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class TankMultiBlock extends MultiBlock implements IDynamicMultiBlock, INamedContainerProvider
{
    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;
    private Cuboid6i innerArea;
    private final NonNullLazy<MultiFluidTank> tank = NonNullLazy.of(() -> new MultiFluidTank(innerArea)
    {
        @Override
        public void onContentsChanged()
        {
            super.onContentsChanged();
            markUnsaved();
            updateInterfacesForOutputSignal();
        }

        @Override
        public void sendUpdated()
        {
            sendMultiUpdated();
        }
    });
    private final MultiItemIOHandler inventory = new MultiItemIOHandler(2)
    {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack)
        {
            return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent();
        }

        @Override
        protected void onContentsChanged(int slot)
        {
            markUnsaved();
            updateInterfacesForOutputSignal();
        }
    }.input(0).output(1);
    private LazyOptional<IFluidHandler> fluidCapability = LazyOptional.empty();
    private LazyOptional<IItemHandler> itemCapability = LazyOptional.empty();

    public TankMultiBlock(World level, ChunkPos pos)
    {
        super(ZYMultiTypes.TANK.get(), level, pos);
    }

    public TankMultiBlock(World level, Cuboid6i innerArea)
    {
        this(level, new ChunkPos(innerArea.center()));
        this.innerArea = innerArea.immutable();
        tank.get();
    }

    @Override
    public void initChildBlocks()
    {
        addChildBlocks(innerArea.minX() - 1, innerArea.minY() - 1, innerArea.minZ() - 1, innerArea.maxX() + 1, innerArea.maxY() + 1, innerArea.maxZ() + 1);
    }

    @Override
    public BlockPos origin()
    {
        return childBlocks.get(0);
    }

    @Override
    public AxisAlignedBB aabb()
    {
        return new AxisAlignedBB(innerArea.minX(), innerArea.minY(), innerArea.minZ(), innerArea.maxX() + 1, innerArea.maxY() + 1, innerArea.maxZ() + 1);
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return ZYLang.TANK;
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player)
    {
        return new TankContainer(id, playerInventory, this);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hitResult)
    {
        if (!level.isClientSide())
            NetworkHooks.openGui((ServerPlayerEntity)player, this, buffer -> buffer.writeVarInt(tank.get().getCapacity()));
        return ActionResultType.sidedSuccess(level.isClientSide());
    }

    @Override
    public void tick()
    {
        if (level.isClientSide())
        {
            tank.get().tick();
            return;
        }

        Optional<IFluidHandlerItem> capability = FluidUtils.getItemFluidHandler(inventory.getStackInSlot(INPUT_SLOT));

        if (!capability.isPresent())
            return;

        IFluidHandlerItem handler = capability.get();
        FluidStack containerFluid = handler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);

        if (!containerFluid.isEmpty())
        {
            int filled = tank.get().fill(containerFluid, IFluidHandler.FluidAction.SIMULATE);
            FluidStack drained = filled > 0 ? handler.drain(filled, IFluidHandler.FluidAction.EXECUTE) : FluidStack.EMPTY;

            if (drained.isEmpty())
                return;

            ItemStack container = handler.getContainer();

            if (!inventory.insertItem(OUTPUT_SLOT, container, true).isEmpty())
                return;

            inventory.extractItem(INPUT_SLOT, 1, false);
            inventory.insertItem(OUTPUT_SLOT, container, false);
            tank.get().fill(drained, IFluidHandler.FluidAction.EXECUTE);
        }
        else
        {
            FluidStack fluid = tank.get().getFluid();

            if (fluid.isEmpty())
                return;

            int filled = handler.fill(fluid, IFluidHandler.FluidAction.EXECUTE);
            ItemStack container = handler.getContainer();

            if (filled <= 0 || !inventory.insertItem(OUTPUT_SLOT, container, true).isEmpty())
                return;

            inventory.extractItem(INPUT_SLOT, 1, false);
            inventory.insertItem(OUTPUT_SLOT, container, false);
            tank.get().drain(filled, IFluidHandler.FluidAction.EXECUTE);
        }
    }

    @Override
    public void render(MatrixStack stack, IRenderTypeBuffer source, int lightMap, float partialTicks)
    {
        tank.get().render(stack, source, level, partialTicks);
    }

    @Override
    public void validate(AddingReason reason)
    {
        this.fluidCapability = LazyOptional.of(tank::get);
        this.itemCapability = LazyOptional.of(() -> inventory);
        super.validate(reason);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        InventoryUtils.dropInventoryItems(level, innerArea.center(), inventory);
        updateInterfacesForOutputSignal();
    }

    @Override
    protected void invalidateCapabilities()
    {
        fluidCapability.invalidate();
        itemCapability.invalidate();
    }

    @Override
    public void load(CompoundNBT tag)
    {
        this.innerArea = Cuboid6i.load(tag.getCompound("InnerArea"));
        tank.get().readFromNBT(tag.getCompound("Tank"));
        inventory.deserializeNBT(tag.getCompound("Inventory"));
    }

    @Override
    public void save(CompoundNBT tag)
    {
        tag.put("InnerArea", innerArea.save(new CompoundNBT()));
        tag.put("Tank", tank.get().writeToNBT(new CompoundNBT()));
        tag.put("Inventory", inventory.serializeNBT());
    }

    @Override
    public void decode(PacketBuffer buffer)
    {
        this.innerArea = Cuboid6i.decode(buffer);
        tank.get().decode(buffer);
    }

    @Override
    public void encode(PacketBuffer buffer)
    {
        innerArea.encode(buffer);
        tank.get().encode(buffer);
    }

    @Override
    public void decodeUpdate(PacketBuffer buffer)
    {
        tank.get().decodeUpdate(buffer);
    }

    @Override
    public void encodeUpdate(PacketBuffer buffer)
    {
        tank.get().encode(buffer);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> type, @Nullable BlockPos pos)
    {
        if (!valid)
            return LazyOptional.empty();

        if (type == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return fluidCapability.cast();
        else if (type == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return itemCapability.cast();

        return LazyOptional.empty();
    }

    public MultiFluidTank fluidTank()
    {
        return tank.get();
    }

    public MultiItemIOHandler inventory()
    {
        return inventory;
    }
}

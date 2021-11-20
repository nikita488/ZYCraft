package nikita488.zycraft.multiblock;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fmllegacy.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import nikita488.zycraft.init.ZYLang;
import nikita488.zycraft.init.ZYMultiTypes;
import nikita488.zycraft.menu.TankMenu;
import nikita488.zycraft.multiblock.fluid.MultiFluidTank;
import nikita488.zycraft.multiblock.inventory.MultiItemIOHandler;
import nikita488.zycraft.util.Cuboid6i;
import nikita488.zycraft.util.FluidUtils;
import nikita488.zycraft.util.InventoryUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class TankMultiBlock extends MultiBlock implements IDynamicMultiBlock, MenuProvider
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

    public TankMultiBlock(Level level, ChunkPos pos)
    {
        super(ZYMultiTypes.TANK.get(), level, pos);
    }

    public TankMultiBlock(Level level, Cuboid6i innerArea)
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
    public AABB aabb()
    {
        return new AABB(innerArea.minX(), innerArea.minY(), innerArea.minZ(), innerArea.maxX() + 1, innerArea.maxY() + 1, innerArea.maxZ() + 1);
    }

    @Override
    public Component getDisplayName()
    {
        return ZYLang.TANK;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowID, Inventory playerInventory, Player player)
    {
        return new TankMenu(windowID, playerInventory, this);
    }

    @Override
    public InteractionResult onBlockActivated(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
    {
        if (!level.isClientSide())
            NetworkHooks.openGui((ServerPlayer)player, this, buffer -> buffer.writeVarInt(tank.get().getCapacity()));
        return InteractionResult.sidedSuccess(level.isClientSide());
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
    public void render(PoseStack stack, MultiBufferSource buffer, int lightMap, float partialTicks)
    {
        tank.get().render(stack, buffer, level, partialTicks);
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
    public void load(CompoundTag tag)
    {
        this.innerArea = Cuboid6i.load(tag.getCompound("InnerArea"));
        tank.get().readFromNBT(tag.getCompound("Tank"));
        inventory.deserializeNBT(tag.getCompound("Inventory"));
    }

    @Override
    public void save(CompoundTag tag)
    {
        tag.put("InnerArea", innerArea.save(new CompoundTag()));
        tag.put("Tank", tank.get().writeToNBT(new CompoundTag()));
        tag.put("Inventory", inventory.serializeNBT());
    }

    @Override
    public void decode(FriendlyByteBuf buffer)
    {
        this.innerArea = Cuboid6i.decode(buffer);
        tank.get().decode(buffer);
    }

    @Override
    public void encode(FriendlyByteBuf buffer)
    {
        innerArea.encode(buffer);
        tank.get().encode(buffer);
    }

    @Override
    public void decodeUpdate(FriendlyByteBuf buffer)
    {
        tank.get().decodeUpdate(buffer);
    }

    @Override
    public void encodeUpdate(FriendlyByteBuf buffer)
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

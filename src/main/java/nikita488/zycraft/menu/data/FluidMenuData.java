package nikita488.zycraft.menu.data;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class FluidMenuData implements IMenuData, Supplier<FluidStack>
{
    private FluidStack fluid = FluidStack.EMPTY;
    @Nullable
    private Supplier<FluidStack> supplier;

    public FluidMenuData() {}

    public FluidMenuData(@Nullable Supplier<FluidStack> supplier)
    {
        this.supplier = supplier;
    }

    @Override
    public boolean canBeUpdated()
    {
        return supplier != null && !fluid.isFluidStackIdentical(supplier.get());
    }

    @Override
    public void update()
    {
        if (supplier != null)
            this.fluid = supplier.get().copy();
    }

    @Override
    public void encode(PacketBuffer buffer)
    {
        buffer.writeFluidStack(fluid);
    }

    @Override
    public void decode(PacketBuffer buffer)
    {
        this.fluid = buffer.readFluidStack();
    }

    @Override
    public FluidStack get()
    {
        return fluid;
    }
}

package nikita488.zycraft.inventory.container.variable;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class FluidContainerVariable implements IContainerVariable
{
    private FluidStack fluid = FluidStack.EMPTY;
    @Nullable
    private Supplier<FluidStack> supplier;

    public FluidContainerVariable() {}

    public FluidContainerVariable(Supplier<FluidStack> supplier)
    {
        this.supplier = supplier;
    }

    @Override
    public boolean needsUpdating()
    {
        return supplier != null && !fluid.isFluidStackIdentical(supplier.get());
    }

    @Override
    public void update()
    {
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

    public FluidStack fluid()
    {
        return fluid;
    }
}

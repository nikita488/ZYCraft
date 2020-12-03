package nikita488.zycraft.container;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Supplier;

public class FluidContainerVar implements IContainerVar
{
    private FluidStack fluid = FluidStack.EMPTY;
    private Supplier<FluidStack> supplier;

    public FluidContainerVar() {}

    public FluidContainerVar(Supplier<FluidStack> supplier)
    {
        this.supplier = supplier;
    }

    @Override
    public boolean isDirty()
    {
        return !fluid.isFluidStackIdentical(supplier.get());
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

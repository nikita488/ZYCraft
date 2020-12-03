package nikita488.zycraft.multiblock.tank;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import nikita488.zycraft.multiblock.Cuboid6i;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class MultiFluidTank extends FluidTank
{
    public static final int AMOUNT_PER_BLOCK = 16 * FluidAttributes.BUCKET_VOLUME;
    private final Cuboid6i cuboid;

    public MultiFluidTank(Cuboid6i cuboid)
    {
        this(cuboid, stack -> true);
    }

    public MultiFluidTank(Cuboid6i cuboid, Predicate<FluidStack> validator)
    {
        super(cuboid.area() * AMOUNT_PER_BLOCK, validator);
        this.cuboid = cuboid;
    }

    public void decode(PacketBuffer buffer)
    {
        fluid = buffer.readFluidStack();
    }

    public void encode(PacketBuffer buffer)
    {
        buffer.writeFluidStack(fluid);
    }

    @Override
    public FluidTank setCapacity(int capacity)
    {
        return this;
    }

    @Override
    public FluidTank setValidator(Predicate<FluidStack> validator)
    {
        return this;
    }

    private class Level implements IFluidHandler
    {
        private final int capacity;

        public Level(int capacity)
        {
            this.capacity = capacity;
        }

        @Override
        public int getTanks()
        {
            return 1;
        }

        @Nonnull
        @Override
        public FluidStack getFluidInTank(int tank)
        {
            FluidStack stack = MultiFluidTank.this.getFluid().copy();
            stack.shrink(capacity);
            return stack;
        }

        @Override
        public int getTankCapacity(int tank)
        {
            return MultiFluidTank.this.getCapacity() - capacity;
        }

        @Override
        public boolean isFluidValid(int tank, @Nonnull FluidStack stack)
        {
            return MultiFluidTank.this.isFluidValid(stack);
        }

        @Override
        public int fill(FluidStack resource, FluidAction action)
        {
            return MultiFluidTank.this.fill(resource, action);
        }

        @Nonnull
        @Override
        public FluidStack drain(FluidStack resource, FluidAction action)
        {
            if (resource.isEmpty() || !resource.isFluidEqual(MultiFluidTank.this.getFluid()))
                return FluidStack.EMPTY;
            return drain(resource.getAmount(), action);
        }

        @Nonnull
        @Override
        public FluidStack drain(int maxDrain, FluidAction action)
        {
            return MultiFluidTank.this.drain(Math.min(maxDrain, MultiFluidTank.this.getFluidAmount() - capacity), action);
        }
    }
}

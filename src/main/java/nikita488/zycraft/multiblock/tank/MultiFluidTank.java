package nikita488.zycraft.multiblock.tank;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import nikita488.zycraft.multiblock.Cuboid6i;
import nikita488.zycraft.multiblock.IPressurizedFluidHandler;
import nikita488.zycraft.multiblock.IPressurizedFluidTank;
import nikita488.zycraft.multiblock.client.FluidCuboidRenderer;
import nikita488.zycraft.util.FluidUtils;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class MultiFluidTank extends FluidTank
{
    public static final int AMOUNT_PER_BLOCK = 16 * FluidAttributes.BUCKET_VOLUME;
    private final Cuboid6i cuboid;
    private final BlockPos lightPos;
    private final int area;
    private final LazyOptional<IFluidHandler>[] levels;

    public MultiFluidTank(Cuboid6i cuboid)
    {
        this(cuboid, stack -> true);
    }

    @SuppressWarnings("unchecked")
    public MultiFluidTank(Cuboid6i cuboid, Predicate<FluidStack> validator)
    {
        super(cuboid.area() * AMOUNT_PER_BLOCK, validator);
        this.cuboid = cuboid;
        this.lightPos = cuboid.center();
        this.area = cuboid.lengthX() * cuboid.lengthZ();
        this.levels = IntStream.range(0, cuboid.lengthY() + 1)
                .mapToObj(i -> LazyOptional.of(() -> new MultiFluidTank.Level(this, i)))
                .toArray(LazyOptional[]::new);
    }

    @Override
    protected void onContentsChanged()
    {
        float fluidHeight = getFluidHeight() - cuboid.minY();
        int level = (int)fluidHeight;
    }

    public LazyOptional<IFluidHandler> getLevel(int height)
    {
        int level = Math.max(height, cuboid.minY()) - cuboid.minY();
        return level < levels.length ? levels[level] : LazyOptional.empty();
    }

    public void invalidate()
    {
        Arrays.stream(levels).forEach(LazyOptional::invalidate);
    }

    public float getFluidHeight()
    {
        return Math.min((float)fluid.getAmount() / (area * AMOUNT_PER_BLOCK), cuboid.lengthY());
    }

    public float getPressure(int height)
    {
        float compressedPressure = ((float)fluid.getAmount() - capacity) / area;
        float fluidHeight = cuboid.minY() + getFluidHeight();
        float heightPressure = fluidHeight - Math.max(height, cuboid.minY());

        return Math.max(0, Math.max(compressedPressure, heightPressure * 100));
    }

    public int getLightValue(BlockPos pos)
    {
        return !fluid.isEmpty() && cuboid.contains(pos) ? fluid.getFluid().getAttributes().getLuminosity(fluid) : 0;
    }

    public void render(MatrixStack stack, IRenderTypeBuffer buffer, int lightMap, float partialTicks)
    {
        FluidCuboidRenderer.render(fluid, capacity, cuboid, 1, stack, buffer, lightPos);
    }

    public void decode(PacketBuffer buffer)
    {
        this.fluid = buffer.readFluidStack();
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

    public int getArea()
    {
        return area;
    }

    public int getMinY()
    {
        return cuboid.minY();
    }

    public class Level implements IPressurizedFluidHandler, IPressurizedFluidTank
    {
        private final MultiFluidTank tank;
        private final int height, capacityShrink;

        public Level(MultiFluidTank tank, int index)
        {
            this.tank = tank;
            this.height = Math.max(tank.getMinY(), tank.getMinY() + index);
            this.capacityShrink = Math.max(0, index) * tank.getArea() * AMOUNT_PER_BLOCK;
        }

/*        public void applyPressure(MultiFluidTank.Level targetLevel, int height)
        {
            if (getFluidAmount() > 0 && targetLevel.getFluidAmount() > 0 && !getFluid().isFluidEqual(targetLevel.getFluid()))
                return;

            MultiFluidTank targetTank = targetLevel.getTank();
            float diff = tank.getPressure(height) - targetTank.getPressure(height);
            int area = Math.min(tank.getArea(), targetTank.getArea());
            int amount;

            if (tank.getFluidAmount() <= tank.getArea() * 1.5D && diff > 0)
                amount = 1;
            else if (targetTank.getFluidAmount() <= targetTank.getArea() * 1.5D && diff < 0)
                amount = -1;
            else if (tank.getSpace() > 0 && targetTank.getSpace() > 0)
                amount = (int)(diff * area * 100);
            else
                amount = (int)(diff * area);

            FluidUtils.transferFromTo(this, targetLevel, amount);
        }*/

        @Nonnull
        @Override
        public FluidStack getFluid()
        {
            if (tank.isEmpty())
                return FluidStack.EMPTY;

            FluidStack stack = tank.getFluid().copy();
            stack.shrink(capacityShrink);
            return stack;
        }

        @Override
        public int getFluidAmount()
        {
            return !tank.isEmpty() ? tank.getFluidAmount() - capacityShrink : 0;
        }

        @Override
        public int getCapacity()
        {
            return tank.getCapacity() - capacityShrink;
        }

        @Override
        public boolean isFluidValid(FluidStack stack)
        {
            return tank.isFluidValid(stack);
        }

        @Override
        public float getPressure()
        {
            return tank.getPressure(height);
        }

        @Override
        public int getArea()
        {
            return tank.getArea();
        }

        @Override
        public void applyPressure(IPressurizedFluidHandler target)
        {
            if (!(target instanceof MultiFluidTank.Level))
                return;

            MultiFluidTank.Level targetLevel = (MultiFluidTank.Level)target;
            MultiFluidTank targetTank = targetLevel.getTank();

            if (getFluidAmount() > 0 && targetLevel.getFluidAmount() > 0 && !getFluid().isFluidEqual(targetLevel.getFluid()))
                return;

            float diff = getPressure() - targetLevel.getPressure();
            int area = Math.min(getArea(), targetLevel.getArea());
            int amount;

            if (tank.getFluidAmount() <= tank.getArea() * 1.5D && diff > 0)
                amount = 1;
            else if (targetTank.getFluidAmount() <= targetTank.getArea() * 1.5D && diff < 0)
                amount = -1;
            else if (tank.getFluidAmount() < tank.getCapacity() && targetTank.getFluidAmount() < targetTank.getCapacity())
                amount = (int)(diff * area * 100);
            else
                amount = (int)(diff * area);

            FluidUtils.transferFromTo(this, targetLevel, amount);
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
            return getFluid();
        }

        @Override
        public int getTankCapacity(int tank)
        {
            return getCapacity();
        }

        @Override
        public boolean isFluidValid(int tank, @Nonnull FluidStack stack)
        {
            return isFluidValid(stack);
        }

        @Override
        public int fill(FluidStack resource, FluidAction action)
        {
            return tank.fill(resource, action);
        }

        @Nonnull
        @Override
        public FluidStack drain(FluidStack resource, FluidAction action)
        {
            if (resource.isEmpty() || !resource.isFluidEqual(tank.getFluid()))
                return FluidStack.EMPTY;
            return drain(resource.getAmount(), action);
        }

        @Nonnull
        @Override
        public FluidStack drain(int maxDrain, FluidAction action)
        {
            return tank.drain(Math.min(getFluidAmount(), maxDrain), action);
        }

        @Override
        public float getTankPressure(int tank)
        {
            return getPressure();
        }

        @Override
        public int getTankArea(int tank)
        {
            return getArea();
        }

        public MultiFluidTank getTank()
        {
            return tank;
        }
    }
}

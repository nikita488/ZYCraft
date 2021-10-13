package nikita488.zycraft.multiblock.fluid;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import nikita488.zycraft.client.renderer.multiblock.MultiFluidRenderer;
import nikita488.zycraft.util.Cuboid6i;
import nikita488.zycraft.util.FluidUtils;
import nikita488.zycraft.util.MathUtils;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class MultiFluidTank extends FluidTank implements IMultiFluidHandler, IMultiFluidTank
{
    public static final int AMOUNT_PER_BLOCK = 16 * FluidAttributes.BUCKET_VOLUME;
    private final Cuboid6i bounds;
    private final BlockPos center;
    private final int area;
    private final Level[] levels;
    private FluidStack lastFluid = FluidStack.EMPTY;

    public MultiFluidTank(Cuboid6i bounds)
    {
        this(bounds, stack -> true);
    }

    public MultiFluidTank(Cuboid6i bounds, Predicate<FluidStack> validator)
    {
        super(bounds.volume() * AMOUNT_PER_BLOCK, validator);

        this.bounds = bounds;
        this.center = bounds.center();
        this.area = bounds.width() * bounds.depth();
        this.levels = new Level[bounds.height() + 1];

        for (int i = 0; i < levels.length; i++)
            this.levels[i] = new Level(i);
    }

    public void tick()
    {
        if (!lastFluid.isEmpty() && fluid.getAmount() != lastFluid.getAmount())
            lastFluid.setAmount(MathUtils.lerp(0.1F, lastFluid.getAmount(), fluid.getAmount()));
    }

    @Override
    public IMultiFluidHandler.Level level(int valvePosY)
    {
        int level = Math.max(valvePosY, bounds.minY()) - bounds.minY();
        return levels[Math.min(level, levels.length - 1)];
    }

    @Override
    public int getArea()
    {
        return area;
    }

    @Override
    public float getPressure(int valvePosY)
    {
        float compressedPressure = (fluid.getAmount() - capacity) / (float)area;
        float heightPressure = getFluidPosY() - Math.max(valvePosY, bounds.minY());

        return Math.max(0F, Math.max(compressedPressure, heightPressure * 100));
    }

    @Override
    public int getArea(int tank)
    {
        return getArea();
    }

    @Override
    public float getPressure(int tank, int valvePosY)
    {
        return getPressure(valvePosY);
    }

    @Override
    public void onContentsChanged()
    {
        if (!fluid.isFluidEqual(lastFluid))
        {
            this.lastFluid = fluid.copy();
            onFluidChanged();
            sendUpdated();
        }
        else if (Math.abs(fluid.getAmount() - lastFluid.getAmount()) > 250)
        {
            lastFluid.setAmount(fluid.getAmount());
            sendUpdated();
        }
    }

    public void sendUpdated() {}

    public void onFluidChanged() {}

    public void render(MatrixStack stack, IRenderTypeBuffer source, IBlockDisplayReader world, float partialTicks)
    {
        if (lastFluid.isEmpty() || capacity <= 0)
            return;

        stack.pushPose();
        stack.translate(-bounds.minX() + 1, -bounds.minY() + 1, -bounds.minZ() + 1);
        MultiFluidRenderer.render(stack, source, lastFluid, bounds, 1F, (float)lastFluid.getAmount() / capacity, world, center);
        stack.popPose();
    }

    public float getFluidPosY()
    {
        return bounds.minY() + Math.min((float)fluid.getAmount() / (area * AMOUNT_PER_BLOCK), bounds.height());
    }

    @Override
    public MultiFluidTank readFromNBT(CompoundNBT tag)
    {
        if (tag.contains("Fluid", Constants.NBT.TAG_COMPOUND))
            setFluid(FluidStack.loadFluidStackFromNBT(tag.getCompound("Fluid")));
        return this;
    }

    @Override
    public CompoundNBT writeToNBT(CompoundNBT tag)
    {
        if (!fluid.isEmpty())
            tag.put("Fluid", fluid.writeToNBT(new CompoundNBT()));
        return tag;
    }

    public void decode(PacketBuffer buffer)
    {
        setFluid(buffer.readFluidStack());
    }

    public void decodeUpdate(PacketBuffer buffer)
    {
        this.fluid = buffer.readFluidStack();

        if (!fluid.isFluidEqual(lastFluid))
        {
            this.lastFluid = new FluidStack(fluid, 1);
            onFluidChanged();
        }
    }

    public void encode(PacketBuffer buffer)
    {
        buffer.writeFluidStack(fluid);
    }

    @Override
    public void setFluid(FluidStack stack)
    {
        super.setFluid(stack);

        if (stack.isFluidEqual(lastFluid))
            return;

        this.lastFluid = stack.copy();
        onFluidChanged();
    }

    @Override
    public MultiFluidTank setCapacity(int capacity)
    {
        super.setCapacity(capacity);
        return this;
    }

    @Override
    public MultiFluidTank setValidator(Predicate<FluidStack> validator)
    {
        super.setValidator(validator);
        return this;
    }

    public class Level implements IMultiFluidHandler.Level, IMultiFluidTank.Level
    {
        private final MultiFluidTank tank;
        private final int inaccessibleAmount;

        public Level(int index)
        {
            this.tank = MultiFluidTank.this;
            this.inaccessibleAmount = Math.max(0, index) * tank.getArea() * AMOUNT_PER_BLOCK;
        }

        @Override
        public IMultiFluidHandler parentTank()
        {
            return tank;
        }

        @Override
        public void applyPressure(IMultiFluidHandler.Level targetLevel, int valvePosY)
        {
            IMultiFluidHandler targetTank = targetLevel.parentTank();
            float diff = tank.getPressure(valvePosY) - targetTank.getPressure(0, valvePosY);
            int targetAmount = targetTank.getFluidInTank(0).getAmount();
            int targetCapacity = targetTank.getTankCapacity(0);
            int targetArea = targetTank.getArea(0);
            int area = Math.min(tank.getArea(), targetArea);
            int amount;

            if (tank.getFluidAmount() <= tank.getArea() * 1.5F && diff > 0F)
                amount = 1;
            else if (targetAmount <= targetArea * 1.5F && diff < 0F)
                amount = -1;
            else if (tank.getFluidAmount() < tank.getCapacity() && targetAmount < targetCapacity)
                amount = (int)(diff * area * 100);
            else
                amount = (int)(diff * area);

            FluidUtils.transferFromTo(this, targetLevel, amount);
        }

        @Nonnull
        @Override
        public FluidStack getFluid()
        {
            return !tank.isEmpty() ? Util.make(tank.getFluid().copy(), stack -> stack.shrink(inaccessibleAmount)) : FluidStack.EMPTY;
        }

        @Override
        public int getFluidAmount()
        {
            return Math.max(0, tank.getFluidAmount() - inaccessibleAmount);
        }

        @Override
        public int getCapacity()
        {
            return Math.max(0, tank.getCapacity() - inaccessibleAmount);
        }

        @Override
        public boolean isFluidValid(FluidStack stack)
        {
            return tank.isFluidValid(stack);
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
            return !resource.isEmpty() && resource.isFluidEqual(tank.getFluid()) ? drain(resource.getAmount(), action) : FluidStack.EMPTY;
        }

        @Nonnull
        @Override
        public FluidStack drain(int maxDrain, FluidAction action)
        {
            return tank.drain(Math.min(getFluidAmount(), maxDrain), action);
        }
    }
}

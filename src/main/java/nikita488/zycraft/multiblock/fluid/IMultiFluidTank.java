package nikita488.zycraft.multiblock.fluid;

import net.minecraftforge.fluids.IFluidTank;

public interface IMultiFluidTank extends IFluidTank
{
    IMultiFluidHandler.Level level(int valvePosY);

    int getArea();

    float getPressure(int valvePosY);

    interface Level extends IFluidTank
    {
        IMultiFluidHandler parentTank();

        void applyPressure(IMultiFluidHandler.Level targetLevel, int valvePosY);
    }
}

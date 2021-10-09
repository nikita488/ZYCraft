package nikita488.zycraft.multiblock.fluid;

import net.minecraftforge.fluids.capability.IFluidHandler;

public interface IMultiFluidHandler extends IFluidHandler
{
    Level level(int valvePosY);

    int getArea(int tank);

    float getPressure(int tank, int valvePosY);

    interface Level extends IFluidHandler
    {
        IMultiFluidHandler parentTank();

        void applyPressure(IMultiFluidHandler.Level targetLevel, int valvePosY);
    }
}

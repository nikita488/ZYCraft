package nikita488.zycraft.multiblock;

import net.minecraftforge.fluids.IFluidTank;

public interface IPressurizedFluidTank extends IFluidTank
{
    float getPressure();

    int getArea();

    void applyPressure(IPressurizedFluidHandler target);
}

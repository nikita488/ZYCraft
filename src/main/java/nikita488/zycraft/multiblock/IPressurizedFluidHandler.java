package nikita488.zycraft.multiblock;

import net.minecraftforge.fluids.capability.IFluidHandler;

public interface IPressurizedFluidHandler extends IFluidHandler
{
    float getTankPressure(int tank);

    int getTankArea(int tank);

    void applyPressure(IPressurizedFluidHandler target);
}

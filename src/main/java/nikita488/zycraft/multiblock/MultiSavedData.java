package nikita488.zycraft.multiblock;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.storage.WorldSavedData;

public class MultiSavedData extends WorldSavedData
{
    public MultiSavedData(String name)
    {
        super(name);
    }

    @Override
    public void read(CompoundNBT tag) {}

    @Override
    public CompoundNBT write(CompoundNBT tag)
    {
        return tag;
    }
}

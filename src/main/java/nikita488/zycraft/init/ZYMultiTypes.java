package nikita488.zycraft.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.multiblock.MultiType;
import nikita488.zycraft.multiblock.TankMultiBlock;
import nikita488.zycraft.multiblock.former.TankFormer;

public class ZYMultiTypes
{
    public static final RegistryEntry<MultiType<TankMultiBlock>> TANK = ZYCraft.registrate().simple("tank", ZYRegistries.Keys.MULTI_TYPES, () ->
            new MultiType<>(TankMultiBlock::new, TankFormer::form));

    public static void init() {}
}

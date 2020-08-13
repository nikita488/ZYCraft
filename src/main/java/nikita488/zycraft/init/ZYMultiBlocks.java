package nikita488.zycraft.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.api.multiblock.MultiType;
import nikita488.zycraft.multiblock.test.TestMultiBlock;
import nikita488.zycraft.multiblock.test.TestMultiFormer;

public class ZYMultiBlocks
{
    public static final RegistryEntry<MultiType> TEST = ZYCraft.REGISTRY.object("test")
            .simple(MultiType.class, () -> new MultiType(TestMultiBlock::new, new TestMultiFormer()));

    public static void init() {}
}

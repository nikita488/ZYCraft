package nikita488.zycraft.api.multiblock;

import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.registries.ForgeRegistryEntry;
import nikita488.zycraft.init.ZYRegistries;

import java.util.function.Supplier;

public class MultiType extends ForgeRegistryEntry<MultiType>
{
    private final Supplier<? extends MultiBlock> factory;
    private final IMultiFormer former;

    public MultiType(Supplier<? extends MultiBlock> factory, IMultiFormer former)
    {
        this.factory = factory;
        this.former = former;
    }

    public MultiBlock get()
    {
        return factory.get();
    }

    public static MultiType get(String name)
    {
        return ZYRegistries.multiType().getValue(new ResourceLocation(name));
    }

    public static ActionResultType formAny(IWorld world, BlockPos pos)
    {
        System.out.println(ZYRegistries.multiType());
        for (MultiType type : ZYRegistries.multiType().getValues())
            if (type.form(world, pos))
                return ActionResultType.SUCCESS;

        return ActionResultType.CONSUME;
    }

    public boolean form(IWorld world, BlockPos pos)
    {
        return former != null && former.form(world, pos);
    }

    public IMultiFormer former()
    {
        return former;
    }
}

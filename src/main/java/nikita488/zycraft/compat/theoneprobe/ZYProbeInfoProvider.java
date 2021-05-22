package nikita488.zycraft.compat.theoneprobe;

import mcjty.theoneprobe.api.*;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.block.FabricatorBlock;
import nikita488.zycraft.init.ZYBlocks;

import java.util.function.Function;

public class ZYProbeInfoProvider implements IProbeInfoProvider, Function<ITheOneProbe, Void>
{
    @Override
    public Void apply(ITheOneProbe probe)
    {
        probe.registerProvider(this);
        return null;
    }

    @Override
    public String getID()
    {
        return ZYCraft.id("default").toString();
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo info, PlayerEntity player, World world, BlockState state, IProbeHitData data)
    {
        if (state.matchesBlock(ZYBlocks.FABRICATOR.get()))
            addFabricatorProbeInfo(info, state);
    }

    private void addFabricatorProbeInfo(IProbeInfo info, BlockState state)
    {
        info.horizontal()
                .item(new ItemStack(Items.REDSTONE), info.defaultItemStyle().width(14).height(14))
                .text(CompoundText.createLabelInfo("Mode: ", state.get(FabricatorBlock.MODE).displayName()));
    }
}

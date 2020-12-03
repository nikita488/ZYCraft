package nikita488.zycraft.multiblock.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import nikita488.zycraft.container.ZYContainer;
import nikita488.zycraft.multiblock.MultiBlock;

import javax.annotation.Nullable;

public class MultiContainer<M extends MultiBlock> extends ZYContainer
{
    @Nullable
    private final M multiBlock;

    public MultiContainer(ContainerType<?> type, int id, M multiBlock)
    {
        super(type, id);
        this.multiBlock = multiBlock;
    }

    @Override
    public boolean canInteractWith(PlayerEntity player)
    {
        return multiBlock != null && multiBlock.isValid();//TODO: I think we really need to check the distance to the child block we are clicked here too
    }
}

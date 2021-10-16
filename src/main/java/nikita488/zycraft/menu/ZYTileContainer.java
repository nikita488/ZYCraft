package nikita488.zycraft.menu;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import nikita488.zycraft.tile.ZYTile;

import javax.annotation.Nullable;

public class ZYTileContainer<T extends ZYTile> extends ZYContainer
{
    @Nullable
    protected final T blockEntity;

    public ZYTileContainer(@Nullable ContainerType<?> type, int windowID, @Nullable T blockEntity)
    {
        super(type, windowID);
        this.blockEntity = blockEntity;
    }

    @Override
    public boolean stillValid(PlayerEntity player)
    {
        return blockEntity != null && blockEntity.stillValid(player);
    }

    @Nullable
    public T blockEntity()
    {
        return blockEntity;
    }
}

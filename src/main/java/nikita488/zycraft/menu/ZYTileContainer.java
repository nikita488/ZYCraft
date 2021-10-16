package nikita488.zycraft.menu;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import nikita488.zycraft.tile.ZYTile;

import javax.annotation.Nullable;

public class ZYTileContainer<T extends ZYTile> extends ZYContainer
{
    @Nullable
    protected final T blockEntity;

    public ZYTileContainer(@Nullable MenuType<?> type, int windowID, @Nullable T blockEntity)
    {
        super(type, windowID);
        this.blockEntity = blockEntity;
    }

    @Override
    public boolean stillValid(Player player)
    {
        return blockEntity != null && blockEntity.stillValid(player);
    }

    @Nullable
    public T blockEntity()
    {
        return blockEntity;
    }
}

package nikita488.zycraft.menu;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import nikita488.zycraft.tile.ZYTile;

import javax.annotation.Nullable;

public class ZYTileContainer<T extends ZYTile> extends ZYContainer
{
    @Nullable
    protected final T tile;

    public ZYTileContainer(@Nullable ContainerType<?> type, int windowID, @Nullable T tile)
    {
        super(type, windowID);
        this.tile = tile;
    }

    @Override
    public boolean canInteractWith(PlayerEntity player)
    {
        return tile != null && tile.isUsableByPlayer(player);
    }

    @Nullable
    public T tile()
    {
        return tile;
    }
}

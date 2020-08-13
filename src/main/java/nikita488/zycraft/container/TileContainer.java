package nikita488.zycraft.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import nikita488.zycraft.tile.BaseTile;

import javax.annotation.Nullable;

public class TileContainer<T extends BaseTile> extends BaseContainer
{
    protected final T tile;

    public TileContainer(@Nullable ContainerType<?> type, int windowID, T tile)
    {
        super(type, windowID);
        this.tile = tile;
    }

    @Override
    public boolean canInteractWith(PlayerEntity player)
    {
        return tile.canInteractWith(player);
    }
}

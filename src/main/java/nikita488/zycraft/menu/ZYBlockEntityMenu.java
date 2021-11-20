package nikita488.zycraft.menu;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import nikita488.zycraft.block.entity.ZYBlockEntity;

import javax.annotation.Nullable;

public class ZYBlockEntityMenu<T extends ZYBlockEntity> extends ZYMenu
{
    @Nullable
    protected final T blockEntity;

    public ZYBlockEntityMenu(@Nullable MenuType<?> type, int windowID, @Nullable T blockEntity)
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

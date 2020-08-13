package nikita488.zycraft.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface IClickableSlot
{
    ItemStack onClick(Container container, int index, int dragType, ClickType clickType, PlayerEntity player);
}

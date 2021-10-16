package nikita488.zycraft.compat.jei;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.client.gui.screen.inventory.FabricatorScreen;
import nikita488.zycraft.network.SetSlotStackPacket;

import java.util.Collections;
import java.util.List;

public class FabricatorGhostIngredientHandler implements IGhostIngredientHandler<FabricatorScreen>
{
    @Override
    public <I> List<Target<I>> getTargets(FabricatorScreen screen, I ingredient, boolean doStart)
    {
        if (!(ingredient instanceof ItemStack))
            return Collections.emptyList();

        AbstractContainerMenu menu = screen.getMenu();
        ObjectList<Target<I>> targets = new ObjectArrayList<>();

        for (int i = 0; i < 9; i++)
        {
            Slot slot = menu.getSlot(i);

            targets.add(new Target<I>()
            {
                @Override
                public Rect2i getArea()
                {
                    return new Rect2i(screen.getGuiLeft() + slot.x, screen.getGuiTop() + slot.y, 16, 16);
                }

                @Override
                public void accept(I ingredient)
                {
                    if (ingredient instanceof ItemStack)
                        ZYCraft.CHANNEL.sendToServer(new SetSlotStackPacket(menu.containerId, slot.getSlotIndex(), (ItemStack)ingredient));
                }
            });
        }

        return targets;
    }

    @Override
    public void onComplete() {}
}

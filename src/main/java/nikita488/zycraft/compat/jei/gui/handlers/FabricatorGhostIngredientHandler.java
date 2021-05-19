package nikita488.zycraft.compat.jei.gui.handlers;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
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

        Container container = screen.getContainer();
        ObjectList<Target<I>> targets = new ObjectArrayList<>();

        for (int i = 0; i < 9; i++)
        {
            Slot slot = container.getSlot(i);

            targets.add(new Target<I>()
            {
                @Override
                public Rectangle2d getArea()
                {
                    return new Rectangle2d(screen.getGuiLeft() + slot.xPos, screen.getGuiTop() + slot.yPos, 16, 16);
                }

                @Override
                public void accept(I ingredient)
                {
                    if (ingredient instanceof ItemStack)
                        ZYCraft.CHANNEL.sendToServer(new SetSlotStackPacket(container.windowId, slot.getSlotIndex(), (ItemStack)ingredient));
                }
            });
        }

        return targets;
    }

    @Override
    public void onComplete() {}
}

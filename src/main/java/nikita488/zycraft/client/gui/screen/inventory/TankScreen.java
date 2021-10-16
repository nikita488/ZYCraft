package nikita488.zycraft.client.gui.screen.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.client.gui.GuiComponent;
import nikita488.zycraft.client.gui.screen.ZYScreen;
import nikita488.zycraft.client.gui.screen.ZYScreen.FluidGaugeWidget;
import nikita488.zycraft.client.texture.GuiComponentManager;
import nikita488.zycraft.enums.ZYType;
import nikita488.zycraft.menu.TankContainer;
import nikita488.zycraft.util.Color;

public class TankScreen extends ZYScreen<TankContainer>
{
    public static final ResourceLocation TEXTURE = ZYCraft.id("textures/gui/container/tank.png");

    public TankScreen(TankContainer container, Inventory inventory, Component title)
    {
        super(container, inventory, title);
        this.imageHeight = 188;
    }

    @Override
    protected void init()
    {
        super.init();
        addButton(new FluidGaugeWidget(leftPos + 24, topPos + 24, menu.fluidData(), menu.capacity()));
    }

    @Override
    protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY)
    {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        renderGUI(stack, TEXTURE, Color.argb(ZYType.BLUE.rgb(), 255));

        bindTexture(GuiComponentManager.ATLAS_ID);
        renderGuiComponentWithColor(stack, leftPos + 122, topPos + 57, 0.25F, GuiComponent.RIGHT_ARROW, 0x7F00FFFF);
        renderGuiComponentWithColor(stack, leftPos + 97, topPos + 53, 0.25F, GuiComponent.DROP, 0xFF0097DD);

        renderIOSlotOverlays(stack, leftPos, topPos, menu.slotOverlays(), menu.ioData());
        RenderSystem.disableBlend();
    }
}

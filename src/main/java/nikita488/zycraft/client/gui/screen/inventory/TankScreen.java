package nikita488.zycraft.client.gui.screen.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.client.gui.GuiComponent;
import nikita488.zycraft.client.gui.screen.ZYScreen;
import nikita488.zycraft.client.texture.GuiComponentManager;
import nikita488.zycraft.enums.ZYType;
import nikita488.zycraft.menu.TankContainer;
import nikita488.zycraft.util.Color;

public class TankScreen extends ZYScreen<TankContainer>
{
    public static final ResourceLocation TEXTURE = ZYCraft.id("textures/gui/container/tank.png");

    public TankScreen(TankContainer container, PlayerInventory inventory, ITextComponent title)
    {
        super(container, inventory, title);
        this.ySize = 188;
    }

    @Override
    protected void init()
    {
        super.init();
        addButton(new FluidGaugeWidget(guiLeft + 24, guiTop + 24, container.fluidData(), container.capacity()));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY)
    {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        renderGUI(stack, TEXTURE, Color.argb(ZYType.BLUE.rgb(), 255));

        bindTexture(GuiComponentManager.ATLAS_ID);
        renderGuiComponentWithColor(stack, guiLeft + 122, guiTop + 57, 0.25F, GuiComponent.RIGHT_ARROW, 0x7F00FFFF);
        renderGuiComponentWithColor(stack, guiLeft + 97, guiTop + 53, 0.25F, GuiComponent.DROP, 0xFF0097DD);

        renderIOSlotOverlays(stack, guiLeft, guiTop, container.slotOverlays(), container.ioData());
        RenderSystem.disableBlend();
    }
}

package nikita488.zycraft.multiblock.tank;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.client.ZYClientSetup;
import nikita488.zycraft.client.gui.ZYScreen;
import nikita488.zycraft.client.gui.ZYWidget;
import nikita488.zycraft.client.texture.CloudSprite;
import org.lwjgl.opengl.GL11;

public class TankScreen extends ZYScreen<TankContainer>
{
    public TankScreen(TankContainer container, PlayerInventory inventory, ITextComponent title)
    {
        super(container, inventory, title);
        this.xSize = 199;
        this.ySize = 218;
        this.titleColor = 0x999999;
        this.playerInventoryTitleColor = 0x999999;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack stack, int mouseX, int mouseY)
    {
        super.drawGuiContainerForegroundLayer(stack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY)
    {
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        drawRepeatableQuad(stack, guiLeft + 6, guiTop + 211, getBlitOffset(), 186, 204, 0xFF0064FF, Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(CloudSprite.NAME), 32);
        drawRepeatableQuad(stack, guiLeft + 6, guiTop + 211, getBlitOffset(), 186, 204, 0xFF434343, ZYClientSetup.getWidgetAtlas().getSprite(ZYWidget.BACKGROUND), 32);

        Minecraft.getInstance().getTextureManager().bindTexture(ZYCraft.modLoc("textures/gui/inventory/tank.png"));
        blit(stack, guiLeft - 1, guiTop - 1, 0, 0, xSize + 2, ySize + 2);

        super.drawGuiContainerBackgroundLayer(stack, partialTicks, mouseX, mouseY);

        /*drawFluidStack(stack, guiLeft + 24, guiTop + 83, getBlitOffset(), 54, 60, container.fluid(), container.capacity());

        Minecraft.getInstance().getTextureManager().bindTexture(WidgetTextureAtlas.NAME);
        drawWidget(stack, guiLeft + 23, guiTop + 22, getBlitOffset(), 56, 62, ZYWidget.TANK_BIG);*/

        RenderSystem.disableBlend();
        GL11.glEnable(GL11.GL_ALPHA_TEST);
    }
}

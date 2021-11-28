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
import nikita488.zycraft.init.ZYLang;
import nikita488.zycraft.menu.FabricatorContainer;
import nikita488.zycraft.util.Color;

public class FabricatorScreen extends ZYScreen<FabricatorContainer>
{
    public static final ResourceLocation TEXTURE = ZYCraft.id("textures/gui/container/fabricator.png");
    private Menu modeMenu;

    public FabricatorScreen(FabricatorContainer menu, PlayerInventory inventory, ITextComponent title)
    {
        super(menu, inventory, title);
    }

    @Override
    protected void init()
    {
        super.init();
        this.modeMenu = new Menu(leftPos - 34, topPos + 23, ZYType.BLUE.rgb(), menu.modeData())
                .addItem(ZYLang.FABRICATOR_AUTO_LOW, new ResourceLocation("block/redstone_torch_off"))
                .addItem(ZYLang.FABRICATOR_AUTO_HIGH, new ResourceLocation("block/redstone_torch"))
                .addItem(ZYLang.FABRICATOR_PULSE, new ResourceLocation("item/redstone"));
    }

    @Override
    protected void renderBg(MatrixStack stack, float partialTicks, int mouseX, int mouseY)
    {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        renderGUI(stack, TEXTURE, Color.argb(ZYType.BLUE.rgb(), 255));
        bindTexture(GuiComponentManager.ATLAS_ID);
        renderGuiComponentWithColor(stack, leftPos + 86, topPos + 29, 0.25F, GuiComponent.RIGHT_ARROW, 0x7F00FFFF);
        renderGuiComponentWithColor(stack, leftPos + 86, topPos + 65, 0.25F, GuiComponent.RIGHT_ARROW, 0x7F00FFFF);
        modeMenu.render(stack, mouseX, mouseY, partialTicks);

        RenderSystem.disableBlend();
    }
}

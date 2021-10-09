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
    private Menu menu;

    public FabricatorScreen(FabricatorContainer container, PlayerInventory inventory, ITextComponent title)
    {
        super(container, inventory, title);
    }

    @Override
    protected void init()
    {
        super.init();
        this.menu = new Menu(guiLeft - 34, guiTop + 23, ZYType.BLUE.rgb(), container.modeData())
                .addItem(ZYLang.FABRICATOR_AUTO_LOW, new ResourceLocation("block/redstone_torch_off"))
                .addItem(ZYLang.FABRICATOR_AUTO_HIGH, new ResourceLocation("block/redstone_torch"))
                .addItem(ZYLang.FABRICATOR_PULSE, new ResourceLocation("item/redstone"));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY)
    {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        renderGUI(stack, TEXTURE, Color.argb(ZYType.BLUE.rgb(), 255));
        bindTexture(GuiComponentManager.ATLAS_ID);
        renderGuiComponentWithColor(stack, guiLeft + 86, guiTop + 29, 0.25F, GuiComponent.RIGHT_ARROW, 0x7F00FFFF);
        renderGuiComponentWithColor(stack, guiLeft + 86, guiTop + 65, 0.25F, GuiComponent.RIGHT_ARROW, 0x7F00FFFF);
        menu.render(stack, mouseX, mouseY, partialTicks);

        RenderSystem.disableBlend();
    }
}

package nikita488.zycraft.client.gui.screen.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.client.ZYSpriteTextureManager;
import nikita488.zycraft.client.gui.screen.ZYScreen;
import nikita488.zycraft.enums.ZYType;
import nikita488.zycraft.init.ZYLang;
import nikita488.zycraft.inventory.container.FabricatorContainer;

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
        this.menu = new Menu(guiLeft - 34, guiTop + 23, ZYType.BLUE.rgb(), container.mode())
                .addItem(ZYLang.AUTO_LOW, new ResourceLocation("block/redstone_torch_off"))
                .addItem(ZYLang.AUTO_HIGH, new ResourceLocation("block/redstone_torch"))
                .addItem(ZYLang.PULSE, new ResourceLocation("item/redstone"));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY)
    {
        renderGUI(stack, TEXTURE, 0xFF0064FF);
        bindTexture(ZYSpriteTextureManager.NAME);
        renderRightArrow(stack, guiLeft + 86, guiTop + 29, 0x7F00FFFF, 0.25F);
        renderRightArrow(stack, guiLeft + 86, guiTop + 65, 0x7F00FFFF, 0.25F);
        menu.render(stack);
    }
}

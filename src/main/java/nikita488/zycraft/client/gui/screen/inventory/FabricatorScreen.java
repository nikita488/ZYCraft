package nikita488.zycraft.client.gui.screen.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.client.gui.GuiComponent;
import nikita488.zycraft.client.gui.screen.ZYScreen;
import nikita488.zycraft.client.texture.GuiComponentManager;
import nikita488.zycraft.enums.ZYType;
import nikita488.zycraft.init.ZYLang;
import nikita488.zycraft.menu.FabricatorMenu;
import nikita488.zycraft.util.Color;

public class FabricatorScreen extends ZYScreen<FabricatorMenu>
{
    public static final ResourceLocation TEXTURE = ZYCraft.id("textures/gui/container/fabricator.png");

    public FabricatorScreen(FabricatorMenu menu, Inventory inventory, Component title)
    {
        super(menu, inventory, title);
    }

    @Override
    protected void init()
    {
        super.init();
        addRenderableOnly(new Menu(leftPos - 34, topPos + 23, ZYType.BLUE.rgb(), menu.modeData())
                .addItem(ZYLang.FABRICATOR_AUTO_LOW, new ResourceLocation("block/redstone_torch_off"))
                .addItem(ZYLang.FABRICATOR_AUTO_HIGH, new ResourceLocation("block/redstone_torch"))
                .addItem(ZYLang.FABRICATOR_PULSE, new ResourceLocation("item/redstone")));
    }

    @Override
    protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY)
    {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        renderGUI(stack, TEXTURE, Color.argb(ZYType.BLUE.rgb(), 255));
        bindTexture(GuiComponentManager.ATLAS_ID);
        renderGuiComponentWithColor(stack, leftPos + 86, topPos + 29, 0.25F, GuiComponent.RIGHT_ARROW, 0x7F00FFFF);
        renderGuiComponentWithColor(stack, leftPos + 86, topPos + 65, 0.25F, GuiComponent.RIGHT_ARROW, 0x7F00FFFF);

        RenderSystem.disableBlend();
    }
}

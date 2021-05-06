package nikita488.zycraft.client.gui.screen.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.client.gui.screen.ZYScreen;
import nikita488.zycraft.inventory.container.FabricatorContainer;

public class FabricatorScreen extends ZYScreen<FabricatorContainer>
{
    public static final ResourceLocation TEXTURE = ZYCraft.modLoc("textures/gui/container/fabricator.png");

    public FabricatorScreen(FabricatorContainer container, PlayerInventory inventory, ITextComponent title)
    {
        super(container, inventory, title);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY)
    {
        renderGUI(stack, TEXTURE, 0xFF0064FF);
    }
}

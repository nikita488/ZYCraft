package nikita488.zycraft.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import nikita488.zycraft.client.ZYClientSetup;
import org.lwjgl.opengl.GL11;

public class ZYScreen<T extends Container> extends ContainerScreen<T>
{
    protected int titleColor = 0x404040;
    protected int playerInventoryTitleColor = 0x404040;

    public ZYScreen(T container, PlayerInventory inventory, ITextComponent title)
    {
        super(container, inventory, title);
    }

    @Override
    protected void init()
    {
        super.init();
        this.titleX = (xSize - font.getStringPropertyWidth(title)) / 2;
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks)
    {
        renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        renderHoveredTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack stack, int mouseX, int mouseY)
    {
        font.func_243248_b(stack, title, titleX, titleY, titleColor);
        font.func_243248_b(stack, playerInventory.getDisplayName(), playerInventoryTitleX, playerInventoryTitleY, playerInventoryTitleColor);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY)
    {
        Minecraft.getInstance().getTextureManager().bindTexture(WidgetTextureAtlas.NAME);
        drawSlots();
    }

    protected void drawSlots()
    {
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        TextureAtlasSprite sprite = ZYClientSetup.getWidgetAtlas().getSprite(ZYWidget.SLOT);
        float u1 = sprite.getMinU();
        float u2 = sprite.getMaxU();
        float v1 = sprite.getMinV();
        float v2 = sprite.getMaxV();

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        for (Slot slot : container.inventorySlots)
        {
            if (!slot.isEnabled())
                continue;

            int x = guiLeft + slot.xPos - 1;
            int y = guiTop + slot.yPos - 1;

            buffer.pos(x, y + 18, getBlitOffset()).tex(u1, v2).endVertex();
            buffer.pos(x + 18, y + 18, getBlitOffset()).tex(u2, v2).endVertex();
            buffer.pos(x + 18, y, getBlitOffset()).tex(u2, v1).endVertex();
            buffer.pos(x, y, getBlitOffset()).tex(u1, v1).endVertex();
        }

        Tessellator.getInstance().draw();
    }

    public static void drawWidget(MatrixStack stack, int x, int y, int z, int width, int height, ZYWidget widget)
    {
        blit(stack, x, y, z, width, height, ZYClientSetup.getWidgetAtlas().getSprite(widget));
    }

    public static void drawFluidTank(MatrixStack stack, int x, int y, int z, float width, float height, IFluidHandler handler)
    {
        drawFluidStack(stack, x, y, z, width, height, handler.getFluidInTank(0), handler.getTankCapacity(0));
    }

    public static void drawFluidStack(MatrixStack stack, int x, int y, int z, float width, float height, FluidStack fluidStack, int capacity)
    {
        if (fluidStack.isEmpty())
            return;

        FluidAttributes attributes = fluidStack.getFluid().getAttributes();
        int color = attributes.getColor(fluidStack);
        TextureAtlasSprite sprite = ModelLoaderRegistry.blockMaterial(attributes.getStillTexture(fluidStack)).getSprite();
        height *= (float)fluidStack.getAmount() / capacity;

        drawRepeatableQuad(stack, x, y, z, width, height, color, sprite, 16);
    }

    public static void drawRepeatableQuad(MatrixStack stack, int x, int y, int z, float width, float height, int color, TextureAtlasSprite sprite, float resolution)
    {
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        int a = (color >> 24) & 0xFF;
        float u1 = sprite.getMinU();
        float v2 = sprite.getMaxV();

        stack.push();
        stack.translate(x, y, z);

        Minecraft.getInstance().getTextureManager().bindTexture(sprite.getAtlasTexture().getTextureLocation());
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEX);

        Matrix4f matrix = stack.getLast().getMatrix();

        for (float quadX = 0; quadX < width; quadX += resolution)
        {
            for (float quadY = 0; quadY < height; quadY += resolution)
            {
                float quadWidth = Math.min(width - quadX, resolution);
                float quadHeight = Math.min(height - quadY, resolution);
                float u2 = u1 + (sprite.getMaxU() - u1) * quadWidth / resolution;
                float v1 = v2 - (v2 - sprite.getMinV()) * quadHeight / resolution;

                buffer.pos(matrix, quadX, -quadY - quadHeight, 0)
                        .color(r, g, b, a)
                        .tex(u1, v1)
                        .endVertex();
                buffer.pos(matrix, quadX, -quadY, 0)
                        .color(r, g, b, a)
                        .tex(u1, v2)
                        .endVertex();
                buffer.pos(matrix, quadX + quadWidth, -quadY, 0)
                        .color(r, g, b, a)
                        .tex(u2, v2)
                        .endVertex();
                buffer.pos(matrix, quadX + quadWidth, -quadY - quadHeight, 0)
                        .color(r, g, b, a)
                        .tex(u2, v1)
                        .endVertex();
            }
        }

        Tessellator.getInstance().draw();
        stack.pop();
    }
}

package nikita488.zycraft.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import nikita488.zycraft.client.ZYClientSetup;
import nikita488.zycraft.client.resources.ZYWidget;
import nikita488.zycraft.client.texture.CloudSprite;
import org.lwjgl.opengl.GL11;

public abstract class ZYScreen<T extends Container> extends ContainerScreen<T>
{
    protected int titleColor = 0x999999;
    protected int playerInventoryTitleColor = 0x999999;

    public ZYScreen(T container, PlayerInventory inventory, ITextComponent title)
    {
        super(container, inventory, title);
        this.xSize = 192;
        this.ySize = 182;
        this.titleY = 14;
        this.playerInventoryTitleX = 16;
        this.playerInventoryTitleY = ySize - 102;
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
        font.drawText(stack, title, titleX, titleY, titleColor);
        font.drawText(stack, playerInventory.getDisplayName(), playerInventoryTitleX, playerInventoryTitleY, playerInventoryTitleColor);
    }

    public void renderGUI(MatrixStack stack, ResourceLocation texture, int argb)
    {
        renderBackground(stack, guiLeft + 8, guiTop + ySize - 8, xSize - 16, ySize - 16, argb);
        minecraft.getTextureManager().bindTexture(texture);
        blit(stack, guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    public void renderBackground(MatrixStack stack, int x, int y, float width, float height, int argb)
    {
        int r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int b = argb & 0xFF;
        int a = (argb >> 24) & 0xFF;

        RenderSystem.color4f(r / 255.0F, g / 255.0F, b / 255.0F, a / 255.0F);
        renderTileableSprite(stack, x, y, width, height, minecraft.getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(CloudSprite.NAME), 32);
        RenderSystem.color4f(67 / 255.0F, 67 / 255.0F, 67 / 255.0F, 1);
        renderTileableSprite(stack, x, y, width, height, ZYClientSetup.widgetTextures().get(ZYWidget.BACKGROUND), 32);
        RenderSystem.color4f(1, 1, 1, 1);
    }

    public void renderWidget(MatrixStack stack, int x, int y, int width, int height, ZYWidget widget)
    {
        renderWidget(stack, x, y, getBlitOffset(), width, height, widget);
    }

    public void renderTileableSprite(MatrixStack stack, int x, int y, float width, float height, TextureAtlasSprite sprite, float resolution)
    {
        renderTileableSprite(stack, x, y, getBlitOffset(), width, height, sprite, resolution);
    }

    public static void renderWidget(MatrixStack stack, int x, int y, int z, int width, int height, ZYWidget widget)
    {
        blit(stack, x, y, z, width, height, ZYClientSetup.widgetTextures().get(widget));
    }

    public static void renderTileableSprite(MatrixStack stack, int x, int y, int z, float width, float height, TextureAtlasSprite sprite, float resolution)
    {
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        float u1 = sprite.getMinU();
        float v2 = sprite.getMaxV();

        stack.push();
        stack.translate(x, y, z);

        Minecraft.getInstance().getTextureManager().bindTexture(sprite.getAtlasTexture().getTextureLocation());
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

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
                        .tex(u1, v1)
                        .endVertex();
                buffer.pos(matrix, quadX, -quadY, 0)
                        .tex(u1, v2)
                        .endVertex();
                buffer.pos(matrix, quadX + quadWidth, -quadY, 0)
                        .tex(u2, v2)
                        .endVertex();
                buffer.pos(matrix, quadX + quadWidth, -quadY - quadHeight, 0)
                        .tex(u2, v1)
                        .endVertex();
            }
        }

        Tessellator.getInstance().draw();
        stack.pop();
    }
}

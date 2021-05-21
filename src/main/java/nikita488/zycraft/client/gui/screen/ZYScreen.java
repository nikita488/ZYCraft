package nikita488.zycraft.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.AbstractButton;
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
import nikita488.zycraft.client.ZYSpriteTextureManager;
import nikita488.zycraft.client.ZYSpriteType;
import nikita488.zycraft.client.texture.CloudSprite;
import nikita488.zycraft.inventory.container.variable.IntContainerVariable;
import nikita488.zycraft.util.Color;
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
        bindTexture(texture);
        blit(stack, guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    public void renderBackground(MatrixStack stack, int x, int y, float width, float height, int argb)
    {
        setColor(argb);
        bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        renderTileableSprite(stack, x, y, width, height, getSprite(AtlasTexture.LOCATION_BLOCKS_TEXTURE, CloudSprite.NAME), 32);

        setColor(0xFF434343);
        bindTexture(ZYSpriteTextureManager.NAME);
        renderTileableSprite(stack, x, y, width, height, ZYClientSetup.sprites().get(ZYSpriteType.BACKGROUND), 32);
        resetColor();
    }

    public void renderRightArrow(MatrixStack stack, int x, int y, int argb, float resolution)
    {
        setColor(argb);
        renderSprite(stack, x, y, (int)(80 * resolution), (int)(32 * resolution), ZYSpriteType.RIGHT_ARROW);
        resetColor();
    }

    public void renderSprite(MatrixStack stack, int x, int y, int width, int height, TextureAtlasSprite sprite)
    {
        blit(stack, x, y, getBlitOffset(), width, height, sprite);
    }

    public void renderSprite(MatrixStack stack, int x, int y, int width, int height, ZYSpriteType type)
    {
        renderSprite(stack, x, y, getBlitOffset(), width, height, type);
    }

    public void renderTileableSprite(MatrixStack stack, int x, int y, float width, float height, TextureAtlasSprite sprite, float resolution)
    {
        renderTileableSprite(stack, x, y, getBlitOffset(), width, height, sprite, resolution);
    }

    public TextureAtlasSprite getSprite(ResourceLocation atlasID, ResourceLocation spriteID)
    {
        return minecraft.getAtlasSpriteGetter(atlasID).apply(spriteID);
    }

    public void bindTexture(ResourceLocation textureID)
    {
        minecraft.getTextureManager().bindTexture(textureID);
    }

    public static void resetColor()
    {
        RenderSystem.color4f(1, 1, 1, 1);
    }

    public static void setColor(int rgb, int a)
    {
        setColor(Color.argb(rgb, a));
    }

    public static void setColor(int argb)
    {
        RenderSystem.color4f(((argb >> 16) & 0xFF) / 255.0F, ((argb >> 8) & 0xFF) / 255.0F, (argb & 0xFF) / 255.0F, ((argb >> 24) & 0xFF) / 255.0F);
    }

    public static void renderSprite(MatrixStack stack, int x, int y, int blitOffset, int width, int height, ZYSpriteType type)
    {
        blit(stack, x, y, blitOffset, width, height, ZYClientSetup.sprites().get(type));
    }

    public static void renderTileableSprite(MatrixStack stack, int x, int y, int blitOffset, float width, float height, TextureAtlasSprite sprite, float resolution)
    {
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        float u1 = sprite.getMinU();
        float v2 = sprite.getMaxV();

        stack.push();
        stack.translate(x, y, blitOffset);
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

    public class Menu
    {
        private final int x, y, selectedColor;
        private final IntContainerVariable selectedItem;
        private int itemCount;

        public Menu(int x, int y, int selectedColor, IntContainerVariable selectedItem)
        {
            this.x = x;
            this.y = y;
            this.selectedColor = selectedColor;
            this.selectedItem = selectedItem;
        }

        public Menu addItem(ITextComponent title, ResourceLocation iconName)
        {
            addButton(new Item(x + 6, y + 5 + 24 * itemCount, title, itemCount, iconName));
            this.itemCount++;
            return this;
        }

        public void render(MatrixStack stack)
        {
            resetColor();
            renderSprite(stack, x, y, 34, 5, ZYSpriteType.MENU_TOP);

            for (int item = 0; item < itemCount; item++)
                renderSprite(stack, x, y + 5 + item * 24, 34, 24, ZYSpriteType.MENU_MIDDLE);

            renderSprite(stack, x, y + 5 + 24 * itemCount, 34, 3, ZYSpriteType.MENU_BOTTOM);
        }

        private class Item extends AbstractButton
        {
            private final int index;
            private final ResourceLocation iconName;

            private Item(int x, int y, ITextComponent title, int index, ResourceLocation iconName)
            {
                super(x, y, 22, 22, title);

                this.index = index;
                this.iconName = iconName;
            }

            @Override
            public void onPress()
            {
                if (container.enchantItem(minecraft.player, index))
                    minecraft.playerController.sendEnchantPacket(container.windowId, index);
            }

            @Override
            public void renderToolTip(MatrixStack stack, int mouseX, int mouseY)
            {
                renderTooltip(stack, getMessage(), mouseX, mouseY);
            }

            @Override
            public void renderWidget(MatrixStack stack, int mouseX, int mouseY, float partialTicks)
            {
                ResourceLocation blockAtlas = AtlasTexture.LOCATION_BLOCKS_TEXTURE;

                if (index == selectedItem.value())
                {
                    setColor(selectedColor, 255);
                    bindTexture(blockAtlas);
                    renderSprite(stack, x, y, 22, 22, getSprite(blockAtlas, CloudSprite.NAME));
                }

                resetColor();
                bindTexture(ZYSpriteTextureManager.NAME);
                renderSprite(stack, x, y, 22, 22, ZYSpriteType.MENU_ITEM);

                bindTexture(blockAtlas);
                renderSprite(stack, x + 3, y + 3, 16, 16, getSprite(blockAtlas, iconName));

                if (isHovered())
                    renderToolTip(stack, mouseX, mouseY);
            }
        }
    }
}

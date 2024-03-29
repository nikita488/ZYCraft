package nikita488.zycraft.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
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
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import nikita488.zycraft.block.state.properties.ItemIOMode;
import nikita488.zycraft.client.ZYClientSetup;
import nikita488.zycraft.client.gui.GuiComponent;
import nikita488.zycraft.client.texture.CloudSprite;
import nikita488.zycraft.client.texture.GuiComponentManager;
import nikita488.zycraft.init.ZYLang;
import nikita488.zycraft.menu.data.FluidMenuData;
import nikita488.zycraft.menu.data.IOMenuData;
import nikita488.zycraft.menu.data.IntMenuData;
import nikita488.zycraft.menu.slot.IOSlotOverlay;
import nikita488.zycraft.util.Color;
import org.lwjgl.opengl.GL11;

public abstract class ZYScreen<T extends Container> extends ContainerScreen<T>
{
    protected int titleColor = 0x999999;
    protected int playerInventoryTitleColor = 0x999999;

    public ZYScreen(T menu, PlayerInventory inventory, ITextComponent title)
    {
        super(menu, inventory, title);
        this.imageWidth = 192;
        this.imageHeight = 182;
        this.titleLabelY = 14;
        this.inventoryLabelX = 16;
    }

    @Override
    protected void init()
    {
        super.init();
        this.titleLabelX = (imageWidth - font.width(title)) / 2;
        this.inventoryLabelY = imageHeight - 102;
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks)
    {
        renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        renderTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(MatrixStack stack, int mouseX, int mouseY)
    {
        font.draw(stack, title, titleLabelX, titleLabelY, titleColor);
        font.draw(stack, inventory.getDisplayName(), inventoryLabelX, inventoryLabelY, playerInventoryTitleColor);

        for (Widget widget : buttons)
        {
            if (!widget.isHovered())
                continue;

            widget.renderToolTip(stack, mouseX - leftPos, mouseY - topPos);
            break;
        }
    }

    public void renderGUI(MatrixStack stack, ResourceLocation texture, int argb)
    {
        renderBackground(stack, leftPos + 8, topPos + imageHeight - 8, imageWidth - 16, imageHeight - 16, argb);
        bindTexture(texture);
        blit(stack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    public void renderBackground(MatrixStack stack, int x, int y, int width, int height, int argb)
    {
        setColor(argb);
        bindTexture(AtlasTexture.LOCATION_BLOCKS);
        renderTileableSprite(stack, x, y, CloudSprite.MATERIAL.sprite(), 32, width, height);

        setColor(0x434343, 255);
        bindTexture(GuiComponentManager.ATLAS_ID);
        renderTileableSprite(stack, x, y, ZYClientSetup.guiComponentManager().get(GuiComponent.BACKGROUND), 32, width, height);
        resetColor();
    }

    public void renderSprite(MatrixStack stack, int x, int y, int width, int height, TextureAtlasSprite sprite)
    {
        blit(stack, x, y, getBlitOffset(), width, height, sprite);
    }

    public void renderGuiComponent(MatrixStack stack, int x, int y, GuiComponent component)
    {
        renderGuiComponent(stack, x, y, getBlitOffset(), component.width(), component.height(), component);
    }

    public void renderGuiComponent(MatrixStack stack, int x, int y, int width, int height, GuiComponent component)
    {
        renderGuiComponent(stack, x, y, getBlitOffset(), width, height, component);
    }

    public void renderGuiComponentWithColor(MatrixStack stack, int x, int y, float resolution, GuiComponent component, int argb)
    {
        renderGuiComponentWithColor(stack, x, y, getBlitOffset(), resolution, component, argb);
    }

    public void renderFluid(MatrixStack stack, int x, int y, FluidStack fluid, int resolution, int width, int height, float density)
    {
        renderFluid(stack, x, y, getBlitOffset(), fluid, resolution, width, height, density);
    }

    public void renderTileableSprite(MatrixStack stack, int x, int y, TextureAtlasSprite sprite, int resolution, int width, int height)
    {
        renderTileableSprite(stack, x, y, getBlitOffset(), sprite, resolution, width, height);
    }

    public TextureAtlasSprite getSprite(ResourceLocation atlasID, ResourceLocation spriteID)
    {
        return minecraft.getTextureAtlas(atlasID).apply(spriteID);
    }

    public void bindTexture(ResourceLocation textureID)
    {
        minecraft.getTextureManager().bind(textureID);
    }

    public static void resetColor()
    {
        RenderSystem.color4f(1F, 1F, 1F, 1F);
    }

    public static void setColor(int rgb, int a)
    {
        setColor(Color.argb(rgb, a));
    }

    public static void setColor(int argb)
    {
        RenderSystem.color4f(((argb >> 16) & 255) / 255F, ((argb >> 8) & 255) / 255F, (argb & 255) / 255F, ((argb >> 24) & 255) / 255F);
    }

    public static void renderGuiComponent(MatrixStack stack, int x, int y, int blitOffset, GuiComponent component)
    {
        renderGuiComponent(stack, x, y, blitOffset, component.width(), component.height(), component);
    }

    public static void renderGuiComponent(MatrixStack stack, int x, int y, int blitOffset, int width, int height, GuiComponent component)
    {
        blit(stack, x, y, blitOffset, width, height, ZYClientSetup.guiComponentManager().get(component));
    }

    public void renderGuiComponentWithColor(MatrixStack stack, int x, int y, int blitOffset, float resolution, GuiComponent component, int argb)
    {
        setColor(argb);
        renderGuiComponent(stack, x, y, blitOffset, (int)(component.width() * resolution), (int)(component.height() * resolution), component);
        resetColor();
    }

    public static void renderFluid(MatrixStack stack, int x, int y, int blitOffset, FluidStack fluid, int resolution, int width, int height, float density)
    {
        y += height;

        if (fluid.isEmpty())
            return;

        FluidAttributes attributes = fluid.getFluid().getAttributes();
        int color = attributes.getColor(fluid);

        if (attributes.isGaseous(fluid))
            color = Color.argb(color, (int)(Math.pow(density, 0.4F) * 255));
        else
            height *= density;

        setColor(color);
        renderTileableSprite(stack, x, y, blitOffset, ModelLoaderRegistry.blockMaterial(attributes.getStillTexture(fluid)).sprite(), resolution, width, height);
        resetColor();
    }

    public static void renderTileableSprite(MatrixStack stack, int x, int y, int blitOffset, TextureAtlasSprite sprite, int resolution, int width, int height)
    {
        BufferBuilder buffer = Tessellator.getInstance().getBuilder();
        float u1 = sprite.getU0();
        float v2 = sprite.getV1();

        stack.pushPose();
        stack.translate(x, y, blitOffset);
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        Matrix4f matrix = stack.last().pose();

        for (int quadX = 0; quadX < width; quadX += resolution)
        {
            for (int quadY = 0; quadY < height; quadY += resolution)
            {
                int quadWidth = Math.min(width - quadX, resolution);
                int quadHeight = Math.min(height - quadY, resolution);
                float u2 = u1 + (sprite.getU1() - u1) * quadWidth / resolution;
                float v1 = v2 - (v2 - sprite.getV0()) * quadHeight / resolution;

                buffer.vertex(matrix, quadX, -quadY - quadHeight, 0)
                        .uv(u1, v1)
                        .endVertex();
                buffer.vertex(matrix, quadX, -quadY, 0)
                        .uv(u1, v2)
                        .endVertex();
                buffer.vertex(matrix, quadX + quadWidth, -quadY, 0)
                        .uv(u2, v2)
                        .endVertex();
                buffer.vertex(matrix, quadX + quadWidth, -quadY - quadHeight, 0)
                        .uv(u2, v1)
                        .endVertex();
            }
        }

        Tessellator.getInstance().end();
        stack.popPose();
    }

    public static void renderIOSlotOverlays(MatrixStack stack, int x, int y, Iterable<IOSlotOverlay> overlays, IOMenuData ioData)
    {
        renderIOSlotOverlays(stack, x, y, overlays, ioData, 128);
    }

    public static void renderIOSlotOverlays(MatrixStack stack, int x, int y, Iterable<IOSlotOverlay> overlays, IOMenuData ioData, int alpha)
    {
        int slotSize = 16;

        for (IOSlotOverlay overlay : overlays)
        {
            for (ItemIOMode mode : ioData.get())
            {
                if (!overlay.canBeRendered(mode))
                    continue;

                int overlayX = x + overlay.x();
                int overlayY = y + overlay.y();
                int offsetX = (overlay.width() - slotSize) / 2;
                int offsetY = (overlay.height() - slotSize) / 2;

                fill(stack, overlayX - offsetX, overlayY - offsetY, overlayX + slotSize + offsetX, overlayY + slotSize + offsetY, Color.argb(mode.rgb(), alpha));
            }
        }
    }

    public class Menu implements IRenderable
    {
        private final int x, y, selectedColor;
        private final IntMenuData selectedItem;
        private int itemCount;

        public Menu(int x, int y, int selectedColor, IntMenuData selectedItem)
        {
            this.x = x;
            this.y = y;
            this.selectedColor = selectedColor;
            this.selectedItem = selectedItem;
        }

        public Menu addItem(ITextComponent tooltip, ResourceLocation iconName)
        {
            addButton(new Item(x + 6, y + 5 + GuiComponent.MENU_MIDDLE.height() * itemCount, tooltip, itemCount, iconName));
            this.itemCount++;
            return this;
        }

        @Override
        public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks)
        {
            resetColor();
            renderGuiComponent(stack, x, y, GuiComponent.MENU_TOP);

            for (int item = 0; item < itemCount; item++)
                renderGuiComponent(stack, x, y + 5 + item * GuiComponent.MENU_MIDDLE.height(), GuiComponent.MENU_MIDDLE);

            renderGuiComponent(stack, x, y + 5 + GuiComponent.MENU_MIDDLE.height() * itemCount, GuiComponent.MENU_BOTTOM);
        }

        private class Item extends AbstractButton
        {
            private final int index;
            private final ResourceLocation iconName;

            private Item(int x, int y, ITextComponent tooltip, int index, ResourceLocation iconName)
            {
                super(x, y, GuiComponent.MENU_ITEM.width(), GuiComponent.MENU_ITEM.height(), tooltip);

                this.index = index;
                this.iconName = iconName;
            }

            @Override
            public void onPress()
            {
                if (menu.clickMenuButton(minecraft.player, index))
                    minecraft.gameMode.handleInventoryButtonClick(menu.containerId, index);
            }

            @Override
            public void renderToolTip(MatrixStack stack, int mouseX, int mouseY)
            {
                renderTooltip(stack, getMessage(), mouseX, mouseY);
            }

            @Override
            public void renderButton(MatrixStack stack, int mouseX, int mouseY, float partialTicks)
            {
                ResourceLocation blockAtlas = AtlasTexture.LOCATION_BLOCKS;

                if (index == selectedItem.getAsInt())
                {
                    setColor(selectedColor, 255);
                    bindTexture(blockAtlas);
                    renderSprite(stack, x, y, width, height, CloudSprite.MATERIAL.sprite());
                }

                resetColor();
                bindTexture(GuiComponentManager.ATLAS_ID);
                renderGuiComponent(stack, x, y, width, height, GuiComponent.MENU_ITEM);

                bindTexture(blockAtlas);
                renderSprite(stack, x + 3, y + 3, 16, 16, getSprite(blockAtlas, iconName));
            }
        }
    }

    public class FluidGaugeWidget extends Widget
    {
        private final FluidMenuData fluidData;
        private final int capacity;

        public FluidGaugeWidget(int x, int y, FluidMenuData fluidData, int capacity)
        {
            super(x, y, GuiComponent.BIG_TANK.width(), GuiComponent.BIG_TANK.height(), StringTextComponent.EMPTY);
            this.fluidData = fluidData;
            this.capacity = capacity;
        }

        @Override
        public boolean isHovered()
        {
            return !fluidData.get().isEmpty() && capacity > 0 && super.isHovered();
        }

        @Override
        protected IFormattableTextComponent createNarrationMessage()
        {
            FluidStack fluid = fluidData.get();
            return ZYLang.copy(ZYLang.NARRATE_GAUGE, fluid.getDisplayName(), fluid.getAmount(), capacity);
        }

        @Override
        public void playDownSound(SoundHandler handler) {}

        @Override
        public void renderToolTip(MatrixStack pose, int mouseX, int mouseY)
        {
            FluidStack stack = fluidData.get();
            ObjectArrayList<ITextComponent> tooltip = new ObjectArrayList<>();

            tooltip.add(stack.getDisplayName());
            tooltip.add(ZYLang.copy(ZYLang.FLUID_TANK_FILLED, stack.getAmount(), capacity));
            renderComponentTooltip(pose, tooltip, mouseX, mouseY);
        }

        @Override
        public void renderButton(MatrixStack pose, int mouseX, int mouseY, float partialTicks)
        {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            FluidStack stack = fluidData.get();

            bindTexture(GuiComponentManager.ATLAS_ID);
            renderGuiComponent(pose, x, y, width, height, GuiComponent.BIG_TANK_BACKGROUND);
            bindTexture(AtlasTexture.LOCATION_BLOCKS);

            if (!stack.isEmpty() && capacity > 0)
                renderFluid(pose, x + 3, y + 3, stack, 16, width - 6, height - 6, (float)stack.getAmount() / capacity);

            bindTexture(GuiComponentManager.ATLAS_ID);
            renderGuiComponent(pose, x, y, width, height, GuiComponent.BIG_TANK_GAUGE);
            renderGuiComponent(pose, x, y, width, height, GuiComponent.BIG_TANK);

            RenderSystem.disableBlend();
        }
    }
}

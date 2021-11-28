package nikita488.zycraft.client;

import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import nikita488.zycraft.ZYCraft;
import org.lwjgl.opengl.GL11;

public class ZYRenderTypes extends RenderState
{
    public static final RenderType MULTI_HIGHLIGHT = RenderType.create(ZYCraft.string("multi_highlight"),
            DefaultVertexFormats.POSITION_COLOR_TEX,
            GL11.GL_QUADS, 256,
            false, true,
            RenderType.State.builder()
                    .setTextureState(new TextureState(ZYCraft.id("textures/misc/multi_highlight.png"), false, false))
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setWriteMaskState(COLOR_WRITE)
                    .setOutputState(ITEM_ENTITY_TARGET)
                    .createCompositeState(false));

    private ZYRenderTypes(String name, Runnable setupTask, Runnable clearTask)
    {
        super(name, setupTask, clearTask);
    }
}

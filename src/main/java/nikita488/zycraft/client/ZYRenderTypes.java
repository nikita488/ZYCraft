package nikita488.zycraft.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import nikita488.zycraft.ZYCraft;
import org.lwjgl.opengl.GL11;

public class ZYRenderTypes extends RenderStateShard
{
    public static final RenderType MULTI_HIGHLIGHT = RenderType.create(ZYCraft.string("multi_highlight"),
            DefaultVertexFormat.POSITION_COLOR_TEX,
            GL11.GL_QUADS, 256,
            false, true,
            RenderType.CompositeState.builder()
                    .setTextureState(new TextureStateShard(ZYCraft.id("textures/misc/multi_highlight.png"), false, false))
                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                    .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                    .createCompositeState(false));

    private ZYRenderTypes(String name, Runnable setupTask, Runnable clearTask)
    {
        super(name, setupTask, clearTask);
    }
}

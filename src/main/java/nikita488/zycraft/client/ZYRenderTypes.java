package nikita488.zycraft.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import nikita488.zycraft.ZYCraft;

public class ZYRenderTypes extends RenderStateShard
{
    public static final RenderType MULTI_HIGHLIGHT = RenderType.create(ZYCraft.string("multi_highlight"),
            DefaultVertexFormat.POSITION_COLOR_TEX,
            VertexFormat.Mode.QUADS, 256,
            false, true,
            RenderType.CompositeState.builder()
                    .setShaderState(POSITION_COLOR_TEX_SHADER)
                    .setTextureState(new TextureStateShard(ZYCraft.id("textures/misc/multi_highlight.png"), false, false))
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setWriteMaskState(COLOR_WRITE)
                    .setOutputState(ITEM_ENTITY_TARGET)
                    .createCompositeState(false));

    private ZYRenderTypes(String name, Runnable setupTask, Runnable clearTask)
    {
        super(name, setupTask, clearTask);
    }
}

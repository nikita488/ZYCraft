package nikita488.zycraft.client.texture;

import com.google.common.collect.Lists;
import net.minecraft.client.renderer.model.BakedQuad;
import team.chisel.ctm.api.texture.ITextureContext;
import team.chisel.ctm.api.util.TextureInfo;
import team.chisel.ctm.client.texture.render.AbstractTexture;
import team.chisel.ctm.client.util.Quad;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CloudTexture extends AbstractTexture<CloudTextureType>
{
    public CloudTexture(CloudTextureType type, TextureInfo info)
    {
        super(type, info);
    }

    @Override
    public List<BakedQuad> transformQuad(BakedQuad quad, @Nullable ITextureContext context, int quadGoal)
    {
        if (quadGoal == 4)
            return Arrays.stream(makeQuad(quad, context).transformUVs(sprites[0]).subdivide(4)).filter(Objects::nonNull).map(Quad::rebake).collect(Collectors.toList());
        return Lists.newArrayList(makeQuad(quad, context).transformUVs(sprites[0]).rebake());
    }
}

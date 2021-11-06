package nikita488.zycraft.client.renderer.multiblock;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import nikita488.zycraft.util.Color;
import nikita488.zycraft.util.Cuboid6i;

import static net.minecraft.client.renderer.LightTexture.block;
import static net.minecraft.client.renderer.LightTexture.pack;
import static net.minecraft.client.renderer.LightTexture.sky;

public class MultiFluidRenderer
{
    private static final Vector3f ORIGIN = new Vector3f();

    public static void render(MatrixStack pose, IRenderTypeBuffer source, FluidStack stack, Cuboid6i bounds, float resolution, float density, IBlockDisplayReader getter, BlockPos lightPos)
    {
        render(pose, source, stack, bounds, resolution, density, WorldRenderer.getLightColor(getter, lightPos));
    }

    public static void render(MatrixStack pose, IRenderTypeBuffer source, FluidStack stack, Cuboid6i bounds, float resolution, float density, int lightMap)
    {
        if (stack.isEmpty())
            return;

        Fluid fluid = stack.getFluid();
        FluidAttributes attributes = fluid.getAttributes();
        int color = attributes.getColor(stack);
        TextureAtlasSprite sprite = ModelLoaderRegistry.blockMaterial(attributes.getStillTexture(stack)).sprite();
        float fluidHeight = bounds.height();

        if (attributes.isGaseous(stack))
            color = Color.argb(color, (int)(Math.pow(density, 0.4F) * 255));
        else
            fluidHeight *= density;

        lightMap = pack(Math.max(block(lightMap), attributes.getLuminosity(stack)), sky(lightMap));

        for (RenderType type : RenderType.chunkBufferLayers())
        {
            if (!RenderTypeLookup.canRenderInLayer(fluid.defaultFluidState(), type))
                continue;

            IVertexBuilder builder = source.getBuffer(type);

            ORIGIN.set(bounds.minX(), bounds.minY(), bounds.minZ());
            fillFluidQuads(pose, builder, Direction.DOWN, resolution, bounds.width(), bounds.depth(), color, sprite, lightMap);
            ORIGIN.set(bounds.minX(), bounds.minY() + fluidHeight, bounds.maxZ() + 1);
            fillFluidQuads(pose, builder, Direction.UP, resolution, bounds.width(), bounds.depth(), color, sprite, lightMap);
            ORIGIN.set(bounds.minX(), bounds.minY(), bounds.maxZ() + 1);
            fillFluidQuads(pose, builder, Direction.NORTH, resolution, bounds.width(), fluidHeight, color, sprite, lightMap);
            ORIGIN.set(bounds.maxX() + 1, bounds.minY(), bounds.minZ());
            fillFluidQuads(pose, builder, Direction.SOUTH, resolution, bounds.width(), fluidHeight, color, sprite, lightMap);
            ORIGIN.set(bounds.maxX() + 1, bounds.minY(), bounds.maxZ() + 1);
            fillFluidQuads(pose, builder, Direction.WEST, resolution, bounds.depth(), fluidHeight, color, sprite, lightMap);
            ORIGIN.set(bounds.minX(), bounds.minY(), bounds.minZ());
            fillFluidQuads(pose, builder, Direction.EAST, resolution, bounds.depth(), fluidHeight, color, sprite, lightMap);
        }
    }

    private static Vector3f cross(Vector3f vec1, Vector3f vec2)
    {
        vec1.cross(vec2);
        return vec1;
    }

    private static Vector3f scale(Vector3f vec, float amount)
    {
        vec = vec.copy();
        vec.mul(amount);
        return vec;
    }

    private static void fillFluidQuads(MatrixStack pose, IVertexBuilder consumer, Direction side, float resolution, float width, float height, int color, TextureAtlasSprite sprite, int lightMap)
    {
        pose.pushPose();
        pose.translate(ORIGIN.x(), ORIGIN.y(), ORIGIN.z());

        Matrix4f matrix = pose.last().pose();
        Vector3f widthAxis = side.getAxis().isHorizontal() ? cross(side.step(), Vector3f.YP) : Vector3f.XP;
        Vector3f heightAxis = side.getAxis().isVertical() ? cross(side.step(), Vector3f.XP) : Vector3f.YP;
        int r = (color >> 16) & 255;
        int g = (color >> 8) & 255;
        int b = color & 255;
        int a = (color >> 24) & 255;
        float u1 = sprite.getU0();
        float v2 = sprite.getV1();
        float sideX = side.getStepX();
        float sideY = side.getStepY();
        float sideZ = side.getStepZ();

        for (float quadX = 0; quadX < width; quadX += resolution)
        {
            for (float quadY = 0; quadY < height; quadY += resolution)
            {
                float quadWidth = Math.min(width - quadX, resolution);
                float quadHeight = Math.min(height - quadY, resolution);
                float u2 = u1 + (sprite.getU1() - u1) * quadWidth / resolution;
                float v1 = v2 - (v2 - sprite.getV0()) * quadHeight / resolution;
                Vector3f x1 = scale(widthAxis, quadX);
                Vector3f x2 = scale(widthAxis, quadX + quadWidth);
                Vector3f y1 = scale(heightAxis, quadY);
                Vector3f y2 = scale(heightAxis, quadY + quadHeight);

                consumer.vertex(matrix, x1.x() + y2.x(), x1.y() + y2.y(), x1.z() + y2.z())
                        .color(r, g, b, a)
                        .uv(u1, v1)
                        .uv2(lightMap)
                        .normal(sideX, sideY, sideZ)
                        .endVertex();
                consumer.vertex(matrix, x1.x() + y1.x(), x1.y() + y1.y(), x1.z() + y1.z())
                        .color(r, g, b, a)
                        .uv(u1, v2)
                        .uv2(lightMap)
                        .normal(sideX, sideY, sideZ)
                        .endVertex();
                consumer.vertex(matrix, x2.x() + y1.x(), x2.y() + y1.y(), x2.z() + y1.z())
                        .color(r, g, b, a)
                        .uv(u2, v2)
                        .uv2(lightMap)
                        .normal(sideX, sideY, sideZ)
                        .endVertex();
                consumer.vertex(matrix, x2.x() + y2.x(), x2.y() + y2.y(), x2.z() + y2.z())
                        .color(r, g, b, a)
                        .uv(u2, v1)
                        .uv2(lightMap)
                        .normal(sideX, sideY, sideZ)
                        .endVertex();
            }
        }

        pose.popPose();
    }
}

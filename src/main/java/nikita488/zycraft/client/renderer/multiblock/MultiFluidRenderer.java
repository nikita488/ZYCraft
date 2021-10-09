package nikita488.zycraft.client.renderer.multiblock;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.*;
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

import static net.minecraft.client.renderer.LightTexture.*;

public class MultiFluidRenderer
{
    public static void render(MatrixStack pose, IRenderTypeBuffer source, FluidStack stack, Cuboid6i bounds, float resolution, float density, IBlockDisplayReader world, BlockPos lightPos)
    {
        render(pose, source, stack, bounds, resolution, density, WorldRenderer.getCombinedLight(world, lightPos));
    }

    public static void render(MatrixStack pose, IRenderTypeBuffer source, FluidStack stack, Cuboid6i bounds, float resolution, float density, int lightMap)
    {
        if (stack.isEmpty())
            return;

        Vector3f origin = new Vector3f();
        Fluid fluid = stack.getFluid();
        FluidAttributes attributes = fluid.getAttributes();
        int color = attributes.getColor(stack);
        TextureAtlasSprite sprite = ModelLoaderRegistry.blockMaterial(attributes.getStillTexture(stack)).getSprite();
        float fluidHeight = bounds.height();

        if (attributes.isGaseous(stack))
            color = Color.argb(color, (int)(Math.pow(density, 0.4F) * 255));
        else
            fluidHeight *= density;

        lightMap = packLight(Math.max(getLightBlock(lightMap), attributes.getLuminosity(stack)), getLightSky(lightMap));

        for (RenderType type : RenderType.getBlockRenderTypes())
        {
            if (!RenderTypeLookup.canRenderInLayer(fluid.getDefaultState(), type))
                continue;

            IVertexBuilder builder = source.getBuffer(type);

            origin.set(bounds.minX(), bounds.minY(), bounds.minZ());
            fillFluidQuads(pose, builder, origin, Direction.DOWN, resolution, bounds.width(), bounds.depth(), color, sprite, lightMap);
            origin.set(bounds.minX(), bounds.minY() + fluidHeight, bounds.maxZ() + 1);
            fillFluidQuads(pose, builder, origin, Direction.UP, resolution, bounds.width(), bounds.depth(), color, sprite, lightMap);
            origin.set(bounds.minX(), bounds.minY(), bounds.maxZ() + 1);
            fillFluidQuads(pose, builder, origin, Direction.NORTH, resolution, bounds.width(), fluidHeight, color, sprite, lightMap);
            origin.set(bounds.maxX() + 1, bounds.minY(), bounds.minZ());
            fillFluidQuads(pose, builder, origin, Direction.SOUTH, resolution, bounds.width(), fluidHeight, color, sprite, lightMap);
            origin.set(bounds.maxX() + 1, bounds.minY(), bounds.maxZ() + 1);
            fillFluidQuads(pose, builder, origin, Direction.WEST, resolution, bounds.depth(), fluidHeight, color, sprite, lightMap);
            origin.set(bounds.minX(), bounds.minY(), bounds.minZ());
            fillFluidQuads(pose, builder, origin, Direction.EAST, resolution, bounds.depth(), fluidHeight, color, sprite, lightMap);
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

    private static void fillFluidQuads(MatrixStack pose, IVertexBuilder consumer, Vector3f origin, Direction side, float resolution, float width, float height, int color, TextureAtlasSprite sprite, int lightMap)
    {
        pose.push();
        pose.translate(origin.getX(), origin.getY(), origin.getZ());

        Matrix4f matrix = pose.getLast().getMatrix();
        Vector3f widthAxis = side.getAxis().isHorizontal() ? cross(side.toVector3f(), Vector3f.YP) : Vector3f.XP;
        Vector3f heightAxis = side.getAxis().isVertical() ? cross(side.toVector3f(), Vector3f.XP) : Vector3f.YP;
        int r = (color >> 16) & 255;
        int g = (color >> 8) & 255;
        int b = color & 255;
        int a = (color >> 24) & 255;
        float u1 = sprite.getMinU();
        float v2 = sprite.getMaxV();
        float sideX = side.getXOffset();
        float sideY = side.getYOffset();
        float sideZ = side.getZOffset();

        for (float quadX = 0; quadX < width; quadX += resolution)
        {
            for (float quadY = 0; quadY < height; quadY += resolution)
            {
                float quadWidth = Math.min(width - quadX, resolution);
                float quadHeight = Math.min(height - quadY, resolution);
                float u2 = u1 + (sprite.getMaxU() - u1) * quadWidth / resolution;
                float v1 = v2 - (v2 - sprite.getMinV()) * quadHeight / resolution;
                Vector3f x1 = scale(widthAxis, quadX);
                Vector3f x2 = scale(widthAxis, quadX + quadWidth);
                Vector3f y1 = scale(heightAxis, quadY);
                Vector3f y2 = scale(heightAxis, quadY + quadHeight);

                consumer.pos(matrix, x1.getX() + y2.getX(), x1.getY() + y2.getY(), x1.getZ() + y2.getZ())
                        .color(r, g, b, a)
                        .tex(u1, v1)
                        .lightmap(lightMap)
                        .normal(sideX, sideY, sideZ)
                        .endVertex();
                consumer.pos(matrix, x1.getX() + y1.getX(), x1.getY() + y1.getY(), x1.getZ() + y1.getZ())
                        .color(r, g, b, a)
                        .tex(u1, v2)
                        .lightmap(lightMap)
                        .normal(sideX, sideY, sideZ)
                        .endVertex();
                consumer.pos(matrix, x2.getX() + y1.getX(), x2.getY() + y1.getY(), x2.getZ() + y1.getZ())
                        .color(r, g, b, a)
                        .tex(u2, v2)
                        .lightmap(lightMap)
                        .normal(sideX, sideY, sideZ)
                        .endVertex();
                consumer.pos(matrix, x2.getX() + y2.getX(), x2.getY() + y2.getY(), x2.getZ() + y2.getZ())
                        .color(r, g, b, a)
                        .tex(u2, v1)
                        .lightmap(lightMap)
                        .normal(sideX, sideY, sideZ)
                        .endVertex();
            }
        }

        pose.pop();
    }
}

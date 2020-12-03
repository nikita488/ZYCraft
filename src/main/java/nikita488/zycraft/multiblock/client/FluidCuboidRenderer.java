package nikita488.zycraft.multiblock.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import nikita488.zycraft.multiblock.Cuboid6i;

public class FluidCuboidRenderer
{
    public static void render(FluidStack fluidStack, int capacity, Cuboid6i cuboid, float resolution, MatrixStack stack, IRenderTypeBuffer buffer, BlockPos lightPos)
    {
        render(fluidStack, capacity, cuboid, resolution, stack, buffer, WorldRenderer.getCombinedLight(Minecraft.getInstance().world, lightPos));
    }

    public static void render(FluidStack fluidStack, int capacity, Cuboid6i cuboid, float resolution, MatrixStack stack, IRenderTypeBuffer buffer, int lightMap)
    {
        Vector3f start = new Vector3f();
        float height = cuboid.lengthY() * (float)fluidStack.getAmount() / capacity;
        FluidAttributes attributes = fluidStack.getFluid().getAttributes();
        int color = attributes.getColor(fluidStack);
        TextureAtlasSprite sprite = ModelLoaderRegistry.blockMaterial(attributes.getStillTexture(fluidStack)).getSprite();
        lightMap = LightTexture.packLight(Math.max(LightTexture.getLightBlock(lightMap), attributes.getLuminosity(fluidStack)), LightTexture.getLightSky(lightMap));

        for (RenderType type : RenderType.getBlockRenderTypes())
        {
            if (!RenderTypeLookup.canRenderInLayer(fluidStack.getFluid().getDefaultState(), type))
                continue;

            IVertexBuilder builder = buffer.getBuffer(type);

            start.set(cuboid.minX(), cuboid.minY(), cuboid.minZ());
            fillFluidQuads(stack, builder, start, cuboid.lengthX(), cuboid.lengthZ(), color, sprite, lightMap, Direction.DOWN, resolution);
            start.set(cuboid.minX(), cuboid.minY() + height, cuboid.maxZ() + 1);
            fillFluidQuads(stack, builder, start, cuboid.lengthX(), cuboid.lengthZ(), color, sprite, lightMap, Direction.UP, resolution);
            start.set(cuboid.minX(), cuboid.minY(), cuboid.maxZ() + 1);
            fillFluidQuads(stack, builder, start, cuboid.lengthX(), height, color, sprite, lightMap, Direction.NORTH, resolution);
            start.set(cuboid.maxX() + 1, cuboid.minY(), cuboid.minZ());
            fillFluidQuads(stack, builder, start, cuboid.lengthX(), height, color, sprite, lightMap, Direction.SOUTH, resolution);
            start.set(cuboid.maxX() + 1, cuboid.minY(), cuboid.maxZ() + 1);
            fillFluidQuads(stack, builder, start, cuboid.lengthZ(), height, color, sprite, lightMap, Direction.WEST, resolution);
            start.set(cuboid.minX(), cuboid.minY(), cuboid.minZ());
            fillFluidQuads(stack, builder, start, cuboid.lengthZ(), height, color, sprite, lightMap, Direction.EAST, resolution);
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

    private static void fillFluidQuads(MatrixStack stack, IVertexBuilder builder, Vector3f start, float width, float height, int color, TextureAtlasSprite sprite, int lightMap, Direction side, float resolution)
    {
        Vector3f widthAxis = side.getAxis().isHorizontal() ? cross(side.toVector3f(), Vector3f.YP) : Vector3f.XP;
        Vector3f heightAxis = side.getAxis().isVertical() ? cross(side.toVector3f(), Vector3f.XP) : Vector3f.YP;
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        int a = (color >> 24) & 0xFF;
        float u1 = sprite.getMinU();
        float v2 = sprite.getMaxV();
        float sideX = side.getXOffset();
        float sideY = side.getYOffset();
        float sideZ = side.getZOffset();

        stack.push();
        stack.translate(start.getX(), start.getY(), start.getZ());

        Matrix4f matrix = stack.getLast().getMatrix();

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

                builder.pos(matrix, x1.getX() + y2.getX(), x1.getY() + y2.getY(), x1.getZ() + y2.getZ())
                        .color(r, g, b, a)
                        .tex(u1, v1)
                        .lightmap(lightMap)
                        .normal(sideX, sideY, sideZ)
                        .endVertex();
                builder.pos(matrix, x1.getX() + y1.getX(), x1.getY() + y1.getY(), x1.getZ() + y1.getZ())
                        .color(r, g, b, a)
                        .tex(u1, v2)
                        .lightmap(lightMap)
                        .normal(sideX, sideY, sideZ)
                        .endVertex();
                builder.pos(matrix, x2.getX() + y1.getX(), x2.getY() + y1.getY(), x2.getZ() + y1.getZ())
                        .color(r, g, b, a)
                        .tex(u2, v2)
                        .lightmap(lightMap)
                        .normal(sideX, sideY, sideZ)
                        .endVertex();
                builder.pos(matrix, x2.getX() + y2.getX(), x2.getY() + y2.getY(), x2.getZ() + y2.getZ())
                        .color(r, g, b, a)
                        .tex(u2, v1)
                        .lightmap(lightMap)
                        .normal(sideX, sideY, sideZ)
                        .endVertex();
            }
        }

        stack.pop();
    }
}

package nikita488.zycraft.client.renderer.multiblock;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.IFluidTypeRenderProperties;
import net.minecraftforge.client.RenderProperties;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import nikita488.zycraft.util.Color;
import nikita488.zycraft.util.Cuboid6i;

import static net.minecraft.client.renderer.LightTexture.block;
import static net.minecraft.client.renderer.LightTexture.pack;
import static net.minecraft.client.renderer.LightTexture.sky;

public class MultiFluidRenderer
{
    public static void render(PoseStack pose, MultiBufferSource source, FluidStack stack, Cuboid6i bounds, float resolution, float density, BlockAndTintGetter getter, BlockPos lightPos)
    {
        render(pose, source, stack, bounds, resolution, density, LevelRenderer.getLightColor(getter, lightPos));
    }

    public static void render(PoseStack pose, MultiBufferSource source, FluidStack stack, Cuboid6i bounds, float resolution, float density, int lightMap)
    {
        if (stack.isEmpty())
            return;

        Vector3f origin = new Vector3f();
        Fluid fluid = stack.getFluid();
        FluidType type = fluid.getFluidType();
        IFluidTypeRenderProperties properties = RenderProperties.get(type);
        int color = properties.getColorTint(stack);
        TextureAtlasSprite sprite = ModelLoaderRegistry.blockMaterial(properties.getStillTexture(stack)).sprite();
        float fluidHeight = bounds.height();

        if (type.isLighterThanAir())
            color = Color.argb(color, (int)(Math.pow(density, 0.4F) * 255));
        else
            fluidHeight *= density;

        lightMap = pack(Math.max(block(lightMap), type.getLightLevel(stack)), sky(lightMap));

        for (RenderType renderType : RenderType.chunkBufferLayers())
        {
            if (!ItemBlockRenderTypes.canRenderInLayer(fluid.defaultFluidState(), renderType))
                continue;

            VertexConsumer builder = source.getBuffer(renderType);

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

    private static void fillFluidQuads(PoseStack pose, VertexConsumer consumer, Vector3f origin, Direction side, float resolution, float width, float height, int color, TextureAtlasSprite sprite, int lightMap)
    {
        pose.pushPose();
        pose.translate(origin.x(), origin.y(), origin.z());

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

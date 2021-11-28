package nikita488.zycraft.multiblock;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public interface IDynamicMultiBlock
{
    default void tick() {}

    default void render(MatrixStack stack, IRenderTypeBuffer source, int lightMap, float partialTicks) {}

    BlockPos origin();

    AxisAlignedBB aabb();

    boolean isValid();
}

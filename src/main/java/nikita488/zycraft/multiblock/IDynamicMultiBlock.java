package nikita488.zycraft.multiblock;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;

public interface IDynamicMultiBlock
{
    default void tick() {}

    default void render(PoseStack stack, MultiBufferSource source, int lightMap, float partialTicks) {}

    BlockPos origin();

    AABB aabb();

    boolean isValid();
}

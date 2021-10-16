package nikita488.zycraft.multiblock.child.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import nikita488.zycraft.block.state.properties.InterfaceAxis;
import nikita488.zycraft.block.state.properties.ZYBlockStateProperties;
import nikita488.zycraft.multiblock.MultiBlock;
import nikita488.zycraft.multiblock.child.IMultiChild;
import nikita488.zycraft.util.ZYConstants;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class MultiInterfaceTile extends MultiChildTile
{
    public static final EnumProperty<InterfaceAxis> AXIS = ZYBlockStateProperties.INTERFACE_AXIS;
    private final Supplier<InterfaceAxis> axis = () -> getBlockState().getValue(AXIS);
    private boolean updateAxis;
    @Nullable
    private Direction validSide;

    public MultiInterfaceTile(BlockEntityType<?> type)
    {
        super(type);
    }

    protected void update()
    {
        if (level.isClientSide() || parentCount() != 1)
            return;

        updateAxis();

        for (Direction side : ZYConstants.DIRECTIONS)
        {
            if (validSide != null && side != validSide)
                continue;

            BlockPos relativePos = worldPosition.relative(side);

            if (level.isLoaded(relativePos))
                processRelativePos(relativePos, side);

            if (validSide != null)
                break;
        }
    }

    protected void updateAxis()
    {
        if (!updateAxis)
            return;

        this.updateAxis = false;

        if (!hasAxis())
            return;

        for (Direction side : ZYConstants.DIRECTIONS)
        {
            BlockPos relativePos = worldPosition.relative(side);

            if (!level.isLoaded(relativePos))
                continue;

            BlockEntity blockEntity = level.getBlockEntity(relativePos);

            if (blockEntity instanceof IMultiChild && ((IMultiChild)blockEntity).hasParent(getParent()))
                continue;

            InterfaceAxis interfaceAxis = InterfaceAxis.get(side.getAxis());

            if (interfaceAxis != axis.get())
                setAxis(interfaceAxis);

            this.validSide = side;
            break;
        }
    }

    protected void processRelativePos(BlockPos pos, Direction side) {}

    @Override
    public void onMultiValidation(MultiBlock multiBlock)
    {
        super.onMultiValidation(multiBlock);
        multiBlock.addInterface(worldPosition);

        if (!level.isClientSide() && parentCount() == 1)
            this.updateAxis = true;
    }

    @Override
    public void onMultiInvalidation(MultiBlock multiBlock)
    {
        super.onMultiInvalidation(multiBlock);

        if (!level.isClientSide() && !hasParents() && hasAxis() && axis.get() != InterfaceAxis.ALL)
        {
            setAxis(InterfaceAxis.ALL);
            this.validSide = null;
        }
    }

    public InterfaceAxis axis()
    {
        return axis.get();
    }

    protected void setAxis(InterfaceAxis axis)
    {
        InterfaceAxis.set(getBlockState(), level, worldPosition, axis);
    }

    public boolean hasAxis()
    {
        return getBlockState().hasProperty(AXIS);
    }
}

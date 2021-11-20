package nikita488.zycraft.multiblock.child.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import nikita488.zycraft.block.state.properties.InterfaceAxis;
import nikita488.zycraft.block.state.properties.ZYBlockStateProperties;
import nikita488.zycraft.multiblock.MultiBlock;
import nikita488.zycraft.multiblock.child.IMultiChild;
import nikita488.zycraft.util.ZYConstants;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class MultiInterfaceBlockEntity extends MultiChildBlockEntity
{
    public static final EnumProperty<InterfaceAxis> AXIS = ZYBlockStateProperties.INTERFACE_AXIS;
    private final Supplier<InterfaceAxis> axis = () -> getBlockState().getValue(AXIS);
    private boolean updateAxis;
    @Nullable
    private Direction validSide;

    public MultiInterfaceBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
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

            if (!level.isLoaded(relativePos) || level.getBlockEntity(relativePos) instanceof IMultiChild child && child.hasParent(getParent()))
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

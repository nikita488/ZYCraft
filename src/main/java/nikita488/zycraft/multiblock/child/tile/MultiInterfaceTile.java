package nikita488.zycraft.multiblock.child.tile;

import net.minecraft.state.EnumProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
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

    public MultiInterfaceTile(TileEntityType<?> type)
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

            BlockPos adjPos = worldPosition.relative(side);

            if (level.isLoaded(adjPos))
                processAdjacentPos(adjPos, side);

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
            BlockPos adjPos = worldPosition.relative(side);

            if (!level.isLoaded(adjPos))
                continue;

            TileEntity adjTile = level.getBlockEntity(adjPos);

            if (adjTile instanceof IMultiChild && ((IMultiChild)adjTile).hasParent(getParent()))
                continue;

            InterfaceAxis interfaceAxis = InterfaceAxis.get(side.getAxis());

            if (interfaceAxis != axis.get())
                setAxis(interfaceAxis);

            this.validSide = side;
            break;
        }
    }

    protected void processAdjacentPos(BlockPos pos, Direction side) {}

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

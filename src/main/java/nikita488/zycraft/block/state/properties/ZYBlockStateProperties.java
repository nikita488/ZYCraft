package nikita488.zycraft.block.state.properties;

import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;

public class ZYBlockStateProperties
{
    public static final BooleanProperty FLIPPED = BooleanProperty.create("flipped");
    public static final EnumProperty<FabricatorMode> FABRICATOR_MODE = EnumProperty.create("mode", FabricatorMode.class);
    public static final EnumProperty<ValveIOMode> VALVE_IO_MODE = EnumProperty.create("mode", ValveIOMode.class);
    public static final EnumProperty<ItemIOMode> ITEM_IO_MODE = EnumProperty.create("mode", ItemIOMode.class);
}

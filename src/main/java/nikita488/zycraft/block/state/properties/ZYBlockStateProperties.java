package nikita488.zycraft.block.state.properties;

import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;

public class ZYBlockStateProperties
{
    public static final BooleanProperty ZYCHORIUM_WATER_POWERED = BooleanProperty.create("powered");
    public static final BooleanProperty ZYCHORIUM_SOIL_FLIPPED = BooleanProperty.create("flipped");
    public static final EnumProperty<FabricatorMode> FABRICATOR_MODE = EnumProperty.create("mode", FabricatorMode.class);

    public static final BooleanProperty USE_SHAPE_FOR_LIGHT_OCCLUSION = BooleanProperty.create("use_shape_for_light_occlusion");
    public static final BooleanProperty SIGNAL_SOURCE = BooleanProperty.create("signal_source");
    public static final BooleanProperty HAS_ANALOG_OUTPUT_SIGNAL = BooleanProperty.create("has_analog_output_signal");
    public static final EnumProperty<InterfaceAxis> INTERFACE_AXIS = EnumProperty.create("axis", InterfaceAxis.class);
    public static final EnumProperty<ValveIOMode> VALVE_IO_MODE = EnumProperty.create("mode", ValveIOMode.class);
    public static final EnumProperty<ItemIOMode> ITEM_IO_MODE = EnumProperty.create("mode", ItemIOMode.class);
}

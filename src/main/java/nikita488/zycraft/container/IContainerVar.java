package nikita488.zycraft.container;

import net.minecraft.network.PacketBuffer;

public interface IContainerVar
{
    boolean isDirty();

    void update();

    void encode(PacketBuffer buffer);

    void decode(PacketBuffer buffer);
}

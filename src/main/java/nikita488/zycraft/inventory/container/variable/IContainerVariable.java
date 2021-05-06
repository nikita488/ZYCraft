package nikita488.zycraft.inventory.container.variable;

import net.minecraft.network.PacketBuffer;

public interface IContainerVariable
{
    boolean needsUpdating();

    void update();

    void encode(PacketBuffer buffer);

    void decode(PacketBuffer buffer);
}

package nikita488.zycraft.menu.data;

import net.minecraft.network.PacketBuffer;

public interface IMenuData
{
    boolean canBeUpdated();

    void update();

    void encode(PacketBuffer buffer);

    void decode(PacketBuffer buffer);
}

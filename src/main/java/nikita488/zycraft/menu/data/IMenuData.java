package nikita488.zycraft.menu.data;

import net.minecraft.network.FriendlyByteBuf;

public interface IMenuData
{
    boolean canBeUpdated();

    void update();

    void encode(FriendlyByteBuf buffer);

    void decode(FriendlyByteBuf buffer);
}

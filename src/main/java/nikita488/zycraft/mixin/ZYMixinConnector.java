package nikita488.zycraft.mixin;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public class ZYMixinConnector implements IMixinConnector
{
    @Override
    public void connect()
    {
        Mixins.addConfiguration("mixins.zycraft.json");
    }
}

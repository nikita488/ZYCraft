package nikita488.zycraft.mixin.client;

import net.minecraft.client.Minecraft;
import nikita488.zycraft.client.ZYClientSetup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin
{
    @Inject(method = "close", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Util;shutdown()V"))
    private void close(CallbackInfo callback)
    {
        ZYClientSetup.guiComponentManager().close();
    }
}

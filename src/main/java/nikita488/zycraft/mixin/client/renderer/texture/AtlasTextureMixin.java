package nikita488.zycraft.mixin.client.renderer.texture;

import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import nikita488.zycraft.client.texture.CloudSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AtlasTexture.class)
public abstract class AtlasTextureMixin
{
    @Shadow
    public abstract ResourceLocation location();

    @Inject(method = "load(Lnet/minecraft/resources/IResourceManager;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite$Info;IIIII)Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;", at = @At("HEAD"), cancellable = true)
    private void stitchCloudSprite(IResourceManager manager, TextureAtlasSprite.Info info, int atlasWidth, int atlasHeight, int mipMapLevel, int originX, int originY, CallbackInfoReturnable<TextureAtlasSprite> cir)
    {
        if (!location().equals(AtlasTexture.LOCATION_BLOCKS) || (!info.name().equals(CloudSprite.NAME) && !info.name().equals(CloudSprite.NAME2)))
            return;

        NativeImage image = new NativeImage(info.width(), info.height(), false);
        image.untrack();
        cir.setReturnValue(new CloudSprite((AtlasTexture)(Object)this, info, mipMapLevel, atlasWidth, atlasHeight, originX, originY, image));
    }
}

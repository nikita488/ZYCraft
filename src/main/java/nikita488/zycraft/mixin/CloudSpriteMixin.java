package nikita488.zycraft.mixin;

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
public abstract class CloudSpriteMixin
{
    @Shadow
    public abstract ResourceLocation getTextureLocation();

    @Inject(method = "loadSprite", at = @At("HEAD"), cancellable = true)
    private void register(IResourceManager manager, TextureAtlasSprite.Info info, int atlasWidth, int atlasHeight, int mipMapLevel, int originX, int originY, CallbackInfoReturnable<TextureAtlasSprite> cir)
    {
        if (!this.getTextureLocation().equals(AtlasTexture.LOCATION_BLOCKS_TEXTURE) || !info.getSpriteLocation().equals(CloudSprite.NAME))
            return;

        NativeImage image = new NativeImage(info.getSpriteWidth(), info.getSpriteHeight(), false);
        image.untrack();
        cir.setReturnValue(new CloudSprite((AtlasTexture)(Object)this, info, mipMapLevel, atlasWidth, atlasHeight, originX, originY, image));
    }
}

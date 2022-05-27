package dev.draylar.oz.mixin;

import dev.draylar.oz.OmegaZoom;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "getFov",
            at = @At(value = "RETURN"), cancellable = true)
    private void applyZoom(CallbackInfoReturnable<Double> cir) {
        if(OmegaZoom.zoomEnabled) {
            cir.setReturnValue(cir.getReturnValueD() * OmegaZoom.appliedZoom);
        }
    }
}

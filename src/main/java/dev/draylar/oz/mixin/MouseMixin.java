package dev.draylar.oz.mixin;

import dev.draylar.oz.OmegaZoom;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public abstract class MouseMixin {

    @Shadow private double cursorDeltaX;
    @Shadow private double cursorDeltaY;
    @Unique private double cacheCursorDeltaX;
    @Unique private double cacheCursorDeltaY;
    @Shadow @Final private MinecraftClient client;
    @Shadow public abstract boolean isCursorLocked();

    @Inject(method = "updateMouse", at = @At("HEAD"))
    private void cacheCursorDelta(CallbackInfo ci) {
        if (!isCursorLocked() || !client.isWindowFocused()) {
            cacheCursorDeltaX = 0.0;
            cacheCursorDeltaY = 0.0;
        } else {
            cacheCursorDeltaX = cursorDeltaX;
            cacheCursorDeltaY = cursorDeltaY;
        }
    }

    @ModifyVariable(method = "updateMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getTutorialManager()Lnet/minecraft/client/tutorial/TutorialManager;"), index = 5)
    private double updateMouseXSpeed(double value) {
        if(OmegaZoom.zoomEnabled && OmegaZoom.CONFIG.smoothMouse) {
            double baseSens = client.options.getMouseSensitivity().getValue() * (double) 0.6f + (double)0.2f;
            double sensCubed = baseSens * baseSens * baseSens;
            return cacheCursorDeltaX * sensCubed * OmegaZoom.getSmoothingAmount();
        }

        return value;
    }

    @ModifyVariable(method = "updateMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getTutorialManager()Lnet/minecraft/client/tutorial/TutorialManager;"), index = 7)
    private double updateMouseYSpeed(double value) {
        if(OmegaZoom.zoomEnabled && OmegaZoom.CONFIG.smoothMouse) {
            double baseSens = client.options.getMouseSensitivity().getValue() * (double)0.6f + (double)0.2f;
            double sensCubed = baseSens * baseSens * baseSens;
            return cacheCursorDeltaY * sensCubed * OmegaZoom.getSmoothingAmount();
        }

        return value;
    }
}

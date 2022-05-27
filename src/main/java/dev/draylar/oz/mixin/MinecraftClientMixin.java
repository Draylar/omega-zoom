package dev.draylar.oz.mixin;

import dev.draylar.oz.OmegaZoom;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(method = "<init>", at = @At("RETURN"))
    private void fixOmegaZoomKeybindConflicts(RunArgs args, CallbackInfo ci) {
        // Disable "Save Activator" hotkey if it is enabled & Omega Zoom key is set to C
        if(KeyBindingHelper.getBoundKeyOf(OmegaZoom.ZOOM).getCode() == GLFW.GLFW_KEY_C && KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.saveToolbarActivatorKey).getCode() == GLFW.GLFW_KEY_C) {
            MinecraftClient.getInstance().options.saveToolbarActivatorKey.setBoundKey(InputUtil.UNKNOWN_KEY);
            OmegaZoom.LOGGER.info("Unbound \"Save Toolbar Activator\" key from C to prevent conflicts with Omega Zoom defaults.");
        }
    }
}

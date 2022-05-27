package dev.draylar.oz;

import dev.draylar.oz.config.OmegaZoomConfig;
import draylar.omegaconfig.OmegaConfig;
import draylar.omegaconfiggui.OmegaConfigGui;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class OmegaZoom implements ClientModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("Omega Zoom");
    public static final OmegaZoomConfig CONFIG = OmegaConfig.register(OmegaZoomConfig.class);

    public static final KeyBinding ZOOM = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "omegazoom.key.zoom",
            GLFW.GLFW_KEY_C,
            KeyBinding.GAMEPLAY_CATEGORY
    ));

    public static final KeyBinding ZOOM_SCALE_IN = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "omegazoom.key.zoom_scale_in",
            GLFW.GLFW_KEY_LEFT_BRACKET,
            KeyBinding.GAMEPLAY_CATEGORY
    ));

    public static final KeyBinding ZOOM_SCALE_OUT = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "omegazoom.key.zoom_scale_out",
            GLFW.GLFW_KEY_RIGHT_BRACKET,
            KeyBinding.GAMEPLAY_CATEGORY
    ));

    public static boolean zoomEnabled = false;
    public static double appliedZoom = CONFIG.zoomMultiplier;
    private static int zoomCount = 0;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if(client == null) {
                zoomEnabled = false;
                return;
            }

            zoomEnabled = ZOOM.isPressed();

            // If Zoom is not enabled & the perm option is also not enabled, reset the extra scale zoom.
            if(!zoomEnabled) {
                if(!CONFIG.saveZoomModifiers) {
                    appliedZoom = CONFIG.zoomMultiplier;
                    zoomCount = 0;
                }
            }

            if(ZOOM_SCALE_IN.wasPressed()) {
                appliedZoom = Math.max(CONFIG.maxZoomValue, (double) Math.round((appliedZoom - CONFIG.incrementPerZoomScale) * 100d) / 100d);
                updateConfigurationZoom();
                zoomCount++;
            } else if(ZOOM_SCALE_OUT.wasPressed()) {
                appliedZoom = Math.min(CONFIG.minZoomValue, (double) Math.round((appliedZoom + CONFIG.incrementPerZoomScale) * 100d) / 100d);
                updateConfigurationZoom();
                zoomCount--;
            }
        });

        OmegaConfigGui.registerConfigScreen(CONFIG);
    }

    public static double getSmoothingAmount() {
        float bonusZoomScale = 1.0f;
        if(zoomCount != 0 && CONFIG.zoomScaleSmoothMultiplier != 0.0) {
            if(zoomCount > 0) {
                bonusZoomScale = (float) Math.pow(CONFIG.zoomScaleSmoothMultiplier, zoomCount);
            } else {
                bonusZoomScale = 1.0f + (float) Math.pow(CONFIG.zoomScaleSmoothMultiplier, 1f / Math.abs(zoomCount));
            }
        }

        return CONFIG.smoothMouseMultiplier * bonusZoomScale;
    }

    private void updateConfigurationZoom() {
        if(CONFIG.saveZoomModifiers) {
            CONFIG.zoomMultiplier = appliedZoom;
            CONFIG.save();
        }
    }
}

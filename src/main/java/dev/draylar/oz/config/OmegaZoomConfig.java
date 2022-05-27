package dev.draylar.oz.config;

import draylar.omegaconfig.api.Comment;
import draylar.omegaconfig.api.Config;
import org.jetbrains.annotations.Nullable;

public class OmegaZoomConfig implements Config {

    @Comment(value = "Zoom scale when Omega Zoom is toggled - lower is more, 1.0x is normal.")
    public double zoomMultiplier = 0.25f;

    @Comment(value = "If true, when zooming, mouse movement will be slowed.")
    public boolean smoothMouse = true;

    @Comment(value = "Speed multiplier of the mouse when smoothMouse option is true")
    public double smoothMouseMultiplier = 2.0f;

    @Comment(value = "If true, Zoom keys ([ & ] by default) will permanently modify your zoom multiplier config option when used.")
    public boolean saveZoomModifiers = false;

    @Comment(value = "Amount of Zoom to add when using the scale keys")
    public float incrementPerZoomScale = 0.05f;

    @Comment(value = "Maximum amount the camera can be zoomed in by")
    public float maxZoomValue = 0.01f;

    @Comment(value = "Minimum amount the camera can be zoomed in by")
    public float minZoomValue = 1.0f;

    @Comment(value = "Additional smoothing amount to apply per zoom amount while scaling zoom")
    public float zoomScaleSmoothMultiplier = 0.75f;

    @Override
    public String getExtension() {
        return "json5";
    }

    @Override
    public String getName() {
        return "omegazoom";
    }

    @Override
    public @Nullable String getModid() {
        return "omegazoom";
    }
}

package com.memeasaur.directionhudFabric.client;

public final class HudConfigTest {
    public static void main(String[] args) {
        HudConfig defaults = new HudConfig();
        defaults.validate();
        for (int style = 0; style < 10; style++) {
            HudConfig config = defaults.copy();
            config.compassIndex = style;
            config.validate();
            HudConfig restored = HudConfig.GSON.fromJson(HudConfig.GSON.toJson(config), HudConfig.class);
            if (restored.compassIndex != style) throw new AssertionError("Style roundtrip");
        }
        for (int bad : new int[]{-1, 10, Integer.MAX_VALUE}) {
            HudConfig c = defaults.copy(); c.compassIndex = bad; invalid(c);
        }
        for (String bad : new String[]{null, "", "red", "z"}) {
            HudConfig c = defaults.copy(); c.markerColor = bad; invalid(c);
        }
        HudConfig bad = defaults.copy(); bad.alignMode = "invalid"; invalid(bad);
        if (DirectionHud.heading(0) != 0 || DirectionHud.heading(90) != 64
                || DirectionHud.heading(180) != 128 || DirectionHud.heading(270) != 192
                || DirectionHud.heading(-90) != 192 || DirectionHud.heading(360) != 0
                || DirectionHud.heading(-360) != 0 || DirectionHud.heading(720) != 0)
            throw new AssertionError("Cardinal direction / wraparound");
        System.out.println("Configuration and compass heading checks passed");
    }
    private static void invalid(HudConfig config) {
        try { config.validate(); } catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Accepted invalid config");
    }
}

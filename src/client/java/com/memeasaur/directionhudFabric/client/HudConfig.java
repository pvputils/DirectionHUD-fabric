package com.memeasaur.directionhudFabric.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import java.nio.file.*;
import java.io.IOException;
import java.util.*;

public class HudConfig {
    static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static Path path() { return FabricLoader.getInstance().getConfigDir().resolve("directionhud.json"); }
    public boolean enabled = true;
    public String alignMode = "topcenter";
    public String markerColor = "c";
    public int compassIndex = 0;
    public int xOffset = 2;
    public int yOffset = 2;
    public int yOffsetBottomCenter = 41;
    public boolean applyXOffsetToCenter = false;
    public boolean applyYOffsetToMiddle = false;
    public boolean showInChat = true;

    void validate() {
        if (!Arrays.asList("topleft", "topcenter", "topright", "middleleft", "middlecenter", "middleright", "bottomleft", "bottomcenter", "bottomright").contains(alignMode))
            throw new IllegalArgumentException("Invalid alignment; use e.g. topcenter or bottomleft");
        if (compassIndex < 0 || compassIndex > 9)
            throw new IllegalArgumentException("Compass index must be between 0 and 9");
        if (markerColor == null || !markerColor.matches("[0-9a-fA-F]"))
            throw new IllegalArgumentException("Marker color must be a single code: 0-9 or a-f");
    }
    static HudConfig load() throws IOException {
        Path PATH = path();
        if (!Files.exists(PATH)) { HudConfig config = new HudConfig(); config.save(); return config; }
        HudConfig config = GSON.fromJson(Files.readString(PATH), HudConfig.class);
        if (config == null) throw new IllegalArgumentException("Config is empty");
        config.validate();
        return config;
    }
    void save() throws IOException {
        Path PATH = path();
        validate();
        Files.createDirectories(PATH.getParent());
        Path temporary = PATH.resolveSibling(PATH.getFileName() + ".tmp");
        Files.writeString(temporary, GSON.toJson(this));
        Files.move(temporary, PATH, StandardCopyOption.REPLACE_EXISTING);
    }
    HudConfig copy() { return GSON.fromJson(GSON.toJson(this), HudConfig.class); }
}



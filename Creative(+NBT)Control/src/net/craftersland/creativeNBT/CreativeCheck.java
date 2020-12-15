package net.craftersland.creativeNBT;

import java.util.Locale;

public enum CreativeCheck {

    ARMOR_STANDS(true),
    CLONE(false),
    ENCHANTS(true),
    ITEM_FRAMES(true),
    SPAWN_EGGS(true);

    private final String bypassPermission;
    private final String configPath;
    private final boolean defaultValue;

    CreativeCheck(boolean defaultValue) {
        String name = name().toLowerCase(Locale.ROOT).replaceAll("_", "");
        this.bypassPermission = "CNC.bypass." + name;
        this.configPath = "Checks." + name;
        this.defaultValue = defaultValue;
    }

    public String getBypassPermission() {
        return bypassPermission;
    }

    public String getConfigPath() {
        return configPath;
    }

    public boolean getDefaultValue() {
        return defaultValue;
    }
}

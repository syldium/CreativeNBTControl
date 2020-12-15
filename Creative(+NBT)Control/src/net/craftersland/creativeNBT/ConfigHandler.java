package net.craftersland.creativeNBT;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permissible;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public class ConfigHandler {

    private final CC eco;
    private final Map<CreativeCheck, Boolean> enabledChecks = new EnumMap<>(CreativeCheck.class);
    private final Set<Material> enabledMaterialChecks = EnumSet.noneOf(Material.class);

    public ConfigHandler(CC eco) {
        this.eco = eco;
        loadConfig();
    }

    public void loadConfig() {
        eco.saveDefaultConfig();

        CC.LOGGER.info("Loading the config file...");
        FileConfiguration config = eco.getConfig();

        for (CreativeCheck check : CreativeCheck.values()) {
            enabledChecks.put(check, config.getBoolean(check.getConfigPath(), check.getDefaultValue()));
        }

        enabledMaterialChecks.clear();
        for (String rawMaterial : config.getStringList("EnabledFor")) {
            try {
                Material material = Material.valueOf(rawMaterial);
                enabledMaterialChecks.add(material);
            } catch (IllegalArgumentException e) {
                CC.LOGGER.severe(rawMaterial + " is not a valid Material!");
            }
        }

        CC.LOGGER.info("Loading complete!");
    }

    public boolean isCheckEnabled(ItemStack itemStack, Permissible permissible) {
        return enabledMaterialChecks.contains(itemStack.getType())
                && !permissible.hasPermission("CNC.bypass");
    }

    public boolean isCheckEnabled(CreativeCheck creativeCheck, Permissible permissible) {
        return enabledChecks.getOrDefault(creativeCheck, creativeCheck.getDefaultValue())
                && !permissible.hasPermission(creativeCheck.getBypassPermission());
    }

    public String getString(String key) {
        String string = eco.getConfig().getString(key);
        if (string == null) {
            eco.getLogger().severe(String.format("Could not locate %s in the config.yml inside of the plugin folder! (Try generating a new one by deleting the current)", key));
            return "errorCouldNotLocateInConfigYml:" + key;
        }
        return string;
    }

    public String getStringWithColor(String key) {
        return ChatColor.translateAlternateColorCodes('&', getString(key));
    }

}

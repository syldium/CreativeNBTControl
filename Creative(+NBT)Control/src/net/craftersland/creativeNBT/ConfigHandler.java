package net.craftersland.creativeNBT;

import java.io.File;
import java.util.List;

public class ConfigHandler {
	
	private CC eco;
	
	public ConfigHandler(CC eco) {
		this.eco = eco;
		loadConfig();
	}
	
	public void loadConfig() {
		File pluginFolder = new File("plugins" + System.getProperty("file.separator") + CC.pluginName);
		if (pluginFolder.exists() == false) {
    		pluginFolder.mkdir();
    	}
		File configFile = new File("plugins" + System.getProperty("file.separator") + CC.pluginName + System.getProperty("file.separator") + "config.yml");
		if (configFile.exists() == false) {
			CC.log.info("No config file found! Creating new one...");
			eco.saveDefaultConfig();
		}
    	try {
    		CC.log.info("Loading the config file...");
    		eco.getConfig().load(configFile);
    		CC.log.info("Loading complete!");
    	} catch (Exception e) {
    		CC.log.severe("Could not load the config file! You need to regenerate the config! Error: " + e.getMessage());
			e.printStackTrace();
    	}
	}
	
	public String getString(String key) {
		if (!eco.getConfig().contains(key)) {
			eco.getLogger().severe("Could not locate " + key + " in the config.yml inside of the " + CC.pluginName + " folder! (Try generating a new one by deleting the current)");
			return "errorCouldNotLocateInConfigYml:" + key;
		} else {
			return eco.getConfig().getString(key);
		}
	}
	
	public String getStringWithColor(String key) {
		if (!eco.getConfig().contains(key)) {
			eco.getLogger().severe("Could not locate " + key + " in the config.yml inside of the " + CC.pluginName + " folder! (Try generating a new one by deleting the current)");
			return "errorCouldNotLocateInConfigYml:" + key;
		} else {
			return eco.getConfig().getString(key).replaceAll("&", "§");
		}
	}
	
	public List<String> getStringList(String key) {
		if (!eco.getConfig().contains(key)) {
			eco.getLogger().severe("Could not locate " + key + " in the config.yml inside of the " + CC.pluginName + " folder! (Try generating a new one by deleting the current)");
			return null;
		} else {
			return eco.getConfig().getStringList(key);
		}
	}
	
	public Integer getInteger(String key) {
		if (!eco.getConfig().contains(key)) {
			eco.getLogger().severe("Could not locate " + key + " in the config.yml inside of the " + CC.pluginName + " folder! (Try generating a new one by deleting the current)");
			return null;
		} else {
			return eco.getConfig().getInt(key);
		}
	}
	
	public Boolean getBoolean(String key) {
		if (!eco.getConfig().contains(key)) {
			eco.getLogger().severe("Could not locate " + key + " in the config.yml inside of the " + CC.pluginName + " folder! (Try generating a new one by deleting the current)");
			return null;
		} else {
			return eco.getConfig().getBoolean(key);
		}
	}

}

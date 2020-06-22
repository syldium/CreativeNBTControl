package net.craftersland.creativeNBT;

import java.util.logging.Logger;

import net.craftersland.creativeNBT.events.CreativeEvent;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CC extends JavaPlugin {
	
	public static Logger log;
	public static String pluginName = "CreativeNbtControl";
	public boolean is19Server = true;
	public boolean is18Server = true;
	public boolean is13Server = true;
	
	private static ConfigHandler cH;
	private static SoundHandler sH;
	
	@Override
    public void onEnable() {
		log = getLogger();
		getMcVersion();
		cH = new ConfigHandler(this);
		sH = new SoundHandler(this);
		//Register Listeners
    	PluginManager pm = getServer().getPluginManager();
    	pm.registerEvents(new CreativeEvent(this), this);
    	CommandHandler cH = new CommandHandler(this);
    	getCommand("cnc").setExecutor(cH);
    	log.info(pluginName + " loaded successfully!");
	}
	
	@Override
    public void onDisable() {
		Bukkit.getScheduler().cancelTasks(this);
		HandlerList.unregisterAll(this);
		log.info(pluginName + " is disabled!");
	}
	
	private void getMcVersion() {
		String[] serverVersion = Bukkit.getBukkitVersion().split("-");
	    String version = serverVersion[0];
	    
	    if (version.matches("1.7.10")) {
	    	is18Server = false;
	    	is19Server = false;
	    	is13Server = false;
	    }
	    if (version.matches("1.7.10") || version.matches("1.7.9") || version.matches("1.7.5") || version.matches("1.7.2") || version.matches("1.8.8") || version.matches("1.8.3") || version.matches("1.8.4") || version.matches("1.8")) {
	    	is19Server = false;
	    	is13Server = false;
	    }
	}
	
	public boolean isIt18Server() {
		return is18Server;
	}
	
	public ConfigHandler getConfigHandler() {
		return cH;
	}
	public SoundHandler getSoundHandler() {
		return sH;
	}

}

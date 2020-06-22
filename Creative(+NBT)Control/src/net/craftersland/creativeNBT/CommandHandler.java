package net.craftersland.creativeNBT;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {
	
	private CC plugin;
	
	public CommandHandler(CC plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(final CommandSender sender, Command command, String cmdlabel, String[] args) {
		if (cmdlabel.equalsIgnoreCase("cnc") == true) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("reload") == true) {
					if (sender instanceof Player) {
						if (sender.hasPermission("CNC.admin") == false) {
							plugin.getSoundHandler().sendPlingSound((Player) sender);
							sender.sendMessage(plugin.getConfigHandler().getStringWithColor("ChatMessages.NoPermission"));
							return true;
						} else {
							plugin.getSoundHandler().sendPlingSound((Player) sender);
							sender.sendMessage(plugin.getConfigHandler().getStringWithColor("ChatMessages.CmdReload"));
						}
					}
					plugin.getConfigHandler().loadConfig();
				}
			} else {
				if (sender instanceof Player) {
					plugin.getSoundHandler().sendConfirmSound((Player) sender);
				}
				sender.sendMessage(plugin.getConfigHandler().getStringWithColor("ChatMessages.CmdHelp"));
			}
		}
		return true;
	}

}

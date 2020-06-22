package net.craftersland.creativeNBT.events;

import net.craftersland.creativeNBT.CC;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class CreativeEvent implements Listener {
	
	private CC cc;
	
	public CreativeEvent(CC cc) {
		this.cc = cc;
	}
	
	@EventHandler
	public void inventoryClickEvent(final InventoryClickEvent event) {
		//Triggers on other inventory click
		//CC.log.warning("Debug 1 - " + event.getClick().isCreativeAction() + " - " + event.getClick().isRightClick() + " - " + event.getClick().isLeftClick() + " - " + event.getClick());
		if (event.getClick() == ClickType.MIDDLE && event.getClick().isCreativeAction() == true) {
			if (event.getInventory().getType() != InventoryType.PLAYER) {
				Player p = (Player) event.getWhoClicked();
				if (p.getGameMode() == GameMode.CREATIVE) {
					if (p.hasPermission("CNC.bypass") == false && event.getCurrentItem() != null) {
						ItemStack cursorItem = event.getCurrentItem();
						if (cursorItem.getType() != Material.AIR) {
							int amount = cursorItem.getAmount();
							short data = cursorItem.getData().getData();
							boolean checkEnchants = true;
							
							for (String s : cc.getConfigHandler().getStringList("EnabledFor")) {
								try {
									if (cursorItem.getType() == Material.valueOf(s)) {
										event.setCursor(new ItemStack(Material.valueOf(s), amount, data));
										checkEnchants = false;
										break;
									}
								} catch (Exception e) {
									CC.log.warning("Error on material: " + s + " Error: " + e.getMessage());
								}
							}
							if (cursorItem.getEnchantments().isEmpty() == false && p.hasPermission("CNC.bypass.enchants") == false && checkEnchants == true) {
								event.setCursor(new ItemStack(cursorItem.getType(), amount, data));
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void inventoryCreativeEvent(final InventoryCreativeEvent event) {
		//Triggers on player inventory click
		//CC.log.warning("Debug 2 - " + event.getClick().isCreativeAction() + " - " + event.getClick().isRightClick() + " - " + event.getClick().isLeftClick() + " - " + event.getClick());
		if (event.getClick() == ClickType.CREATIVE) {
			Player p = (Player) event.getWhoClicked();
			if (p.hasPermission("CNC.bypass") == false && event.getCursor() != null) {
				ItemStack cursorItem = event.getCursor();
				if (cursorItem.getType() != Material.AIR) {
					int amount = cursorItem.getAmount();
					short data = cursorItem.getData().getData();
					boolean checkEnchants = true;
					
					for (String s : cc.getConfigHandler().getStringList("EnabledFor")) {
						try {
							if (cursorItem.getType() == Material.valueOf(s)) {
								event.setCursor(new ItemStack(Material.valueOf(s), amount, data));
								checkEnchants = false;
								break;
							}
						} catch (Exception e) {
							CC.log.warning("Error on material: " + s + " Error: " + e.getMessage());
						}
					}
					if (cursorItem.getEnchantments().isEmpty() == false && p.hasPermission("CNC.bypass.enchants") == false && checkEnchants == true) {
						event.setCursor(new ItemStack(cursorItem.getType(), amount, data));
					}
				}
			}
		}
	}

}

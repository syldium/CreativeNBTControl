package net.craftersland.creativeNBT.events;

import net.craftersland.creativeNBT.CC;

import java.util.Map.Entry;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;

public class CreativeEvent implements Listener {

	private CC cc;

	public CreativeEvent(CC cc) {
		this.cc = cc;
	}
	
	@EventHandler(ignoreCancelled = true)
	public void InventoryDragEvent(InventoryDragEvent event) {
		if (event.getWhoClicked().getGameMode() == GameMode.CREATIVE) {
			int newAmount = 0;
			for (Entry<Integer, ItemStack> is : event.getNewItems().entrySet())
				newAmount += is.getValue().getAmount();

			//CC.log.warning("DragEvent - Type: " + event.getType().toString() + " - New Size: " + event.getNewItems().size() + " - New Amount: " + newAmount + " - Old Amount: " + event.getOldCursor().getAmount() + " - Result: " + event.getResult().toString());
			if (newAmount > event.getOldCursor().getAmount())
				event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void inventoryClickEvent(final InventoryClickEvent event) {
		//Triggers on other inventory click
		//CC.log.warning("Debug 1 - " + event.getClick().isCreativeAction() + " - " + event.getClick().isRightClick() + " - " + event.getClick().isLeftClick() + " - " + event.getClick() +  " - " + event.getClick().isKeyboardClick());
		if ((event.getClick() == ClickType.MIDDLE && event.getClick().isCreativeAction() == true) || event.getClick() == ClickType.UNKNOWN) {
			if (event.getInventory().getType() != InventoryType.PLAYER) {
				Player p = (Player) event.getWhoClicked();
				if (p.getGameMode() == GameMode.CREATIVE) {
					if (p.hasPermission("CNC.bypass") == false && event.getCurrentItem() != null) {
						ItemStack cursorItem = event.getCurrentItem();
						if (cursorItem.getType() != Material.AIR) {
							int amount = cursorItem.getAmount();
							short data = cursorItem.getData().getData();
							boolean checkEnchants = true;

							if (cursorItem.getItemMeta() instanceof SpawnEggMeta) {
								event.setCursor(new ItemStack(cursorItem.getType(), amount, data));
								checkEnchants = false;
							} else {
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
	}

	@EventHandler(ignoreCancelled = true)
	public void inventoryCreativeEvent(final InventoryCreativeEvent event) {
		//Triggers on player inventory click
		//CC.log.warning("Debug 2 - " + event.getClick().isCreativeAction() + " - " + event.getClick().isRightClick() + " - " + event.getClick().isLeftClick() + " - " + event.getClick() + " - " + event.getSlotType() + " - " + event.getRawSlot() + " - " + event.getClickedInventory().getType().toString());
		if (event.getClick() == ClickType.CREATIVE) {
			Player p = (Player) event.getWhoClicked();
			if (p.hasPermission("CNC.bypass") == false && event.getCursor() != null) {
				ItemStack cursorItem = event.getCursor();
				if (cursorItem.getType() != Material.AIR) {
					int amount = cursorItem.getAmount();
					short data = cursorItem.getData().getData();
					boolean checkEnchants = true;

					if (cursorItem.getItemMeta() instanceof SpawnEggMeta) {
						event.setCursor(new ItemStack(cursorItem.getType(), amount, data));
						checkEnchants = false;
					} else {
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

	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		final Player p = event.getPlayer();
		Entity entity = event.getRightClicked();

		ItemStack item = p.getInventory().getItemInMainHand();

		if (item == null || item.getType() == Material.AIR)
			return;

		if (entity.getType().equals(EntityType.ITEM_FRAME)) {
			if (p.hasPermission("CNC.bypass") == false && p.getGameMode() == GameMode.CREATIVE) {
				event.setCancelled(true);
				p.sendMessage(cc.getConfigHandler().getStringWithColor("ChatMessages.OpenSurvivalFrame"));
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerArmorStandManipulate(PlayerArmorStandManipulateEvent event) {
		final Player p = event.getPlayer();

		ItemStack item = event.getPlayerItem();

		if (item != null && (event.getArmorStandItem() == null || event.getArmorStandItem().getType() == Material.AIR)) {

			if (item.getType() == Material.AIR)
				return;

			if (p.hasPermission("CNC.bypass") == false && p.getGameMode() == GameMode.CREATIVE) {
				event.setCancelled(true);
				p.sendMessage(cc.getConfigHandler().getStringWithColor("ChatMessages.OpenSurvivalArmor"));
			}
		}
	}

}

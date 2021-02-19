package net.craftersland.creativeNBT.events;

import net.craftersland.creativeNBT.CC;

import net.craftersland.creativeNBT.CreativeCheck;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;

import java.util.Map;

import static net.craftersland.creativeNBT.BukkitVersion.*;

public class CreativeEvent implements Listener {

	private final CC cc;

	public CreativeEvent(CC cc) {
		this.cc = cc;
	}

	@EventHandler(ignoreCancelled = true)
	public void InventoryDragEvent(InventoryDragEvent event) {
		if (event.getWhoClicked().getGameMode() != GameMode.CREATIVE
				|| !cc.getConfigHandler().isCheckEnabled(CreativeCheck.CLONE, event.getWhoClicked())) {
			return;
		}

		int newAmount = 0;
		for (ItemStack itemStack : event.getNewItems().values())
			newAmount += itemStack.getAmount();

		//CC.LOGGER.warning("DragEvent - Type: " + event.getType().toString() + " - New Size: " + event.getNewItems().size() + " - New Amount: " + newAmount + " - Old Amount: " + event.getOldCursor().getAmount() + " - Result: " + event.getResult().toString());
		if (newAmount > event.getOldCursor().getAmount()) {
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void inventoryClickEvent(final InventoryClickEvent event) {
		//CC.LOGGER.warning("Debug 1 - " + event.getClick().isCreativeAction() + " - " + event.getClick().isRightClick() + " - " + event.getClick().isLeftClick() + " - " + event.getClick() +  " - " + event.getClick().isKeyboardClick() +  " - "  + event.getAction());
		ClickType click = event.getClick();
		if (click.isCreativeAction() || click == ClickType.UNKNOWN
				|| event.getAction() == InventoryAction.CLONE_STACK && cc.getConfigHandler().isCheckEnabled(CreativeCheck.CLONE, event.getWhoClicked())) {
			ItemStack cursorItem = event.getCursor();
			if (cursorItem == null || cursorItem.getType() == Material.AIR) {
				return;
			}

			if (hasIllegalNbt(event.getWhoClicked(), cursorItem)) {
				// noinspection deprecation
				event.setCursor(sanitizeItemStack(cursorItem));
			}
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void itemDropEvent(PlayerDropItemEvent event) {
		Item item = event.getItemDrop();
		ItemStack itemStack = item.getItemStack();
		if (hasIllegalNbt(event.getPlayer(), itemStack)) {
			item.setItemStack(sanitizeItemStack(itemStack));
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		final Player p = event.getPlayer();
		Entity entity = event.getRightClicked();

		ItemStack item = getItemInHand(event);

		if (item == null || item.getType() == Material.AIR)
			return;

		if (entity instanceof ItemFrame) {
			ItemFrame itemFrame = (ItemFrame) entity;
			if (itemFrame.getItem().getType() != Material.AIR) {
				return;
			}

			if (p.getGameMode() == GameMode.CREATIVE && cc.getConfigHandler().isCheckEnabled(CreativeCheck.ITEM_FRAMES, p)) {
				event.setCancelled(true);
				itemFrame.setItem(item);
				item.setAmount(item.getAmount() - 1);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerInteractEntity(EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof Player)) {
			return;
		}

		Player player = (Player) event.getDamager();

		if (event.getEntity() instanceof ItemFrame) {
			if (player.getGameMode() != GameMode.CREATIVE
					|| !cc.getConfigHandler().isCheckEnabled(CreativeCheck.ITEM_FRAMES, player)) {
				return;
			}

			ItemFrame itemFrame = (ItemFrame) event.getEntity();
			ItemStack itemStack = itemFrame.getItem();
			if (isFixed(itemFrame) || itemStack.getType() == Material.AIR) {
				return;
			}

			if (!doEntityDrops(itemFrame.getWorld())) {
				return;
			}

			itemFrame.getWorld().dropItemNaturally(itemFrame.getLocation(), itemStack);
			itemFrame.setItem(null);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerArmorStandManipulate(PlayerArmorStandManipulateEvent event) {
		final Player p = event.getPlayer();

		ItemStack item = event.getPlayerItem();

		if (item != null && (event.getArmorStandItem() == null || event.getArmorStandItem().getType() == Material.AIR)) {

			if (item.getType() == Material.AIR)
				return;

			if (cc.getConfigHandler().isCheckEnabled(CreativeCheck.ARMOR_STANDS, p) && p.getGameMode() == GameMode.CREATIVE) {
				event.setCancelled(true);
				setItem(event.getRightClicked(), event.getSlot(), item);
				item.setAmount(item.getAmount() - 1);
			}
		}
	}

	/**
	 * Tests according to the plugin configuration if the {@link ItemStack} seems suspicious.
	 *
	 * @param player The player who owns the item, to check some permissions.
	 * @param item The item to test.
	 * @return If the {@link ItemStack} should be recreated without metadata.
	 */
	private boolean hasIllegalNbt(HumanEntity player, ItemStack item) {
		if (player.getGameMode() != GameMode.CREATIVE) {
			return false;
		}

		if (item != null && item.getType() != Material.AIR) {
			if (item.getItemMeta() instanceof SpawnEggMeta) {
				return cc.getConfigHandler().isCheckEnabled(CreativeCheck.SPAWN_EGGS, player);
			}
			if (cc.getConfigHandler().isCheckEnabled(item, player)) {
				return true;
			}

			if (cc.getConfigHandler().isCheckEnabled(CreativeCheck.ENCHANTS, player)) {
				for (Map.Entry<Enchantment, Integer> enchantment : item.getEnchantments().entrySet()) {
					if (enchantment.getKey().getMaxLevel() < enchantment.getValue()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Removes each metadata from an {@link ItemStack}.
	 *
	 * @param original The original {@link ItemStack}.
	 * @return A new {@link ItemStack}.
	 */
	private ItemStack sanitizeItemStack(ItemStack original) {
		if (cc.is13Server) {
			return new ItemStack(original.getType(), original.getAmount());
		}
		// noinspection deprecation
		return new ItemStack(original.getType(), original.getAmount(), original.getDurability());
	}

}

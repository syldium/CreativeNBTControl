package net.craftersland.creativeNBT;

import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class BukkitVersion {

    /**
     * If item frames can be locked (1.16). {@link ItemFrame#isFixed()}
     */
    private static final boolean ITEM_FRAME_IS_FIXED;

    /**
     * If the {@link GameRule} enum exists.
     */
    private static final boolean GAMERULE_ENUM;

    /**
     * If the method {@link PlayerInteractEntityEvent#getHand()} exists.
     */
    private static final boolean INTERACT_EVENT_GET_HAND;

    static {
        boolean isFixed = false;
        try {
            ItemFrame.class.getMethod("isFixed");
            isFixed = true;
        } catch (NoSuchMethodException ignored) { }
        ITEM_FRAME_IS_FIXED = isFixed;

        boolean gameRule = false;
        try {
            Class.forName("org.bukkit.GameRule");
            gameRule = true;
        } catch (ClassNotFoundException ignored) { }
        GAMERULE_ENUM = gameRule;

        boolean getHand = false;
        try {
            PlayerInteractEntityEvent.class.getMethod("getHand");
            getHand = true;
        } catch (NoSuchMethodException ignored) { }
        INTERACT_EVENT_GET_HAND = getHand;
    }

    public static boolean isFixed(ItemFrame itemFrame) {
        return ITEM_FRAME_IS_FIXED && itemFrame.isFixed();
    }

    public static boolean doEntityDrops(World world) {
        // noinspection deprecation
        Boolean doEntityDrops = GAMERULE_ENUM ?
                world.getGameRuleValue(GameRule.DO_ENTITY_DROPS)
                : world.getGameRuleValue("doEntityDrops").equalsIgnoreCase("true");
        return doEntityDrops == null || doEntityDrops;
    }

    public static ItemStack getItemInHand(PlayerInteractEntityEvent event) {
        if (INTERACT_EVENT_GET_HAND) {
            return event.getPlayer().getInventory().getItem(event.getHand());
        }
        // noinspection deprecation
        return event.getPlayer().getItemInHand();
    }
}

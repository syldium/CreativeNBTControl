package net.craftersland.creativeNBT;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundHandler {
	
	private CC as;
	
	public SoundHandler(CC as) {
		this.as = as;
	}
	
	public void sendPlingSound(Player p) {
		if (as.is13Server == true) {
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 3F, 3F);
		} else if (as.is19Server == true) {
			p.playSound(p.getLocation(), Sound.valueOf("BLOCK_NOTE_PLING"), 3F, 3F);
		} else {
			p.playSound(p.getLocation(), Sound.valueOf("NOTE_PLING"), 3F, 3F);
		}
	}
	
	public void sendLevelUpSound(Player p) {
		if (as.is19Server == true) {
			p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
		} else {
			p.playSound(p.getLocation(), Sound.valueOf("LEVEL_UP"), 1F, 1F);
		}
	}
	
	public void sendConfirmSound(Player p) {
		if (as.is19Server == true) {
			p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F);
		} else {
			p.playSound(p.getLocation(), Sound.valueOf("SUCCESSFUL_HIT"), 1F, 1F);
		}
	}

}

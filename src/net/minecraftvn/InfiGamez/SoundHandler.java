package net.minecraftvn.InfiGamez;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundHandler {
	
	private Main plugin;
	
	public SoundHandler(Main m) {
		this.plugin = m;
	}
	
	public void sendItemBreakSound(Player p) {
		p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, 1.0F);
	}
	
	public void sendClickSound(Player p) {
		p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
	}
	
	public void sendLevelUpSound(Player p) {
		p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
	}
	
	public void sendPlingSound(Player p) {
		p.playSound(p.getLocation(), Sound.valueOf("BLOCK_NOTE_PLING"), 3.0F, 3.0F);
	}

}

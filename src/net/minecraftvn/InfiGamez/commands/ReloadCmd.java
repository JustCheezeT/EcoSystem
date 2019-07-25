package net.minecraftvn.InfiGamez.commands;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.minecraftvn.InfiGamez.Main;

public class ReloadCmd {
	
	private Main money;
	
	public ReloadCmd(Main m) {
		this.money = m;
	}
	
	public boolean runCmd(CommandSender sender) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (p.hasPermission("EcoSystem.admin")) {
				try {
					money.getConfig().load(new File("plugins"+System.getProperty("file.separator")+"EcoSystem"+System.getProperty("file.separator")+"config.yml"));
				} catch (Exception e) {
					money.getConfigurationHandler().printMessage(p, "chatMessages.reloadFail", "0", p, p.getName());
					money.getSoundHandler().sendPlingSound(p);
					e.printStackTrace();
					return false;
				}
				money.getInterestHandler().resetTask();
				money.getSoundHandler().sendLevelUpSound(p);
				money.getConfigurationHandler().printMessage(p, "chatMessages.reloadComplete", "0", p, p.getName());
				return true;
			}
			money.getSoundHandler().sendPlingSound(p);
			money.getConfigurationHandler().printMessage(p, "chatMessages.noPermission", "0", p, p.getName());
			return false;
		} else {
			try {
				money.getConfig().load(new File("plugins"+System.getProperty("file.separator")+"EcoSystem"+System.getProperty("file.separator")+"config.yml"));
			} catch (Exception e) {
				sender.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + ">> " + ChatColor.RED + "Không thể tải lại config!");
				e.printStackTrace();
				return false;
			}
			sender.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + ">> " + ChatColor.GREEN + "Đã tải lại config!");
			return true;
		}
	}

}

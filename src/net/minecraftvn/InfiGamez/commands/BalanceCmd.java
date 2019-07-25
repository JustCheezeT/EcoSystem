package net.minecraftvn.InfiGamez.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.minecraftvn.InfiGamez.Main;

public class BalanceCmd {
	
	private Main money;
	
	public BalanceCmd(Main m) {
		this.money = m;
	}
	
	public boolean runUserCmd(CommandSender sender) {
		if (sender instanceof Player) {
			final Player p = (Player) sender;
			if (p.hasPermission("EcoSystem.cmd.balance")) {
				if (money.cooldown.contains(p.getUniqueId())) {
					money.getConfigurationHandler().printMessage(p, "chatMessages.tooFastInteraction", "0", p, p.getName());
					money.getSoundHandler().sendPlingSound(p);
    				return true;
    			}
				money.getMoneyDatabaseInterface().getBalance(p);
				money.getConfigurationHandler().printMessage(p, "chatMessages.balance", "0", p, p.getName());
				money.getSoundHandler().sendClickSound(p);
				if (money.setupTitleManager() == true) {
					money.getConfigurationHandler().actionBarMessage(p, "actionBarMessages.balance");
				}
				money.cooldown.add(p.getUniqueId());
				Double delayCalc = 20.00 / 1000.00 * Double.parseDouble(money.getConfigurationHandler().getString("general.timeBetweenTwoInteractions"));
				int delay = delayCalc.intValue();
				Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(money, new Runnable() {
                    public void run() {
                    	money.cooldown.remove(p.getUniqueId());
                    }
            }, delay);
			} else {
				money.getSoundHandler().sendPlingSound(p);
				money.getConfigurationHandler().printMessage(p, "chatMessages.noPermission", "0", p, p.getName());
				return false;
			}
		} else {
			sender.sendMessage("Bạn phải là người chơi để thực hiện lệnh!");
		}
		return true;
	}
	
	public boolean runAdminCmd(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (p.hasPermission("EcoSystem.admin")) {
				Player target = Bukkit.getPlayer(args[1]);
				if (target != null) {
					if (target.isOnline()) {
						if (money.getMoneyDatabaseInterface().hasAccount(target) == false) {
							money.getConfigurationHandler().printMessage(((Player) sender).getPlayer(), "chatMessages.accountDoesNotExist", "0", target, target.getName());
							money.getSoundHandler().sendPlingSound(p);
							return false;
						}
						String amount = money.getMoneyDatabaseInterface().getBalance(target).toString();
						money.getConfigurationHandler().printMessage(((Player) sender).getPlayer(), "chatMessages.balanceCommand", amount, target, target.getName());
						money.getSoundHandler().sendClickSound(p);
						return true;
					    }
					} else {
						try {
							UUID targetUUID = UUID.fromString(args[1]);
							if (money.getMoneyDatabaseInterface().hasAccount(targetUUID) == false) {
								money.getConfigurationHandler().printMessage((Player) sender, "chatMessages.accountDoesNotExist", "0", null, "null");
								money.getSoundHandler().sendPlingSound(p);
								return false;
							}
							String amount = money.getMoneyDatabaseInterface().getBalance(targetUUID).toString();
							money.getConfigurationHandler().printMessage((Player) sender, "chatMessages.balanceCommand", amount, null, "null");
							money.getSoundHandler().sendClickSound(p);
							return true;
						} catch (Exception e) {
							OfflinePlayer offp = Bukkit.getOfflinePlayer(args[1]);
							if (offp != null) {
								if (money.getMoneyDatabaseInterface().hasAccount(offp.getUniqueId()) == false) {
									money.getConfigurationHandler().printMessage((Player) sender, "chatMessages.accountDoesNotExist", "0", null, "null");
									money.getSoundHandler().sendPlingSound(p);
									return false;
								}
								String amount = money.getMoneyDatabaseInterface().getBalance(offp.getUniqueId()).toString();
								money.getConfigurationHandler().printMessage((Player) sender, "chatMessages.balanceCommand", amount, null, "null");
								money.getSoundHandler().sendClickSound(p);
								return true;
							} else {
								money.getConfigurationHandler().printMessage((Player) sender, "chatMessages.accountDoesNotExist", "0", null, "null");
								money.getSoundHandler().sendPlingSound(p);
								return false;
							}
						}
				}
				
			} else {
				money.getSoundHandler().sendPlingSound(p);
				money.getConfigurationHandler().printMessage(p, "chatMessages.noPermission", "0", p, p.getName());
				return false;
			}
		} else {
			Player target = Bukkit.getPlayer(args[1]);
			if (target != null) {
				if (target.isOnline()) {
					if (money.getMoneyDatabaseInterface().hasAccount(target) == false) {
						sender.sendMessage(ChatColor.RED +  ">> " + ChatColor.WHITE + "" + target.getName() + ChatColor.RED + " không có tài khoản ngân hàng!");
						return false;
					}
					String amount = money.getMoneyDatabaseInterface().getBalance(target).toString();
					sender.sendMessage(ChatColor.GREEN +  ">> Số tiền của " + ChatColor.WHITE + target.getName() + ChatColor.GREEN + " là: " + ChatColor.WHITE + "" + amount);
					return true;
				    }
				} else {
					try {
						UUID targetUUID = UUID.fromString(args[1]);
						if (money.getMoneyDatabaseInterface().hasAccount(targetUUID) == false) {
							sender.sendMessage(ChatColor.RED +  ">> " + ChatColor.WHITE + "" + targetUUID + ChatColor.RED + " không có tài khoản ngân hàng!");
							return false;
						}
						String amount = money.getMoneyDatabaseInterface().getBalance(targetUUID).toString();
						sender.sendMessage(ChatColor.GREEN +  ">> Số tiền của " + ChatColor.WHITE + targetUUID + ChatColor.GREEN + " là: " + ChatColor.WHITE + "" + amount);
						return true;
					} catch (Exception e) {
						OfflinePlayer offp = Bukkit.getOfflinePlayer(args[1]);
						if (offp != null) {
							if (money.getMoneyDatabaseInterface().hasAccount(offp.getUniqueId()) == false) {
								sender.sendMessage(ChatColor.RED +  ">> " + ChatColor.WHITE + "" + offp.getName() + ChatColor.RED + " không có tài khoản ngân hàng!");
								return false;
							}
							String amount = money.getMoneyDatabaseInterface().getBalance(offp.getUniqueId()).toString();
							sender.sendMessage(ChatColor.GREEN +  ">> Số tiền của " + ChatColor.WHITE + offp.getName() + ChatColor.GREEN + " là: " + ChatColor.WHITE + "" + amount);
							return true;
						} else {
							sender.sendMessage(ChatColor.RED +  ">> " + ChatColor.WHITE + "" + offp.getName() + ChatColor.RED + " không có tài khoản ngân hàng!");
							return false;
						}
					}
			}
		}
		return true;
	}

}

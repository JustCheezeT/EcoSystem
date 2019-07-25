package net.minecraftvn.InfiGamez;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerListener implements Listener{
	
	private Main money;
	public static Economy econ = null;
	private Set<UUID> safety = new HashSet<UUID>();

	public PlayerListener(Main money) {
		this.money = money;
	}
	
	private boolean isEventSafe(final UUID pU) {
		if (safety.contains(pU) == true) {
			return false;
		}
		safety.add(pU);
		Bukkit.getScheduler().runTaskLaterAsynchronously(money, new Runnable() {

			@Override
			public void run() {
				safety.remove(pU);
			}
			
		}, 2L);
		return true;
	}
	
	private boolean isSign(Material sourceMaterial) {
		if (sourceMaterial.equals(Material.valueOf("WALL_SIGN")) || sourceMaterial.equals(Material.valueOf("SIGN_POST")) || sourceMaterial.equals(Material.valueOf("SIGN"))) {
			return true;
		} 
		return false;
	}
	
	@EventHandler
	public void onClick(final PlayerInteractEvent event) {
		final Player p = event.getPlayer();
		
		if (event.getClickedBlock() != null) {
		    if (isSign(event.getClickedBlock().getType())) {
		    	if (isEventSafe(event.getPlayer().getUniqueId()) == true) {
		    		final Sign sign = (Sign) event.getClickedBlock().getState();
			    	Bukkit.getScheduler().runTaskAsynchronously(money, new Runnable() {

						@Override
						public void run() {
							if (sign.getLine(0).contains(money.getConfigurationHandler().getString("signFormat.signColor") + ChatColor.BOLD + "[Eco System]")) {
						    	if (Main.perms.has(p, "EcoSystem.use")) {
						    		if (p.isSneaking()) {
						    			money.getConfigurationHandler().printMessage(p, "chatMessages.denyIfSneaking", "0", p, p.getName());
						    			money.getSoundHandler().sendPlingSound(p);
					    				return;
					    			}
						    		if ((sign.getLine(1).equals(money.getConfigurationHandler().getString("signFormat.balance")))) {
						    			if (money.cooldown.contains(p.getUniqueId())) {
						    				money.getConfigurationHandler().printMessage(p, "chatMessages.tooFastInteraction", "0", p, p.getName());
						    				money.getSoundHandler().sendPlingSound(p);
						    				return;
						    			} else {
						    				money.cooldown.add(p.getUniqueId());
											Double delayCalc = 20.00 / 1000.00 * Double.parseDouble(money.getConfigurationHandler().getString("general.timeBetweenTwoInteractions"));
											int delay = delayCalc.intValue();
											Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(money, new Runnable() {
				                                public void run() {
				                                	money.cooldown.remove(p.getUniqueId());
				                                }
											}, delay);
						    			}
										money.getMoneyDatabaseInterface().getBalance(p);
										money.getConfigurationHandler().printMessage(p, "chatMessages.balance", "0", p, p.getName());
										money.getSoundHandler().sendClickSound(p);
					    				if (money.setupTitleManager() == true) {
					    					money.getConfigurationHandler().actionBarMessage(p, "actionBarMessages.balance");
					    				}
										return;
									}

						    		if ((sign.getLine(1).equals(money.getConfigurationHandler().getString("signFormat.deposit")))) {

						    			if (money.cooldown.contains(p.getUniqueId())) {
						    				money.getConfigurationHandler().printMessage(p, "chatMessages.tooFastInteraction", "0", p, p.getName());
						    				money.getSoundHandler().sendPlingSound(p);
						    				return;
						    			} else {
						    				money.cooldown.add(p.getUniqueId());
											Double delayCalc = 20.00 / 1000.00 * Double.parseDouble(money.getConfigurationHandler().getString("general.timeBetweenTwoInteractions"));
											int delay = delayCalc.intValue();
											Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(money, new Runnable() {
				                                public void run() {
				                                	money.cooldown.remove(p.getUniqueId());
				                                }
											}, delay);
						    			}
						    			final Double amount = Double.parseDouble(sign.getLine(2));
						    			if (Main.econ.getBalance(p) >= amount) {
						    				Double bankBalance = money.getMoneyDatabaseInterface().getBalance(p);
						    				if (bankBalance + amount > Double.parseDouble(money.getConfigurationHandler().getString("general.maxBankLimitMoney"))) {
						    					money.getConfigurationHandler().printMessage(p, "chatMessages.reachedMaximumMoneyInAccount", amount + "", p, p.getName());
						    					money.getSoundHandler().sendPlingSound(p);
						    					return;
						    				}
						    				Bukkit.getScheduler().runTask(money, new Runnable() {

												@Override
												public void run() {
													Main.econ.withdrawPlayer(p, amount);
												}
						    					
						    				});
						    				money.getMoneyDatabaseInterface().setBalance(p, bankBalance + amount);
						    				money.getConfigurationHandler().printMessage(p, "chatMessages.depositedSuccessfully", amount + "", p, p.getName());
						    				money.getSoundHandler().sendClickSound(p);
						    				if (money.setupTitleManager() == true) {
						    					money.getConfigurationHandler().actionBarMessage(p, "actionBarMessages.balanceLeft");
						    				}
						    				return;
						    			}
						    			money.getConfigurationHandler().printMessage(p, "chatMessages.notEnoughMoneyInPoket", amount + "", p, p.getName());
						    			money.getSoundHandler().sendPlingSound(p);
						    			return;
						    		}
						    		if ((sign.getLine(1).equals(money.getConfigurationHandler().getString("signFormat.withdraw")))) {
						    			if (money.cooldown.contains(p.getUniqueId())) {
						    				money.getConfigurationHandler().printMessage(p, "chatMessages.tooFastInteraction", "0", p, p.getName());
						    				money.getSoundHandler().sendPlingSound(p);
						    				return;
						    			} else {
						    				money.cooldown.add(p.getUniqueId());
											Double delayCalc = 20.00 / 1000.00 * Double.parseDouble(money.getConfigurationHandler().getString("general.timeBetweenTwoInteractions"));
											int delay = delayCalc.intValue();
											Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(money, new Runnable() {
				                                public void run() {
				                                	money.cooldown.remove(p.getUniqueId());
				                                }
											}, delay);
						    			}
						    			Double bankBalance = money.getMoneyDatabaseInterface().getBalance(p);
						    			final Double amount = Double.parseDouble(sign.getLine(2));
						    			if (bankBalance >= amount) {
						    				if (Main.econ.getBalance(p) + amount > Double.parseDouble(money.getConfigurationHandler().getString("general.maxPocketLimitMoney"))) {
						    					money.getConfigurationHandler().printMessage(p, "chatMessages.reachedMaximumMoneyInPocket", amount + "", p, p.getName());
						    					money.getSoundHandler().sendPlingSound(p);
						    					return;
						    				}
						    				money.getMoneyDatabaseInterface().setBalance(p, bankBalance - amount);
						    				Bukkit.getScheduler().runTask(money, new Runnable() {

												@Override
												public void run() {
													Main.econ.depositPlayer(p, amount);
												}
						    					
						    				});
						    				money.getConfigurationHandler().printMessage(p, "chatMessages.withdrewSuccessfully", amount + "", p, p.getName());
						    				money.getSoundHandler().sendClickSound(p);
						    				if (money.setupTitleManager() == true) {
						    					money.getConfigurationHandler().actionBarMessage(p, "actionBarMessages.balanceLeft");
						    				}
						    				return;
						    			}
						    			money.getConfigurationHandler().printMessage(p, "chatMessages.notEnoughMoneyInAccount", amount + "", p, p.getName());
						    			money.getSoundHandler().sendPlingSound(p);
						    			return;
						    		}
								}
						    	money.getConfigurationHandler().printMessage(p, "chatMessages.notAllowed", "0", p, p.getName());
						    }
						}
			    		
			    	});
		    	}
		    }
		}
				
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onSignPlace(SignChangeEvent event) {
		Player p = event.getPlayer();
		if (event.getLine(0).contains("[Eco System]")){
			if (Main.perms.has(p, "EcoSystem.admin")) {
				if (event.getLine(1).toLowerCase().contains("balance")) {
					if (!event.getLine(2).isEmpty() || !event.getLine(3).isEmpty()) {
						money.getConfigurationHandler().printMessage(p, "chatMessages.errorWhileCreatingSign", "0", p, p.getName());
						money.getSoundHandler().sendPlingSound(p);
						p.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + ">> " + ChatColor.RED + "Dòng 3 và 4 phải được để trống cho sign.");
						return;
					}
					event.setLine(0, "§" + money.getConfigurationHandler().getString("signFormat.signColor") + ChatColor.BOLD + "[Eco System]");
					event.setLine(1, money.getConfigurationHandler().getString("signFormat.balance"));
					event.setLine(2, "");
					event.setLine(3, "");
					money.getConfigurationHandler().printMessage(p, "chatMessages.createdSignSuccessfully", "0", p, p.getName());
					money.getSoundHandler().sendLevelUpSound(p);
					p.playEffect(event.getBlock().getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
					return;
				}
				if (event.getLine(1).toLowerCase().contains("deposit")) {
					if (event.getLine(2).matches("^[0-9]{1,15}+(.[0-9]{1,2})?$")) {
						if (!event.getLine(3).isEmpty()) {
							money.getConfigurationHandler().printMessage(p, "chatMessages.errorWhileCreatingSign", "0", p, p.getName());
							p.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + ">> " + ChatColor.RED + "Dòng 4 phải được để trống cho sign.");
							money.getSoundHandler().sendPlingSound(p);
							return;
						}
						Double numberProcessing = Double.parseDouble(event.getLine(2));
						if (numberProcessing == 0) {
							money.getConfigurationHandler().printMessage(p, "chatMessages.errorWhileCreatingSign", "0", p, p.getName());
							p.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + ">> " + ChatColor.RED + "Dùng một số lớn hơn 0, các format hỗ trợ: 1 or 1.0 or 1.00");
							money.getSoundHandler().sendPlingSound(p);
							return;
						}
						String formatedNumber = numberProcessing.toString();
						if (formatedNumber.endsWith(".0")) {
							Double.parseDouble(event.getLine(2));
							DecimalFormat format1 = new DecimalFormat("#0");
							event.setLine(2, format1.format(numberProcessing));
						} else {
							DecimalFormat format2 = new DecimalFormat("#0.00");
							event.setLine(2, format2.format(numberProcessing));
						}
						event.setLine(0, "§" + money.getConfigurationHandler().getString("signFormat.signColor") + ChatColor.BOLD + "[Bank]");
						event.setLine(1, money.getConfigurationHandler().getString("signFormat.deposit"));
						event.setLine(3, "");
						money.getConfigurationHandler().printMessage(p, "chatMessages.createdSignSuccessfully", "0", p, p.getName());
						money.getSoundHandler().sendLevelUpSound(p);
						p.playEffect(event.getBlock().getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
						return;
					}
					p.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + ">> " + ChatColor.RED + "Trên dòng 3 hãy nhập vào một số, với format: 1 or 1.0 or 1.00");
					return;
				}
				if (event.getLine(1).toLowerCase().contains("withdraw")) {
					if (event.getLine(2).matches("^[0-9]{1,15}+(.[0-9]{1,2})?$")) {
						if (!event.getLine(3).isEmpty()) {
							money.getConfigurationHandler().printMessage(p, "chatMessages.errorWhileCreatingSign", "0", p, p.getName());
							p.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + ">> " + ChatColor.RED + "Dòng 4 phải được để trống cho sign.");
							money.getSoundHandler().sendPlingSound(p);
							return;
						}
						Double numberProcessing = Double.parseDouble(event.getLine(2));
						if (numberProcessing == 0) {
							money.getConfigurationHandler().printMessage(p, "chatMessages.errorWhileCreatingSign", "0", p, p.getName());
							p.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + ">> " + ChatColor.RED + "Dùng một số lớn hơn 0, các format hỗ trợ: 1 or 1.0 or 1.00");
							money.getSoundHandler().sendPlingSound(p);
							return;
						}
						String formatedNumber = numberProcessing.toString();
						if (formatedNumber.endsWith(".0")) {
							Double.parseDouble(event.getLine(2));
							DecimalFormat format1 = new DecimalFormat("#0");
							event.setLine(2, format1.format(numberProcessing));
						} else {
							DecimalFormat format2 = new DecimalFormat("#0.00");
							event.setLine(2, format2.format(numberProcessing));
						}
						event.setLine(0, "§" + money.getConfigurationHandler().getString("signFormat.signColor") + ChatColor.BOLD + "[Bank]");
						event.setLine(1, money.getConfigurationHandler().getString("signFormat.withdraw"));
						event.setLine(2, event.getLine(2));
						event.setLine(3, "");
						money.getConfigurationHandler().printMessage(p, "chatMessages.createdSignSuccessfully", "0", p, p.getName());
						money.getSoundHandler().sendLevelUpSound(p);
						p.playEffect(event.getBlock().getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
						return;
					}
					p.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + ">> " + ChatColor.RED + "Trên dòng 3 hãy nhập vào một số, với format: 1 or 1.0 or 1.00");
					return;
				} else {
					money.getConfigurationHandler().printMessage(p, "chatMessages.errorWhileCreatingSign", "0", p, p.getName());
					p.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + ">> " + ChatColor.RED + "Các lựa chọn cho dòng 2: balance, deposit, withdraw");
					money.getSoundHandler().sendItemBreakSound(p);
					return;
				}
			}
			money.getConfigurationHandler().printMessage(p, "chatMessages.notAllowed", "0", p, p.getName());
			event.setLine(0, "NoPermission");
			money.getSoundHandler().sendPlingSound(p);
			return;
		}

	}
	
	@EventHandler
	public void onSignRemove(BlockBreakEvent event){
		Player p = event.getPlayer();
		Block testblock = event.getBlock();			
			if (isSign(testblock.getType())) {
					Sign sign = (Sign) testblock.getState();
					if (sign.getLine(0).contains(money.getConfigurationHandler().getString("signFormat.signColor") + ChatColor.BOLD + "[Bank]")) {
						if (Main.perms.has(p, "EcoSystem.admin")) {
							if (p.isSneaking()) {
								money.getConfigurationHandler().printMessage(p, "chatMessages.removedSignSuccessfully", "0", p, p.getName());
								money.getSoundHandler().sendItemBreakSound(p);
								return;
							}
							event.setCancelled(true);
							p.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + ">> " + ChatColor.RED + "Ngồi để phá sign!");
							money.getSoundHandler().sendPlingSound(p);
							return;
						} else {
							money.getConfigurationHandler().printMessage(p, "chatMessages.notAllowed", "0", p, p.getName());
							event.setCancelled(true);
							money.getSoundHandler().sendPlingSound(p);
							return;
							
						}
	                }
				}
			}

}

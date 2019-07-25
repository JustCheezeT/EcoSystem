package net.minecraftvn.InfiGamez;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {
	
	private Main money;
	
	public CommandHandler(Main money) {
		this.money = money;
	}
	
	public boolean onCommand(final CommandSender sender, final Command command, final String cmdlabel, final String[] args) {
		Player p;
		if (cmdlabel.equalsIgnoreCase("bank")) {
			if (args.length == 0) {
				if (sender instanceof Player) {
					p = (Player) sender;
					sendHelp(p);
					return true;
				} else {
					sendConsoleHelp(sender);
					return false;
				}
			} else 	if (args.length == 1) {
				if (args[0].equalsIgnoreCase("reload")) {
					money.getReloadCmd().runCmd(sender);
				} else if (args[0].equalsIgnoreCase("help")) {
					if (sender instanceof Player) {
						p = (Player) sender;
						sendHelp(p);
						return true;
					} else {
						sendConsoleHelp(sender);
						return false;
					}
				} else if (args[0].equalsIgnoreCase("balance")) {
					money.getBalanceCmd().runUserCmd(sender);
				} else if (args[0].equalsIgnoreCase("interest")) {
					money.getInterestCmd().runUserCmd(sender);
				} else {
					if (sender instanceof Player) {
						p = (Player) sender;
						sendHelp(p);
						return false;
					} else {
						sendConsoleHelp(sender);
						return false;
					}
				}
			} else if (args.length == 2) {
				if (args[0].equalsIgnoreCase("balance")) {
					money.getBalanceCmd().runAdminCmd(sender, args);
				} else if (args[0].equalsIgnoreCase("deposit")) {
					money.getDepositCmd().runUserCmd(sender, args);
				} else if (args[0].equalsIgnoreCase("withdraw")) {
					money.getWithdrawCmd().runUserCmd(sender, args);
				} else {
					if (sender instanceof Player) {
						p = (Player) sender;
						sendHelp(p);
						return true;
					} else {
						sendConsoleHelp(sender);
						return false;
					}
				}
			} else if (args.length == 3) {
				if (args[0].equalsIgnoreCase("set")) {
					money.getSetCmd().runCmd(sender, args);
				} else if (args[0].equalsIgnoreCase("deposit")) {
					money.getDepositCmd().runAdminCmd(sender, args);
				} else if (args[0].equalsIgnoreCase("withdraw")) {
					money.getWithdrawCmd().runAdminCmd(sender, args);
				} else {
					if (sender instanceof Player) {
						p = (Player) sender;
						sendHelp(p);
						return true;
					} else {
						sendConsoleHelp(sender);
						return false;
					}
				}
			}
		}
		
		return false;
	}
	
	public void sendHelp(Player p) {
		p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0F, 1.0F);
		p.sendMessage(" ");
		p.sendMessage(ChatColor.DARK_AQUA + "-=-=-=-=-=-=-=-< " + ChatColor.AQUA + "" + ChatColor.BOLD + "EcoSystem" + ChatColor.DARK_AQUA + " >-=-=-=-=-=-=-=-=-");
		if (p.hasPermission("EcoSystem.admin")) {
			p.sendMessage(" ");
			p.sendMessage(ChatColor.AQUA + "        Kiểm tra số dư của người chơi khác:");
			p.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + ">> " + ChatColor.WHITE + "/bank balance <tên người chơi>" + ChatColor.GRAY + " - cho người chơi Online.");
			p.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + ">> " + ChatColor.WHITE + "/bank balance <UUID người chơi>" + ChatColor.GRAY + " - cho người chơi Offline.");
		
			p.sendMessage(" ");
			p.sendMessage(ChatColor.AQUA + "        Đặt số dư của người chơi khác:");
			p.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + ">> " + ChatColor.WHITE + "/bank set <tên người chơi> amount" + ChatColor.GRAY + " - cho người chơi Online.");
			p.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + ">> " + ChatColor.WHITE + "/bank set <UUID người chơi> amount" + ChatColor.GRAY + " - cho người chơi Offline.");
			p.sendMessage(" ");
			
			p.sendMessage(ChatColor.AQUA + "        Tải lại config:");
			p.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + ">> " + ChatColor.WHITE + "/bank reload");
			p.sendMessage(" ");
			
			p.sendMessage(ChatColor.DARK_AQUA + "-=-=-=-=-=-=-=-=-< " + ChatColor.AQUA + "" + ChatColor.BOLD + "Trang cho Admin" + ChatColor.DARK_AQUA + " >-=-=-=-=-=-=-=-=-");
			p.sendMessage(" ");
		} else if (p.hasPermission("EcoSystem.balance") || p.hasPermission("EcoSystem.deposit") || p.hasPermission("EcoSystem.withdraw") || p.hasPermission("EcoSystem.admin")) {
			p.sendMessage(" ");
			p.sendMessage(ChatColor.AQUA + "        Kiểm tra số dư của bạn:");
			p.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + ">> " + ChatColor.WHITE + "/bank balance");
			p.sendMessage(ChatColor.AQUA + "        Gửi tiền vào ngân hàng:");
			p.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + ">> " + ChatColor.WHITE + "/bank deposit <amount>");
			p.sendMessage(ChatColor.AQUA + "        Nhận lại tiền từ ngân hàng:");
			p.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + ">> " + ChatColor.WHITE + "/bank withdraw <amount>");
			p.sendMessage(" ");
		} else {
			p.sendMessage(" ");
			p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Sign");
			p.sendMessage(" ");
			p.sendMessage(ChatColor.DARK_AQUA + "-=-=-=-=-=-=-=-=-=-< " + ChatColor.AQUA + "" + ChatColor.BOLD + "Trang Trợ Giúp" + ChatColor.DARK_AQUA + " >-=-=-=-=-=-=-=-=-=-=-");
			p.sendMessage(" ");
		}
	}
	
	public void sendConsoleHelp(CommandSender sender) {
		sender.sendMessage(" ");
		sender.sendMessage(ChatColor.DARK_AQUA + "-=-=-=-=-=-=-=-< " + ChatColor.AQUA + "" + ChatColor.BOLD + "EcoSystem" + ChatColor.DARK_AQUA + " >-=-=-=-=-=-=-=-=-=-");
			sender.sendMessage(" ");
			sender.sendMessage(ChatColor.AQUA + "        Kiểm tra số dư của người chơi khác:");
			sender.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + ">> " + ChatColor.WHITE + "/bank balance <tên người chơi>" + ChatColor.GRAY + " - cho người chơi Online.");
			sender.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + ">> " + ChatColor.WHITE + "/bank balance <UUID người chơi>" + ChatColor.GRAY + " - cho người chơi Offline.");
		
			sender.sendMessage(" ");
			sender.sendMessage(ChatColor.AQUA + "        Đặt số dư của người chơi khác:");
			sender.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + ">> " + ChatColor.WHITE + "/bank set <tên người chơi> amount" + ChatColor.GRAY + " - cho người chơi Online.");
			sender.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + ">> " + ChatColor.WHITE + "/bank set <UUID người chơi> amount" + ChatColor.GRAY + " - cho người chơi Offline.");
			sender.sendMessage(" ");
			
			sender.sendMessage(ChatColor.AQUA + "        Tải lại config:");
			sender.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + ">> " + ChatColor.WHITE + "/bank reload");
			sender.sendMessage(" ");
			sender.sendMessage(ChatColor.DARK_AQUA + "-=-=-=-=-=-=-=-=-< " + ChatColor.AQUA + "" + ChatColor.BOLD + "Trang cho Console" + ChatColor.DARK_AQUA + " >-=-=-=-=-=-=-=-=-");
			sender.sendMessage(" ");
	}

}

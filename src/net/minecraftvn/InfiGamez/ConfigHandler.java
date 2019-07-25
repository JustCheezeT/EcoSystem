package net.minecraftvn.InfiGamez;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ConfigHandler {
	
	private Main money;

	public ConfigHandler(Main money) {
		this.money = money;
		loadConfig();
	}
	
	public void loadConfig() {
		File pluginFolder = new File("plugins" + System.getProperty("file.separator") + money.pluginName);
		if (pluginFolder.exists() == false) {
    		pluginFolder.mkdir();
    	}
		
		File configFile = new File("plugins" + System.getProperty("file.separator") + money.pluginName + System.getProperty("file.separator") + "config.yml");
		if (configFile.exists() == false) {
    		Main.log.info("Đang tạo file config mới...");
    		money.saveDefaultConfig();
		}
    	
    	try {
    		Main.log.info("Đang load file config...");
    		money.getConfig().load(configFile);
    	} catch (Exception e) {
    		Main.log.severe("Không thể load file config! Hãy tải lại config! Lỗi: " + e.getMessage());
			e.printStackTrace();
    	}
	}
	
	public String getString(String key) {
		if (!money.getConfig().contains(key)) {
			money.getLogger().severe("Không thể định vị " + key + " trong file config.yml trong folder " + money.pluginName + "! (Thử xóa file tải lại xem :v)");
			return "errorCouldNotLocateInConfigYml:" + key;
		} else {
			return money.getConfig().getString(key);
		}
	}
	
	public String getStringWithColor(String key) {
		if (!money.getConfig().contains(key)) {
			money.getLogger().severe("Không thể định vị " + key + " trong file config.yml trong folder " + money.pluginName + "! (Thử xóa file tải lại xem :v)");
			return "errorCouldNotLocateInConfigYml:" + key;
		} else {
			return money.getConfig().getString(key).replaceAll("&", "§");
		}
	}
	
	public List<String> getStringList(String key) {
		if (!money.getConfig().contains(key)) {
			money.getLogger().severe("Không thể định vị " + key + " trong file config.yml trong folder " + money.pluginName + "! (Thử xóa file tải lại xem :v)");
			return null;
		} else {
			return money.getConfig().getStringList(key);
		}
	}
	
	public Integer getInteger(String key) {
		if (!money.getConfig().contains(key)) {
			money.getLogger().severe("Không thể định vị " + key + " trong file config.yml trong folder " + money.pluginName + "! (Thử xóa file tải lại xem :v)");
			return null;
		} else {
			return money.getConfig().getInt(key);
		}
	}
	
	public Boolean getBoolean(String key) {
		if (!money.getConfig().contains(key)) {
			money.getLogger().severe("Không thể định vị " + key + " trong file config.yml trong folder " + money.pluginName + "! (Thử xóa file tải lại xem :v)");
			return null;
		} else {
			return money.getConfig().getBoolean(key);
		}
	}
	
	public void printMessage(Player player, String messageKey, String amount, Player player2, String player2Name) {
		if (money.getConfig().contains(messageKey)) {
			List<String> message = new ArrayList<String>();
			message.add(money.getConfig().getString(messageKey));

			if (getString(messageKey).equals("")) {
				return;
			}
			
			if (player2 != null) {
				if (!player2.equals("")) {
					message.set(0, message.get(0).replaceAll("%player2", player2Name));
				}
			}
			
			DecimalFormat f = new DecimalFormat("#,##0.00");
			
			if (amount != null && !amount.equals("")) {
				Double amountDouble = Double.parseDouble(amount);
				if (amountDouble.toString().endsWith(".0")) {
					DecimalFormat fr = new DecimalFormat("#,##0");
					message.set(0, message.get(0).replaceAll("%amount", "" + fr.format(amountDouble)));
				} else {
				message.set(0, message.get(0).replaceAll("%amount", "" + f.format(amountDouble)));
				}
			}

			message.set(0, message.get(0).replaceAll("%pocket", ""+Main.econ.getBalance(player)));
			
			if (money.getMoneyDatabaseInterface().hasAccount(player2)) 
			{
				if (money.getMoneyDatabaseInterface().getBalance(player2).toString().endsWith(".0")) {
					DecimalFormat fr = new DecimalFormat("#,##0");
					message.set(0, message.get(0).replaceAll("%balance", "" + fr.format(money.getMoneyDatabaseInterface().getBalance(player2))));
				} else 
				message.set(0, message.get(0).replaceAll("%balance", "" + f.format(money.getMoneyDatabaseInterface().getBalance(player2))));
				} else {
				message.set(0, message.get(0).replaceAll("%balance", "0.00"));
				}
				
			message.set(0, message.get(0).replaceAll("%player", player.getName()));
			
			if (player != null) {				
				//Message format
				player.sendMessage(parseFormattingCodes(getString("chatMessages.prefix")) + parseFormattingCodes(message.get(0)));
				for (int i = 1; i < message.size(); i++) {
					player.sendMessage(parseFormattingCodes(message.get(i)));
				}
			}
			
		} else {
			money.getLogger().severe("Không thể định vị '" + messageKey + "' trong file config.yml trong folder EcoSystem!");
			player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + ">> " + ChatColor.RED + "Không thể định vị '" + messageKey + "' trong file config.yml trong folder EcoSystem!");
		}
	}

	public String parseFormattingCodes(String message) {
		message = message.replaceAll("&0", ChatColor.BLACK + "");
		message = message.replaceAll("&1", ChatColor.DARK_BLUE + "");
		message = message.replaceAll("&2", ChatColor.DARK_GREEN + "");
		message = message.replaceAll("&3", ChatColor.DARK_AQUA + "");
		message = message.replaceAll("&4", ChatColor.DARK_RED + "");
		message = message.replaceAll("&5", ChatColor.DARK_PURPLE + "");
		message = message.replaceAll("&6", ChatColor.GOLD + "");
		message = message.replaceAll("&7", ChatColor.GRAY + "");
		message = message.replaceAll("&8", ChatColor.DARK_GRAY + "");
		message = message.replaceAll("&9", ChatColor.BLUE + "");
		message = message.replaceAll("&a", ChatColor.GREEN + "");
		message = message.replaceAll("&b", ChatColor.AQUA + "");
		message = message.replaceAll("&c", ChatColor.RED + "");
		message = message.replaceAll("&d", ChatColor.LIGHT_PURPLE + "");
		message = message.replaceAll("&e", ChatColor.YELLOW + "");
		message = message.replaceAll("&f", ChatColor.WHITE + "");
		message = message.replaceAll("&l", ChatColor.BOLD + "");
		message = message.replaceAll("&o", ChatColor.ITALIC + "");
		message = message.replaceAll("&m", ChatColor.STRIKETHROUGH + "");
		message = message.replaceAll("&n", ChatColor.UNDERLINE + "");
		message = message.replaceAll("&k", ChatColor.MAGIC + "");
		message = message.replaceAll("&r", ChatColor.RESET + "");
		return message;
	}
	
	public void actionBarMessage(Player player, String messageKey) {
		if (money.getConfig().contains(messageKey)) {
			List<String> message = new ArrayList<String>();
			message.add(money.getConfig().getString(messageKey));
			
			if (message.get(0) == "") {
				return;
			}

			DecimalFormat f = new DecimalFormat("#,##0.00");
			if (money.getMoneyDatabaseInterface().getBalance(player).toString().endsWith(".0")) {
				DecimalFormat fr = new DecimalFormat("#,##0");
				message.set(0, message.get(0).replaceAll("%bankBalance", "" + fr.format(money.getMoneyDatabaseInterface().getBalance(player))));
			} else {
				message.set(0, message.get(0).replaceAll("%bankBalance", "" + f.format(money.getMoneyDatabaseInterface().getBalance(player))));
			}
			
			if (Double.toString(Main.econ.getBalance(player)).endsWith(".0")) {
				DecimalFormat fr = new DecimalFormat("#,##0");
				message.set(0, message.get(0).replaceAll("%pocketBalance", "" + fr.format(Main.econ.getBalance(player))));
			} else {
				message.set(0, message.get(0).replaceAll("%pocketBalance", "" + f.format(Main.econ.getBalance(player))));
			}
			if (player != null) {
				player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message.get(0).replaceAll("&", "§")));
				return;
			}
			
		} else {
			money.getLogger().severe("Không thể định vị '" + messageKey + "' trong file config.yml trong folder EcoSystem!");
			player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + ">> " + ChatColor.RED + "Không thể định vị '" + messageKey + "' trong file config.yml trong folder EcoSystem!");
			return;
		}
		
	}

}

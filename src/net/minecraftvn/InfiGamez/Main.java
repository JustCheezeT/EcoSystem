package net.minecraftvn.InfiGamez;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import net.minecraftvn.InfiGamez.commands.BalanceCmd;
import net.minecraftvn.InfiGamez.commands.DepositCmd;
import net.minecraftvn.InfiGamez.commands.InterestCmd;
import net.minecraftvn.InfiGamez.commands.ReloadCmd;
import net.minecraftvn.InfiGamez.commands.SetCmd;
import net.minecraftvn.InfiGamez.commands.WithdrawCmd;
import net.minecraftvn.InfiGamez.database.AccountDatabaseInterface;
import net.minecraftvn.InfiGamez.database.DatabaseManagerFlatFile;
import net.minecraftvn.InfiGamez.database.DatabaseManagerInterface;
import net.minecraftvn.InfiGamez.database.DatabaseManagerMysql;
import net.minecraftvn.InfiGamez.database.MoneyFlatFileInterface;
import net.minecraftvn.InfiGamez.database.MoneyMysqlInterface;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
	
	public static Logger log;
	public static Economy econ = null;
	public static Permission perms = null;
	public String pluginName = "EcoSystem";
	public Set<UUID> cooldown = new HashSet<UUID>();
	
	private static ConfigHandler cH;
	private DatabaseManagerInterface databaseManager;
	private AccountDatabaseInterface<Double> moneyDatabaseInterface;
	private boolean enabled = false;
	private static SoundHandler sH;
	private static ReloadCmd rCmd;
	private static BalanceCmd bCmd;
	private static SetCmd sCmd;
	private static DepositCmd dCmd;
	private static WithdrawCmd wCmd;
	private static InterestHandler iH;
	private static InterestCmd iCmd;
	
	@Override
    public void onEnable(){
    	log = getLogger();
    	
        if (!setupEconomy() ) {
            log.severe(ChatColor.RED + "Phát hiện thiếu plugin Vault! Vui lòng tải Vault để Plugin có thể hoạt động");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        setupPermissions();
        
    	if (setupTitleManager() == false) {
        	log.warning(ChatColor.YELLOW + "Không có TitleManager, đã vô hiệu hóa title.");
        } else {
        	log.info("Đã hook với TitleManager!");
        }
    	
        cH = new ConfigHandler(this);
        sH = new SoundHandler(this);
        
        if (cH.getString("database.typeOfDatabase").equalsIgnoreCase("mysql")) {
        	log.info("Đang sử dụng MySQL...");
        	databaseManager = new DatabaseManagerMysql(this);
        	moneyDatabaseInterface = new MoneyMysqlInterface(this);
        } else {
        	if (!new File("plugins"+System.getProperty("file.separator")+"EcoSystem"+System.getProperty("file.separator")+"Accounts").exists()) {
        		(new File("plugins"+System.getProperty("file.separator")+"EcoSystem"+System.getProperty("file.separator")+"Accounts")).mkdir();
        	}
        	log.info(pluginName + " đã load thành công!");
        	databaseManager = new DatabaseManagerFlatFile(this);
        	moneyDatabaseInterface = new MoneyFlatFileInterface(this);
        }
        rCmd = new ReloadCmd(this);
        bCmd = new BalanceCmd(this);
        sCmd = new SetCmd(this);
        dCmd = new DepositCmd(this);
        wCmd = new WithdrawCmd(this);
        iH = new InterestHandler(this);
        iCmd = new InterestCmd(this);

    	PluginManager pm = getServer().getPluginManager();
    	pm.registerEvents(new PlayerListener(this), this);
    	CommandHandler cH = new CommandHandler(this);
    	getCommand("bank").setExecutor(cH);
    	enabled = true;
    	log.info(pluginName + "đã load thành công!");
	}
	
	@Override
	public void onDisable() {
		if (enabled == true) {
			Bukkit.getScheduler().cancelTasks(this);
			HandlerList.unregisterAll(this);
			if (databaseManager.getConnection() != null) {
				log.info("Đang đóng kết nối MySQL...");
				databaseManager.closeDatabase();
			}
		}
		log.info(pluginName + " đã vô hiệu hóa!");
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        log.info("Đang sử dụng Plugin kinh tế: " + rsp.getProvider().getName());
        return econ != null;
    }
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        log.info("Đang sử dụng Plugin permission: " + rsp.getProvider().getName());
        return perms != null;
    }

    public boolean setupTitleManager() {
    	if (getServer().getPluginManager().getPlugin("TitleManager") != null) {
        	return true;
        }
          else {
        	  return false;        	  
          }
    }
    
    public AccountDatabaseInterface<Double> getMoneyDatabaseInterface() {
    	return moneyDatabaseInterface;
    }
    public ConfigHandler getConfigurationHandler() {
		return cH;
	}
    public DatabaseManagerInterface getDatabaseManagerInterface() {
		return databaseManager;
	}
    public SoundHandler getSoundHandler() {
    	return sH;
    }
    public ReloadCmd getReloadCmd() {
    	return rCmd;
    }
    public BalanceCmd getBalanceCmd() {
    	return bCmd;
    }
    public SetCmd getSetCmd() {
    	return sCmd;
    }
    public DepositCmd getDepositCmd() {
    	return dCmd;
    }
    public WithdrawCmd getWithdrawCmd() {
    	return wCmd;
    }
    public InterestHandler getInterestHandler() {
    	return iH;
    }
    public InterestCmd getInterestCmd() {
    	return iCmd;
    }

}

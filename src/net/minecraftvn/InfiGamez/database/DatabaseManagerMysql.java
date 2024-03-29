package net.minecraftvn.InfiGamez.database;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import net.minecraftvn.InfiGamez.Main;

public class DatabaseManagerMysql implements DatabaseManagerInterface{
	
	private Connection conn = null;
	private Main money;
	
	public DatabaseManagerMysql(Main money) {
		this.money = money;
		connectToDatabase();
		setupDatabase();
	}
	
	private void connectToDatabase() {
		Main.log.info("Connecting to the database...");
		try {
            Class.forName("com.mysql.jdbc.Driver");
            Properties properties = new Properties();
            properties.setProperty("user", money.getConfigurationHandler().getString("database.mysql.user"));
            properties.setProperty("password", money.getConfigurationHandler().getString("database.mysql.password"));
            properties.setProperty("autoReconnect", "true");
            properties.setProperty("verifyServerCertificate", "false");
            properties.setProperty("useSSL", money.getConfigurationHandler().getString("database.mysql.ssl"));
            properties.setProperty("requireSSL", money.getConfigurationHandler().getString("database.mysql.ssl"));

            conn = DriverManager.getConnection("jdbc:mysql://" + money.getConfigurationHandler().getString("database.mysql.host") + ":" + money.getConfigurationHandler().getString("database.mysql.port") + "/" + money.getConfigurationHandler().getString("database.mysql.databaseName"), properties);
           
          } catch (ClassNotFoundException e) {
        	  Main.log.severe("Could not locate drivers for mysql! Error: " + e.getMessage());
            return;
          } catch (SQLException e) {
        	  Main.log.severe("Could not connect to mysql database! Error: " + e.getMessage());
            return;
          }
		Main.log.info("Database connection successful!");
	}
	
	@Override
	public boolean setupDatabase() {
		PreparedStatement query = null;
	      try {	        
	        String data = "CREATE TABLE IF NOT EXISTS `" + money.getConfigurationHandler().getString("database.mysql.tableName") + "` (id int(10) AUTO_INCREMENT, player_uuid varchar(50) NOT NULL UNIQUE, player_name varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL, money double(30,2) NOT NULL, last_seen varchar(30) NOT NULL, sync_complete varchar(5) NOT NULL, PRIMARY KEY(id));";
	        query = conn.prepareStatement(data);
	        query.execute();
	      } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	      } finally {
	    	  try {
	    		  if (query != null) {
	    			  query.close();
	    		  }
	    	  } catch (Exception e) {
	    		  e.printStackTrace();
	    	  }
	      }
	      
	      updateTables();
		return true;
	}
	
	@Override
	public Connection getConnection() {
		checkConnection();
		return conn;
	}
	
	public boolean checkConnection() {
		try {
			if (conn == null) {
				Main.log.warning("Connection failed. Reconnecting...");
				if (reConnect() == true) return true;
				return false;
			}
			if (!conn.isValid(3)) {
				Main.log.warning("Connection is idle or terminated. Reconnecting...");
				if (reConnect() == true) return true;
				return false;
			}
			if (conn.isClosed() == true) {
				Main.log.warning("Connection is closed. Reconnecting...");
				if (reConnect() == true) return true;
				return false;
			}
			return true;
		} catch (Exception e) {
			Main.log.severe("Could not reconnect to Database!");
		}
		return true;
	}
	
	public boolean reConnect() {
		try {            
            long start = 0;
			long end = 0;
			
		    start = System.currentTimeMillis();
		    Main.log.info("Attempting to establish a connection to the MySQL server!");
		    Class.forName("com.mysql.jdbc.Driver");
		    Properties properties = new Properties();
		    properties.setProperty("user", money.getConfigurationHandler().getString("database.mysql.user"));
            properties.setProperty("password", money.getConfigurationHandler().getString("database.mysql.password"));
            properties.setProperty("autoReconnect", "true");
            properties.setProperty("verifyServerCertificate", "false");
            properties.setProperty("useSSL", money.getConfigurationHandler().getString("database.mysql.ssl"));
            properties.setProperty("requireSSL", money.getConfigurationHandler().getString("database.mysql.ssl"));
		    conn = DriverManager.getConnection("jdbc:mysql://" + money.getConfigurationHandler().getString("database.mysql.host") + ":" + money.getConfigurationHandler().getString("database.mysql.port") + "/" + money.getConfigurationHandler().getString("database.mysql.databaseName"), properties);
		    end = System.currentTimeMillis();
		    Main.log.info("Connection to MySQL server established!");
		    Main.log.info("Connection took " + ((end - start)) + "ms!");
            return true;
		} catch (Exception e) {
			Main.log.severe("Could not connect to MySQL server! because: " + e.getMessage());
			return false;
		}
	}
	
	@Override
	public boolean closeDatabase() {
		try {
			conn.close();
			conn = null;
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private void updateTables() {
		if (conn != null) {
			DatabaseMetaData md = null;
	    	ResultSet rs1 = null;
	    	ResultSet rs2 = null;
	    	ResultSet rs3 = null;
	    	PreparedStatement query1 = null;
	    	PreparedStatement query2 = null;
	    	PreparedStatement query3 = null;
	    	try {
	    		md = conn.getMetaData();
	            rs1 = md.getColumns(null, null, money.getConfigurationHandler().getString("database.mysql.tableName"), "sync_complete");
	            if (rs1.next()) {
			    } else {
			        String data = "ALTER TABLE `" + money.getConfigurationHandler().getString("database.mysql.tableName") + "` ADD sync_complete varchar(5) NOT NULL DEFAULT 'true';";
			        query1 = conn.prepareStatement(data);
			        query1.execute();
			    }
	            rs2 = md.getColumns(null, null, money.getConfigurationHandler().getString("database.mysql.tableName"), "player_name");
	            if (rs2.next()) {
			    } else {
			        String data = "ALTER TABLE `" + money.getConfigurationHandler().getString("database.mysql.tableName") + "` ADD player_name varchar(50) NOT NULL DEFAULT 'true';";
			        query2 = conn.prepareStatement(data);
			        query2.execute();
			    }
	            rs3 = md.getColumns(null, null, money.getConfigurationHandler().getString("database.mysql.tableName"), "last_seen");
	            if (rs3.next()) {
			    } else {
			        String data = "ALTER TABLE `" + money.getConfigurationHandler().getString("database.mysql.tableName") + "` ADD last_seen varchar(30) NOT NULL DEFAULT 'true';";
			        query3 = conn.prepareStatement(data);
			        query3.execute();
			    }
	    	} catch (Exception e) {
	    		Main.log.severe("Error updating inventory table! Error: " + e.getMessage());
    			e.printStackTrace();
	    	} finally {
	    		try {
	    			if (query1 != null) {
	    				query1.close();
	    			}
	    			if (query2 != null) {
	    				query2.close();
	    			}
	    			if (query3 != null) {
	    				query3.close();
	    			}
	    			if (rs1 != null) {
	    				rs1.close();
	    			}
	    			if (rs2 != null) {
	    				rs2.close();
	    			}
	    			if (rs3 != null) {
	    				rs3.close();
	    			}
	    		} catch (Exception e) {
	    			e.printStackTrace();
	    		}
	    	}
		}
	}

}

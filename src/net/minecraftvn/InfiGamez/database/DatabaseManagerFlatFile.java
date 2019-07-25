package net.minecraftvn.InfiGamez.database;

import java.sql.Connection;

import net.minecraftvn.InfiGamez.Main;

public class DatabaseManagerFlatFile implements DatabaseManagerInterface {
	
	@SuppressWarnings("unused")
	private Main money;

	public DatabaseManagerFlatFile(Main money) {
		this.money = money;
		
		setupDatabase();
	}

	@Override
	public boolean setupDatabase() {
		return true;
	}

	@Override
	public boolean closeDatabase() {
		return true;
	}
	
	@Override
	public Connection getConnection() {
		return null;
	}

}

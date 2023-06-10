package me.MeStorage.Utils;

public enum menus {
	MAIN("Main"),
	
	DRIVES("Drives"),
	
	SERVERS("Servers");

	public String type;
	menus(String string) {
		type = string;
	}
}

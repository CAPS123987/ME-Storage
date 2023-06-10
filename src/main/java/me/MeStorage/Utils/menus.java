package me.MeStorage.Utils;

public enum menus {
	MAIN("Main"),
	
	DISKS("Disks"),
	
	SERVERS("Servers");

	public String type;
	menus(String string) {
		type = string;
	}
}

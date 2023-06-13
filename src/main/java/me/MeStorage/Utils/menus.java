package me.MeStorage.Utils;

public enum menus {
	MAIN("Main"),
	
	DISKS("Disks"),
	
	SERVERS("Servers"),
	
	EXPORT("Export"),
	
	SEARCH("Search");

	public String type;
	menus(String string) {
		type = string;
	}
}

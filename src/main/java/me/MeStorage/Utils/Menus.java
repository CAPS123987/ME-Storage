package me.MeStorage.Utils;

public enum Menus {
	MAIN("Main"),
	
	DISKS("Disks"),
	
	SERVERS("Servers"),
	
	EXPORT("Export"),
	
	SEARCH("Search");

	public String type;
	Menus(String string) {
		type = string;
	}
}

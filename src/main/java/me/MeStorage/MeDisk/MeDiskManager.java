package me.MeStorage.MeDisk;

import java.util.HashMap;

import java.util.Map;import me.MeStorage.MEStorage.MeStorage;

public class MeDiskManager {
	protected Map<Integer,MeDisk> networks = new HashMap<Integer,MeDisk>();
	
	public MeDiskManager(Map<Integer,MeDisk> networks) {
		this.networks = networks;
	}
	public Map<Integer,MeDisk> getDisks(){
		return networks;
	}
	public void addDisk(int i,MeDisk disk) {
		networks.put(i, disk);
		MeStorage.saveDisks();
	}
	public void removeDisk(int i) {
		networks.remove(i);
		MeStorage.saveDisks();
	}
}

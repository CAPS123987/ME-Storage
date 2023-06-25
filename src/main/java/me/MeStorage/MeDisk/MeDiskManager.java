package me.MeStorage.MeDisk;

import java.util.HashMap;

import java.util.Map;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import me.MeStorage.MEStorage.MeStorage;
import me.MeStorage.Utils.MeItemUtils;

public class MeDiskManager {
	protected Map<Integer,MeDisk> networks = new HashMap<Integer,MeDisk>();
	
	public MeDiskManager(Map<Integer,MeDisk> networks) {
		this.networks = networks;
	}
	public Map<Integer,MeDisk> getDisks(){
		return networks;
	}
	public int createDisc(SlimefunItem item) {
		int Final = 1;
		for(int id = 1;networks.containsKey(id);id++) {
			Final++;
		}
		MeDisk disk = new MeDisk();
		int capacity = MeItemUtils.getCapacity(item);
		disk.setCapacity(capacity);
		addDisk(Final,disk);
		
		return Final;
	}
	
	private void addDisk(int i,MeDisk disk) {
		networks.put(i, disk);
		MeStorage.saveDisks();
	}
	public void removeDisk(int i) {
		networks.remove(i);
		MeStorage.saveDisks();
	}
}

package me.MeStorage.MeDisk;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import me.MeStorage.MEStorage.MeStorage;
import me.MeStorage.Utils.MeItemUtils;

public class MeDiskManager {
	protected Map<Integer,MeDisk> disks = new HashMap<Integer,MeDisk>();
	protected int diskCount;
	
    FileConfiguration cfg = MeStorage.instance.getConfig();
	
	public MeDiskManager(int diskcount) {
		diskCount=diskcount;
	}
	
	public Map<Integer,MeDisk> getDisks(){
		return disks;
	}
	
	public int getDiskCount() {
		return diskCount;
	}
	
	public MeDisk getDisk(int id){
		if(disks.containsKey(id)) {
			return disks.get(id);
		}
		File file = new File(MeStorage.instance.getDataFolder(),id+".yml");
		if(file.exists()) {
			FileConfiguration yaml=YamlConfiguration.loadConfiguration(file);
			MeDisk disk = (MeDisk) yaml.get("disk");
			disks.put(id, disk);
			return disk;
		}else {
			
			MeDisk disk = createDiskFile(id);
			disks.put(id, disk);
			return disk;
		}
	}
	
	public int createDisc(SlimefunItem item) {
		diskCount++;
		MeDisk disk = new MeDisk();
		int capacity = MeItemUtils.getCapacity(item);
		disk.setCapacity(capacity);
		addDisk(diskCount,disk);
		
		return diskCount;
	}
	
	private void addDisk(int i,MeDisk disk) {
		disks.put(i, createDiskFile(i,disk));
		MeStorage.saveDisks();
	}
	
	private MeDisk createDiskFile(int id){
		return createDiskFile(id,new MeDisk());
	}
	
	private MeDisk createDiskFile(int id,MeDisk disk){
		File file = new File(MeStorage.instance.getDataFolder(),id+".yml");
		try {file.createNewFile();} catch (IOException e1) {e1.printStackTrace();}
		FileConfiguration yaml=YamlConfiguration.loadConfiguration(file);
		yaml.set("disk", disk);
		try {yaml.save(file);} catch (IOException e) {e.printStackTrace();}
		return (MeDisk) yaml.get("disk");
	}
}

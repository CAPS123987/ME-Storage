package me.CAPS123987.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.CAPS123987.MEStorage.MeStorage;

public interface ItemUtils {
	FileConfiguration cfg = MeStorage.instance.getConfig();
	
	
	default boolean saveItem(int disc,ItemStack item) {
		File file = new File(MeStorage.instance.getDataFolder(),String.valueOf(disc)+".yml") ;
		FileConfiguration yaml=YamlConfiguration.loadConfiguration(file);
		
		return saveItem(file,yaml,item);
	}
	
	default boolean saveItem(File file,FileConfiguration yaml,ItemStack item) {
		int max = Integer.parseInt(getMax(yaml))-1;
		int maxCapacity = yaml.getInt("maxCapacity");
		int curentCapacity = yaml.getInt("curentCapacity")+item.getAmount();
		
		if(curentCapacity<=maxCapacity) {
			for(int i = 0; max>0;max--) {
				
				ItemStack oldItem = (ItemStack)yaml.get(String.valueOf(max));
				
				if(oldItem!=null) {
					
					if(oldItem.isSimilar(item)) {
						if(oldItem.getAmount()!=oldItem.getMaxStackSize()) {
							int tempMax = oldItem.getMaxStackSize()-oldItem.getAmount();
							if(tempMax<=item.getAmount()) {
								item.setAmount(item.getAmount()-tempMax);
								oldItem.setAmount(oldItem.getAmount()+tempMax);
								yaml.set(String.valueOf(max), oldItem);
							}else {
								oldItem.setAmount(oldItem.getAmount()+item.getAmount());
								yaml.set(String.valueOf(max), oldItem);
								
								item.setAmount(0);
								break;
							}
						}
					}
				}
			}
			if(item.getAmount()>0) {
				yaml.set(getMax(yaml),item);
				addMax(yaml);
			}
			yaml.set("curentCapacity",curentCapacity);
			saveFile(file, yaml);
			return true;
		}
		return false;
	}
	
	default ItemStack loadItem(int disc,int item) {
		File file = new File(MeStorage.instance.getDataFolder(),String.valueOf(disc)+".yml") ;
		FileConfiguration yaml=YamlConfiguration.loadConfiguration(file);
		
		if(yaml.contains(String.valueOf(item))) {
			return (ItemStack)yaml.get(String.valueOf(item));
		}else {
			return new ItemStack(Material.AIR);
		}
		
	}
	
	default void removeItem(int disc,ItemStack item) {
		File file = new File(MeStorage.instance.getDataFolder(),String.valueOf(disc)+".yml") ;
		FileConfiguration yaml=YamlConfiguration.loadConfiguration(file);
		int max = Integer.parseInt(getMax(disc))-1;
		for(; max<1;max--) {
			ItemStack oldItem = (ItemStack)yaml.get(String.valueOf(max));
			if(oldItem.isSimilar(item)) {
				yaml.set(String.valueOf(max), null);
				break;
			}
		}
		
	}
	
	default void removeItem(int disc,int amount,ItemStack item) {
		File file = new File(MeStorage.instance.getDataFolder(),String.valueOf(disc)+".yml") ;
		FileConfiguration yaml=YamlConfiguration.loadConfiguration(file);
		int max = Integer.parseInt(getMax(disc))-1;
		for(; max<1;max--) {
			ItemStack oldItem = (ItemStack)yaml.get(String.valueOf(max));
			
			if(oldItem.isSimilar(item)) {
				if(oldItem.getAmount()>=amount) {
					yaml.set(String.valueOf(max), null);
					break;
				}
			}
		}
		
	}
	
	
	default void createDisc(int disc,int capacity) {
		File file = new File(MeStorage.instance.getDataFolder(),String.valueOf(disc)+".yml");
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			FileConfiguration yaml=YamlConfiguration.loadConfiguration(file);
			yaml.set("max", 1);
			yaml.set("maxCapacity", capacity);
			yaml.set("curentCapacity", 0);
			saveFile(file,yaml);
		}
		//saveFile(yaml);
		
	}
	
	static String getMax(int disc) {
		File file = new File(MeStorage.instance.getDataFolder(),String.valueOf(disc)+".yml") ;
		FileConfiguration yaml=YamlConfiguration.loadConfiguration(file);
		return getMax(yaml);
	}
	
	static String getMax(FileConfiguration file) {
		int max = file.getInt("max");
		return String.valueOf(max);
	}
	
	static void addMax(int disc) {
		File file = new File(MeStorage.instance.getDataFolder(),String.valueOf(disc)+".yml") ;
		FileConfiguration yaml=YamlConfiguration.loadConfiguration(file);
		addMax(yaml);
	}
	
	static void addMax(FileConfiguration file) {
		int max = file.getInt("max");
		file.set("max", max+1);
	}
	
	
	static void saveFile(File f,FileConfiguration file) {
		try {
			file.save(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	default boolean setdiscId(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		if(!meta.getLore().get(1).contains("No ID")) {return true;}
		int capacity = Integer.parseInt(meta.getLore().get(0).replaceAll("[^0-9]", ""));;
		int drive = cfg.getInt("drive");
		lore.remove(1);
		lore.add(String.valueOf(drive));
		cfg.set("drive", drive+1);
		MeStorage.instance.saveConfig();
		meta.setLore(lore);
		item.setItemMeta(meta);
		createDisc(drive,capacity);
		
		return false;
	}
	
}

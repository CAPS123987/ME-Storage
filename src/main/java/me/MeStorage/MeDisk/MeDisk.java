package me.MeStorage.MeDisk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

public class MeDisk implements ConfigurationSerializable{
	
	List<ItemStack> items = new ArrayList<ItemStack>();
	int capacity;
	private int currentCapacity = 0;
	
	
	public MeDisk() {
	}
	
	public List<ItemStack> getItems(){
		return items;
	}
	public int getCapacity() {
		return capacity;
	}
	public int getCurent() {
		return currentCapacity;
	}
	public void setItems(List<ItemStack> items) {
		this.items = items;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public void setCurent(int curent) {
		this.currentCapacity = curent;
	}
	
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("items", items);
		map.put("capacity", capacity);
		map.put("current", currentCapacity);
		return map;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public static MeDisk deserialize(Map<String, Object> args) {
		MeDisk meDisk = new MeDisk();
		meDisk.setItems((List<ItemStack>) args.get("items"));
		meDisk.setCurent((int) args.get("current"));
		meDisk.setCapacity((int) args.get("capacity"));
		return meDisk;
    }

}

package me.MeStorage.MeDisk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import me.MeStorage.MEStorage.MeStorage;

public class MeDisk implements ConfigurationSerializable{
	
	Map<ItemStack,Integer> items = new HashMap<ItemStack,Integer>();
	int capacity;
	private int currentCapacity = 0;
	
	
	public MeDisk() {
	}
	
	public Map<ItemStack, Integer> getItems(){
		return items;
	}
	public int getCapacity() {
		return capacity;
	}
	public int getCurent() {
		return currentCapacity;
	}
	
	
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public void setCurent(int curent) {
		this.currentCapacity = curent;
	}
	public int getSpace() {
		return capacity-currentCapacity;
	}
	public boolean isFull() {
		if(capacity-currentCapacity==0) {
			return true;
		}
		return false;
	}
	public void setItems(Map<ItemStack, Integer> items) {
		this.items=items;
	}
	public boolean pushItem(ItemStack item) {
		if(item.getType()==Material.AIR) {
			return false;
		}
		if(item.getAmount()<=0) {
			return false;
		}
		if(item.getAmount()>getSpace()) {
			return false;
		}
		
		ItemStack temp = item.clone();
		temp.setAmount(1);
		
		if(items.containsKey(temp)) {
			items.replace(temp, item.getAmount()+items.get(temp));
		}else {
			items.put(temp, item.getAmount());
		}
		
		currentCapacity = currentCapacity + item.getAmount();
		MeStorage.saveDisks();
		
		return true;
	}
	
	public int tryExportItem(ItemStack origoItem) {
		ItemStack item = origoItem.clone();
		item.setAmount(1);
		if(items.containsKey(item)) {
			if(items.get(item)>=origoItem.getAmount()) {
				return origoItem.getAmount();
			}else {
				return items.get(item);
			}
		}
		return 0;
			
	}
	public void exportItem(ItemStack origoItem) {
		ItemStack item = origoItem.clone();
		item.setAmount(1);
		if(items.containsKey(item)) {
			if(items.get(item)>=origoItem.getAmount()) {
				if(items.get(item)-origoItem.getAmount()==0) {
					items.remove(item);
				}else {
					items.replace(item, items.get(item)-origoItem.getAmount());
				}
			}
		}
	}
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("capacity", capacity);
		map.put("current", currentCapacity);
		
		List<ItemStack> item = new ArrayList<ItemStack>();
		List<Integer> amount = new ArrayList<Integer>();
		
		items.forEach((item2,amount2)->{
			item.add(item2);
			amount.add(amount2);
		});
		
		map.put("item",item);
		map.put("amount",amount);
		
		
		return map;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public static MeDisk deserialize(Map<String, Object> args) {
		MeDisk meDisk = new MeDisk();
		meDisk.setCurent((int) args.get("current"));
		meDisk.setCapacity((int) args.get("capacity"));
		
		List<ItemStack> tempItem =(List<ItemStack>) args.get("item");
		List<Integer> tempAmount = (List<Integer>) args.get("amount");
		
		Map<ItemStack,Integer> items2 = new HashMap<ItemStack,Integer>();
		if(!tempItem.isEmpty()) {
			tempItem.forEach((item)->
				items2.put(item, tempAmount.get(tempItem.indexOf(item)))
			);
		}
		meDisk.setItems(items2);
		
		return meDisk;
    }

}

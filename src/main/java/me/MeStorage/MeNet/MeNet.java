package me.MeStorage.MeNet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

public class MeNet implements ConfigurationSerializable{
	Location main;
	List<Location> connectors = new ArrayList<Location>();
	List<Location> machines = new ArrayList<Location>();
	
	public MeNet() {
	}
	
	public void setMain(Location main) {
		Validate.notNull(main,"Main can't be null");
		this.main = main;
	}
	
	public void setConnectors(List<Location> list) {
		connectors = list;
	}
	
	public void setMachines(List<Location> list) {
		machines = list;
	}
	
	public List<Location> getConnectors() {
		return connectors;
	}
	
	public List<Location> getMachines() {
		return machines;
	}
	
	public void removeConnector(Location l) {
		Validate.notNull(l,"Location can't be null");
		connectors.remove(l);
	}
	
	public void removeMachine(Location l) {
		Validate.notNull(l,"Location can't be null");
		machines.remove(l);
	}
	
	public void addConnector(Location l) {
		Validate.notNull(l,"Location can't be null");
		connectors.add(l);
	}
	
	public void addMachine(Location l) {
		Validate.notNull(l,"Location can't be null");
		machines.add(l);
		
	}
	
	
	public Location getMain() {
		return main;
	}
	
	@SuppressWarnings("unchecked")
	public static MeNet deserialize(Map<String, Object> args) {
		MeNet meNet = new MeNet();
		meNet.setMain((Location) args.get("main"));
		meNet.setConnectors((List<Location>) args.get("connectors"));
		meNet.setMachines((List<Location>) args.get("machines"));
		return meNet;
    }

	@Override
	public Map<String, Object> serialize() {
		//ConfigurationSerialization.deserializeObject(map);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("connectors", connectors);
		map.put("machines", machines);
		map.put("main", main);
		return map;
	}
	
}

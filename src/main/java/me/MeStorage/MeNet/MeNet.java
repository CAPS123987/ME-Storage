package me.MeStorage.MeNet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class MeNet implements ConfigurationSerializable{
	protected Location main;
	protected List<Location> connectors = new ArrayList<Location>();
	protected List<Location> machines = new ArrayList<Location>();
	protected int id;
	
	
	public MeNet() {
	}
	
	public int getId() {
		return id;
	}
	
	public Location getMain() {
		return main;
	}
	
	public List<Location> getConnectors() {
		return connectors;
	}
	
	public List<Location> getMachines() {
		return machines;
	}
	
	public void setMain(Location main) {
		Validate.notNull(main,"Main can't be null");
		this.main = main;
	}
	
	public void setConnectors(List<Location> list) {
		Validate.notNull(list,"List can't be null");
		connectors = list;
	}
	
	public void setMachines(List<Location> list) {
		Validate.notNull(list,"List can't be null");
		machines = list;
	}
	
	
	public void setId(int id) {
		this.id = id;
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
	
	
	
	@SuppressWarnings("unchecked")
	public static MeNet deserialize(Map<String, Object> args) {
		MeNet meNet = new MeNet();
		meNet.setMain((Location) args.get("main"));
		meNet.setConnectors((List<Location>) args.get("connectors"));
		meNet.setMachines((List<Location>) args.get("machines"));
		meNet.setId((int) args.get("id"));
		return meNet;
    }

	@Override
	public Map<String, Object> serialize() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("connectors", connectors);
		map.put("machines", machines);
		map.put("main", main);
		map.put("id", id);
		return map;
	}
	
}

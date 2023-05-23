package me.MeStorage.MeNet;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;

public class MeNetManager {
	public List<MeNet> networks = new CopyOnWriteArrayList<>();
	
	
	public MeNetManager(List<MeNet> networks) {
		this.networks = networks;
	}
	public List<MeNet> getNetworks(){
		return networks;
	}
	public void addNetwork(MeNet net) {
		networks.add(net);
	}
	public void removeNetwork(MeNet net) {
		networks.remove(net);
	}
	
	
}

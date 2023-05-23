package me.MeStorage.MeNet;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;

import me.MeStorage.MEStorage.MeStorage;

public class MeNetManager {
	protected List<MeNet> networks = new CopyOnWriteArrayList<>();
	
	
	public MeNetManager(List<MeNet> networks) {
		this.networks = networks;
	}
	public List<MeNet> getNetworks(){
		return networks;
	}
	public void addNetwork(MeNet net) {
		networks.add(net);
		MeStorage.saveNets();
	}
	public void removeNetwork(MeNet net) {
		networks.remove(net);
	}
	
	
}

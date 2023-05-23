package me.MeStorage.Utils;

import org.bukkit.Location;

import me.MeStorage.MEStorage.MeStorage;
import me.MeStorage.MeNet.MeNet;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public interface MeNetUtils {
	default MeNet getNetByMain(Location main) {
		for(MeNet net : MeStorage.getNet().getNetworks()) {
			if(net.getMain().equals(main)) {
				return net;
			}
		}
		return null;
	}
	default void removeNet(Location main) {
		MeNet toremove = getNetByMain(main);
		
		for(Location l :toremove.getConnectors()) {
			BlockStorage.addBlockInfo(l, "main", "");
		}
		for(Location l :toremove.getMachines()) {
			BlockStorage.addBlockInfo(l, "main", "");
		}
		MeStorage.getNet().getNetworks().remove(toremove);
		MeStorage.saveNets();
	}
}

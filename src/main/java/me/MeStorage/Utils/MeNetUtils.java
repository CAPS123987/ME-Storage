package me.MeStorage.Utils;

import org.bukkit.Location;

import me.MeStorage.MEStorage.MeStorage;
import me.MeStorage.MeNet.MeNet;

public interface MeNetUtils {
	default MeNet getNetByMain(Location main) {
		for(MeNet net : MeStorage.getNet().getNetworks()) {
			if(net.getMain().equals(main)) {
				return net;
			}
		}
		return null;
	}
}

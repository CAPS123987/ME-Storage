package me.MeStorage.Utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public interface ScanNetwork {
	
	static final Vector[] sides = {new Vector(1,0,0),new Vector(-1,0,0),new Vector(0,1,0),new Vector(0,-1,0),new Vector(0,0,1),new Vector(0,0,-1)};
	
	List<Location> buffer = new ArrayList<Location>();
	default void scanall(Location blockFrom,Location main) {
		BlockStorage.addBlockInfo(main.getBlock(), "machines","");
		scan(blockFrom,main);
		for(Location l:buffer) {
			BlockStorage.addBlockInfo(l,"scanned","false");
		}
	}
	
	
	
	default void scan(Location blockFrom,Location main) {
		for(Vector v:sides) {
			Location newLoc = blockFrom.clone().add(v);
			SlimefunItem sfitem = BlockStorage.check(newLoc);
			if(sfitem!=null) {
				String type = BlockStorage.getLocationInfo(newLoc, "MeType");
				if(type!=null) {
					if(BlockStorage.getLocationInfo(newLoc, "scanned").equals("false")) {
						if(type.equals("Connector")){
							BlockStorage.addBlockInfo(newLoc,"scanned","true");
							scan(newLoc,main);
						}else {
							String mach = "";
							if(BlockStorage.getLocationInfo(main, "machines")!= null) {
								mach = BlockStorage.getLocationInfo(main, "machines");
							}
							BlockStorage.addBlockInfo(main.getBlock(), "machines", mach+main.getX()+";"+main.getY()+";"+main.getZ()+";"+main.getWorld().getName()+":");
							BlockStorage.addBlockInfo(newLoc,"scanned","true");
							buffer.add(newLoc);
						}
					}
				}
			}
		}
		BlockStorage.addBlockInfo(blockFrom,"scanned","true");
		buffer.add(blockFrom);
	}
}

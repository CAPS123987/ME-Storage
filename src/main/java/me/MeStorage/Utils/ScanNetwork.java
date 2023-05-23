package me.MeStorage.Utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import me.MeStorage.MEStorage.MeStorage;
import me.MeStorage.MeNet.MeNet;
import me.MeStorage.System.MeConnector;
import me.MeStorage.System.MeStorageControler;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public interface ScanNetwork {
	
	static final Vector[] sides = {new Vector(1,0,0),new Vector(-1,0,0),new Vector(0,1,0),new Vector(0,-1,0),new Vector(0,0,1),new Vector(0,0,-1)};
	
	
	default void scanall(Location blockFrom,Location main) {
		List<Location> buffer = new ArrayList<Location>();
		BlockStorage.addBlockInfo(main.getBlock(), "machines","");
		scan(blockFrom,main,buffer);
		for(Location l:buffer) {
			BlockStorage.addBlockInfo(l,"scanned","false");
		}
		buffer.clear();
	}
	
	
	
	default void scan(Location blockFrom,Location main,List<Location> buffer) {
		for(Vector v:sides) {
			Location newLoc = blockFrom.clone().add(v);
			SlimefunItem sfitem = BlockStorage.check(newLoc);
			if(sfitem!=null) {
				String type = BlockStorage.getLocationInfo(newLoc, "MeType");
				if(type!=null) {
					if(BlockStorage.getLocationInfo(newLoc, "scanned").equals("false")) {
						if(type.equals("Connector")){
							BlockStorage.addBlockInfo(newLoc,"main",main.getX()+";"+main.getY()+";"+main.getZ()+";"+main.getWorld().getName());
							BlockStorage.addBlockInfo(newLoc,"scanned","true");
							scan(newLoc,main,buffer);
						}else {
							String mach = "";
							if(BlockStorage.getLocationInfo(main, "machines")!= null) {
								mach = BlockStorage.getLocationInfo(main, "machines");
							}
							BlockStorage.addBlockInfo(main.getBlock(), "machines", mach+main.getX()+";"+main.getY()+";"+main.getZ()+";"+main.getWorld().getName()+":");
							BlockStorage.addBlockInfo(newLoc,"scanned","true");
							BlockStorage.addBlockInfo(newLoc,"main",main.getX()+";"+main.getY()+";"+main.getZ()+";"+main.getWorld().getName());
							buffer.add(newLoc);
						}
					}
				}
			}
		}
		BlockStorage.addBlockInfo(blockFrom,"scanned","true");
		buffer.add(blockFrom);
	}
	default void findClose(Block b) {
		for(Vector v : sides) {
			Location newBlock = b.getLocation().clone().add(v);
			SlimefunItem sfitem =BlockStorage.check(newBlock.getBlock());
			if(sfitem instanceof MeConnector) {
				String loc = BlockStorage.getLocationInfo(newBlock, "main");
				if(loc!=null) {
					Location main = stringToLoc(loc);
					for(MeNet net : MeStorage.getNet().getNetworks()) {
						Bukkit.broadcastMessage("test1");
						if(net.getMain() == main) {
							Bukkit.broadcastMessage("test1.2");
							net.addConnector(b.getLocation());
							MeStorage.saveNets();
						}
					}
				
					break;
				}
			}
			if(sfitem instanceof MeStorageControler) {
				BlockStorage.addBlockInfo(b, "main", newBlock.getX()+";"+newBlock.getY()+";"+newBlock.getZ()+";"+newBlock.getWorld().getName());
				for(MeNet net : MeStorage.getNet().getNetworks()) {
					Bukkit.broadcastMessage("test2 main: "+net.getMain()+" block: "+newBlock);
					
					if(net.getMain() == newBlock) {
						Bukkit.broadcastMessage("test2.2");
						net.addConnector(b.getLocation());
						MeStorage.saveNets();
					}
				}
				break;
			}
		}
	}
	default Location stringToLoc(String s) {
		String[] newloc = s.split(";");
		Location mainloc = new Location(Bukkit.getWorld(newloc[3]),Double.parseDouble(newloc[0]),Double.parseDouble(newloc[1]),Double.parseDouble(newloc[2]));
		return mainloc;
	}
}

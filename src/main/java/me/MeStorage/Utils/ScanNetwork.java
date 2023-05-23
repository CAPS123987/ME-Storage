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

public interface ScanNetwork extends MeNetUtils{
	
	static final Vector[] sides = {new Vector(1,0,0),new Vector(-1,0,0),new Vector(0,1,0),new Vector(0,-1,0),new Vector(0,0,1),new Vector(0,0,-1)};
	public static Location nullLoc = new Location(Bukkit.getWorld("world"),0,-128,0);
	
	default void scanall(Location blockFrom,Location main,Location isntHere) {
		List<Location> buffer = new ArrayList<Location>();
		List<Location> connectors = new ArrayList<Location>();
		List<Location> machines = new ArrayList<Location>();
		BlockStorage.addBlockInfo(main.getBlock(), "machines","");
		scan(blockFrom,main,buffer,connectors,machines,isntHere);
		for(Location l:buffer) {
			BlockStorage.addBlockInfo(l,"scanned","false");
		}
		buffer.clear();
		MeNet net = getNetByMain(main);
		List<Location> old_connectors = net.getConnectors();
		List<Location> old_machines = net.getMachines();
		net.setConnectors(connectors);
		net.setMachines(machines);
		old_connectors.removeAll(connectors);
		old_machines.removeAll(machines);
		for(Location l:old_connectors) {
			BlockStorage.addBlockInfo(l, "main", "");
		}
		for(Location l:old_machines) {
			//BlockStorage.addBlockInfo(l, "main", "");
		}
		MeStorage.saveNets();
		
	}
	
	
	
	default void scan(Location blockFrom,Location main,List<Location> buffer,List<Location> connectors,List<Location> machines,Location isntHere) {
		for(Vector v:sides) {
			Location newLoc = blockFrom.clone().add(v);
			if(newLoc.distance(isntHere)>0.1) {
				SlimefunItem sfitem = BlockStorage.check(newLoc);
				if(sfitem!=null) {
					String type = BlockStorage.getLocationInfo(newLoc, "MeType");
					if(type!=null) {
						if(BlockStorage.getLocationInfo(newLoc, "scanned").equals("false")) {
							if(type.equals("Connector")){
								BlockStorage.addBlockInfo(newLoc,"main",main.getX()+";"+main.getY()+";"+main.getZ()+";"+main.getWorld().getName());
								BlockStorage.addBlockInfo(newLoc,"scanned","true");
								connectors.add(newLoc);
								scan(newLoc,main,buffer,connectors,machines,isntHere);
							}else {
								BlockStorage.addBlockInfo(newLoc,"scanned","true");
								BlockStorage.addBlockInfo(newLoc,"main",main.getX()+";"+main.getY()+";"+main.getZ()+";"+main.getWorld().getName());
								machines.add(newLoc);
								buffer.add(newLoc);
							}
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
				if(!(loc==null||loc=="")) {
					BlockStorage.addBlockInfo(b, "main", loc);
					Location main = stringToLoc(loc);
					for(MeNet net : MeStorage.getNet().getNetworks()) {
						if(net.getMain().distance(main)<0.1) {
							net.addConnector(b.getLocation());
							scanall(main,main,nullLoc);
							break;
						}
					}
					break;
				}
			}
			if(sfitem instanceof MeStorageControler) {
				BlockStorage.addBlockInfo(b, "main", newBlock.getX()+";"+newBlock.getY()+";"+newBlock.getZ()+";"+newBlock.getWorld().getName());
				for(MeNet net : MeStorage.getNet().getNetworks()) {
					if(net.getMain().distance(newBlock)<0.1) {
						net.addConnector(b.getLocation());
						scanall(newBlock,newBlock,nullLoc);
						break;
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

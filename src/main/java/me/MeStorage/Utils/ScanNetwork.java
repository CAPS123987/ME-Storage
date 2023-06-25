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
	
	default void scanall(Location blockFrom,int Net,Location isntHere) {
		List<Location> buffer = new ArrayList<Location>();
		List<Location> connectors = new ArrayList<Location>();
		List<Location> machines = new ArrayList<Location>();
		List<Location> servers = new ArrayList<Location>();
		
		List<Integer> disks = new ArrayList<Integer>();
		scan(blockFrom,Net,buffer,connectors,machines,isntHere,0,servers,disks);
		for(Location l:buffer) {
			BlockStorage.addBlockInfo(l,"scanned","false");
		}
		for(Location l:connectors) {
			BlockStorage.addBlockInfo(l,"main",String.valueOf(Net));
		}
		for(Location l:machines) {
			BlockStorage.addBlockInfo(l,"main",String.valueOf(Net));
		}
		for(Location l:servers) {
			BlockStorage.addBlockInfo(l,"main",String.valueOf(Net));
		}
		
		
		buffer.clear();
		
		MeNet net = getNetById(Net);
		
		net.setDisks(disks);
		
		List<Location> old_connectors = net.getConnectors();
		List<Location> old_servers = net.getServers();
		List<Location> old_machines = net.getMachines();
		
		net.setConnectors(connectors);
		net.setMachines(machines);
		net.setServers(servers);
		
		old_servers.removeAll(servers);
		old_connectors.removeAll(connectors);
		old_machines.removeAll(machines);
		
		for(Location l:old_connectors) {
			BlockStorage.addBlockInfo(l, "main", null);
		}
		
		for(Location l:old_servers) {
			BlockStorage.addBlockInfo(l, "main", null);
		}
		
		for(Location l:old_machines) {
			BlockStorage.addBlockInfo(l, "main", null);
		}
		
		MeStorage.saveNets();
		
	}
	
	
	
	default void scan(Location blockFrom,int main,List<Location> buffer,List<Location> connectors,List<Location> machines,Location isntHere,int size,List<Location> servers,List<Integer> disks) {
		if(size>MeStorage.getMaxSize())return;
		for(Vector v:sides) {
			Location newLoc = blockFrom.clone().add(v);
			if(newLoc.distance(isntHere)>0.1) {
				SlimefunItem sfitem = BlockStorage.check(newLoc);
				if(sfitem!=null) {
					String type = BlockStorage.getLocationInfo(newLoc, "MeType");
					if(type!=null) {
						String Main = BlockStorage.getLocationInfo(newLoc, "main");
						if(Main==null) {
							Main ="";
						}
						boolean can = false;
						try {
							int i =Integer.parseInt(Main);
							if(i==main) {
								can = true;
							}else {
								can=false;
							}
							
						}catch(Exception e) {
							can = true;
						}
						if(BlockStorage.getLocationInfo(newLoc, "scanned").equals("false")&&can) {
							if(type.equals("Connector")){
								//BlockStorage.addBlockInfo(newLoc,"main", String.valueOf(main));
								BlockStorage.addBlockInfo(newLoc,"scanned","true");
								
								connectors.add(newLoc);
								buffer.add(newLoc);
								scan(newLoc,main,buffer,connectors,machines,isntHere,size+1,servers,disks);
							}else if(type.equals("MeStore")){
								BlockStorage.addBlockInfo(newLoc,"scanned","true");
								//BlockStorage.addBlockInfo(newLoc,"main",String.valueOf(main));
								disks.addAll(getDisks(newLoc));
								servers.add(newLoc);
								buffer.add(newLoc);
							}else {
								BlockStorage.addBlockInfo(newLoc,"scanned","true");
								//BlockStorage.addBlockInfo(newLoc,"main",String.valueOf(main));
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
					BlockStorage.addBlockInfo(b.getLocation(), "main", loc);
					MeNet net = getNetById(Integer.parseInt(loc));

					scanall(net.getMain(),net.getId(),nullLoc);
					
					break;
				}
			}
			if(sfitem instanceof MeStorageControler) {
				
				String loc = BlockStorage.getLocationInfo(newBlock, "net");
				
				BlockStorage.addBlockInfo(b.getLocation(), "main", loc);
				MeNet net = getNetById(Integer.parseInt(loc));

				scanall(newBlock,net.getId(),nullLoc);
				break;
			}
		}
	}
	static List<Integer> getDisks(Location l) {
		List<Integer> id = new ArrayList<Integer>();
		
		String disks = BlockStorage.getLocationInfo(l, "Drives");
		String[] list = disks.split(",");
		
		//for disks
		for(String diskId:list) {
			if(!diskId.isEmpty()) {
				id.add(Integer.parseInt(diskId));
			}
		}
		
		return id;
	}
}

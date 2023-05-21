package me.MeStorage.System;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.util.Vector;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import me.MeStorage.Items.Items;
import me.MeStorage.Utils.ScanNetwork;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public class MeConnector extends SlimefunItem implements ScanNetwork{
	
	
	public MeConnector() {
		super(Items.meStorage,Items.MECONNECTOR,RecipeType.ENHANCED_CRAFTING_TABLE,Items.recipe_TEST_ITEM);
		addItemHandler(onPlace());
	}
	public BlockPlaceHandler onPlace(){
		return new BlockPlaceHandler(false) {

			@Override
			public void onPlayerPlace(BlockPlaceEvent e) {
				Block b = e.getBlock();
				BlockStorage.addBlockInfo(b, "scanned", "false");
				BlockStorage.addBlockInfo(b, "MeType", "Connector");
				for(Vector v : sides) {
					Location newBlock = b.getLocation().clone().add(v);
					SlimefunItem sfitem =BlockStorage.check(newBlock.getBlock());
					if(sfitem instanceof MeStorageControler) {
						BlockStorage.addBlockInfo(b, "main", newBlock.getX()+";"+newBlock.getY()+";"+newBlock.getZ()+";"+newBlock.getWorld().getName());
						
						break;
					}
					if(sfitem instanceof MeConnector) {
						String loc = BlockStorage.getLocationInfo(newBlock, "main");
						if(loc!=null) {
						BlockStorage.addBlockInfo(b, "main", loc);
							String[] newloc = loc.split(";");
							Location mainloc = new Location(Bukkit.getWorld(newloc[3]),Double.parseDouble(newloc[0]),Double.parseDouble(newloc[1]),Double.parseDouble(newloc[2]));
							scanall(b.getLocation(),mainloc);
						}
					}
				}
			}
		};
	}
}

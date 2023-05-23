package me.MeStorage.System;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import me.MeStorage.Items.Items;
import me.MeStorage.MEStorage.MeStorage;
import me.MeStorage.MeNet.MeNet;
import me.MeStorage.Utils.MeNetUtils;
import me.MeStorage.Utils.ScanNetwork;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import net.md_5.bungee.api.ChatColor;

public class MeConnector extends SlimefunItem implements ScanNetwork, MeNetUtils{
	
	
	public MeConnector() {
		super(Items.meStorage,Items.MECONNECTOR,RecipeType.ENHANCED_CRAFTING_TABLE,Items.recipe_TEST_ITEM);
		addItemHandler(onPlace(),onUse());
	}
	public BlockPlaceHandler onPlace(){
		return new BlockPlaceHandler(false) {

			@Override
			public void onPlayerPlace(BlockPlaceEvent e) {
				
				
				
				
				Block b = e.getBlock();
				BlockStorage.addBlockInfo(b, "scanned", "false");
				BlockStorage.addBlockInfo(b, "MeType", "Connector");
				findClose(b);
				
				
				/*for(Vector v : sides) {
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
							break;
						}
					}
				}*/
				
				
			}
		};
	}
	public BlockUseHandler onUse() {
		return new BlockUseHandler() {

			@Override
			public void onRightClick(PlayerRightClickEvent e) {
				Block b = e.getClickedBlock().get();
				String main = BlockStorage.getLocationInfo(b.getLocation(), "main");
				if(main == null) {
					e.getPlayer().sendMessage(ChatColor.RED+"not Connected");
				}else {
					e.getPlayer().sendMessage(ChatColor.GREEN+"connected");
				}
				
			}
			
		};
	}
}

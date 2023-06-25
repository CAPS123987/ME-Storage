package me.MeStorage.System;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import me.MeStorage.Items.Items;
import me.MeStorage.Utils.MeNetUtils;
import me.MeStorage.Utils.ScanNetwork;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import net.md_5.bungee.api.ChatColor;

public class MeConnector extends SlimefunItem implements ScanNetwork, MeNetUtils{
	
	
	public MeConnector() {
		super(Items.meStorage,Items.MECONNECTOR,RecipeType.ENHANCED_CRAFTING_TABLE,Items.recipe_TEST_ITEM);
		addItemHandler(onPlace(),onUse(),onBreak());
	}
	public BlockPlaceHandler onPlace(){
		return new BlockPlaceHandler(false) {
			
			@Override
			public void onPlayerPlace(BlockPlaceEvent e) {

				Block b = e.getBlock();
				BlockStorage.addBlockInfo(b, "scanned", "false");
				BlockStorage.addBlockInfo(b, "MeType", "Connector");
				findClose(b);
				//MeStorage.getDisk().getDisks().get(1).addItem2(new ItemStack(Material.BEDROCK));

				
			}
		};
	}
	public BlockBreakHandler onBreak(){
		return new BlockBreakHandler(false,false) {

			@Override
			public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
				String loc = BlockStorage.getLocationInfo(e.getBlock().getLocation(), "main");
				if(!(loc==null||loc=="")) {
					try{scanall(getNetById(Integer.parseInt(loc)).getMain(),Integer.parseInt(loc),e.getBlock().getLocation());}catch(Exception e2) {};
					
				}
			}
			
		};
	}
	
	public BlockUseHandler onUse() {
		return new BlockUseHandler() {

			@Override
			public void onRightClick(PlayerRightClickEvent e) {
				Block b = e.getClickedBlock().get();
				String main = BlockStorage.getLocationInfo(b.getLocation(), "main");
				try {Integer.parseInt(main);e.getPlayer().sendMessage(ChatColor.GREEN+"connected");}
				catch(Exception e2) {e.getPlayer().sendMessage(ChatColor.RED+"not Connected");}
				
			}
			
		};
	}
}

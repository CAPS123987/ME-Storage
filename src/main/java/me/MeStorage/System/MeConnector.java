package me.MeStorage.System;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
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
import me.MeStorage.MEStorage.MeStorage;
import me.MeStorage.MeNet.MeNet;
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
				
				
			}
		};
	}
	public BlockBreakHandler onBreak(){
		return new BlockBreakHandler(false,false) {

			@Override
			public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
				String loc = BlockStorage.getLocationInfo(e.getBlock().getLocation(), "main");
				if(!(loc==null||loc=="")) {
					Location main = stringToLoc(loc);
					scanall(main,main,e.getBlock().getLocation());
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
				if(main == null||main=="") {
					e.getPlayer().sendMessage(ChatColor.RED+"not Connected");
				}else {
					e.getPlayer().sendMessage(ChatColor.GREEN+"connected");
				}
				
			}
			
		};
	}
}

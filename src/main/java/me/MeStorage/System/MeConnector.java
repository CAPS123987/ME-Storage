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

public class MeConnector extends MeComponent implements ScanNetwork, MeNetUtils{
	
	
	public MeConnector() {
		super(Items.meStorage,Items.MECONNECTOR,RecipeType.ENHANCED_CRAFTING_TABLE,Items.recipe_TEST_ITEM);
		addItemHandler(onUse());
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
	@Override
	String MeType() {
		// TODO Auto-generated method stub
		return "Connector";
	}
	@Override
	void placeHandler(BlockPlaceEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	void breakHandler(BlockBreakEvent e) {
		// TODO Auto-generated method stub
		
	}
}

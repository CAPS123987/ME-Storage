package me.MeStorage.System;

import java.util.List;
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
import me.MeStorage.MeNet.MeNet;
import me.MeStorage.Utils.ScanNetwork;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import net.md_5.bungee.api.ChatColor;

public class MeInterface extends MeComponent implements ScanNetwork{
	public MeInterface() {
		super(Items.meStorage,Items.MEINTERFACE,RecipeType.ENHANCED_CRAFTING_TABLE,Items.recipe_TEST_ITEM);
		addItemHandler(Click());
	}
	
	public BlockUseHandler Click() {
		return new BlockUseHandler(){

			@Override
			public void onRightClick(PlayerRightClickEvent e) {
				Block b = e.getClickedBlock().get();
				e.cancel();
				try {
					MeNet net = getNetById(Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), "main").replaceAll("[^0-9]", "")));
					
					Location main = net.getMain();
					
					BlockMenu menu = BlockStorage.getInventory(main);
					
					
					menu.open(e.getPlayer());
					
				}catch(Exception e2) {
					e.getPlayer().sendMessage(ChatColor.RED+"ERROR");
				}
				
			}
		};
	}

	@Override
	String MeType() {
		// TODO Auto-generated method stub
		return "MeInterface";
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

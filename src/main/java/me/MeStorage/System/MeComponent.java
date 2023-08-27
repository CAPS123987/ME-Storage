package me.MeStorage.System;

import java.util.List;
import java.util.logging.Level;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import me.MeStorage.MEStorage.MeStorage;
import me.MeStorage.MeNet.MeNet;
import me.MeStorage.Utils.ScanNetwork;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public abstract class MeComponent extends SlimefunItem implements ScanNetwork{

	protected MeComponent(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(itemGroup, item, recipeType, recipe);
		
		addItemHandler(onPlace(),onBreak());
		// TODO Auto-generated constructor stub
	}
	
	public BlockPlaceHandler onPlace() {
		return new BlockPlaceHandler(false) {

			@Override
			public void onPlayerPlace(@Nonnull BlockPlaceEvent e) {
				// TODO Auto-generated method stub
				Block b = e.getBlock();
				placeHandler(e);
				BlockStorage.addBlockInfo(b, "MeType", MeType());
				BlockStorage.addBlockInfo(b, "scanned", "false");
				findClose(b);
			}
		};
	}
	
	public BlockBreakHandler onBreak() {
        return new BlockBreakHandler(false, false) {

			@Override
			public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
				Block b = e.getBlock();
				breakHandler(e);
				
				try {
                	MeNet net = getNetById(Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), "main").replaceAll("[^0-9]", "")));
                	
                	scanall(net.getMain(),net.getId(),b.getLocation());
                }catch(Exception er) {}
				
			}
        	
        };
	}
	
	abstract String MeType();

	abstract void placeHandler(BlockPlaceEvent e);
	
	abstract void breakHandler(BlockBreakEvent e);
}

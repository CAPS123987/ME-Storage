package me.MeStorage.System;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.MeStorage.Items.Items;
import me.MeStorage.MEStorage.MeStorage;
import me.MeStorage.MeNet.MeNet;
import me.MeStorage.Utils.ETInventoryBlock;
import me.MeStorage.Utils.MeNetUtils;
import me.MeStorage.Utils.ScanNetwork;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.AdvancedMenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

@SuppressWarnings("deprecation")
public class MeStorageControler extends SimpleSlimefunItem<BlockTicker> implements ETInventoryBlock,
EnergyNetComponent,MeNetUtils,ScanNetwork{
	
	private final int[] border = {0,1,2,3,4,5,9,14,18,23,27,32,36,41,45,47,48,50};
	private final int page_back = 46;
	private final int page_next = 49;
	private final int[] inputBorder = {6,7,8,15,17,24,26,33,35,42,44,51,52,53};
    private final int[] outputBorder = {};
    private final int[] input = {};
    private final int[] output = {};

	public MeStorageControler() {
		super(Items.meStorage,Items.MESTORAGECONTROLER,RecipeType.ENHANCED_CRAFTING_TABLE,Items.recipe_TEST_ITEM);
		
		createPreset(this, this::constructMenu,this::newInstance);
		addItemHandler(onPlace(),onBreak());
	}
	public BlockPlaceHandler onPlace(){
		return new BlockPlaceHandler(false) {

			@Override
			public void onPlayerPlace(BlockPlaceEvent e) {
				MeNet newNet = new MeNet();
				newNet.setMain(e.getBlock().getLocation());
				int empty = findEmpty();
				newNet.setId(empty);
				BlockStorage.addBlockInfo(e.getBlock().getLocation(), "net", String.valueOf(empty));
				MeStorage.getNet().addNetwork(newNet);
				scanall(e.getBlock().getLocation(),empty,nullLoc);
			}
			
		};
	}
	
	public void newInstance(BlockMenu menu,Block b) {
		Bukkit.broadcastMessage("now");
		menu.replaceExistingItem(page_next,new ItemStack(Material.AIR)/*MeNextButton(1,getSlotsOfDisks(b.getLocation()))*/);
		menu.addMenuClickHandler(page_next, (p, s, i, a)->{
			//Bukkit.broadcastMessage(getSlotsOfDiscs(b.getLocation())+"");
			menu.replaceExistingItem(page_next,new ItemStack(Material.AIR)/*MeNextButton(1,getSlotsOfDisks(b.getLocation()))*/);
			return false;
		});
	}
	
	public BlockBreakHandler onBreak(){
		return new BlockBreakHandler(false, false) {

			@Override
			public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
				removeNet(e.getBlock().getLocation());
				
			}
			
		};
	}

	@Override
	public EnergyNetComponentType getEnergyComponentType() {
		// TODO Auto-generated method stub
		return EnergyNetComponentType.CONSUMER;
		
	}

	@Override
	public int getCapacity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int[] getInputSlots() {
		// TODO Auto-generated method stub
		return input;
	}

	@Override
	public int[] getOutputSlots() {
		// TODO Auto-generated method stub
		return output;
	}

	@Override
	public BlockTicker getItemHandler() {
		// TODO Auto-generated method stub
		return new BlockTicker() {

			@Override
			public boolean isSynchronized() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void tick(Block b, SlimefunItem item, Config data) {
				// TODO Auto-generated method stub
				
			}
			
		};
	}
	/*public int getSlotsOfDisks(Location main) {
		String id =BlockStorage.getLocationInfo(main, "net");
		Bukkit.broadcastMessage(id);
		MeNet net= getNetById(Integer.parseInt(id));
		List<Location> servers = net.getServers();
		int count =0;
		for(Location l:servers) {
			String drives = BlockStorage.getLocationInfo(l, "Drives");
			String[] list = drives.split(",");
			for(String drive:list) {
				count = count +getDifferentItemCount(Integer.parseInt(drive));
			}
		}
		return count;
	}*/
	
	
	private void constructMenu(BlockMenuPreset preset) {
		
		
        for (int i : border) {
            preset.addItem(i, new CustomItemStack(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "),
                ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : inputBorder) {
            preset.addItem(i, new CustomItemStack(new ItemStack(Material.CYAN_STAINED_GLASS_PANE), " "),
                ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : outputBorder) {
            preset.addItem(i, new CustomItemStack(new ItemStack(Material.ORANGE_STAINED_GLASS_PANE), " "),
                ChestMenuUtils.getEmptyClickHandler());
        }
        
        
		
		//preset.addMenuClickHandler(page_next, MeNextButton(1,1));
		
		preset.addItem(page_back,new ItemStack(Material.AIR)/*MeBackButton(1,1)*/,ChestMenuUtils.getEmptyClickHandler());
        
        for (int i : getOutputSlots()) {
            preset.addMenuClickHandler(i, new ChestMenu.AdvancedMenuClickHandler() {

                @Override
                public boolean onClick(Player p, int slot, ItemStack cursor, ClickAction action) {
                	
                    return false;
                }

                @Override
                public boolean onClick(InventoryClickEvent e, Player p, int slot, ItemStack cursor,
                                       ClickAction action) {
                	
                    return cursor == null || cursor.getType() == Material.AIR;
                }
            });
        	}
    }
}
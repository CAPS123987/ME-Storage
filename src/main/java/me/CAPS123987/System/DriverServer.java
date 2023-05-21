package me.CAPS123987.System;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.CAPS123987.Items.Items;
import me.CAPS123987.MEStorage.MeStorage;
import me.CAPS123987.Utils.ETInventoryBlock;
import me.CAPS123987.Utils.ItemUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;


@SuppressWarnings("deprecation")
public class DriverServer extends SimpleSlimefunItem<BlockTicker> implements ETInventoryBlock,
EnergyNetComponent,ItemUtils {
	FileConfiguration cfg = MeStorage.instance.getConfig();
	
	
	private int[] border = {0,1,2,3,4,5,6,7,8};
	public int[] slots = {};
	private final int[] inputBorder = {};
    private final int[] outputBorder = {};
    public final int status = 4;
    public final int statusUpdate = 13;
    private final int[] input = {};
    private final int[] output = {};
    
	public DriverServer(int stage,SlimefunItemStack item,ItemStack[] stack){
		
		super(
				Items.meStorage
				, item
				, RecipeType.ENHANCED_CRAFTING_TABLE
				, stack);
		
		border = border(stage);
		slots = slots(stage);
		createPreset(this, this::constructMenu);
		addItemHandler(onBreak(),onPlace());
		
		
	}
	public void updateStatus(BlockMenu menu,Block b) {
		
		String status = BlockStorage.getLocationInfo(b.getLocation(),"Status");
		
		if(status.equals("on")) {
			ItemStack item = new ItemStack(Material.GREEN_WOOL);
			ItemMeta meta = item.getItemMeta();
			List<String> lore = new ArrayList<String>();
			String drives = "";
			for(int i:slots) {
				if(menu.getItemInSlot(i)!=null) {
					String id = menu.getItemInSlot(i).getItemMeta().getLore().get(1);
					drives = drives+"Drive no. "+id+", ";
				}
			}
			lore.add(ChatColor.AQUA+"Registered Drives: "+drives);
			meta.setLore(lore);
			meta.setDisplayName(ChatColor.GREEN+"Status: Server on");
			item.setItemMeta(meta);
			menu.replaceExistingItem(this.status, item);
		}else {
			ItemStack item = new ItemStack(Material.RED_WOOL);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.RED+"Status: Server off");
			item.setItemMeta(meta);
			menu.replaceExistingItem(this.status, item);
			
		}
		
	}
	
	public BlockTicker getItemHandler() {
        return new BlockTicker() {

			@Override
			public boolean isSynchronized() {
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			public void tick(Block b, SlimefunItem item, Config data) {
				// TODO Auto-generated method stub
				BlockMenu menu = BlockStorage.getInventory(b);
				menu.addPlayerInventoryClickHandler((p, s, i, a) -> {
		        	
		        	SlimefunItem sfItem2 = isSlimefun(p.getItemOnCursor());
		        	SlimefunItem sfItem = isSlimefun(i);
		        	if(sfItem!=null) {
		        		if(sfItem.isItem(Items.DRIVE1)||sfItem.isItem(Items.DRIVE2)||sfItem.isItem(Items.DRIVE3)||sfItem.isItem(Items.DRIVE4)) {
		        			setdiscId(i);
		        			return true;
		        		}
		        	}
		        	if(sfItem2!=null) {
		        		if(sfItem2.isItem(Items.DRIVE1)||sfItem2.isItem(Items.DRIVE2)||sfItem2.isItem(Items.DRIVE3)||sfItem2.isItem(Items.DRIVE4)) {
		        			setdiscId(p.getItemOnCursor());
		        			return true;
		        		}
		        	}
		        	
		        	return false;
		        	});
				menu.addMenuOpeningHandler((p)->{
					updateStatus(menu,b);
				});
				menu.addMenuClickHandler(statusUpdate, (p, s, i, a)->{
					String status = BlockStorage.getLocationInfo(b.getLocation(),"Status");
					
					if(status.equals("on")) {
						BlockStorage.addBlockInfo(b, "Status", "off");
					}else {
						BlockStorage.addBlockInfo(b, "Status", "on");
					}
					updateStatus(menu,b);
		        	return false;
		        });
				
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
	
	public SlimefunItem isSlimefun(ItemStack i) {
    	if(i == null) {
    		return null;
    	}
    	if(SlimefunItem.getByItem(i) == null) {
        	return null;
        }else {
        	return SlimefunItem.getByItem(i);
        }
    }
    public BlockBreakHandler onBreak() {
        return new BlockBreakHandler(false, false) {

            @Override
            public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
                Block b = e.getBlock();
                BlockMenu inv = BlockStorage.getInventory(b);

                if (inv != null) {
                    inv.dropItems(b.getLocation(), slots);
                }
            }
        };
    }
    public BlockPlaceHandler onPlace() {
    	return new BlockPlaceHandler(true) {

			@Override
			public void onPlayerPlace(BlockPlaceEvent e) {
				// TODO Auto-generated method stub
				Block b = e.getBlock();
				BlockStorage.addBlockInfo(b, "Status", "on");
			}
    		
    	};
    }
    
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
        preset.addItem(statusUpdate, new CustomItemStack(new ItemStack(Material.ORANGE_WOOL),ChatColor.GOLD+ "Turn on/off"),
                ChestMenuUtils.getEmptyClickHandler());
        
        
        for (int i1 : slots) {
            preset.addMenuClickHandler(i1, (p, s, i, a)->{
            	SlimefunItem sfItem2 = isSlimefun(p.getItemOnCursor());
	        	SlimefunItem sfItem = isSlimefun(i);
	        	if(sfItem!=null) {
	        		if(sfItem.isItem(Items.DRIVE1)||sfItem.isItem(Items.DRIVE2)||sfItem.isItem(Items.DRIVE3)||sfItem.isItem(Items.DRIVE4)) {
	        			setdiscId(i);
	        			return true;
	        		}
	        	}
	        	if(sfItem2!=null) {
	        		if(sfItem2.isItem(Items.DRIVE1)||sfItem2.isItem(Items.DRIVE2)||sfItem2.isItem(Items.DRIVE3)||sfItem2.isItem(Items.DRIVE4)) {
	        			setdiscId(p.getItemOnCursor());
	        			return true;
	        		}
	        	}
            	
            	return false;
            });
        }
        
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
        /*for (int i : getInputSlots()) {
        	preset.addItem(i, new CustomItemStack(new ItemStack(Material.AIR)));
        }*/
    }
    public int[] border(int stage) {

    	switch(stage){
    	case 1:
        	int[] i = {0,1,2,3,4,5,6,7,8,9,11,13,15,17,18,19,20,21,22,23,24,25,26};
        	return i;
    	case 2:
    		int[] i1 = {0,1,2,3,4,5,6,7,8,9,11,13,15,17,19,21,23,25,27,28,29,30,31,32,33,34,35};
        	return i1;
    	case 3:
    		int[] i2 = {0,1,2,3,4,5,6,7,8,9,11,13,15,17,19,21,23,25,27,29,31,33,35,36,37,38,39,40,41,42,43,44};
        	return i2;
    	default:
    		int[] i3 = {0,1,2,3,4,5,6,7,8,9,11,13,15,17,18,19,20,21,22,23,24,25,26};
    		return i3;
    	}
    }
    public int[] slots(int stage) {

    	switch(stage){
    	case 1:
        	int[] i = {10,12,14,16};
        	return i;
    	case 2:
    		int[] i1 = {10,12,14,16,18,20,22,24,26};
        	return i1;
    	case 3:
    		int[] i2 = {10,12,14,16,18,20,22,24,26,28,30,32,34};
        	return i2;
    	default:
    		int[] i3 = {10,12,14,16};
    		return i3;
    	}
    }
	
}

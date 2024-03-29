package me.MeStorage.System;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

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
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.MeStorage.Items.Items;
import me.MeStorage.MEStorage.MeStorage;
import me.MeStorage.Utils.ETInventoryBlock;
import me.MeStorage.Utils.MeItemUtils;
import me.MeStorage.Utils.ScanNetwork;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

@SuppressWarnings("deprecation")
public class DiskServer extends MeComponent implements ETInventoryBlock,
EnergyNetComponent,ScanNetwork {
	FileConfiguration cfg = MeStorage.instance.getConfig();
	
	
	private static int[] border = {0,1,2,3,4,5,6,7,8};
	private static int[] slots = {};
	private static final int[] inputBorder = {};
    private static final int[] outputBorder = {};
    public static final int status = 4;
    public static final int statusUpdate = 3;
    private static final int[] input = {};
    private static final int[] output = {};
    private static final int rename = 5;
    
	public DiskServer(int stage,SlimefunItemStack item,ItemStack[] stack){
		
		super(
				Items.meStorage
				, item
				, RecipeType.ENHANCED_CRAFTING_TABLE
				, stack);
		
		border = border(stage);
		slots = slots(stage);
		createPreset(this, this::constructMenu,this::newInstance);
		//addItemHandler(onBreak(),onPlace());
		
		
	}
	
	public void newInstance(BlockMenu menu,Block b) {
		addHandlers(menu,b);
		updateStatus(menu,b,getSlotTier(BlockStorage.getLocationInfo(b.getLocation(),"id")));
		String name = BlockStorage.getLocationInfo(b.getLocation(), "name");
		if(name == null) {
			menu.replaceExistingItem(rename, new CustomItemStack(new ItemStack(Material.NAME_TAG),ChatColor.WHITE+ "Rename server"));
		}else {
			menu.replaceExistingItem(rename, new CustomItemStack(new ItemStack(Material.NAME_TAG),ChatColor.WHITE+ "Rename server",ChatColor.GRAY+"now: "+name));
		}
		
	}
	public void updateStatus(BlockMenu menu,Block b,int tier) {
		
		String status = BlockStorage.getLocationInfo(b.getLocation(),"Status");
		
		if(status!=null) {
			if(status.equals("on")) {
				ItemStack item = new ItemStack(Material.GREEN_WOOL);
				ItemMeta meta = item.getItemMeta();
				List<String> lore = new ArrayList<String>();
				String drives = "";
				String store = "";
				for(int i:slots(tier)) {
					if(menu.getItemInSlot(i)!=null) {
						String id = menu.getItemInSlot(i).getItemMeta().getLore().get(1);
						
						//saveItem(Integer.parseInt(id),new ItemStack(Material.BEDROCK));
						drives = drives+"Drive no. "+id+", ";
						store = store + id+",";
					}
				}
				BlockStorage.addBlockInfo(b, "Drives", store);
				lore.add(ChatColor.AQUA+"Registered Drives: "+drives);
				meta.setLore(lore);
				meta.setDisplayName(ChatColor.GREEN+"Status: Server on");
				item.setItemMeta(meta);
				menu.replaceExistingItem(DiskServer.status, item);
			}else {
				BlockStorage.addBlockInfo(b, "Drives", "");
				ItemStack item = new ItemStack(Material.RED_WOOL);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(ChatColor.RED+"Status: Server off");
				item.setItemMeta(meta);
				menu.replaceExistingItem(DiskServer.status, item);
				
			}
		}
		
	}
	
	
	public void addHandlers(BlockMenu menu,Block b) {
		int[] slot = slots(getSlotTier(BlockStorage.getLocationInfo(b.getLocation(), "id")));
		menu.addPlayerInventoryClickHandler((p, s, i, a) -> {
        	
        	SlimefunItem sfItem2 = isSlimefun(p.getItemOnCursor());
        	SlimefunItem sfItem = isSlimefun(i);
        	if(sfItem!=null && (sfItem.isItem(Items.DISK1)||sfItem.isItem(Items.DISK2)||sfItem.isItem(Items.DISK3)||sfItem.isItem(Items.DISK4)||sfItem.isItem(Items.DISK5))) {
        			
        		
        		
       			ItemMeta meta = i.getItemMeta();
       			List<String> lore = meta.getLore();
       			if(lore.get(1).contains("ID")) {
       				int id = MeStorage.getDiskManager().createDisc(sfItem);
	       			lore.set(1, String.valueOf(id));
	       			meta.setLore(lore);
	        		i.setItemMeta(meta);
				}
        		return true;
        		
        	}
        	if(sfItem2!=null && (sfItem2.isItem(Items.DISK1)||sfItem2.isItem(Items.DISK2)||sfItem2.isItem(Items.DISK3)||sfItem2.isItem(Items.DISK4)||sfItem2.isItem(Items.DISK5))) {
        			
        			ItemMeta meta = p.getItemOnCursor().getItemMeta();
        			List<String> lore = meta.getLore();
        			if(lore.get(1).contains("ID")) {
        				int id = MeStorage.getDiskManager().createDisc(sfItem2);
	        			lore.set(1, String.valueOf(id));
	        			meta.setLore(lore);
	        			p.getItemOnCursor().setItemMeta(meta);
        			}
        			return true;
        		
        		}
        	return false;
        	});

		menu.addMenuClickHandler(statusUpdate, (p, s, i, a)->{
			String status = BlockStorage.getLocationInfo(b.getLocation(),"Status");
			
			if(status.equals("on")) {
				BlockStorage.addBlockInfo(b, "Status", "off");
				defaultSlots(menu,slot);
				newInstance(menu,b);
			}else {
				BlockStorage.addBlockInfo(b, "Status", "on");
				for (int i1 : slot) {
		            menu.addMenuClickHandler(i1, (p1, s1, i2, a1)->{
		            	newInstance(menu,b);
		            	return false;
		            });
				}
			}
			updateStatus(menu,b,getSlotTier(BlockStorage.getLocationInfo(b.getLocation(),"id")));
			findClose(b);
        	return false;
        });
		menu.addMenuClickHandler(rename, (p,s,i,a)->{
			p.sendMessage(ChatColor.YELLOW+"Rename server by typing in chat and then enter");
			MeItemUtils.renameServer(p, b,menu);
			return false;
		});
		
		if(BlockStorage.getLocationInfo(b.getLocation(),"Status").equals("on")) {
			for (int i1 : slot) {
	            menu.addMenuClickHandler(i1, (p1, s1, i2, a1)->{
	            	newInstance(menu,b);
	            	return false;
	            });
			}
		}
	}
	


	@Override
	@Nonnull
	public EnergyNetComponentType getEnergyComponentType() {
		return EnergyNetComponentType.CONSUMER;
	}
	

	@Override
	public int getCapacity() {
		return 0;
	}

	@Override
	public int[] getInputSlots() {
		return input;
	}

	@Override
	public int[] getOutputSlots() {
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
    public void defaultSlots(BlockMenu preset,int[] slot) {
    	
    	for (int i1 : slot) {
            preset.addMenuClickHandler(i1, (p, s, i, a)->{
            	SlimefunItem sfItem2 = isSlimefun(p.getItemOnCursor());
	        	SlimefunItem sfItem = isSlimefun(i);
	        	if(sfItem!=null) {
	        		if(sfItem.isItem(Items.DISK1)||sfItem.isItem(Items.DISK2)||sfItem.isItem(Items.DISK3)||sfItem.isItem(Items.DISK4)||sfItem.isItem(Items.DISK5)) {
	        			//TODO setdiskId(i);
	        			return true;
	        		}
	        	}
	        	if(sfItem2!=null) {
	        		if(sfItem2.isItem(Items.DISK1)||sfItem2.isItem(Items.DISK2)||sfItem2.isItem(Items.DISK3)||sfItem2.isItem(Items.DISK4)||sfItem2.isItem(Items.DISK5)) {
	        			//TODO setdiskId(p.getItemOnCursor());
	        			return true;
	        		}
	        	}
            	
            	return false;
            });
        }
    }
    
    private void constructMenu(BlockMenuPreset preset) {
    	
    	
        for (int i : border) {
            preset.addItem(i, ChestMenuUtils.getBackground(),
                ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : inputBorder) {
            preset.addItem(i, ChestMenuUtils.getInputSlotTexture(),
                ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : outputBorder) {
            preset.addItem(i, ChestMenuUtils.getOutputSlotTexture(),
                ChestMenuUtils.getEmptyClickHandler());
        }
        preset.addItem(statusUpdate, new CustomItemStack(new ItemStack(Material.ORANGE_WOOL),ChatColor.GOLD+ "Turn on/off"),
                ChestMenuUtils.getEmptyClickHandler());
        preset.addItem(rename, new CustomItemStack(new ItemStack(Material.NAME_TAG),ChatColor.WHITE+ "Rename server"),
                ChestMenuUtils.getEmptyClickHandler());
        
        
        for (int i1 : slots) {
            preset.addMenuClickHandler(i1, (p, s, i, a)->{
            	SlimefunItem sfItem2 = isSlimefun(p.getItemOnCursor());
	        	SlimefunItem sfItem = isSlimefun(i);
	        	if(sfItem!=null) {
	        		if(sfItem.isItem(Items.DISK1)||sfItem.isItem(Items.DISK2)||sfItem.isItem(Items.DISK3)||sfItem.isItem(Items.DISK4)||sfItem.isItem(Items.DISK5)) {
	        			//TODO setdiskId(i);
	        			return true;
	        		}
	        	}
	        	if(sfItem2!=null) {
	        		if(sfItem2.isItem(Items.DISK1)||sfItem2.isItem(Items.DISK2)||sfItem2.isItem(Items.DISK3)||sfItem2.isItem(Items.DISK4)||sfItem2.isItem(Items.DISK5)) {
	        			//TODO setdiskId(p.getItemOnCursor());
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
    public int getSlotTier(String s) {
    	switch(s) {
    	case "SERVER1":
    		return 1;
    	case "SERVER2":
    		return 2;
    	case "SERVER3":
    		return 3;
    	default:
    		return 1;
    	}
    }

	@Override
	String MeType() {

		return "MeStore";
	}

	@Override
	void placeHandler(BlockPlaceEvent e) {		
		
		Block b = e.getBlock();
		BlockStorage.addBlockInfo(b, "Status", "off");
		BlockStorage.addBlockInfo(b, "Drives", "");
		
		addHandlers(BlockStorage.getInventory(b),b);
	}

	@Override
	void breakHandler(BlockBreakEvent e) {
		// TODO Auto-generated method stub
		Block b = e.getBlock();
        BlockMenu inv = BlockStorage.getInventory(b);

        if (inv != null) {
            inv.dropItems(b.getLocation(), slots(getSlotTier(BlockStorage.getLocationInfo(b.getLocation(),"id"))));
        }
        
        BlockStorage.clearBlockInfo(b);
		
	}
	
}

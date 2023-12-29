package me.MeStorage.System;

import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.libraries.dough.chat.ChatInput;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.MeStorage.Items.Heads;
import me.MeStorage.Items.Items;
import me.MeStorage.MEStorage.MeStorage;
import me.MeStorage.MeDisk.MeDisk;
import me.MeStorage.MeNet.MeNet;
import me.MeStorage.Utils.ETInventoryBlock;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import net.md_5.bungee.api.ChatColor;

public class MeImporter extends MeComponent implements ETInventoryBlock{
	
	private final int size;
	private final int[] inputs;
	private final int[] border;
	private final int speed;
	
	private final int settings = 4;
	private int tick = 1;
	
	public MeImporter(SlimefunItemStack item, ItemStack[] recipe,int size,int speed) {
		super(Items.meStorage, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
		this.size = size;
		this.speed = speed;
		
		inputs = getInputsBySize(size);
		border = getBorderBySize(size);
		
		createPreset(this,this::constructMenu,this::newInstance);
		addItemHandler(tick());
		// TODO Auto-generated constructor stub
	}
	
	public BlockTicker tick() {
		return new BlockTicker() {

			@Override
			public boolean isSynchronized() {
				// TODO Auto-generated method stub
				return true;
			}
			
			@Override
			public void uniqueTick() {
				if(speed!=0) {
					tick++;
					if(tick>=speed+1) {
						tick = 1;
					}
				}
		    }
			
			@Override
			public void tick(Block b, SlimefunItem it, Config data) {
				BlockMenu menu = BlockStorage.getInventory(b);
				
				String id =BlockStorage.getLocationInfo(b.getLocation(), "main");
				
				if(id!=null) {
					if(tick==speed) {
						importItems(menu,id,b);
					}
					if(speed==0) {
						importItems(menu,id,b);
					}
				}
			}
		};
	}
	public void importItems(BlockMenu menu,String id,Block b) {
		
		MeNet netw= getNetById(Integer.parseInt(id));
		for(int i:inputs) {
			ItemStack item = menu.getItemInSlot(i);
			//not air
			if(item!=null) {
				try {
					int disk2 = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(),"disk"));
					
					
					item.setAmount(netw.pushItem(item, disk2).getAmount());
					/*if(netw.getDisks().contains(disk2)) {
						MeDisk disk = MeStorage.getDiskManager().getDisk(disk2);
						if(!disk.isFull()) {
							if(disk.pushItem(item)) {
								item.setAmount(0);
								if(speed!=0)return;
							}
						}
					}*/
				}catch(Exception e) {
					
					
					item.setAmount(netw.pushItem(item).getAmount());
					/*for(int diskId:netw.getDisks()) {
					
						MeDisk disk = MeStorage.getDiskManager().getDisk(diskId);
						if(!disk.isFull()) {
							if(disk.pushItem(item)) {
								item.setAmount(0);
								if(speed!=0)return;
							}
						}
					}*/
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void newInstance(BlockMenu menu,Block b) {
		menu.replaceExistingItem(settings, new SlimefunItemStack("SETTINGS_ICON_IMPORTER",
				Heads.SETTINGS,
				ChatColor.YELLOW+"Settings",
				ChatColor.GRAY+"Click to edit Disk to push items in: "+BlockStorage.getLocationInfo(b.getLocation(), "disk")
				));
		menu.addMenuClickHandler(settings,
                (p,s,i,a)->{
                	p.sendMessage(ChatColor.YELLOW+"Type number of disk to input items to (to set any disk just type anythink else)");
                	p.closeInventory();
                	ChatInput.waitForPlayer(MeStorage.instance, p,(message)->{
                		try {
                			BlockStorage.addBlockInfo(b, "disk", Integer.parseInt(message)+"");
                			p.sendMessage(ChatColor.YELLOW+"Disk set to: "+message);
                		}catch(Exception e) {
                			BlockStorage.addBlockInfo(b, "disk", "any");
                			p.sendMessage(ChatColor.YELLOW+"Disk set to: any");
                		}
                		newInstance(menu,b);
                	});
                	return false;});
	}
	
	@SuppressWarnings("deprecation")
	private void constructMenu(BlockMenuPreset preset) {
		for(int i:border) {
			preset.addItem(i, ChestMenuUtils.getInputSlotTexture(),
	                ChestMenuUtils.getEmptyClickHandler());
		}
	}

	@Override
	public int[] getInputSlots() {
		// TODO Auto-generated method stub
		return inputs;
	}

	@Override
	public int[] getOutputSlots() {
		// TODO Auto-generated method stub
		return new int[0];
	}
	
	
	
	public static int[] getInputsBySize(int size) {
		
		switch (size){
			case 9:
				int[] list1= {9,10,11,12,13,14,15,16,17};
				return list1;
			case 18:
				int[] list2= {9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26};
				return list2;
			case 27:
				int[] list3= {9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35};
				return list3;
			default :
				int[] list4= {9,10,11,12,13,14,15,16,17};
				return list4;
		}
	}
	public static int[] getBorderBySize(int size) {
		
		switch (size){
			case 9:
				int[] list1= {0,1,2,3,5,6,7,8,18,19,20,21,22,23,24,25,26};
				return list1;
			case 18:
				int[] list2= {0,1,2,3,5,6,7,8,27,28,29,30,31,32,33,34,35};
				return list2;
			case 27:
				int[] list3= {0,1,2,3,5,6,7,8,36,37,38,39,40,41,42,43,44};
				return list3;
			default :
				int[] list4= {0,1,2,3,5,6,7,8,18,19,20,21,22,23,24,25,26};
				return list4;
		}
	}

	@Override
	String MeType() {
		// TODO Auto-generated method stub
		return "Importer";
	}

	@Override
	void placeHandler(BlockPlaceEvent e) {
		// TODO Auto-generated method stub
		BlockStorage.addBlockInfo(e.getBlock(), "disk", "any");
	}

	@Override
	void breakHandler(BlockBreakEvent e) {
		Block b = e.getBlock();
		
		BlockStorage.getInventory(b).dropItems(b.getLocation(), inputs);
		
	}
}

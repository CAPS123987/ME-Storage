package me.MeStorage.System;

import java.util.Map;

import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
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

public class MeExporter extends MeComponent implements ETInventoryBlock{
	
	private final int size;
	private final int speed;
	
	private final int[] outputs;
	private final int[] border;
	
	private final int settings = 4;
	private int tick = 1;
	private final int exportItem;
	private final int numberItem;

	public MeExporter(SlimefunItemStack item, ItemStack[] recipe,int size,int speed) {
		super(Items.meStorage, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
		// 
		this.size = size;
		this.speed = speed;
		
		outputs = getOutputsBySize(size);
		border = getBorderBySize(size);
		exportItem = getExportItem(size);
		numberItem = getNumberItem(size);
		
		addItemHandler(tick());
		createPreset(this,this::constructMenu,this::newInstance);
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
				tick++;
				if(tick>=speed+1) {
					tick = 1;
				}
		    }
			
			@SuppressWarnings("deprecation")
			@Override
			public void tick(Block b, SlimefunItem it, Config data) {
				
				if(tick!=speed) return;
				
				BlockMenu menu = BlockStorage.getInventory(b);
				
				String id = BlockStorage.getLocationInfo(b.getLocation(), "main");
				
				if(id==null) return;
				
				if(menu.getItemInSlot(exportItem)==null) return;
				
				ItemStack item = menu.getItemInSlot(exportItem).clone();
				
				item.setAmount(Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), "amount")));
				
				if(!menu.fits(item, outputs)) return;
				
				MeNet net= getNetById(Integer.parseInt(id));
				
				try {
					int diskId = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), "disk"));
					exportByNumber(net,item,menu,diskId);
				}catch(Exception e) {
					exportNormal(net,item,menu);
				}
				
			}
		};
	}
	
	private void exportNormal(MeNet net,ItemStack item,BlockMenu menu) {
		for(int diskId:net.getDisks()) {
			MeDisk disk = MeStorage.getDiskManager().getDisk(diskId);
			
			//export
			int able =disk.tryExportItem(item);
			if(able!=0) {
				item.setAmount(able);
				disk.exportItem(item);
				menu.pushItem(item, outputs);
								
				break;
			}
		}
	}
	
	private void exportByNumber(MeNet net,ItemStack item,BlockMenu menu,int number) {
		if(!net.getDisks().contains(number)) return;
		
		MeDisk disk = MeStorage.getDiskManager().getDisk(number);
		
		//export
		int able =disk.tryExportItem(item);
		if(able!=0) {
			item.setAmount(able);
			disk.exportItem(item);
			menu.pushItem(item, outputs);
		}
	}
	
	@SuppressWarnings("deprecation")
	private void constructMenu(BlockMenuPreset preset) {
		for(int i:border) {
			preset.addItem(i, ChestMenuUtils.getOutputSlotTexture(),
	                ChestMenuUtils.getEmptyClickHandler());
		}
	}
	
	@SuppressWarnings("deprecation")
	public void newInstance(BlockMenu menu,Block b) {
		menu.replaceExistingItem(settings, new SlimefunItemStack("SETTINGS_ICON_IMPORTER",
				Heads.SETTINGS,
				ChatColor.YELLOW+"Settings",
				ChatColor.GRAY+"Click to edit Disk to get items from: "+BlockStorage.getLocationInfo(b.getLocation(), "disk")
				));
		menu.addMenuClickHandler(settings,
                (p,s,i,a)->{
                	p.sendMessage(ChatColor.YELLOW+"Type number of disk to get items from (to set any disk just type anythink else)");
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
		
		
		menu.replaceExistingItem(numberItem, new SlimefunItemStack("SETTINGS_NUMBER",
				Heads.SETTINGS,
				ChatColor.YELLOW+"Amount",
				ChatColor.GRAY+"Click to edit Amount of items to export per interval: "+BlockStorage.getLocationInfo(b.getLocation(), "amount")
				));
		menu.addMenuClickHandler(numberItem,
                (p,s,i,a)->{
                	p.sendMessage(ChatColor.YELLOW+"Type Amount of items to export per interval");
                	p.closeInventory();
                	ChatInput.waitForPlayer(MeStorage.instance, p,(message)->{
                		try {
                			int amount = Integer.parseInt(message);
                			if(amount>64) amount = 64;
                			BlockStorage.addBlockInfo(b, "amount", amount+"");
                			p.sendMessage(ChatColor.YELLOW+"Amount set to: "+amount);
                		}catch(Exception e) {
                			BlockStorage.addBlockInfo(b, "amount", 1+"");
                			p.sendMessage(ChatColor.YELLOW+"Amount set to: 1");
                		}
                		newInstance(menu,b);
                		
                	});
                	return false;});
	}
	
	@Override
	String MeType() {
		// TODO Auto-generated method stub
		return "Exporter";
	}

	@Override
	void placeHandler(BlockPlaceEvent e) {
		BlockStorage.addBlockInfo(e.getBlock(), "disk", "any");
		BlockStorage.addBlockInfo(e.getBlock(), "amount", "1");
		
	}

	@Override
	void breakHandler(BlockBreakEvent e) {
		Block b = e.getBlock();
		
		BlockStorage.getInventory(b).dropItems(b.getLocation(), outputs);
		
	}
	
	public static int[] getOutputsBySize(int size) {
		
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
				int[] list1= {0,1,2,3,5,6,7,8,18,19,20,21,22,23,24,25,26,27,28,29,31,33,34,35};
				return list1;
			case 18:
				int[] list2= {0,1,2,3,5,6,7,8,27,28,29,30,31,32,33,34,35,36,37,38,40,42,43,44};
				return list2;
			case 27:
				int[] list3= {0,1,2,3,5,6,7,8,36,37,38,39,40,41,42,43,44,45,46,47,49,51,52,53};
				return list3;
			default :
				int[] list4= {0,1,2,3,5,6,7,8,18,19,20,21,22,23,24,25,26};
				return list4;
		}
	}
	public static int getExportItem(int size) {
		switch (size){
		case 9:
			return 32;
		case 18:
			return 41;
		case 27:
			return 50;
		default :
			return 32;
		}
	}
	public static int getNumberItem(int size) {
		switch (size){
		case 9:
			return 30;
		case 18:
			return 39;
		case 27:
			return 48;
		default :
			return 30;
		}
	}

	@Override
	public int[] getInputSlots() {
		// TODO Auto-generated method stub
		return new int[0];
	}

	@Override
	public int[] getOutputSlots() {
		// TODO Auto-generated method stub
		return outputs;
	}

}

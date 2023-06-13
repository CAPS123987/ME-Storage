package me.MeStorage.System;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.libraries.dough.chat.ChatInput;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.MeStorage.Items.Items;
import me.MeStorage.Utils.MeItemUtils;
import me.MeStorage.MEStorage.MeStorage;
import me.MeStorage.MeDisk.MeDisk;
import me.MeStorage.MeNet.MeNet;
import me.MeStorage.Utils.ETInventoryBlock;
import me.MeStorage.Utils.MeNetUtils;
import me.MeStorage.Utils.ScanNetwork;
import me.MeStorage.Utils.menus;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.AdvancedMenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import net.md_5.bungee.api.ChatColor;

@SuppressWarnings("deprecation")
public class MeStorageControler extends SlimefunItem implements ETInventoryBlock,MeNetUtils,ScanNetwork{
	
	private final int[] border_main = {1,2,3,4,9,14,18,23,27,32,36,41,45,47,48,50};
	private final int[] border_server = {1,2,3,4,5,6,7,8,9,17,18,26,27,35,36,44,45,46,47,48,49,50,51,52,53};
	private final int[] server_slots = {10,11,12,13,14,15,16,	19,20,21,22,23,24,25,	28,29,30,31,32,33,34,	37,38,39,40,41,42};
	private final int[] search_slots = {10,11,12,13,14,15,16,	19,20,21,22,23,24,25,	28,29,30,31,32,33,34,	37,38,39,40,41,42,43};
	private final int[] main_slots = {10,11,12,13,	19,20,21,22,	28,29,30,31,	37,38,39,40};
	private final int page_back = 46;
	private final int page_next = 49;
	private final int page_back_search = 46;
	private final int page_next_search = 52;
	private final int server_inspect = 0;
	private final int disk_inspect = 5;
	private final int server_overflow = 43;
	private final int search = 2;
	private final int buyItem = 22;
	private final int[] inputBorder_main = {6,7,8,15,17,24,26,33,35,42,44,51,52,53};
    private final int[] outputBorder = {};
    private final int errorSlot = 22;
    private final int[] input = {16,25,34,43};
    
    public Map<Block,ItemStack> exportItems = new HashMap<Block,ItemStack>();
    private final AdvancedMenuClickHandler noInput = new ChestMenu.AdvancedMenuClickHandler() {

   

        @Override
        public boolean onClick(Player p, int slot, ItemStack cursor, ClickAction action) {
        	
            return false;
        }

        @Override
        public boolean onClick(InventoryClickEvent e, Player p, int slot, ItemStack cursor,
                               ClickAction action) {
        	
            return cursor == null || cursor.getType() == Material.AIR;
        }
    };

	public MeStorageControler() {
		super(Items.meStorage,Items.MESTORAGECONTROLER,RecipeType.ENHANCED_CRAFTING_TABLE,Items.recipe_TEST_ITEM);
		
		createPreset(this, this::constructMenu,this::newInstance);
		addItemHandler(onPlace(),onBreak(),tick());
	}
	public BlockTicker tick() {
		return new BlockTicker(){
			
			@Override
			public boolean isSynchronized() {
				return true;
			}

			@Override
			public void tick(Block b, SlimefunItem it, Config data) {
				if(BlockStorage.getLocationInfo(b.getLocation(),"menu").equals(menus.MAIN.type)) {
					BlockMenu menu = BlockStorage.getInventory(b);
					for(int i:input) {
						ItemStack item = menu.getItemInSlot(i);
						if(item!=null) {
							String id =BlockStorage.getLocationInfo(b.getLocation(), "net");
							MeNet net= getNetById(Integer.parseInt(id));
							List<Location> servers = net.getServers();
							List<ItemStack> items = new ArrayList<ItemStack>();
							
							
							items.add(new ItemStack(Material.AIR));
							for(Location l:servers) {
								String disks = BlockStorage.getLocationInfo(l, "Drives");
								String[] list = disks.split(",");
								for(String diskId:list) {
									
									if(!diskId.isEmpty()) {
										
										MeDisk disk = MeStorage.getDisk().getDisks().get(Integer.parseInt(diskId));
										if(!disk.isFull()) {
											if(disk.pushItem(item)) {
												newInstance(menu,b);
												menu.replaceExistingItem(i, new ItemStack(Material.AIR));
												return;
											}
										}
									}
								}
							}
							newInstance(menu,b);
						}
					}
				}
			}
			
		};
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
				BlockStorage.addBlockInfo(e.getBlock().getLocation(), "menu", menus.MAIN.type);
				BlockStorage.addBlockInfo(e.getBlock().getLocation(), "page", "1");
				MeStorage.getNet().addNetwork(newNet);
				scanall(e.getBlock().getLocation(),empty,nullLoc);
			}
			
		};
	}
	
	public void newInstance(BlockMenu menu,Block b) {
		menu.addMenuOpeningHandler((p)->{
			newInstance(menu,b);
		});
		clearMenu(menu);
		
		if(MeStorage.hasError()) {
			menu.replaceExistingItem(errorSlot, new CustomItemStack(Material.BARRIER,ChatColor.DARK_RED+"Error occurred while loading server, please contact server admin"));
			return;
		}
		if(BlockStorage.getLocationInfo(b.getLocation(),"menu").equals(menus.DISKS.type)) {
			serverBorder(menu);
			diskPage(menu,b);
			
		}
		if(BlockStorage.getLocationInfo(b.getLocation(),"menu").equals(menus.MAIN.type)) {
			mainBorer(menu);
			mainPage(menu,b);
			
		}
		if(BlockStorage.getLocationInfo(b.getLocation(),"menu").equals(menus.SERVERS.type)) {
			serverBorder(menu);
			serverPage(menu,b);
		}
		if(BlockStorage.getLocationInfo(b.getLocation(),"menu").equals(menus.SEARCH.type)) {
			serverBorder(menu);
			searchPage(menu,b);
		}
		if(BlockStorage.getLocationInfo(b.getLocation(),"menu").equals(menus.EXPORT.type)) {
			if(exportItems.containsKey(b)) {
				backButton(menu,b);
				itemExport(menu,b);
				
			}else {
				BlockStorage.addBlockInfo(b, "menu", menus.MAIN.type);
				newInstance(menu,b);
			}
		}

	}
	
	public void itemExport(BlockMenu menu, Block b){
		ItemStack item = exportItems.get(b);
		menu.replaceExistingItem(buyItem, item);
		
		menu.replaceExistingItem(19,new CustomItemStack(new ItemStack(Material.PURPLE_STAINED_GLASS_PANE,32),ChatColor.LIGHT_PURPLE+"-32"));
		addAmount(menu,b,19,-32);
		menu.replaceExistingItem(20,new CustomItemStack(new ItemStack(Material.PURPLE_STAINED_GLASS_PANE,16),ChatColor.LIGHT_PURPLE+"-16"));
		addAmount(menu,b,20,-16);
		menu.replaceExistingItem(21,new CustomItemStack(new ItemStack(Material.PURPLE_STAINED_GLASS_PANE,1),ChatColor.LIGHT_PURPLE+"-1"));
		addAmount(menu,b,21,-1);
		menu.replaceExistingItem(23,new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,ChatColor.BLUE+"+1"));
		addAmount(menu,b,23,1);
		menu.replaceExistingItem(24,new CustomItemStack(new ItemStack(Material.CYAN_STAINED_GLASS_PANE,16),ChatColor.BLUE+"+16"));
		addAmount(menu,b,24,16);
		menu.replaceExistingItem(25,new CustomItemStack(new ItemStack(Material.CYAN_STAINED_GLASS_PANE,32),ChatColor.BLUE+"+32"));
		addAmount(menu,b,25,32);
		menu.replaceExistingItem(31,new CustomItemStack(new ItemStack(Material.CYAN_STAINED_GLASS_PANE,64),ChatColor.BLUE+"64"));
		addAmount(menu,b,31,64);
		menu.replaceExistingItem(31,new CustomItemStack(Material.CHEST,ChatColor.GREEN+"Export"));
		menu.addMenuClickHandler(31, (p,s,i,a)->{
			String id =BlockStorage.getLocationInfo(b.getLocation(), "net");
			MeNet net= getNetById(Integer.parseInt(id));
			List<Location> servers = net.getServers();
			List<ItemStack> items = new ArrayList<ItemStack>();
			items.add(new ItemStack(Material.AIR));
			//for servers
			for(Location l:servers) {
				String disks = BlockStorage.getLocationInfo(l, "Drives");
				String[] list = disks.split(",");
				
				//for disks
				for(String diskId:list) {
					if(!diskId.isEmpty()) {
						MeDisk disk = MeStorage.getDisk().getDisks().get(Integer.parseInt(diskId));
						
						//export
						int able =disk.tryExportItem(item);
							
						if(able!=0) {
							item.setAmount(able);
							Map<Integer, ItemStack> map= p.getInventory().addItem(item);
							
							if(map.size()>0) {
								p.getWorld().dropItemNaturally(p.getLocation(), map.get(0));
							}
							disk.exportItem(item);
								
							
							break;
						}
						
					}
				}
			}
			
			return false;
		});
	}
	
	public void addAmount(BlockMenu menu,Block b,int slot,int amount) {
		menu.addMenuClickHandler(slot, (p,s,i,a)->{
			
			ItemStack item = exportItems.get(b);
			int newAmount = item.getAmount()+amount;
			if(newAmount>item.getMaxStackSize()) {
				newAmount = item.getMaxStackSize();
			}
			if(newAmount<1) {
				newAmount = 1;
			}
			item.setAmount(newAmount);
			exportItems.replace(b, item);
			newInstance(menu,b);
			return false;
		});
	}
	
	public void searchPage(BlockMenu menu,Block b) {
		backButton(menu,b);
		
		
		String message = BlockStorage.getLocationInfo(b.getLocation(), "search");
		Map<ItemStack,Integer> itemList = new LinkedHashMap<ItemStack,Integer>();
		Map<ItemStack,Integer> items = new LinkedHashMap<ItemStack,Integer>();
		getItems(itemList,b);
		itemList.forEach((item,amount)->{
			String name = item.getItemMeta().getDisplayName();
			if(name==null||name.isEmpty()) {
				name = item.getType().name();
			}
			
			if(StringUtils.containsIgnoreCase(name,message)) {
				items.put(item, amount);
			}
		});
		
		menuNextButtonSearch(menu,b,page_next_search,items);
		
		menuBackButtonSearch(menu,b,page_back_search,items);
		
		menuSearchButton(menu,b,8);
		
		int page = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(),"page"));
		int newPage = (page-1)*28;
		
		
		int count = 1;
		for(Entry<ItemStack,Integer> entry:items.entrySet()) {
			if(count>newPage) {
				ItemStack item1 = entry.getKey();
				int amount = entry.getValue();
				ItemStack item2 = item1.clone();
				ItemMeta meta = item2.getItemMeta();
				List<String> lore = meta.getLore();
				if(lore == null) {lore = new ArrayList<String>();}
				
				lore.add(ChatColor.GRAY+"Amount: "+amount);
				meta.setLore(lore);
				item2.setItemMeta(meta);
				menu.pushItem(item2, search_slots);
				
			}
			if(count==newPage+29) {
				break;
			}
			count++;
			
		}
		itemClick(menu, b, search_slots);
	}
	
	public void diskPage(BlockMenu menu, Block b) {

		backButton(menu,b);
		
		menu.addPlayerInventoryClickHandler((p,s,i,a)->false);
		
		MeNet net = getNetByMain(b.getLocation());
		List<Location> locations = net.getServers();
		int count = 1;
		for(Location l:locations) {
			String disks = BlockStorage.getLocationInfo(l, "Drives");
			String[] list = disks.split(",");
			for(String disk:list) {
				if(!disk.isEmpty()) {
					String name = BlockStorage.getLocationInfo(l, "name");
					SlimefunItemStack item = null;
					MeDisk meDisk = MeStorage.getDisk().getDisks().get(Integer.parseInt(disk));
					if(name == null) {
						item =new SlimefunItemStack("DISK",
								MeItemUtils.DISK_HEAD,
								ChatColor.WHITE+"Disk "+disk,
								ChatColor.GRAY+"Located in server "+count,
								ChatColor.GRAY+"Items: "+meDisk.getCurent()+"/"+meDisk.getCapacity()
						    );
					}else {
						item =new SlimefunItemStack("DISK",
								MeItemUtils.DISK_HEAD,
								ChatColor.WHITE+"Disk "+disk,
								ChatColor.GRAY+"Located in server "+name,
								ChatColor.GRAY+"Items: "+meDisk.getCurent()+"/"+meDisk.getCapacity()
						    );
					}
					ItemStack temp = menu.pushItem(item, server_slots);
					if(temp!=null) {
						menu.replaceExistingItem(server_overflow, new CustomItemStack(Material.BLUE_STAINED_GLASS_PANE,ChatColor.BLUE+"And More"));
						break;
					}
				}
			}
			count++;
		}
		
		for(int i:server_slots) {
			menu.addMenuClickHandler(i,(p,s,i2,a)->false); 
		}
		
		
	}
	
	public void itemClick(BlockMenu menu,Block b,int[] slots) {
		for(int slot:slots) {
			menu.addMenuClickHandler(slot, (p,s,i,a)->{
				BlockStorage.addBlockInfo(b, "menu", menus.EXPORT.type);
				exportItems.put(b, MeItemUtils.removeAmount(i));
				newInstance(menu,b);
				return false;
			});
		}
		for(int i:slots) {
			if(menu.getItemInSlot(i)==null) {
				menu.replaceExistingItem(i, new CustomItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE," "));
				menu.addMenuClickHandler(i,(p,s,i2,a)->false);
			}
		}
	}
	
	public void serverPage(BlockMenu menu,Block b) {
		backButton(menu,b);
		
		menu.addPlayerInventoryClickHandler((p,s,i,a)->false);
		
		MeNet net = getNetByMain(b.getLocation());
		List<Location> locations = net.getServers();
		int count = 1;
		for(Location l:locations) {
			String name = BlockStorage.getLocationInfo(l, "name");
			SlimefunItemStack item = null;
			if(name == null) {
				item =new SlimefunItemStack("SERVER",
						MeItemUtils.SERVER_HEAD,
				        ChatColor.WHITE+"Server "+count +" located at:",
				        ChatColor.GRAY+"X: "+l.getBlockX(),
				        ChatColor.GRAY+"Y: "+l.getBlockY(),
				        ChatColor.GRAY+"Z: "+l.getBlockZ(),
				        ChatColor.GRAY+"Status: "+BlockStorage.getLocationInfo(l, "Status")
				    );
			}else {
				item =new SlimefunItemStack("SERVER",
						MeItemUtils.SERVER_HEAD,
				        ChatColor.WHITE+name +" located at:",
				        ChatColor.GRAY+"X: "+l.getBlockX(),
				        ChatColor.GRAY+"Y: "+l.getBlockY(),
				        ChatColor.GRAY+"Z: "+l.getBlockZ(),
				        ChatColor.GRAY+"Status: "+BlockStorage.getLocationInfo(l, "Status")
				    );
			}
			
			ItemStack temp = menu.pushItem(item, server_slots);
			
			if(temp!=null) {
				menu.replaceExistingItem(server_overflow, new CustomItemStack(Material.BLUE_STAINED_GLASS_PANE,ChatColor.BLUE+"And More"));
				break;
			}
			count ++;
		}
		for(int i:server_slots) {
			menu.addMenuClickHandler(i,(p,s,i2,a)->false); 
		}
		
		
	}
	
	public void mainPage(BlockMenu menu,Block b) {
		listItems(menu,b);
		if(Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(),"page"))>(Math.floor((getSlotsOfDisks(b.getLocation()))-1)/16.0)+1){
			BlockStorage.addBlockInfo(b, "page", "1");
		}
		for(int i:input) {
			menu.addMenuClickHandler(i, (p,s,i2,a)->true);
		}
		menuServerInspectButton(menu, b, server_inspect);
		
		menuDiskInspectButton(menu, b, disk_inspect);
		
		menuNextButton(menu,b,page_next);
		
		menuBackButton(menu,b,page_back);
		
		menuSearchButton(menu, b, search);
		
		itemClick(menu,b,main_slots);
		
		
		menu.addMenuOpeningHandler((p)->newInstance(menu,b));
	}
	
	public void menuNextButton(BlockMenu menu, Block b,int slot) {
		menu.replaceExistingItem(slot,MeItemUtils.getNextButton(Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(),"page")),(int) (Math.floor((getSlotsOfDisks(b.getLocation()))-1)/16.0)+1));
		
		menu.addMenuClickHandler(slot, (p, s, i, a)->{
			int page = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(),"page"));
			int maxPages = (int) (Math.floor((getSlotsOfDisks(b.getLocation()))-1)/16.0)+1;
			if(page+1!=maxPages+1) {
				BlockStorage.addBlockInfo(b, "page", String.valueOf(page+1));
				page = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(),"page"));
			}
			newInstance(menu,b);
			return false;
		});
	}
	
	public void menuNextButtonSearch(BlockMenu menu, Block b,int slot,Map<ItemStack,Integer> map) {
		menu.replaceExistingItem(slot,MeItemUtils.getNextButton(Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(),"page")),(int) (Math.floor((map.size())-1)/28.0)+1));
		
		menu.addMenuClickHandler(slot, (p, s, i, a)->{
			int page = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(),"page"));
			int maxPages = (int) (Math.floor(map.size()-1)/16.0)+1;
			if(page+1!=maxPages+1) {
				BlockStorage.addBlockInfo(b, "page", String.valueOf(page+1));
				page = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(),"page"));
			}
			newInstance(menu,b);
			return false;
		});
	}
	
	public void menuBackButtonSearch(BlockMenu menu, Block b,int slot,Map<ItemStack,Integer> map) {
		menu.replaceExistingItem(slot,MeItemUtils.getPreviousButton(Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(),"page")), (int) (Math.floor(map.size()-1)/28.0)+1));
		
		menu.addMenuClickHandler(slot, (p, s, i, a)->{
			int page = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(),"page"));
			
			if(page-1!=0) {
				BlockStorage.addBlockInfo(b, "page", String.valueOf(page-1));
				page = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(),"page"));
			}
			newInstance(menu,b);
			
			return false;
		});
	}
	
	public void menuBackButton(BlockMenu menu, Block b,int slot) {
		menu.replaceExistingItem(slot,MeItemUtils.getPreviousButton(Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(),"page")), (int) (Math.floor((getSlotsOfDisks(b.getLocation()))-1)/16.0)+1));
		
		menu.addMenuClickHandler(slot, (p, s, i, a)->{
			int page = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(),"page"));
			
			if(page-1!=0) {
				BlockStorage.addBlockInfo(b, "page", String.valueOf(page-1));
				page = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(),"page"));
			}
			newInstance(menu,b);
			
			return false;
		});
	}
	
	public void menuServerInspectButton(BlockMenu menu,Block b,int slot) {
		menu.replaceExistingItem(slot,new CustomItemStack(Material.YELLOW_STAINED_GLASS_PANE,ChatColor.YELLOW+"Inspect current servers",ChatColor.GRAY+"Click to show registered servers"));
		menu.addMenuClickHandler(slot, (p,s,i,a)->{
			BlockStorage.addBlockInfo(b, "menu", menus.SERVERS.type);
			newInstance(menu,b);
			return false;
		});
	}
	
	public void menuDiskInspectButton(BlockMenu menu,Block b,int slot) {
		menu.replaceExistingItem(slot,new CustomItemStack(Material.YELLOW_STAINED_GLASS_PANE,ChatColor.YELLOW+"Inspect current Drives",ChatColor.GRAY+"Click to show registered servers"));
		menu.addMenuClickHandler(slot, (p,s,i,a)->{
			BlockStorage.addBlockInfo(b, "menu", menus.DISKS.type);
			newInstance(menu,b);
			return false;
		});
	}
	
	public void menuSearchButton(BlockMenu menu,Block b,int slot) {
		menu.replaceExistingItem(slot, new CustomItemStack(Material.NAME_TAG,ChatColor.GRAY+"Search for item"));
		menu.addMenuClickHandler(slot, (p,s,i2,a)->{
			p.closeInventory();
			ChatInput.waitForPlayer(MeStorage.instance, p, (message) -> {
				BlockMenu menu2 = BlockStorage.getInventory(b);
				BlockStorage.addBlockInfo(b, "menu", menus.SEARCH.type);
				BlockStorage.addBlockInfo(b, "search", message);
				BlockStorage.addBlockInfo(b, "page", "1");
				newInstance(menu,b);
				menu2.open(p);
			});
			return false;
		});
	}
	
	public void listItems(BlockMenu menu,Block b) {
		
		Map<ItemStack,Integer> itemList = new LinkedHashMap<ItemStack,Integer>();
		int page = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), "page"));
		getItems(itemList,b);
		page = (page-1)*16;
		int current = 1;
		Set<ItemStack> items = itemList.keySet();
		for(ItemStack item2:items) {
			if(current>page) {
				ItemStack item = item2.clone();
				int amount = itemList.get(item);
				ItemMeta meta = item.getItemMeta();
				List<String> lore = meta.getLore();
				if(lore == null) {lore = new ArrayList<String>();}
				
				lore.add(ChatColor.GRAY+"Amount: "+amount);
				//meta.setLore(newLore);
				meta.setLore(lore);
				item.setItemMeta(meta);
				menu.pushItem(item, main_slots);
			}
			current++;
		}
		
	}
	
	public void getItems(Map<ItemStack,Integer> itemList,Block b) {
		String id =BlockStorage.getLocationInfo(b.getLocation(), "net");
		MeNet net= getNetById(Integer.parseInt(id));
		List<Location> servers = net.getServers();
		List<ItemStack> items = new ArrayList<ItemStack>();
		items.add(new ItemStack(Material.AIR));
		//for servers
		for(Location l:servers) {
			String disks = BlockStorage.getLocationInfo(l, "Drives");
			String[] list = disks.split(",");
			
			//for disks
			for(String diskId:list) {
				if(!diskId.isEmpty()) {
					MeDisk disk = MeStorage.getDisk().getDisks().get(Integer.parseInt(diskId));
					
					Map<ItemStack,Integer> diskItems = disk.getItems();
					
					//for items in server
					diskItems.forEach((item,amount1)->{
						
							if(!items.contains(item)) {
								items.add(item);
								itemList.put(item, amount1);
							}else {
								int amount = itemList.get(item);
								itemList.replace(item, amount1+amount);
						}
					});
				}
			}
		}
	}
	
	public void backButton(BlockMenu menu,Block b) {
		menu.replaceExistingItem(server_inspect,new CustomItemStack(Material.YELLOW_STAINED_GLASS_PANE,ChatColor.YELLOW+"Back to main menu",ChatColor.GRAY+"Click to go back to main menu"));
		menu.addMenuClickHandler(server_inspect, (p,s,i,a)->{
			if(exportItems.containsKey(b)) {
				exportItems.remove(b);
			}
			BlockStorage.addBlockInfo(b, "menu", menus.MAIN.type);
			newInstance(menu,b);
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
	public int[] getInputSlots() {
		int[] i = {};
		return i;
	}

	@Override
	public int[] getOutputSlots() {
		int[] i = {};
		return i;
	}
	
	public void clearMenu(BlockMenu menu) {
		menu.addPlayerInventoryClickHandler((p,s,i,a)->true);
		for(int i = 0;i!=54;i++) {
			menu.replaceExistingItem(i, new ItemStack(Material.AIR));
			menu.addMenuClickHandler(i, (p, s, i2, a) -> false);
		}
	}
	
	public double getSlotsOfDisks(Location main) {
		
		String id =BlockStorage.getLocationInfo(main, "net");
		MeNet net= getNetById(Integer.parseInt(id));
		List<Location> servers = net.getServers();
		List<ItemStack> items = new ArrayList<ItemStack>();
		
		items.add(new ItemStack(Material.AIR));
		double count =0;
		for(Location l:servers) {
			String disks = BlockStorage.getLocationInfo(l, "Drives");
			String[] list = disks.split(",");
			for(String diskId:list) {
				
				if(!diskId.isEmpty()) {
					
					MeDisk disk = MeStorage.getDisk().getDisks().get(Integer.parseInt(diskId));
					Map<ItemStack,Integer> diskItems = disk.getItems();
					
					//for items in server
					for(Map.Entry<ItemStack,Integer> entry:diskItems.entrySet()) {
						ItemStack item = entry.getKey();
						if(!items.contains(item)) {
							items.add(item);
							count++;
						}
					}
				}
				
			}
		}
		return count;
	}
	
	private void constructMenu(BlockMenuPreset preset) {
		preset.addItem(53, ChestMenuUtils.getBackground(),
                ChestMenuUtils.getEmptyClickHandler());
	}

	public void mainBorer(BlockMenu menu) {

	
		for (int i : border_main) {
			menu.replaceExistingItem(i, ChestMenuUtils.getBackground());
			menu.addMenuClickHandler(i, ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : inputBorder_main) {
        	menu.replaceExistingItem(i, ChestMenuUtils.getInputSlotTexture());
        	menu.addMenuClickHandler(i, ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : outputBorder) {
        	menu.replaceExistingItem(i, ChestMenuUtils.getOutputSlotTexture());
        	menu.addMenuClickHandler(i, ChestMenuUtils.getEmptyClickHandler());
        }
        
        
        
        for (int i : getOutputSlots()) {
        	menu.addMenuClickHandler(i,noInput); 
        	}

    }

	public void serverBorder(BlockMenu menu) {
		
		for (int i : border_server) {
			menu.replaceExistingItem(i, ChestMenuUtils.getBackground());
			menu.addMenuClickHandler(i, ChestMenuUtils.getEmptyClickHandler());
		}

	}
}
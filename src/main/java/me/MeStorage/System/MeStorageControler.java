package me.MeStorage.System;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
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
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.AdvancedMenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import net.md_5.bungee.api.ChatColor;

@SuppressWarnings("deprecation")
public class MeStorageControler extends SlimefunItem implements ETInventoryBlock,MeNetUtils,ScanNetwork{
	
	private final int[] border_main = {1,2,3,4,9,14,18,23,27,32,36,41,45,47,48,50};
	private final int[] border_server = {1,2,3,4,5,6,7,8,9,17,18,26,27,35,36,44,45,46,47,48,49,50,51,52,53};
	private final int[] server_slots = {10,11,12,13,14,15,16,	19,20,21,22,23,24,25,	28,29,30,31,32,33,34,	37,38,39,40,41,42};
	private final int[] main_slots = {10,11,12,13,	19,20,21,22,	28,29,30,31,	37,38,39,40};
	private final int page_back = 46;
	private final int page_next = 49;
	private final int server_inspect = 0;
	private final int disk_inspect = 5;
	private final int server_overflow = 43;
	private final int[] inputBorder_main = {6,7,8,15,17,24,26,33,35,42,44,51,52,53};
    private final int[] outputBorder = {};
    private final int[] input = {16,25,34,43};
    
    public Map<Location,ItemStack> pendingItems = new HashMap<Location,ItemStack>();
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
		
		if(pendingItems.containsKey(b.getLocation())) {
			
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
					if(name == null) {
						item =new SlimefunItemStack("DISK",
								MeItemUtils.DISK_HEAD,
								ChatColor.WHITE+"Disk "+disk,
								ChatColor.GRAY+"Located in server "+count
						    );
					}else {
						item =new SlimefunItemStack("DISK",
								MeItemUtils.DISK_HEAD,
								ChatColor.WHITE+"Disk "+disk,
								ChatColor.GRAY+"Located in server "+name
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
		menu.replaceExistingItem(server_inspect,new CustomItemStack(Material.YELLOW_STAINED_GLASS_PANE,ChatColor.YELLOW+"Inspect current servers",ChatColor.GRAY+"Click to show registered servers"));
		menu.addMenuClickHandler(server_inspect, (p,s,i,a)->{
			BlockStorage.addBlockInfo(b, "menu", menus.SERVERS.type);
			newInstance(menu,b);
			return false;
		});
		
		menu.replaceExistingItem(disk_inspect,new CustomItemStack(Material.YELLOW_STAINED_GLASS_PANE,ChatColor.YELLOW+"Inspect current Drives",ChatColor.GRAY+"Click to show registered servers"));
		menu.addMenuClickHandler(disk_inspect, (p,s,i,a)->{
			BlockStorage.addBlockInfo(b, "menu", menus.DISKS.type);
			newInstance(menu,b);
			return false;
		});
		
		
		menu.replaceExistingItem(page_next,MeItemUtils.getNextButton(Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(),"page")),(int) (Math.floor((getSlotsOfDisks(b.getLocation()))-1)/16.0)+1));
		
		menu.addMenuClickHandler(page_next, (p, s, i, a)->{
			int page = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(),"page"));
			int maxPages = (int) (Math.floor((getSlotsOfDisks(b.getLocation()))-1)/16.0)+1;
			if(page+1!=maxPages+1) {
				BlockStorage.addBlockInfo(b, "page", String.valueOf(page+1));
				page = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(),"page"));
			}
			newInstance(menu,b);
			return false;
		});
		
		menu.replaceExistingItem(page_back,MeItemUtils.getPreviousButton(Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(),"page")), (int) (Math.floor((getSlotsOfDisks(b.getLocation()))-1)/16.0)+1));
		
		menu.addMenuClickHandler(page_back, (p, s, i, a)->{
			int page = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(),"page"));
			
			if(page-1!=0) {
				BlockStorage.addBlockInfo(b, "page", String.valueOf(page-1));
				page = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(),"page"));
			}
			newInstance(menu,b);
			
			return false;
		});
		
		for(int i:input) {
			menu.replaceExistingItem(i, new CustomItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE,ChatColor.GRAY+"Input",ChatColor.GRAY+"Click with item to put in disk"));
			menu.addMenuClickHandler(i, (p,s,i2,a)->{
				if(p.getItemOnCursor()==null) {
					return false;
				}
				String id =BlockStorage.getLocationInfo(b.getLocation(), "net");
				MeNet net= getNetById(Integer.parseInt(id));
				List<Location> servers = net.getServers();
				List<ItemStack> items = new ArrayList<ItemStack>();
				
				//Bukkit.broadcastMessage(s+" "+menu.getItemInSlot(s).toString());
				items.add(new ItemStack(Material.AIR));
				for(Location l:servers) {
					String disks = BlockStorage.getLocationInfo(l, "Drives");
					String[] list = disks.split(",");
					for(String diskId:list) {
						
						if(!diskId.isEmpty()) {
							
							MeDisk disk = MeStorage.getDisk().getDisks().get(Integer.parseInt(diskId));
							if(!disk.isFull()) {
								if(disk.pushItem(p.getItemOnCursor())) {
									menu.replaceExistingItem(s, new ItemStack(Material.AIR));
									newInstance(menu,b);
									p.setItemOnCursor(new ItemStack(Material.AIR));
									return false;
								}
							}
						}
					}
				}
				newInstance(menu,b);
				return false;
			});
		}
	}
	
	
	/**
	 * this adds items to menu
	 **/
	public void listItems(BlockMenu menu,Block b) {
		
		Map<ItemStack,Integer> itemList = new LinkedHashMap<ItemStack,Integer>();
		int page = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), "page"));
		getItems(itemList,b,page*16);
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
	
	
	/**
	 * this lists items from server
	 **/
	public void getItems(Map<ItemStack,Integer> itemList,Block b,final int page) {
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
						
						if(items.size()<=page) {
							if(!items.contains(item)) {
								items.add(item);
								itemList.put(item, amount1);
							}else {
								int amount = itemList.get(item);
								itemList.replace(item, amount1+amount);
							}
						}
					});
				}
			}
		}
	}
	
	public void backButton(BlockMenu menu,Block b) {
		menu.replaceExistingItem(server_inspect,new CustomItemStack(Material.YELLOW_STAINED_GLASS_PANE,ChatColor.YELLOW+"Back to main menu",ChatColor.GRAY+"Click go back to main menu"));
		menu.addMenuClickHandler(server_inspect, (p,s,i,a)->{
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
					int size =disk.getItems().size();
					count=count+size;
				}
				
			}
		}
		return count;
	}
	
	private void constructMenu(BlockMenuPreset preset) {
		preset.addItem(53, new CustomItemStack(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "),
                ChestMenuUtils.getEmptyClickHandler());
	}

	public void mainBorer(BlockMenu menu) {

	
		for (int i : border_main) {
			menu.replaceExistingItem(i, new CustomItemStack(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "));
			menu.addMenuClickHandler(i, ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : inputBorder_main) {
        	menu.replaceExistingItem(i, new CustomItemStack(new ItemStack(Material.CYAN_STAINED_GLASS_PANE), " "));
        	menu.addMenuClickHandler(i, ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : outputBorder) {
        	menu.replaceExistingItem(i, new CustomItemStack(new ItemStack(Material.ORANGE_STAINED_GLASS_PANE), " "));
        	menu.addMenuClickHandler(i, ChestMenuUtils.getEmptyClickHandler());
        }
        
        
        
        for (int i : getOutputSlots()) {
        	menu.addMenuClickHandler(i,noInput); 
        	}

    }

	public void serverBorder(BlockMenu menu) {
		for(int i = 0;i!=54;i++) {
			menu.replaceExistingItem(i, new ItemStack(Material.AIR));
		}
		
		for (int i : border_server) {
			menu.replaceExistingItem(i, new CustomItemStack(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "));
			menu.addMenuClickHandler(i, ChestMenuUtils.getEmptyClickHandler());
		}

	}
}
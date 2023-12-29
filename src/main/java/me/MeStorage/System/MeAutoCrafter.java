package me.MeStorage.System;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.core.services.MinecraftRecipeService;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
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

@SuppressWarnings("deprecation")
public class MeAutoCrafter extends MeComponent implements ETInventoryBlock,
EnergyNetComponent{
	
	private final int speed = 2;
	
	private int tick = 1;
	
	private static final int[] border = {0,4,5,6,7,8,17,26,35,36,40,41,42,43,44};
	private static final int[] inputBorder = {1,2,3,13,22,31,9,18,27,37,38,39};
    private static final int[] outputBorder = {14,15,16,23,25,32,33,34};
	private static final int[] craftSlots = {10,11,12,19,20,21,28,29,30};
	private static final int outputSlot = 24;
	private static final CustomItemStack emptyCraft = new CustomItemStack(new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE),ChatColor.GOLD+ "Click with item");
	
	private Map<Block, ItemStack[]> recipeTableList = new HashMap<Block, ItemStack[]>();
	private Map<Block, Recipe> recipeList = new HashMap<Block, Recipe>();


	public MeAutoCrafter(SlimefunItemStack item, ItemStack[] recipe) {
		super(Items.meStorage, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
		createPreset(this, this::constructMenu,this::newInstance);
		addItemHandler(tick());
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

			@Override
			public void tick(Block b, SlimefunItem sfItem, Config data) {
				if(tick!=speed) return;
				
				
				
				String id = BlockStorage.getLocationInfo(b.getLocation(), "main");
				
				MeNet net= getNetById(Integer.parseInt(id));
				
				Recipe rec = recipeList.get(b);
				
				if(rec instanceof ShapelessRecipe) {
					for(ItemStack item: ((ShapelessRecipe)rec).getIngredientList()) {
						
					}
				}
				
				if(rec instanceof ShapedRecipe) {
					for(Map.Entry<Character, ItemStack> entry: ((ShapedRecipe)rec).getIngredientMap().entrySet()) {
						
					}
				}
				
				
				
			}
			
		};
	}
	
	private void exportItem(MeNet net, ItemStack item) {
		for(int diskId:net.getDisks()) {
			MeDisk disk = MeStorage.getDiskManager().getDisk(diskId);
			
			//export
			int able =disk.tryExportItem(item);
			if(able!=0) {
				item.setAmount(able);
				disk.exportItem(item);
				//menu.pushItem(item, outputs);
								
				break;
			}
		}
	}

	public void newInstance(BlockMenu menu,Block b) {
		
		registerCurrentCraft(b, menu);
		updateOutput(b,menu);
		
		for(int i : craftSlots) {
			if(menu.getItemInSlot(i)==null) {
				menu.replaceExistingItem(i, emptyCraft);
			}
			menu.addMenuClickHandler(i, (p, slot, item, a)->{
				
				if(item.getType().equals(Material.AIR)) {
					menu.replaceExistingItem(slot, emptyCraft);
				}
				
				updateCrafting(b, slot, p.getItemOnCursor(), menu);
				
        		return false;
        	});
		}
	}
	
	private void updateCrafting(Block b, int slot, ItemStack item, BlockMenu menu) {
		
		ItemStack[] rec = recipeTableList.get(b);
		
		rec[getCraftIndex(slot)] = item;
		
		ItemStack tempItem = item.clone();
		tempItem.setAmount(1);
		if(!tempItem.getType().equals(Material.AIR)) {
			menu.replaceExistingItem(slot, tempItem);
		}else {
			menu.replaceExistingItem(slot, emptyCraft);
		}
		
		for(int i = 0;i<9;i++) {
			if(rec[i]!=null) {
				Bukkit.broadcastMessage(rec[i].getType().name());
			}else {
				Bukkit.broadcastMessage("null");
				
			}
		}
		
		recipeTableList.replace(b, rec);
		
		updateOutput(b,menu);
	}
	
	private void updateOutput(Block b, BlockMenu menu) {
		Recipe recipe = Bukkit.getServer().getCraftingRecipe(recipeTableList.get(b),b.getWorld());
		if(recipe != null) {
			ItemStack result = recipe.getResult();
			
			menu.replaceExistingItem(outputSlot, result);
		}else {
			menu.replaceExistingItem(outputSlot, new ItemStack(Material.AIR));
		}
	}
	
	private void registerCurrentCraft(Block b, BlockMenu menu) {
		
		Bukkit.broadcastMessage("register");
		
		if(!recipeTableList.containsKey(b)) {
			ItemStack[] rec = new ItemStack[9];
			for(int craftSlot:craftSlots) {
				
				if(menu.getItemInSlot(craftSlot)==null) {
					rec[getCraftIndex(craftSlot)]=null;
				}else {
				
					if(!menu.getItemInSlot(craftSlot).getItemMeta().equals(emptyCraft.getItemMeta())) {
						rec[getCraftIndex(craftSlot)]=menu.getItemInSlot(craftSlot);
					}else {
						rec[getCraftIndex(craftSlot)]=null;
					}
					
				}
			}
			recipeTableList.put(b, rec);
		}
	}
	
	private int getCraftIndex(int i) {
		int idx = 0;
		for(int slot:craftSlots) {
			if(slot==i) {
				return idx;
			}
			idx ++;
		}
		return 0;
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
        preset.addItem(outputSlot, ChestMenuUtils.getBackground(),
                ChestMenuUtils.getEmptyClickHandler());
	}
	
	@Override
	@Nonnull
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
		return new int[0];
	}

	@Override
	public int[] getOutputSlots() {
		// TODO Auto-generated method stub
		return new int[0];
	}

	@Override
	String MeType() {
		// TODO Auto-generated method stub
		return "MeAutoCrafter";
	}

	@Override
	void placeHandler(BlockPlaceEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	void breakHandler(BlockBreakEvent e) {
		recipeTableList.remove(e.getBlock());
		
	}

}

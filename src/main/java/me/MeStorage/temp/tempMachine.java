package me.MeStorage.temp;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.MeStorage.Items.Items;
import me.MeStorage.MEStorage.MeStorage;
import me.MeStorage.MeDisk.MeDisk;
import me.MeStorage.Utils.ETInventoryBlock;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

@SuppressWarnings("deprecation")
public class tempMachine extends SlimefunItem implements ETInventoryBlock{
	
	private final int[] border = {0,9};
	private final int[] inputBorder = {};
    private final int[] outputBorder = {};
	
	public tempMachine() {
		super(Items.meStorage,Items.TEST_ITEM,RecipeType.ENHANCED_CRAFTING_TABLE,Items.recipe_TEST_ITEM);
		createPreset(this, this::constructMenu,this::newInstance);
		addItemHandler(tick());
	}

	@Override
	public int[] getInputSlots() {
		// TODO Auto-generated method stub
		
		
		int[] i = {};
		return i;
				
	}

	@Override
	public int[] getOutputSlots() {
		// TODO Auto-generated method stub
		int[] i = {};
		return i;
	}
	public void newInstance(BlockMenu menu,Block b) {
		
	}
	
	public BlockTicker tick() {
		return new BlockTicker(){

			@Override
			public boolean isSynchronized() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void tick(Block b, SlimefunItem it, Config data) {
				// TODO Auto-generated method stub
				BlockMenu menu = BlockStorage.getInventory(b);
				ItemStack item = menu.getItemInSlot(1); 
				
				if(item == null) {
					return;
				}
				if(item.getType().equals(Material.AIR)) {
					return;
				}
				MeDisk disk =MeStorage.getDisk().getDisks().get(1);
				disk.pushItem(item);
				menu.replaceExistingItem(1, new ItemStack(Material.AIR));
				
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

package me.MeStorage.Items;

import java.util.List;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemHandler;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import net.md_5.bungee.api.ChatColor;

public class MeRemoteControler extends SlimefunItem{
	public MeRemoteControler() {
		super(Items.meStorage,Items.MEREMOTECONTROLER,RecipeType.ENHANCED_CRAFTING_TABLE,Items.recipe_TEST_ITEM);
		addItemHandler(onRightClick());
	}
	
	
	
    private ItemHandler onRightClick() {
        return new ItemUseHandler() {

            @Override
            public void onRightClick(PlayerRightClickEvent e) {

                Optional<Block> blockClicked = e.getClickedBlock();           
                Optional<SlimefunItem> sfBlockClicked = e.getSlimefunBlock();
                ItemStack item = e.getItem();
                if (blockClicked.isPresent() && sfBlockClicked.isPresent()) {
                    Location blockLoc = blockClicked.get().getLocation();
                    SlimefunItem sfBlock = sfBlockClicked.get();
                    


                    if (sfBlock != null && sfBlock.getId().equals(Items.MESTORAGECONTROLER.getItemId()) && blockLoc != null) {
                        e.cancel();
                        ItemMeta im = item.getItemMeta();
                        String locationString = LocationToString(blockLoc);
                        
                        List<String> lore = im.getLore();
                        lore.set(1, locationString);
                        im.setLore(lore);
                        
                        item.setItemMeta(im);
                        return;
                        
                    }
                }
                
                try {
                ItemMeta im = item.getItemMeta();
                List<String> lore = im.getLore();
                Location main = StringToLocation(lore.get(1));;
                
                BlockMenu menu = BlockStorage.getInventory(main);
                
                menu.open(e.getPlayer());
                
                }catch(Exception e2) {e.getPlayer().sendMessage(ChatColor.RED+"ERROR");}
                
            } 
        };
    }
    
    
	
	private String LocationToString(Location l) {
        return l.getWorld().getName()+";"+l.getBlockX()+";"+l.getBlockY()+";"+l.getBlockZ();
    }
	
	private static final Location StringToLocation(String locString) {
        String[] locComponents = locString.split(";");
        return new Location(Bukkit.getWorld(locComponents[0]), Double.parseDouble(locComponents[1]), Double.parseDouble(locComponents[2]), Double.parseDouble(locComponents[3]));
    }
}

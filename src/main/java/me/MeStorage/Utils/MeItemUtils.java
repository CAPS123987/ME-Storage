package me.MeStorage.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import me.MeStorage.Items.Items;
import me.MeStorage.MEStorage.MeStorage;

public class MeItemUtils {
	
	private static final ItemStack PREV_BUTTON_ACTIVE = new SlimefunItemStack("_UI_PREVIOUS_ACTIVE", Material.LIME_STAINED_GLASS_PANE, "&r\u21E6 Previous Page");
    private static final ItemStack NEXT_BUTTON_ACTIVE = new SlimefunItemStack("_UI_NEXT_ACTIVE", Material.LIME_STAINED_GLASS_PANE, "&rNext Page \u21E8");
    private static final ItemStack PREV_BUTTON_INACTIVE = new SlimefunItemStack("_UI_PREVIOUS_INACTIVE", Material.BLACK_STAINED_GLASS_PANE, "&8\u21E6 Previous Page");
    private static final ItemStack NEXT_BUTTON_INACTIVE = new SlimefunItemStack("_UI_NEXT_INACTIVE", Material.BLACK_STAINED_GLASS_PANE, "&8Next Page \u21E8");
    public static final String SERVER_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTM4YjA1ZTUwZWYxYzhjMDJjZTMwZDhkMDliODQ2ZTdlOTE0NWFjNDExNzE1N2Y2NTMwYjRkOGUxZTMyOTg1NCJ9fX0=";
    	//https://minecraft-heads.com/custom-heads/decoration/51890-server
    public static final String DISK_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2Y2YzA0NGZmNjA2NmQ0OWZhOTEyYzExZGFiOTU1ZWU1OWZlM2JlN2RjNjM2NjM1MmRkMTBiMDVkM2NlZjU0MSJ9fX0=";
    	//https://minecraft-heads.com/custom-heads/miscellaneous/60402-google-drive
    public static ItemStack getNextButton(int page, int pages) {
        if (pages == 1 || page == pages) {
            return new CustomItemStack(NEXT_BUTTON_INACTIVE, meta -> {
                meta.setLore(Arrays.asList("", ChatColor.GRAY + "(" + page + " / " + pages + ")"));
            });
        } else {
            return new CustomItemStack(NEXT_BUTTON_ACTIVE, meta -> {
                meta.setLore(Arrays.asList("", ChatColor.GRAY + "(" + page + " / " + pages + ")"));
            });
        }
    }
	
    public static ItemStack getPreviousButton(int page, int pages) {
        if (pages == 1 || page == 1) {
            return new CustomItemStack(PREV_BUTTON_INACTIVE, meta -> {
                meta.setLore(Arrays.asList("", ChatColor.GRAY + "(" + page + " / " + pages + ")"));
            });
        } else {
            return new CustomItemStack(PREV_BUTTON_ACTIVE, meta -> {
                meta.setLore(Arrays.asList("", ChatColor.GRAY + "(" + page + " / " + pages + ")"));
            });
        }
    }

	public static int getCapacity(SlimefunItem item) {
		if(item.isItem(Items.DISK1)) {
			return MeStorage.sizeDisk1;
		}
		if(item.isItem(Items.DISK2)) {
			return MeStorage.sizeDisk2;
		}
		if(item.isItem(Items.DISK3)) {
			return MeStorage.sizeDisk3;
		}
		if(item.isItem(Items.DISK4)) {
			return MeStorage.sizeDisk4;
		}
		return 0;
	}
	
}

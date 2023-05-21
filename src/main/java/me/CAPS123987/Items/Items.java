package me.CAPS123987.Items;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import me.CAPS123987.MEStorage.MeStorage;
import net.md_5.bungee.api.ChatColor;

public class Items {
	public static final ItemGroup meStorage = new ItemGroup(new NamespacedKey(MeStorage.getInstance(),
	        "Me_Storage"),
	        new CustomItemStack(Material.COBBLESTONE, "&aMe Storage")
	    );
	public static final SlimefunItemStack TEST_ITEM = new SlimefunItemStack("TEST_ITEM",
			Material.COAL_BLOCK,
	        "TEST_ITEM",
	        ""
	    );
	public static final SlimefunItemStack SERVER1 = new SlimefunItemStack("SERVER1",
			Material.COAL_BLOCK,
	        "SERVER1",
	        ""
	    );
	public static final SlimefunItemStack SERVER2 = new SlimefunItemStack("SERVER2",
			Material.COAL_BLOCK,
	        "SERVER2",
	        ""
	    );
	public static final SlimefunItemStack SERVER3 = new SlimefunItemStack("SERVER3",
			Material.COAL_BLOCK,
	        "SERVER3",
	        
	        ""
	    );
	public static final SlimefunItemStack DRIVE1 = new SlimefunItemStack("DRIVE1",
			Material.LEATHER_HELMET,
	        "DRIVE1",
	        ChatColor.RESET+"Capacity: 128",
	        ChatColor.RED+"No ID"
	    );
	public static final SlimefunItemStack DRIVE2 = new SlimefunItemStack("DRIVE2",
			Material.CHAINMAIL_HELMET,
	        "DRIVE2",
	        ChatColor.RESET+"Capacity: 1 024",
	        ChatColor.RED+"No ID"
	    );
	public static final SlimefunItemStack DRIVE3 = new SlimefunItemStack("DRIVE3",
			Material.IRON_HELMET,
	        "DRIVE3",
	        ChatColor.RESET+"Capacity: 8 192",
	        ChatColor.RED+"No ID"
	    );
	public static final SlimefunItemStack DRIVE4 = new SlimefunItemStack("DRIVE4",
			Material.DIAMOND_HELMET,
	        "DRIVE4",
	        ChatColor.RESET+"Capacity: 65 536",
	        ChatColor.RED+"No ID"
	    );
	public static final ItemStack[] recipe_TEST_ITEM= {
			null,null,null,
			null,new ItemStack(Material.PINK_WOOL),null,
			null,null,null
	};
}

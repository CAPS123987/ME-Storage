package me.MeStorage.Items;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import me.MeStorage.MEStorage.MeStorage;
import net.md_5.bungee.api.ChatColor;

public class Items {
	public static final ItemGroup meStorage = new ItemGroup(new NamespacedKey(MeStorage.getInstance(),
	        "Me_Storage"),
	        new CustomItemStack(Material.COBBLESTONE, "&aMe Storage")
	    );
	public static final SlimefunItemStack TEST_ITEM = new SlimefunItemStack("TEST_ITEM",
			Material.COAL_BLOCK,
	        "TEST_ITEM",
	        "do not use"
	    );
	public static final SlimefunItemStack MESTORAGECONTROLER = new SlimefunItemStack("MESTORAGECONTROLER",
			Material.IRON_BLOCK,
	        "MESTORAGECONTROLER",
	        "WIP(controler)"
	    );
	public static final SlimefunItemStack MECONNECTOR = new SlimefunItemStack("MECONNECTOR",
			Material.POLISHED_DEEPSLATE,
	        "MECONNECTOR",
	        "WIP(connector)"
	    );
	public static final SlimefunItemStack MEINTERFACE = new SlimefunItemStack("MEINTERFACE",
			Material.WARPED_HYPHAE,
	        "MEINTERFACE",
	        "WIP(interface)"
	    );
	public static final SlimefunItemStack MEREMOTECONTROLER = new SlimefunItemStack("MEREMOTECONTROLER",
			Material.HEART_OF_THE_SEA,
	        "MEREMOTECONTROLER",
	        "WIP(RemoteControler)"
	    );
	public static final SlimefunItemStack MEIMPORTER1 = new SlimefunItemStack("MEIMPORTER1",
			Material.LIGHT_BLUE_WOOL,
	        "MEIMPORTER1",
	        "interval: 3 ticks",
	        "WIP(importer)"
	    );
	public static final SlimefunItemStack MEIMPORTER2 = new SlimefunItemStack("MEIMPORTER2",
			Material.LIGHT_BLUE_WOOL,
	        "MEIMPORTER2",
	        "interval: 2 ticks",
	        "WIP(importer)"
	    );
	public static final SlimefunItemStack MEIMPORTER3 = new SlimefunItemStack("MEIMPORTER3",
			Material.LIGHT_BLUE_WOOL,
	        "MEIMPORTER3",
	        "interval: 1 ticks",
	        "WIP(importer)"
	    );
	public static final SlimefunItemStack MEIMPORTER4 = new SlimefunItemStack("MEIMPORTER4",
			Material.LIGHT_GRAY_WOOL,
	        "MEIMPORTER4",
	        "no interval",
	        "WIP(importer)"
	    );
	public static final SlimefunItemStack MEEXPORTER1 = new SlimefunItemStack("MEEXPORTE1",
			Material.RED_WOOL,
	        "MEEXPORTER1",
	        "interval: 3 ticks",
	        "WIP(exporter)"
	    );
	public static final SlimefunItemStack MEEXPORTER2 = new SlimefunItemStack("MEEXPORTE2",
			Material.RED_WOOL,
	        "MEEXPORTER2",
	        "interval: 2 ticks",
	        "WIP(exporter)"
	    );
	public static final SlimefunItemStack MEEXPORTER3 = new SlimefunItemStack("MEEXPORTE3",
			Material.RED_WOOL,
	        "MEEXPORTER3",
	        "interval: 1 ticks",
	        "WIP(exporter)"
	    );
	public static final SlimefunItemStack MECRAFTER = new SlimefunItemStack("MECRAFTER",
			Material.CYAN_WOOL,
	        "MECRAFTER"
	    );
	public static final SlimefunItemStack SERVER1 = new SlimefunItemStack("SERVER1",
			Material.COAL_BLOCK,
	        "SERVER1",
	        "WIP"
	    );
	public static final SlimefunItemStack SERVER2 = new SlimefunItemStack("SERVER2",
			Material.COAL_BLOCK,
	        "SERVER2",
	        "WIP"
	    );
	public static final SlimefunItemStack SERVER3 = new SlimefunItemStack("SERVER3",
			Material.COAL_BLOCK,
	        "SERVER3",
	        "WIP"
	    );
	public static final SlimefunItemStack DISK1 = new SlimefunItemStack("DISK1",
			Material.LEATHER_HELMET,
	        "DISK1",
	        ChatColor.RESET+"Capacity: "+MeStorage.sizeDisk1,
	        ChatColor.RED+"No ID"
	    );
	public static final SlimefunItemStack DISK2 = new SlimefunItemStack("DISK2",
			Material.CHAINMAIL_HELMET,
	        "DISK2",
	        ChatColor.RESET+"Capacity: "+MeStorage.sizeDisk2,
	        ChatColor.RED+"No ID"
	    );
	public static final SlimefunItemStack DISK3 = new SlimefunItemStack("DISK3",
			Material.IRON_HELMET,
	        "DISK3",
	        ChatColor.RESET+"Capacity: "+MeStorage.sizeDisk3,
	        ChatColor.RED+"No ID"
	    );
	public static final SlimefunItemStack DISK4 = new SlimefunItemStack("DISK4",
			Material.DIAMOND_HELMET,
	        "DISK4",
	        ChatColor.RESET+"Capacity: "+MeStorage.sizeDisk4,
	        ChatColor.RED+"No ID"
	    );
	public static final SlimefunItemStack DISK5 = new SlimefunItemStack("DISK5",
			Material.NETHERITE_HELMET,
	        "DISK5",
	        ChatColor.RESET+"Capacity: "+MeStorage.sizeDisk5,
	        ChatColor.RED+"No ID"
	    );
	public static final ItemStack[] recipe_TEST_ITEM= {
			null,null,null,
			null,new ItemStack(Material.PINK_WOOL),null,
			null,null,null
	};
}

package me.CAPS123987.Items;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import me.CAPS123987.MEStorage.MeStorage;

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
	public static final ItemStack[] recipe_TEST_ITEM= {
			null,null,null,
			null,new ItemStack(Material.PINK_WOOL),null,
			null,null,null
	};
}

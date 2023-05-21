package me.MeStorage.MEStorage;

import org.bukkit.plugin.java.JavaPlugin;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import me.MeStorage.Items.Items;
import me.MeStorage.System.DriverServer;
import me.MeStorage.System.MeConnector;
import me.MeStorage.System.MeStorageControler;

public class MeStorage extends JavaPlugin implements SlimefunAddon {
	public static MeStorage instance;
    @Override
    public void onEnable() {
        // Read something from your config.yml
        Config cfg = new Config(this);
        instance = this;
        if (cfg.getBoolean("options.auto-update")) {
            // You could start an Auto-Updater for example
        }
        
        new SlimefunItem(Items.meStorage, Items.DRIVE1, RecipeType.ENHANCED_CRAFTING_TABLE , Items.recipe_TEST_ITEM).register(this);
        new SlimefunItem(Items.meStorage, Items.DRIVE2, RecipeType.ENHANCED_CRAFTING_TABLE , Items.recipe_TEST_ITEM).register(this);
        new SlimefunItem(Items.meStorage, Items.DRIVE3, RecipeType.ENHANCED_CRAFTING_TABLE , Items.recipe_TEST_ITEM).register(this);
        new SlimefunItem(Items.meStorage, Items.DRIVE4, RecipeType.ENHANCED_CRAFTING_TABLE , Items.recipe_TEST_ITEM).register(this);

        new DriverServer(1,Items.SERVER1,Items.recipe_TEST_ITEM).register(this);
        new DriverServer(2,Items.SERVER2,Items.recipe_TEST_ITEM).register(this);
        new DriverServer(3,Items.SERVER3,Items.recipe_TEST_ITEM).register(this);
        new MeStorageControler().register(this);
        new MeConnector().register(this);
    }

    @Override
    public void onDisable() {
        // Logic for disabling the plugin...
    }

    @Override
    public String getBugTrackerURL() {
        // You can return a link to your Bug Tracker instead of null here
        return null;
    }
    public static MeStorage getInstance() {
        return instance;
    }

    @Override
    public JavaPlugin getJavaPlugin() {
        /*
         * You will need to return a reference to your Plugin here.
         * If you are using your main class for this, simply return "this".
         */
        return this;
    }

}

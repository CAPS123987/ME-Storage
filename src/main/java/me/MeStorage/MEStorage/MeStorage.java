package me.MeStorage.MEStorage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import me.MeStorage.Items.Items;
import me.MeStorage.MeNet.MeNet;
import me.MeStorage.MeNet.MeNetManager;
import me.MeStorage.System.DriverServer;
import me.MeStorage.System.MeConnector;
import me.MeStorage.System.MeStorageControler;

public class MeStorage extends JavaPlugin implements SlimefunAddon {
	public static MeStorage instance;
	private static MeNetManager meNetManager;
    @Override
    public void onEnable() {
        // Read something from your config.yml
        Config cfg = new Config(this);
        instance = this;
        if (cfg.getBoolean("options.auto-update")) {
            // You could start an Auto-Updater for example
        }
        
        ConfigurationSerialization.registerClass(MeNet.class);
        
        new SlimefunItem(Items.meStorage, Items.DRIVE1, RecipeType.ENHANCED_CRAFTING_TABLE , Items.recipe_TEST_ITEM).register(this);
        new SlimefunItem(Items.meStorage, Items.DRIVE2, RecipeType.ENHANCED_CRAFTING_TABLE , Items.recipe_TEST_ITEM).register(this);
        new SlimefunItem(Items.meStorage, Items.DRIVE3, RecipeType.ENHANCED_CRAFTING_TABLE , Items.recipe_TEST_ITEM).register(this);
        new SlimefunItem(Items.meStorage, Items.DRIVE4, RecipeType.ENHANCED_CRAFTING_TABLE , Items.recipe_TEST_ITEM).register(this);

        new DriverServer(1,Items.SERVER1,Items.recipe_TEST_ITEM).register(this);
        new DriverServer(2,Items.SERVER2,Items.recipe_TEST_ITEM).register(this);
        new DriverServer(3,Items.SERVER3,Items.recipe_TEST_ITEM).register(this);
        new MeStorageControler().register(this);
        new MeConnector().register(this);
        
        PluginStart();
        
    }
    
    @SuppressWarnings("unchecked")
    private void PluginStart() {
    	File file = new File(MeStorage.instance.getDataFolder(),"Networks.yml");
    	
    	if(file.exists()) {
			
			FileConfiguration yaml=YamlConfiguration.loadConfiguration(file);
			List<MeNet> list = (List<MeNet>) yaml.get("networks");
			meNetManager = new MeNetManager(list);
			
		}else {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			FileConfiguration yaml=YamlConfiguration.loadConfiguration(file);
			List<MeNet> list = new CopyOnWriteArrayList<>();
			
			yaml.set("networks", list);
			try {
				yaml.save(file);
			} catch (IOException e) {
				Bukkit.broadcastMessage(e.toString());
			}
			meNetManager = new MeNetManager(list);
		}
    	
    	
    	
    }
    
    public static MeNetManager getNet() {
    	return meNetManager;
    }

    @Override
    public void onDisable() {
    	onStop();
    }
    
    private void onStop() {
    	saveNets();
    }
    public static void saveNets() {
    	File file = new File(MeStorage.instance.getDataFolder(),"Networks.yml");
    	FileConfiguration yaml=YamlConfiguration.loadConfiguration(file);
    	yaml.set("networks", getNet().getNetworks());
    	try {
			yaml.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
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

package me.MeStorage.MEStorage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import me.MeStorage.AutoSave.AutoSave;
import me.MeStorage.Items.Items;
import me.MeStorage.MeDisk.MeDisk;
import me.MeStorage.MeDisk.MeDiskManager;
import me.MeStorage.MeNet.MeNet;
import me.MeStorage.MeNet.MeNetManager;
import me.MeStorage.System.DiskServer;
import me.MeStorage.System.MeConnector;
import me.MeStorage.System.MeStorageControler;
import me.MeStorage.temp.tempMachine;

public class MeStorage extends JavaPlugin implements SlimefunAddon {
	public static MeStorage instance;
	private static MeNetManager meNetManager;
	private static MeDiskManager meDiskManager;
	private final AutoSave autoSave = new AutoSave();
	private static int maxSize;
	
	public static int sizeDisk1;
	public static int sizeDisk2;
	public static int sizeDisk3;
	public static int sizeDisk4;
    @Override
    public void onEnable() {
        // Read something from your config.yml
        Config cfg = new Config(this);
        instance = this;
        
        sizeDisk1 = cfg.getInt("Disk1Size");
        sizeDisk2 = cfg.getInt("Disk2Size");
        sizeDisk3 = cfg.getInt("Disk3Size");
        sizeDisk4 = cfg.getInt("Disk4Size");
        
        if (cfg.getBoolean("options.auto-update")) {
            // You could start an Auto-Updater for example
        }
        maxSize = cfg.getInt("max-net-size");
        ConfigurationSerialization.registerClass(MeNet.class);
        ConfigurationSerialization.registerClass(MeDisk.class);
        
        new SlimefunItem(Items.meStorage, Items.DISK1, RecipeType.ENHANCED_CRAFTING_TABLE , Items.recipe_TEST_ITEM).register(this);
        new SlimefunItem(Items.meStorage, Items.DISK2, RecipeType.ENHANCED_CRAFTING_TABLE , Items.recipe_TEST_ITEM).register(this);
        new SlimefunItem(Items.meStorage, Items.DISK3, RecipeType.ENHANCED_CRAFTING_TABLE , Items.recipe_TEST_ITEM).register(this);
        new SlimefunItem(Items.meStorage, Items.DISK4, RecipeType.ENHANCED_CRAFTING_TABLE , Items.recipe_TEST_ITEM).register(this);

        new DiskServer(1,Items.SERVER1,Items.recipe_TEST_ITEM).register(this);
        new DiskServer(2,Items.SERVER2,Items.recipe_TEST_ITEM).register(this);
        new DiskServer(3,Items.SERVER3,Items.recipe_TEST_ITEM).register(this);
        new MeStorageControler().register(this);
        new MeConnector().register(this);
        new tempMachine().register(this);
       
        PluginStart(cfg);
        
    }
    
    private void PluginStart(Config cfg) {
    	
    	
    	loadNet(cfg);
    	loadDisk(cfg);
    	autoSave.start(this, cfg.getInt("auto-save-delay-in-minutes"));
    	
    	
    }
    
    @SuppressWarnings("unchecked")
	private void loadNet(Config cfg) {
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
    
    private void loadDisk(Config cfg) {
    	File file = new File(MeStorage.instance.getDataFolder(),"Disk.yml");
    	
    	if(file.exists()) {
			
			FileConfiguration yaml=YamlConfiguration.loadConfiguration(file);
			/*MeDisk test = yaml.getSerializable("disks", MeDisk.class);
			List<Map<?, ?>> tempDisk = yaml.getMapList("disks");
			*/

			meDiskManager = new MeDiskManager(loadHashMap(yaml));
			//arrayToHashMap(tempDisk);
			
			//Map<Integer,MeDisk> list = (Map<Integer,MeDisk>) yaml.getMapList("disks");
			//meDiskManager = new MeDiskManager(list);
			
		}else {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			FileConfiguration yaml=YamlConfiguration.loadConfiguration(file);
			Map<Integer,MeDisk> list = new HashMap<Integer,MeDisk>();
			
			yaml.set("Disks", list);
			saveHashMap(list,yaml);
			try {
				yaml.save(file);
			} catch (IOException e) {
				Bukkit.broadcastMessage(e.toString());
			}
			meDiskManager = new MeDiskManager(list);
		}
    }
    
    public static Map<Integer,MeDisk> arrayToHashMap(List<Map<?, ?>> list){
    	for(Map<?, ?> map :list) {
    		instance.getLogger().log(Level.WARNING,map.toString());
    	}
    	return new HashMap<Integer,MeDisk>();
    }
    
    public static int getMaxSize() {
    	return maxSize;
    }
    
    public static MeNetManager getNet() {
    	return meNetManager;
    }
    
    public static MeDiskManager getDisk() {
    	return meDiskManager;
    }

    @Override
    public void onDisable() {
    	onStop();
    }
    
    private void onStop() {
    	saveNets();
    }
    public static boolean saveNets() {
    	try {
	    	File file = new File(MeStorage.instance.getDataFolder(),"Networks.yml");
	    	FileConfiguration yaml=YamlConfiguration.loadConfiguration(file);
	    	yaml.set("networks", getNet().getNetworks());
	    	try {
				yaml.save(file);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
    	}catch(Exception e) {
    		return false;
    	}
    }
    
    public static boolean saveDisks() {
    	try {
	    	File file = new File(MeStorage.instance.getDataFolder(),"Disk.yml");
	    	FileConfiguration yaml=YamlConfiguration.loadConfiguration(file);
	    	//yaml.set("disks", getDisk().getDisks());
	    	saveHashMap(getDisk().getDisks(),yaml);
	    	try {
				yaml.save(file);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
	    	
    	}catch(Exception e) {
    		return false;
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
    
    public static void saveHashMap(Map<Integer, MeDisk> hm,FileConfiguration config) {
    	for (Integer key : hm.keySet()) {
    		config.set("Disks."+key, hm.get(key));
    		}
    }
    
    public Map<Integer, MeDisk> loadHashMap(FileConfiguration config) {
    	Map<Integer, MeDisk> hm = new HashMap<Integer, MeDisk>();
			for (String key : config.getConfigurationSection("Disks").getKeys(false)) {
				hm.put(Integer.parseInt(key), (MeDisk) config.get("Disks."+key));
			}
    	return hm;
    }
    
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }
    
    public static Logger logger() {
        return instance.getLogger();
    }

}

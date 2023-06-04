package me.MeStorage.AutoSave;

import java.util.logging.Level;

import me.MeStorage.MEStorage.MeStorage;
import net.md_5.bungee.api.ChatColor;

public class AutoSave {
	/**
     * This method starts the {@link AutoSavingService} with the given interval.
     * 
     * @param plugin
     *            The current instance of Slimefun
     * @param interval
     *            The interval in which to run this task
     */
    public void start(MeStorage plugin, int interval) {
        plugin.getServer().getScheduler().runTaskTimer(plugin, this::save, 2000L, interval * 60L * 20L);

    }
    public void save() {
    	
    	
    	if(MeStorage.saveNets()) {
    		MeStorage.logger().log(Level.INFO,"MeNets saved");
    	}else {
    		MeStorage.logger().log(Level.WARNING,ChatColor.RED+"MeNets NOT saved");
    	}
    	if(MeStorage.saveDisks()) {
    		MeStorage.logger().log(Level.INFO,"MeDisks saved");
    	}else {
    		MeStorage.logger().log(Level.WARNING,ChatColor.RED+"MeDisks NOT saved");
    	}
    }
}

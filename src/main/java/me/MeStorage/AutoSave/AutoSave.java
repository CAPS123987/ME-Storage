package me.MeStorage.AutoSave;

import me.MeStorage.MEStorage.MeStorage;

public class AutoSave {
	private int interval;

    /**
     * This method starts the {@link AutoSavingService} with the given interval.
     * 
     * @param plugin
     *            The current instance of Slimefun
     * @param interval
     *            The interval in which to run this task
     */
    public void start(MeStorage plugin, int interval) {
        this.interval = interval;

        plugin.getServer().getScheduler().runTaskTimer(plugin, this::save, 2000L, interval * 60L * 20L);

    }
    public void save() {
    	MeStorage.saveNets();
    }
}

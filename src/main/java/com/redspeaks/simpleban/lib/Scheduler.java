package com.redspeaks.simpleban.lib;

import com.redspeaks.simpleban.SimpleBan;
import org.bukkit.Bukkit;

public class Scheduler {

    public static void runSync(Runnable runnable) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(SimpleBan.getInstance(), runnable);
    }

    public static void runSync(Runnable runnable, long delay) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(SimpleBan.getInstance(), runnable, delay);
    }

    public static void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(SimpleBan.getInstance(), runnable);
    }

    public static void runASyncTimer(Runnable runnable, long delay, long period) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(SimpleBan.getInstance(), runnable, delay, period);
    }

    public static void runSyncTimer(Runnable runnable, long delay, long period) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(SimpleBan.getInstance(), runnable, delay, period);
    }
}

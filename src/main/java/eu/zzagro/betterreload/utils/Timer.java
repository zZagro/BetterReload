package eu.zzagro.betterreload.utils;

import de.ancash.yaml.configuration.file.YamlFile;
import eu.zzagro.betterreload.BetterReload;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Timer {

    private final BetterReload plugin;

    public Timer(BetterReload plugin) {
        this.plugin = plugin;
    }

    private int task;
    private int task2;

    public void playerKickTimer() {
        YamlFile config = plugin.config;
        int timer = config.getInt("ShutdownTimerInSeconds") * 20;
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.kickPlayer(plugin.getConfig().getString("KickMessage").replace('&', '§'));
                }
                plugin.getServer().reload();
            }
        }, timer);
    }

    public void actionBarTimer() {
        YamlFile config = plugin.config;
        final int[] timer = {config.getInt("ShutdownTimerInSeconds")};

        this.task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    plugin.nms.sendActionBar(player, "§cServer restarting in " + timer[0] + " seconds!");
                }
                timer[0]--;

                if (timer[0] == 0) {
                    Bukkit.getScheduler().cancelTask(task);
                }
            }
        }, 0, 20);
    }
}

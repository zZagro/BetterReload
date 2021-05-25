package eu.zzagro.betterreload.listeners;

import de.ancash.yaml.configuration.file.YamlFile;
import eu.zzagro.betterreload.BetterReload;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

public class PlayerLoginListener implements Listener {

    private final BetterReload plugin;

    public PlayerLoginListener(BetterReload plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        YamlFile config = plugin.config;
        Player player = e.getPlayer();
        if (plugin.getUptime() <= config.getInt("PlayerUnableToJoinTime")) {
            if (!(player.hasPermission(config.getString("Permissions.JoinBypass")))) {
                e.disallow(PlayerLoginEvent.Result.KICK_OTHER, config.getString("KickMessage").replace('&', '§'));
            }
        }
    }
}

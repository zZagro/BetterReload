package eu.zzagro.betterreload.commands;

import de.ancash.yaml.configuration.file.YamlFile;
import eu.zzagro.betterreload.BetterReload;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ServerReloadCmd implements CommandExecutor {

    private final BetterReload plugin;

    public ServerReloadCmd(BetterReload plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        YamlFile config = plugin.config;
        String prefix = ChatColor.translateAlternateColorCodes('&', config.getString("Prefix") + " ");
        String noPerms = ChatColor.translateAlternateColorCodes('&', config.getString("NoPermission"));
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission(config.getString("Permissions.ServerReload"))) {
                if (command.getName().equalsIgnoreCase("server")) {
                    if (args.length == 1) {
                        if (args[0].equalsIgnoreCase("reload")) {
                            plugin.timer.playerKickTimer();
                            plugin.timer.actionBarTimer();
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                plugin.nms.sendTitle(p, config.getString("ServerReloadAnnouncement").replace('&', '§').replace("%seconds%", String.valueOf(config.getInt("ShutdownTimerInSeconds"))), 100);
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}

package eu.zzagro.betterreload.commands;

import de.ancash.yaml.configuration.file.YamlFile;
import eu.zzagro.betterreload.BetterReload;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class SetTimerCmd implements CommandExecutor {

    private final BetterReload plugin;

    public SetTimerCmd(BetterReload plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        YamlFile config = plugin.config;
        String prefix = ChatColor.translateAlternateColorCodes('&', config.getString("Prefix") + " ");
        String noPerms = ChatColor.translateAlternateColorCodes('&', config.getString("NoPermission"));
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission(config.getString("Permissions.SetTimer"))) {
                if (command.getName().equalsIgnoreCase("settimer")) {
                    if (args.length == 1) {
                        if (isInt(args[0])) {
                            config.set("ShutdownTimerInSeconds", Integer.parseInt(args[0]));
                            player.sendMessage(prefix + plugin.color("&aTime set to &6" + args[0] + " &aseconds!"));
                            try {
                                config.save();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            player.sendMessage(prefix + plugin.color("&cThe time has to be a number"));
                        }
                    } else {
                        player.sendMessage(prefix + plugin.color("&cInvalid format!"));
                    }
                }
            } else {
                player.sendMessage(prefix + noPerms);
            }
        }
        return false;
    }

    public static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}

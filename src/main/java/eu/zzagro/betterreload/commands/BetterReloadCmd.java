package eu.zzagro.betterreload.commands;

import de.ancash.yaml.configuration.file.YamlFile;
import de.ancash.yaml.exceptions.InvalidConfigurationException;
import eu.zzagro.betterreload.BetterReload;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class BetterReloadCmd implements CommandExecutor {

    private final BetterReload plugin;

    public BetterReloadCmd(BetterReload plugin) {
        this.plugin = plugin;
    }

    File configFile = new File("plugins/BetterReload/config.yml");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        YamlFile config = plugin.config;
        String prefix = ChatColor.translateAlternateColorCodes('&', config.getString("Prefix") + " ");
        String noPerms = ChatColor.translateAlternateColorCodes('&', config.getString("NoPermission"));
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission(config.getString("Permissions.BetterReload"))) {
                if (command.getName().equalsIgnoreCase("betterreload")) {
                    if (args.length == 0) {
                        player.sendMessage(prefix + plugin.color("&6/betterreload reload &a- Reloads the config.yml\n&6/betterreload help &a- Shows all available commands!"));
                    } else if (args.length == 1) {
                        if (args[0].equalsIgnoreCase("reload")) {
                            try {
                                config.load();
                            } catch (IOException | InvalidConfigurationException e) {
                                e.printStackTrace();
                            }

                            player.sendMessage(plugin.color(prefix + "&aConfig reloaded!"));
                        } else if (args[0].equalsIgnoreCase("help")) {
                            player.sendMessage(plugin.color(prefix + "&aAvailable commands:\n&6/server reload &a- Reload the Server after with a timer and kick all players before!\n&6/settimer <seconds> &a- Sets the server reload timer!"));
                        } else {
                            player.sendMessage(plugin.color(prefix + "&c Wrong usage!"));
                        }
                    } else {
                        player.sendMessage(plugin.color(prefix + "&c Wrong usage!"));
                    }
                }
            } else {
                player.sendMessage(prefix + noPerms);
            }
        }
        return false;
    }
}

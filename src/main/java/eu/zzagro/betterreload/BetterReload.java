package eu.zzagro.betterreload;

import de.ancash.misc.FileUtils;
import de.ancash.yaml.configuration.file.YamlFile;
import de.ancash.yaml.exceptions.InvalidConfigurationException;
import eu.zzagro.betterreload.commands.BetterReloadCmd;
import eu.zzagro.betterreload.commands.ServerReloadCmd;
import eu.zzagro.betterreload.commands.SetTimerCmd;
import eu.zzagro.betterreload.listeners.PlayerLoginListener;
import eu.zzagro.betterreload.utils.NMS;
import eu.zzagro.betterreload.utils.Timer;
import org.bukkit.ChatColor;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public final class BetterReload extends JavaPlugin {

    public YamlFile config = new YamlFile(new File("plugins/BetterReload/config.yml"));

    public long serverStart;

    public Timer timer = new Timer(this);
    public NMS nms = new NMS(this);
    public PlayerLoginListener loginListener = new PlayerLoginListener(this);

    @Override
    public void onEnable() {
        serverStart = System.currentTimeMillis();

        getCommand("betterreload").setExecutor(new BetterReloadCmd(this));
        getCommand("server").setExecutor(new ServerReloadCmd(this));
        getCommand("settimer").setExecutor(new SetTimerCmd(this));

        getServer().getPluginManager().registerEvents(new PlayerLoginListener(this), this);

        createConfig();
        permissionAdd();
    }

    @Override
    public void onDisable() {

    }

    public void createConfig() {
        try {
            if (!(config.exists())) {
                FileUtils.copyInputStreamToFile(getResource("config.yml"), new File("plugins/BetterReload/config.yml"));
            }
            config.createOrLoad();
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

    public String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public void permissionAdd() {
        PluginManager pm = getServer().getPluginManager();
        Set<Permission> permissions = pm.getPermissions();

        if (config.exists()) {
            Permission betterReload = new Permission(config.getString("Permissions.BetterReload"));
            if (!permissions.contains(betterReload)) {
                pm.addPermission(betterReload);
            }
            Permission serverReload = new Permission(config.getString("Permissions.ServerReload"));
            if (!permissions.contains(serverReload)) {
                pm.addPermission(serverReload);
            }
            Permission setTimer = new Permission(config.getString("Permissions.SetTimer"));
            if (!permissions.contains(setTimer)) {
                pm.addPermission(setTimer);
            }
            Permission joinBypass = new Permission(config.getString("Permissions.JoinBypass"));
            if (!permissions.contains(joinBypass)) {
                pm.addPermission(joinBypass);
            }
        }
    }

    public int getUptime() {
        long now = System.currentTimeMillis();
        long diff = now - serverStart;

        return (int) (diff / 1000 % 60);
    }
}

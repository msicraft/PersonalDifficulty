package me.msicraft.personaldifficulty;

import me.msicraft.personaldifficulty.API.UpdateChecker;
import me.msicraft.personaldifficulty.API.Util.DifficultyUtil;
import me.msicraft.personaldifficulty.API.Util.PlayerUtil;
import me.msicraft.personaldifficulty.Command.MainCommand;
import me.msicraft.personaldifficulty.Command.MainTabComplete;
import me.msicraft.personaldifficulty.DataFile.MessageDataFile;
import me.msicraft.personaldifficulty.Event.BasicEvent;
import me.msicraft.personaldifficulty.Event.DamageRelated;
import me.msicraft.personaldifficulty.Event.ExpRelated;
import me.msicraft.personaldifficulty.bStats.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class PersonalDifficulty extends JavaPlugin {

    private static PersonalDifficulty plugin;
    public static PersonalDifficulty getPlugin() { return plugin; }

    public static String getPrefix() {
        return "[Personal Difficulty]";
    }

    public static MessageDataFile messageConfig;

    @Override
    public void onEnable() {
        createConfigFiles();
        plugin = this;
        messageConfig = new MessageDataFile(this);
        final int configVersion = plugin.getConfig().contains("config-version", true) ? plugin.getConfig().getInt("config-version") : -1;
        if (configVersion != 1) {
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getPrefix() + ChatColor.RED + " You are using the old config");
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getPrefix() + ChatColor.RED + " Created the latest config.yml after replacing the old config.yml with config_old.yml");
            replaceConfig();
            createConfigFiles();
        } else {
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getPrefix() + " You are using the latest version of config.yml");
        }
        reloadDataFiles();
        eventRegister();
        getCommand("personaldifficulty").setExecutor(new MainCommand());
        getCommand("personaldifficulty").setTabCompleter(new MainTabComplete());
        new UpdateChecker(this, -1).getPluginUpdateCheck(version -> {
            if (this.getDescription().getVersion().equals(version)) {
                getLogger().info("There is not a new update available.");
            } else {
                getLogger().info("A new version of the plugin is available: (v" + version + "), Current: v" + getDescription().getVersion());
                getLogger().info("If the current version is higher, it is the development version.");
            }
        });
        new Metrics(this, 19236);
        getLogger().info("Enabled metrics. You may opt-out by changing plugins/bStats/config.yml");
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isOnline()) {
                PlayerUtil.addPlayerData(player);
            }
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + getPrefix() + " Plugin Enable");
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerUtil.savePlayerData(player);
        }
        for (String difficultyName : DifficultyUtil.getDifficultyNames()) {
            DifficultyUtil.saveCustomDifficulty(difficultyName);
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + getPrefix() + ChatColor.RED +" Plugin Disable");
    }

    private void eventRegister() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new BasicEvent(), this);
        pluginManager.registerEvents(new DamageRelated(), this);
        pluginManager.registerEvents(new ExpRelated(), this);
    }

    public void reloadDataFiles() {
        reloadConfig();
        DifficultyUtil.setUp();
    }

    protected FileConfiguration config;

    private void createConfigFiles() {
        File configf = new File(getDataFolder(), "config.yml");
        if (!configf.exists()) {
            configf.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }
        FileConfiguration config = new YamlConfiguration();
        try {
            config.load(configf);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void replaceConfig() {
        File file = new File(getDataFolder(), "config.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        File config_old = new File(getDataFolder(),"config_old-" + dateFormat.format(date) + ".yml");
        file.renameTo(config_old);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getPrefix() + " Plugin replaced the old config.yml with config_old.yml and created a new config.yml");
    }

}

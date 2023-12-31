package me.msicraft.personaldifficulty.DataFile;

import me.msicraft.personaldifficulty.API.Data.CustomDifficulty;
import me.msicraft.personaldifficulty.PersonalDifficulty;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class PlayerDataFile {

    private static final String folderpath = PersonalDifficulty.getPlugin().getDataFolder() + File.separator + "PlayerData";
    private File file;
    private FileConfiguration config;

    public File getFile() { return this.file; }
    public FileConfiguration getConfig() { return this.config; }
    public boolean hasConfigFile() { return this.file.exists(); }

    public static boolean hasFile(UUID uuid) {
        String fileS = uuid.toString() + ".yml";
        File file = new File(folderpath, fileS);
        return file.exists();
    }

    public static File[] getDataFileList() {
        File folder = new File(folderpath);
        return folder.listFiles();
    }

    public PlayerDataFile(Player player) {
        String fileS = player.getUniqueId() + ".yml";
        this.file = new File(folderpath, fileS);
        if (!file.exists()) {
            createFile(player);
        }
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    public PlayerDataFile(UUID uuid) {
        String fileS = uuid.toString() + ".yml";
        this.file = new File(folderpath, fileS);
        if (!file.exists()) {
            createFile(uuid);
        }
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    public void createFile(Player player) {
        if(!this.file.exists()) {
            try {
                YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(this.file);
                yamlConfiguration.set("Name", player.getName());
                yamlConfiguration.set("Difficulty", CustomDifficulty.BasicDifficulty.basic.name());
                yamlConfiguration.save(this.file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void createFile(UUID uuid) {
        if(!this.file.exists()) {
            try {
                YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(this.file);
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                yamlConfiguration.set("Name", offlinePlayer.getName());
                yamlConfiguration.set("Difficulty", CustomDifficulty.BasicDifficulty.basic.name());
                yamlConfiguration.save(this.file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void reloadConfig() {
        if(this.config == null) {
            //Bukkit.getConsoleSender().sendMessage("파일이 존재하지 않음");
            return;
        }
        this.config = YamlConfiguration.loadConfiguration(this.file);
        Reader defaultConfigStream;
        try {
            defaultConfigStream = new InputStreamReader(PersonalDifficulty.getPlugin().getResource(this.file.getName()), StandardCharsets.UTF_8);
            if(defaultConfigStream != null) {
                YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(defaultConfigStream);
                config.setDefaults(defaultConfig);
            }
        } catch(NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    public void saveConfig() {
        try {
            getConfig().save(this.file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveDefaultConfig(Player player) {
        String fileS = player.getUniqueId() + ".yml";
        if (this.file == null) {
            this.file = new File(folderpath, fileS);
        }
        if (!this.file.exists()) {
            PersonalDifficulty.getPlugin().saveResource(fileS, false);
        }
    }

}

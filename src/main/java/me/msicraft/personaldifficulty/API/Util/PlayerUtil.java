package me.msicraft.personaldifficulty.API.Util;

import me.msicraft.personaldifficulty.API.Data.CustomDifficulty;
import me.msicraft.personaldifficulty.API.Data.PlayerData;
import me.msicraft.personaldifficulty.DataFile.PlayerDataFile;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerUtil {

    private static final Map<UUID, PlayerData> playerDataMap = new HashMap<>();

    public static void addPlayerData(Player player) {
        UUID uuid = player.getUniqueId();
        if (!playerDataMap.containsKey(uuid)) {
            playerDataMap.put(uuid, new PlayerData(player));
            checkDifficulty(player);
        }
    }

    public static void removePlayerData(Player player) {
        UUID uuid = player.getUniqueId();
        savePlayerData(playerDataMap.get(uuid));
        playerDataMap.remove(uuid);
    }

    public static PlayerData getPlayerData(Player player) {
        UUID uuid = player.getUniqueId();
        PlayerData playerData;
        if (playerDataMap.containsKey(uuid)) {
            playerData = playerDataMap.get(uuid);
        } else {
            playerData = new PlayerData(player);
            playerDataMap.put(uuid, playerData);
        }
        return playerData;
    }

    public static void savePlayerData(Player player) {
        checkDifficulty(player);
        PlayerData playerData = getPlayerData(player);
        PlayerDataFile playerDataFile = playerData.getPlayerDataFile();
        FileConfiguration config = playerDataFile.getConfig();
        config.set("Difficulty", playerData.getDifficultyName());
        playerDataFile.saveConfig();
    }

    public static void savePlayerData(PlayerData playerData) {
        checkDifficulty(playerData);
        PlayerDataFile playerDataFile = playerData.getPlayerDataFile();
        FileConfiguration config = playerDataFile.getConfig();
        config.set("Difficulty", playerData.getDifficultyName());
        playerDataFile.saveConfig();
    }

    public static void checkDifficulty(Player player) {
        PlayerData playerData = getPlayerData(player);
        String name = playerData.getDifficultyName();
        if (!DifficultyUtil.hasDifficultyName(name)) {
            playerData.setDifficultyName(CustomDifficulty.basicDifficulty.basic.name());
        }
    }

    public static void checkDifficulty(PlayerData playerData) {
        String name = playerData.getDifficultyName();
        if (!DifficultyUtil.hasDifficultyName(name)) {
            playerData.setDifficultyName(CustomDifficulty.basicDifficulty.basic.name());
        }
    }

}

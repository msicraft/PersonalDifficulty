package me.msicraft.personaldifficulty.API.Data;

import me.msicraft.personaldifficulty.DataFile.PlayerDataFile;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class PlayerData {

    private Player player;
    private PlayerDataFile playerDataFile;
    private String difficultyName;

    public PlayerData(Player player) {
        this.player = player;
        this.playerDataFile = new PlayerDataFile(player);
        FileConfiguration config = playerDataFile.getConfig();
        difficultyName = config.contains("Difficulty") ? config.getString("Difficulty") : CustomDifficulty.BasicDifficulty.basic.name();
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerDataFile getPlayerDataFile() {
        return playerDataFile;
    }

    public String getDifficultyName() {
        return difficultyName;
    }

    public void setDifficultyName(String difficultyName) {
        this.difficultyName = difficultyName;
    }

}

package me.msicraft.personaldifficulty.Event;

import me.msicraft.personaldifficulty.API.CustomEvent.PlayerDifficultyChangeEvent;
import me.msicraft.personaldifficulty.API.Data.PlayerData;
import me.msicraft.personaldifficulty.API.Util.PlayerUtil;
import me.msicraft.personaldifficulty.DataFile.PlayerDataFile;
import me.msicraft.personaldifficulty.GUI.Event.ChatEditEvent;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BasicEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        PlayerUtil.addPlayerData(player);
        ChatEditEvent.removeEditKeys(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        PlayerUtil.removePlayerData(player);
        ChatEditEvent.removeEditKeys(player);
    }

    @EventHandler
    public void changeDifficulty(PlayerDifficultyChangeEvent e) {
        OfflinePlayer offlinePlayer = e.getPlayer();
        if (offlinePlayer.isOnline()) {
            PlayerData playerData = PlayerUtil.getPlayerData(offlinePlayer.getPlayer());
            playerData.setDifficultyName(e.getAfterDifficulty());
        } else {
            PlayerDataFile playerDataFile = new PlayerDataFile(offlinePlayer.getUniqueId());
            playerDataFile.getConfig().set("Difficulty", e.getAfterDifficulty());
            playerDataFile.saveConfig();
        }
    }

}

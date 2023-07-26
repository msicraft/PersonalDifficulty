package me.msicraft.personaldifficulty.Event;

import me.msicraft.personaldifficulty.API.CustomEvent.PlayerDifficultyChangeEvent;
import me.msicraft.personaldifficulty.API.Data.PlayerData;
import me.msicraft.personaldifficulty.API.Util.PlayerUtil;
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
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        PlayerUtil.removePlayerData(player);
    }

    @EventHandler
    public void changeDifficulty(PlayerDifficultyChangeEvent e) {
        Player player = e.getPlayer();
        PlayerData playerData = PlayerUtil.getPlayerData(player);
        playerData.setDifficultyName(e.getAfterDifficulty());
    }

}

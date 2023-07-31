package me.msicraft.personaldifficulty.API.CustomEvent;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerDifficultyChangeEvent extends Event {

    private final static HandlerList handlers = new HandlerList();

    private OfflinePlayer offlinePlayer;
    private String beforeDifficulty;
    private String afterDifficulty;

    public PlayerDifficultyChangeEvent(OfflinePlayer offlinePlayer, String beforeDifficulty, String afterDifficulty) {
        this.offlinePlayer= offlinePlayer;
        this.beforeDifficulty = beforeDifficulty;
        this.afterDifficulty = afterDifficulty;
    }

    public OfflinePlayer getPlayer() {
        return offlinePlayer;
    }

    public String getBeforeDifficulty() {
        return beforeDifficulty;
    }

    public String getAfterDifficulty() {
        return afterDifficulty;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}

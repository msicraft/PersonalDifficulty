package me.msicraft.personaldifficulty.API.CustomEvent;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerDifficultyChangeEvent extends Event {

    private final static HandlerList handlers = new HandlerList();

    private Player player;
    private String beforeDifficulty;
    private String afterDifficulty;

    public PlayerDifficultyChangeEvent(Player player, String beforeDifficulty, String afterDifficulty) {
        this.player = player;
        this.beforeDifficulty = beforeDifficulty;
        this.afterDifficulty = afterDifficulty;
    }

    public Player getPlayer() {
        return player;
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

package me.msicraft.personaldifficulty.Event;

import me.msicraft.personaldifficulty.API.Data.CustomDifficulty;
import me.msicraft.personaldifficulty.API.Data.PlayerData;
import me.msicraft.personaldifficulty.API.Util.DifficultyUtil;
import me.msicraft.personaldifficulty.API.Util.PlayerUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

public class ExpRelated implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerGetExp(PlayerExpChangeEvent e) {
        Player player = e.getPlayer();
        PlayerData playerData = PlayerUtil.getPlayerData(player);
        if (playerData.getDifficultyName().equals(CustomDifficulty.BasicDifficulty.basic.name())) {
            return;
        }
        CustomDifficulty customDifficulty = DifficultyUtil.getCustomDifficulty(playerData.getDifficultyName());
        double multiplier = Double.parseDouble(customDifficulty.getObjectValue(CustomDifficulty.OptionVariables.ExpMultiplier).toString());
        int amount = e.getAmount();
        double roundCal = Math.round(amount * multiplier * 10.0) / 10.0;
        int cal = (int) Math.round(roundCal);
        e.setAmount(cal);
    }

}

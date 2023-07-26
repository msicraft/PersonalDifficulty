package me.msicraft.personaldifficulty.Event;

import me.msicraft.personaldifficulty.API.Data.CustomDifficulty;
import me.msicraft.personaldifficulty.API.Data.PlayerData;
import me.msicraft.personaldifficulty.API.Util.DifficultyUtil;
import me.msicraft.personaldifficulty.API.Util.PlayerUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageRelated implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAttack(EntityDamageByEntityEvent e) {
        Entity damager = e.getDamager();
        if (damager instanceof Player) {
            Player player = (Player) damager;
            PlayerData playerData = PlayerUtil.getPlayerData(player);
            if (playerData.getDifficultyName().equals(CustomDifficulty.basicDifficulty.basic.name())) {
                return;
            }
            double multiplier = DifficultyUtil.getCustomDifficulty(playerData.getDifficultyName()).getAttackDamageMultiplier();
            double originalDamage = e.getDamage();
            double cal = originalDamage * multiplier;
            e.setDamage(cal);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamaged(EntityDamageByEntityEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            PlayerData playerData = PlayerUtil.getPlayerData(player);
            if (playerData.getDifficultyName().equals(CustomDifficulty.basicDifficulty.basic.name())) {
                return;
            }
            double multiplier = DifficultyUtil.getCustomDifficulty(playerData.getDifficultyName()).getDamageTakenMultiplier();
            double originalDamage = e.getDamage();
            double cal = originalDamage * multiplier;
            e.setDamage(cal);
        }
    }

}

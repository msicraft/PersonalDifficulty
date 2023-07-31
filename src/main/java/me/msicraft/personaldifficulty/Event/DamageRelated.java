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
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageRelated implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAttack(EntityDamageByEntityEvent e) {
        Entity damager = e.getDamager();
        if (damager instanceof Player) {
            Player player = (Player) damager;
            PlayerData playerData = PlayerUtil.getPlayerData(player);
            if (playerData.getDifficultyName().equals(CustomDifficulty.BasicDifficulty.basic.name())) {
                return;
            }
            CustomDifficulty customDifficulty = DifficultyUtil.getCustomDifficulty(playerData.getDifficultyName());
            double originalDamage = e.getDamage();
            double attackDamageMultiplier = Double.parseDouble(customDifficulty.getObjectValue(CustomDifficulty.OptionVariables.AttackDamageMultiplier).toString());
            double cal = originalDamage * attackDamageMultiplier;
            e.setDamage(cal);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamaged(EntityDamageByEntityEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            PlayerData playerData = PlayerUtil.getPlayerData(player);
            if (playerData.getDifficultyName().equals(CustomDifficulty.BasicDifficulty.basic.name())) {
                return;
            }
            CustomDifficulty customDifficulty = DifficultyUtil.getCustomDifficulty(playerData.getDifficultyName());
            EntityDamageEvent.DamageCause damageCause = e.getCause();
            double originalDamage = e.getDamage();
            if (DifficultyUtil.getEnvironmentalDamageList().contains(damageCause)) {
                double environmentalDamageMultiplier = Double.parseDouble(customDifficulty.getObjectValue(CustomDifficulty.OptionVariables.EnvironmentMultiplier).toString());
                originalDamage = originalDamage * environmentalDamageMultiplier;
            }
            double takenDamageMultiplier = Double.parseDouble(customDifficulty.getObjectValue(CustomDifficulty.OptionVariables.DamageTakenMultiplier).toString());
            double cal = originalDamage * takenDamageMultiplier;
            e.setDamage(cal);
        }
    }

}

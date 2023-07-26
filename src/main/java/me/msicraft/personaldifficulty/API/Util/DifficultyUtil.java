package me.msicraft.personaldifficulty.API.Util;

import me.msicraft.personaldifficulty.API.Data.CustomDifficulty;
import me.msicraft.personaldifficulty.PersonalDifficulty;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class DifficultyUtil {

    private static final CustomDifficulty basicDifficulty = new CustomDifficulty(CustomDifficulty.basicDifficulty.basic.name());

    private static final Map<String, CustomDifficulty> difficultyMap = new HashMap<>();

    private static final List<String> difficultyNames = new ArrayList<>();
    public static List<String> getDifficultyNames() { return difficultyNames; }
    public static boolean hasDifficultyName(String name) { return difficultyNames.contains(name); }

    public static void setUp() {
        if (!difficultyMap.isEmpty()) {
            difficultyMap.clear();
        }
        if (!difficultyNames.isEmpty()) {
            difficultyNames.clear();
        }
        ConfigurationSection section = PersonalDifficulty.getPlugin().getConfig().getConfigurationSection("Difficulty");
        if (section != null) {
            Set<String> stringSet = section.getKeys(false);
            difficultyNames.addAll(stringSet);
        }
        for (String name : difficultyNames) {
            registerDifficulty(name);
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + PersonalDifficulty.getPrefix() + ChatColor.WHITE +
                " A total of " + ChatColor.GREEN  + difficultyNames.size() + ChatColor.WHITE + " difficulties have been registered");
    }

    public static void saveCustomDifficulty(String name) {
        if (difficultyMap.containsKey(name)) {
            CustomDifficulty customDifficulty = getCustomDifficulty(name);
            saveCustomDifficulty(customDifficulty);
        }
    }

    public static void saveCustomDifficulty(CustomDifficulty customDifficulty) {
        String path = "Difficulty." + customDifficulty.getName();
        PersonalDifficulty.getPlugin().getConfig().set(path + ".DamageTakenMultiplier", customDifficulty.getDamageTakenMultiplier());
        PersonalDifficulty.getPlugin().getConfig().set(path + ".AttackDamageMultiplier", customDifficulty.getAttackDamageMultiplier());
        PersonalDifficulty.getPlugin().getConfig().set(path + ".ExpMultiplier", customDifficulty.getExpMultiplier());
        PersonalDifficulty.getPlugin().saveConfig();
    }

    public static void registerDifficulty(String name) {
        CustomDifficulty customDifficulty = new CustomDifficulty(name);
        difficultyMap.put(name, customDifficulty);
        if (!difficultyNames.contains(name)) {
            difficultyNames.add(name);
            saveCustomDifficulty(customDifficulty);
        }
    }

    public static void unregisterDifficulty(String name) {
        if (hasDifficultyName(name)) {
            CustomDifficulty customDifficulty = getCustomDifficulty(name);
            String path = "Difficulty." + customDifficulty.getName();
            if (PersonalDifficulty.getPlugin().getConfig().contains(path)) {
                PersonalDifficulty.getPlugin().getConfig().set(path, null);
                PersonalDifficulty.getPlugin().saveConfig();
            }
            difficultyNames.removeIf(s -> s.equals(name));
            difficultyMap.remove(name);
        }
    }

    public static CustomDifficulty getCustomDifficulty(String name) {
        CustomDifficulty customDifficulty = basicDifficulty;
        if (difficultyMap.containsKey(name)) {
            customDifficulty = difficultyMap.get(name);
        }
        return customDifficulty;
    }

}

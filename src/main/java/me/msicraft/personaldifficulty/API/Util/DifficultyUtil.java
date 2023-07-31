package me.msicraft.personaldifficulty.API.Util;

import me.msicraft.personaldifficulty.API.Data.CustomDifficulty;
import me.msicraft.personaldifficulty.PersonalDifficulty;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.*;

public class DifficultyUtil {

    private static final CustomDifficulty basicDifficulty = new CustomDifficulty(CustomDifficulty.BasicDifficulty.basic.name());

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
        setEnvironmentalDamageList();
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + PersonalDifficulty.getPrefix() + ChatColor.WHITE +
                " A total of " + ChatColor.GREEN  + difficultyNames.size() + ChatColor.WHITE + " difficulties have been registered");
    }

    public static void saveCustomDifficulty(String name) {
        if (difficultyMap.containsKey(name)) {
            CustomDifficulty customDifficulty = getCustomDifficulty(name);
            for (CustomDifficulty.OptionVariables optionVariable : CustomDifficulty.OptionVariables.values()) {
                saveCustomDifficulty(customDifficulty, optionVariable);
            }
            PersonalDifficulty.getPlugin().saveConfig();
        }
    }

    public static void saveCustomDifficulty(CustomDifficulty customDifficulty, CustomDifficulty.OptionVariables optionVariable) {
        String path = "Difficulty." + customDifficulty.getName() + "." + optionVariable.name();
        Object value = customDifficulty.getObjectValue(optionVariable);
        switch (optionVariable.getType()) {
            case String:
                PersonalDifficulty.getPlugin().getConfig().set(path, value.toString());
                break;
            case Double:
                PersonalDifficulty.getPlugin().getConfig().set(path, Double.parseDouble(value.toString()));
                break;
            case Integer:
                PersonalDifficulty.getPlugin().getConfig().set(path, Integer.parseInt(value.toString()));
                break;
            case Boolean:
                PersonalDifficulty.getPlugin().getConfig().set(path, Boolean.parseBoolean(value.toString()));
                break;
        }
    }

    public static void registerDifficulty(String name) {
        CustomDifficulty customDifficulty = new CustomDifficulty(name);
        difficultyMap.put(name, customDifficulty);
        if (!difficultyNames.contains(name)) {
            difficultyNames.add(name);
            saveCustomDifficulty(name);
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

    private static final List<EntityDamageEvent.DamageCause> environmentalDamageList = new ArrayList<>();

    public static List<EntityDamageEvent.DamageCause> getEnvironmentalDamageList() {
        return environmentalDamageList;
    }

    private static void setEnvironmentalDamageList() {
        if (!environmentalDamageList.isEmpty()) {
            environmentalDamageList.clear();
        }
        if (PersonalDifficulty.getPlugin().getConfig().contains("EnvironmentalDamageList")) {
            List<String> list = PersonalDifficulty.getPlugin().getConfig().getStringList("EnvironmentalDamageList");
            EntityDamageEvent.DamageCause damageCause;
            for (String s : list) {
                try {
                    damageCause = EntityDamageEvent.DamageCause.valueOf(s.toUpperCase());
                    environmentalDamageList.add(damageCause);
                } catch (IllegalArgumentException ignored) {
                }
            }
        }
    }

}

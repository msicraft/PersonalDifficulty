package me.msicraft.personaldifficulty.API.Data;

import me.msicraft.personaldifficulty.PersonalDifficulty;

import java.util.Arrays;
import java.util.List;

public class CustomDifficulty {

    public enum basicDifficulty {
        basic
    }

    private static final List<String> variables = Arrays.asList("DamageTakenMultiplier", "AttackDamageMultiplier", "ExpMultiplier");
    public static List<String> getVariables() { return variables; }

    private String name;

    private double damageTakenMultiplier = 1.0;
    private double attackDamageMultiplier = 1.0;

    private double expMultiplier = 1.0;

    public CustomDifficulty(String name) {
        this.name = name;
        if (!name.equals(basicDifficulty.basic.name())) {
            if (PersonalDifficulty.getPlugin().getConfig().contains("Difficulty." + name)) {
                for (String var : variables) {
                    String path = "Difficulty." + name + "." + var;
                    if (PersonalDifficulty.getPlugin().getConfig().contains(path)) {
                        switch (var) {
                            case "DamageTakenMultiplier":
                                damageTakenMultiplier = PersonalDifficulty.getPlugin().getConfig().getDouble(path);
                                break;
                            case "AttackDamageMultiplier":
                                attackDamageMultiplier = PersonalDifficulty.getPlugin().getConfig().getDouble(path);
                                break;
                            case "ExpMultiplier":
                                expMultiplier = PersonalDifficulty.getPlugin().getConfig().getDouble(path);
                                break;
                        }
                    }
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    public double getDamageTakenMultiplier() {
        return damageTakenMultiplier;
    }

    public double getAttackDamageMultiplier() {
        return attackDamageMultiplier;
    }

    public double getExpMultiplier() {
        return expMultiplier;
    }

    public void setDamageTakenMultiplier(double damageTakenMultiplier) {
        this.damageTakenMultiplier = damageTakenMultiplier;
    }

    public void setAttackDamageMultiplier(double attackDamageMultiplier) {
        this.attackDamageMultiplier = attackDamageMultiplier;
    }

    public void setExpMultiplier(double expMultiplier) {
        this.expMultiplier = expMultiplier;
    }
}

package me.msicraft.personaldifficulty.API.Data;

import me.msicraft.personaldifficulty.PersonalDifficulty;

import java.util.HashMap;
import java.util.Map;

public class CustomDifficulty {

    public enum BasicDifficulty {
        basic
    }

    public enum OptionVariablesType {
        String, Double, Integer, Boolean
    }

    //private static final List<String> variables = Arrays.asList("DamageTakenMultiplier", "AttackDamageMultiplier", "ExpMultiplier");
    //public static List<String> getVariables() { return variables; }

    public enum OptionVariables {
        DamageTakenMultiplier(OptionVariablesType.Double),
        AttackDamageMultiplier(OptionVariablesType.Double),
        ExpMultiplier(OptionVariablesType.Double),
        EnvironmentMultiplier(OptionVariablesType.Double);

        final private OptionVariablesType type;
        public OptionVariablesType getType() {
            return type;
        }
        OptionVariables(OptionVariablesType type) {
            this.type = type;
        }
    }

    private String name;

    private final Map<OptionVariables, Object> optionMap = new HashMap<>();

    public Map<OptionVariables, Object> getOptionMap() {
        return optionMap;
    }

    public CustomDifficulty(String name) {
        this.name = name;
        if (!name.equals(BasicDifficulty.basic.name())) {
            if (PersonalDifficulty.getPlugin().getConfig().contains("Difficulty." + name)) {
                for (OptionVariables var : OptionVariables.values()) {
                    String path = "Difficulty." + name + "." + var.name();
                    if (PersonalDifficulty.getPlugin().getConfig().contains(path)) {
                        optionMap.put(var, PersonalDifficulty.getPlugin().getConfig().get(path));
                    } else {
                        Object o = null;
                        switch (var.getType()) {
                            case String:
                                o = "UnKnown";
                                break;
                            case Double:
                                o = 1.0;
                                break;
                            case Integer:
                                o = 1;
                                break;
                            case Boolean:
                                o = false;
                                break;
                        }
                        optionMap.put(var, o);
                    }
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    public Object getObjectValue(OptionVariables optionVariable) {
        Object o = null;
        if (optionMap.containsKey(optionVariable)) {
            o = optionMap.get(optionVariable);
        }
        return o;
    }

    public void setObjectValue(OptionVariables optionVariable, Object o) {
        optionMap.put(optionVariable, o);
    }

}

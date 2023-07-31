package me.msicraft.personaldifficulty.Command;

import me.msicraft.personaldifficulty.API.Data.CustomDifficulty;
import me.msicraft.personaldifficulty.API.Util.DifficultyUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class MainTabComplete implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("personaldifficulty")) {
            if (args.length == 1) {
                List<String> arguments = new ArrayList<>();
                arguments.add("help");
                arguments.add("reload");
                arguments.add("difficulty");
                arguments.add("get");
                arguments.add("set");
                arguments.add("register");
                arguments.add("unregister");
                arguments.add("gui");
                return arguments;
            }
            if (args.length == 2) {
                if (args[0].equals("difficulty") || args[0].equals("unregister")) {
                    return new ArrayList<>(DifficultyUtil.getDifficultyNames());
                }
            }
            if (args.length == 3) {
                String name = args[1];
                if (DifficultyUtil.hasDifficultyName(name)) {
                    List<String> arguments = new ArrayList<>();
                    arguments.add("get");
                    arguments.add("set");
                    return arguments;
                }
            }
            if (args.length == 3 && args[0].equals("set")) {
                return new ArrayList<>(DifficultyUtil.getDifficultyNames());
            }
            if (args.length == 4) {
                String name = args[1];
                if (DifficultyUtil.hasDifficultyName(name)) {
                    if (args[2].equals("get") || args[2].equals("set")) {
                        List<String> arguments = new ArrayList<>();
                        for (CustomDifficulty.OptionVariables var : CustomDifficulty.OptionVariables.values()) {
                            arguments.add(var.name());
                        }
                        return arguments;
                    }
                }
            }
            if (args.length == 5) {
                String name = args[1];
                if (DifficultyUtil.hasDifficultyName(name)) {
                    if (args[2].equals("set")) {
                        List<String> arguments = new ArrayList<>();
                        arguments.add("[value]");
                        return arguments;
                    }
                }
            }
        }
        return null;
    }
}

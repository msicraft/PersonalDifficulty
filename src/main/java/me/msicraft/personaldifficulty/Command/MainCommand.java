package me.msicraft.personaldifficulty.Command;

import me.msicraft.personaldifficulty.API.CustomEvent.PlayerDifficultyChangeEvent;
import me.msicraft.personaldifficulty.API.Data.CustomDifficulty;
import me.msicraft.personaldifficulty.API.Data.PlayerData;
import me.msicraft.personaldifficulty.API.Util.DifficultyUtil;
import me.msicraft.personaldifficulty.API.Util.MessageUtil;
import me.msicraft.personaldifficulty.API.Util.PlayerUtil;
import me.msicraft.personaldifficulty.GUI.MainGui;
import me.msicraft.personaldifficulty.PersonalDifficulty;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand implements CommandExecutor {

    private void sendPermissionMessage(CommandSender commandSender) {
        String message = MessageUtil.getPermissionErrorMessage();
        if (message != null && !message.equals("")) {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("personaldifficulty")) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "/personaldifficulty help");
            }
            if (args.length >= 1) {
                String var = args[0];
                Player target = null;
                PlayerData playerData = null;
                if (var != null) {
                    switch (var) {
                        case "help":
                            if (!sender.hasPermission("personaldifficulty.command.help")) {
                                sendPermissionMessage(sender);
                                return true;
                            }
                            if (args.length == 1) {
                                sender.sendMessage(ChatColor.YELLOW + "/personaldifficulty help : " + ChatColor.WHITE + "List of plugin commands");
                                sender.sendMessage(ChatColor.YELLOW + "/personaldifficulty reload : " + ChatColor.WHITE + "Reload the plugin config files");
                                return true;
                            }
                            break;
                        case "reload":
                            if (args.length == 1) {
                                if (!sender.hasPermission("personaldifficulty.command.reload")) {
                                    sendPermissionMessage(sender);
                                    return true;
                                }
                                PersonalDifficulty.getPlugin().reloadDataFiles();
                                sender.sendMessage(PersonalDifficulty.getPrefix() + ChatColor.GREEN + " Plugin config files reloaded");
                                return true;
                            }
                            break;
                        case "get":
                            if (!sender.hasPermission("personaldifficulty.command.get")) {
                                sendPermissionMessage(sender);
                                return true;
                            }
                            try {
                                target = Bukkit.getPlayer(args[1]);
                            } catch (ArrayIndexOutOfBoundsException e) {
                                if (sender instanceof Player) {
                                    target = (Player) sender;
                                }
                            }
                            if (target != null) {
                                playerData = PlayerUtil.getPlayerData(target);
                                sender.sendMessage(ChatColor.GREEN + "Target player: " + ChatColor.GRAY + target.getName());
                                sender.sendMessage(ChatColor.GREEN + "Difficulty: " + ChatColor.GRAY + playerData.getDifficultyName());
                            }
                            break;
                        case "set":
                            if (!sender.hasPermission("personaldifficulty.command.set")) {
                                sendPermissionMessage(sender);
                                return true;
                            }
                            String difficulty = CustomDifficulty.BasicDifficulty.basic.name();
                            try {
                                target = Bukkit.getPlayer(args[1]);
                                difficulty = args[2];
                            } catch (ArrayIndexOutOfBoundsException e) {
                                if (sender instanceof Player) {
                                    target = (Player) sender;
                                }
                            }
                            if (!DifficultyUtil.hasDifficultyName(difficulty)) {
                                sender.sendMessage(ChatColor.RED + "Difficulty does not exist");
                                return true;
                            } else {
                                if (target != null) {
                                    playerData = PlayerUtil.getPlayerData(target);
                                    sender.sendMessage(ChatColor.GREEN + "Target player: " + ChatColor.GRAY + target.getName());
                                    sender.sendMessage(ChatColor.GREEN + "Before: " + ChatColor.GRAY + playerData.getDifficultyName());
                                    sender.sendMessage(ChatColor.GREEN + "After: " + ChatColor.GRAY + difficulty);
                                    Bukkit.getPluginManager().callEvent(new PlayerDifficultyChangeEvent(target, playerData.getDifficultyName(), difficulty));
                                    return true;
                                }
                            }
                            break;
                        case "register": //pd register [difficultyname]
                            if (!sender.hasPermission("personaldifficulty.command.register")) {
                                sendPermissionMessage(sender);
                                return true;
                            }
                            try {
                                String difficultyName = args[1];
                                if (DifficultyUtil.hasDifficultyName(difficultyName)) {
                                    sender.sendMessage(ChatColor.RED + "Difficulty already exists");
                                    return true;
                                } else {
                                    sender.sendMessage(ChatColor.GREEN + "Difficulty " + ChatColor.GRAY + difficultyName + ChatColor.GREEN + " has been added");
                                    DifficultyUtil.registerDifficulty(difficultyName);
                                }
                            } catch (ArrayIndexOutOfBoundsException e) {
                                sender.sendMessage(ChatColor.RED + "/pd register [difficultyname]");
                            }
                            break;
                        case "unregister": //pd unregister [difficultyname]
                            if (!sender.hasPermission("personaldifficulty.command.unregister")) {
                                sendPermissionMessage(sender);
                                return true;
                            }
                            try {
                                String difficultyName = args[1];
                                if (DifficultyUtil.hasDifficultyName(difficultyName)) {
                                    sender.sendMessage(ChatColor.GREEN + "Difficulty " + ChatColor.GRAY + difficultyName + ChatColor.GREEN + " has been removed");
                                    DifficultyUtil.unregisterDifficulty(difficultyName);
                                } else {
                                    sender.sendMessage(ChatColor.RED + "Difficulty does not exist");
                                }
                                return true;
                            } catch (ArrayIndexOutOfBoundsException e) {
                                sender.sendMessage(ChatColor.RED + "/pd unregister [difficultyname]");
                            }
                            break;
                        case "gui": //pd gui
                            if (!sender.hasPermission("personaldifficulty.command.gui")) {
                                sendPermissionMessage(sender);
                                return true;
                            }
                            if (sender instanceof Player) {
                                Player player = (Player) sender;
                                MainGui mainGui = new MainGui();
                                player.openInventory(mainGui.getInventory());
                                mainGui.setUp();
                            }
                            break;
                        case "difficulty": //pd difficulty [customDifficultyName] [get,set] [variable] [{optional}value]
                            if (!sender.hasPermission("personaldifficulty.command.difficulty")) {
                                sendPermissionMessage(sender);
                                return true;
                            }
                            String difficultyName;
                            String arg2 = null;
                            CustomDifficulty.OptionVariables variable = null;
                            try {
                                difficultyName = args[1];
                                arg2 = args[2];
                                variable = CustomDifficulty.OptionVariables.valueOf(args[3]);
                            } catch (ArrayIndexOutOfBoundsException e) {
                                difficultyName = CustomDifficulty.BasicDifficulty.basic.name();
                            }
                            if (difficultyName.equals(CustomDifficulty.BasicDifficulty.basic.name())) {
                                sender.sendMessage(ChatColor.RED + "/pd difficulty [customDifficultyName] [get,set] [variable] [{optional}value]");
                                return true;
                            }
                            if (!sender.hasPermission("personaldifficulty.command.difficulty." + difficultyName)) {
                                sendPermissionMessage(sender);
                                return true;
                            }
                            if (DifficultyUtil.hasDifficultyName(difficultyName)) {
                                CustomDifficulty customDifficulty = DifficultyUtil.getCustomDifficulty(difficultyName);
                                sender.sendMessage(ChatColor.GREEN + "Difficulty Name: " + ChatColor.GRAY + customDifficulty.getName());
                                switch (arg2) {
                                    case "get":
                                        sendDifficultyInfo(sender, variable, customDifficulty.getObjectValue(variable));
                                        break;
                                    case "set":
                                        Object value;
                                        try {
                                            value = args[4];
                                        } catch (ArrayIndexOutOfBoundsException e) {
                                            value = 1.0;
                                        }
                                        Object object = customDifficulty.getObjectValue(variable);
                                        customDifficulty.setObjectValue(variable, value);
                                        sendDifficultyChangeMessage(sender, variable, object, value);
                                        break;
                                }
                            } else {
                                sender.sendMessage(ChatColor.RED + "Difficulty does not exist");
                            }
                            return true;
                    }
                }
            }
        }
        return false;
    }

    private void sendDifficultyInfo(CommandSender sender, CustomDifficulty.OptionVariables optionVariables, Object value) {
        sender.sendMessage(ChatColor.GREEN + optionVariables.name() + ": " + ChatColor.GRAY + value);
    }

    private void sendDifficultyChangeMessage(CommandSender sender, CustomDifficulty.OptionVariables optionVariables, Object before, Object after) {
        sender.sendMessage(ChatColor.GREEN + "Before " + optionVariables.name() + ": " + ChatColor.GRAY + before);
        sender.sendMessage(ChatColor.GREEN + "After " + optionVariables.name() + ": " + ChatColor.GRAY + after);
    }

}

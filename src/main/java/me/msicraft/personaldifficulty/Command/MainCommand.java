package me.msicraft.personaldifficulty.Command;

import me.msicraft.personaldifficulty.API.CustomEvent.PlayerDifficultyChangeEvent;
import me.msicraft.personaldifficulty.API.Data.CustomDifficulty;
import me.msicraft.personaldifficulty.API.Data.PlayerData;
import me.msicraft.personaldifficulty.API.Util.DifficultyUtil;
import me.msicraft.personaldifficulty.API.Util.MessageUtil;
import me.msicraft.personaldifficulty.API.Util.PlayerUtil;
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
                            String difficulty = CustomDifficulty.basicDifficulty.basic.name();
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
                        case "difficulty": //pd difficulty [customDifficultyName] [get,set] [variable] [{optional}value]
                            if (!sender.hasPermission("personaldifficulty.command.difficulty")) {
                                sendPermissionMessage(sender);
                                return true;
                            }
                            String difficultyName;
                            String arg2 = null;
                            String variable = null;
                            try {
                                difficultyName = args[1];
                                arg2 = args[2];
                                variable = args[3];
                            } catch (ArrayIndexOutOfBoundsException e) {
                                difficultyName = CustomDifficulty.basicDifficulty.basic.name();
                            }
                            if (difficultyName.equals(CustomDifficulty.basicDifficulty.basic.name())) {
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
                                        switch (variable) {
                                            case "DamageTakenMultiplier":
                                                sender.sendMessage(ChatColor.GREEN + variable + ": " + ChatColor.GRAY + customDifficulty.getDamageTakenMultiplier());
                                                break;
                                            case "AttackDamageMultiplier":
                                                sender.sendMessage(ChatColor.GREEN + variable + ": " + ChatColor.GRAY + customDifficulty.getAttackDamageMultiplier());
                                                break;
                                            case "ExpMultiplier":
                                                sender.sendMessage(ChatColor.GREEN + variable + ": " + ChatColor.GRAY + customDifficulty.getExpMultiplier());
                                                break;
                                        }
                                        break;
                                    case "set":
                                        double value;
                                        try {
                                            String s = args[4];
                                            value = Double.parseDouble(s);
                                        } catch (ArrayIndexOutOfBoundsException e) {
                                            value = 1.0;
                                        }
                                        double before;
                                        switch (variable) {
                                            case "DamageTakenMultiplier":
                                                before = customDifficulty.getDamageTakenMultiplier();
                                                sender.sendMessage(ChatColor.GREEN + "Before " + variable + ": " + ChatColor.GRAY + before);
                                                sender.sendMessage(ChatColor.GREEN + "After " + variable + ": " + ChatColor.GRAY + value);
                                                customDifficulty.setDamageTakenMultiplier(value);
                                                break;
                                            case "AttackDamageMultiplier":
                                                before = customDifficulty.getAttackDamageMultiplier();
                                                sender.sendMessage(ChatColor.GREEN + "Before " + variable + ": " + ChatColor.GRAY + before);
                                                sender.sendMessage(ChatColor.GREEN + "After " + variable + ": " + ChatColor.GRAY + value);
                                                customDifficulty.setAttackDamageMultiplier(value);
                                                break;
                                            case "ExpMultiplier":
                                                before = customDifficulty.getExpMultiplier();
                                                sender.sendMessage(ChatColor.GREEN + "Before " + variable + ": " + ChatColor.GRAY + before);
                                                sender.sendMessage(ChatColor.GREEN + "After " + variable + ": " + ChatColor.GRAY + value);
                                                customDifficulty.setExpMultiplier(value);
                                                break;
                                        }
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

}

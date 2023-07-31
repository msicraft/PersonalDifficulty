package me.msicraft.personaldifficulty.GUI.Event;

import me.msicraft.personaldifficulty.API.CustomEvent.PlayerDifficultyChangeEvent;
import me.msicraft.personaldifficulty.API.Data.CustomDifficulty;
import me.msicraft.personaldifficulty.API.Util.DifficultyUtil;
import me.msicraft.personaldifficulty.API.Util.PlayerUtil;
import me.msicraft.personaldifficulty.GUI.DifficultyEditorGui;
import me.msicraft.personaldifficulty.GUI.PlayerGui;
import me.msicraft.personaldifficulty.PersonalDifficulty;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class ChatEditEvent implements Listener {

    private static final String editDifficultyKey = "PD_EditDifficulty_ToPlayer";
    private static final String editOptionKey = "PD_EditDifficultyOption";

    public static void removeEditKeys(Player player) {
        if (ChatEditEvent.hasEditDifficultyTag(player)) {
            ChatEditEvent.setEditDifficultyKey(player, false, null);
        }
        if (ChatEditEvent.hasEditOptionTag(player)) {
            ChatEditEvent.setEditOptionKey(player, false, null);
        }
    }

    public static boolean hasEditDifficultyTag(Player player) {
        PersistentDataContainer data = player.getPersistentDataContainer();
        return data.has(new NamespacedKey(PersonalDifficulty.getPlugin(), editDifficultyKey), PersistentDataType.STRING);
    }

    public static void setEditDifficultyKey(Player player, boolean editing, UUID uuid) {
        PersistentDataContainer data = player.getPersistentDataContainer();
        if (editing) {
            data.set(new NamespacedKey(PersonalDifficulty.getPlugin(), editDifficultyKey), PersistentDataType.STRING, uuid.toString());
        } else {
            data.remove(new NamespacedKey(PersonalDifficulty.getPlugin(), editDifficultyKey));
        }
    }

    public static boolean hasEditOptionTag(Player player) {
        PersistentDataContainer data = player.getPersistentDataContainer();
        return data.has(new NamespacedKey(PersonalDifficulty.getPlugin(), editOptionKey), PersistentDataType.STRING);
    }

    public static void setEditOptionKey(Player player, boolean editing, String difficultyName) {
        PersistentDataContainer data = player.getPersistentDataContainer();
        if (editing) {
            data.set(new NamespacedKey(PersonalDifficulty.getPlugin(), editOptionKey), PersistentDataType.STRING, difficultyName);
        } else {
            data.remove(new NamespacedKey(PersonalDifficulty.getPlugin(), editOptionKey));
        }
    }

    private static String getEditingUUIDToString(Player player) {
        String uuidS = null;
        if (hasEditDifficultyTag(player)) {
            PersistentDataContainer data = player.getPersistentDataContainer();
            uuidS = data.get(new NamespacedKey(PersonalDifficulty.getPlugin(), editDifficultyKey), PersistentDataType.STRING);
        }
        return uuidS;
    }

    private static String getEditingOptionKeyByDifficulty(Player player) {
        String s = null;
        if (hasEditOptionTag(player)) {
            PersistentDataContainer data = player.getPersistentDataContainer();
            String a = data.get(new NamespacedKey(PersonalDifficulty.getPlugin(), editOptionKey), PersistentDataType.STRING);
            String[] b = a.split(":");
            s = b[0];
        }
        return s;
    }

    private static String getEditingOptionKeyByVariable(Player player) {
        String s = null;
        if (hasEditOptionTag(player)) {
            PersistentDataContainer data = player.getPersistentDataContainer();
            String a = data.get(new NamespacedKey(PersonalDifficulty.getPlugin(), editOptionKey), PersistentDataType.STRING);
            String[] b = a.split(":");
            s = b[1];
        }
        return s;
    }

    @EventHandler
    public void editDifficultyToPlayer(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        if (hasEditDifficultyTag(player)) {
            String editingUUIDToString = getEditingUUIDToString(player);
            if (editingUUIDToString != null) {
                String message = e.getMessage();
                e.setCancelled(true);
                PlayerGui playerGui = new PlayerGui(player);
                if (message.equals("cancel")) {
                    Bukkit.getScheduler().runTask(PersonalDifficulty.getPlugin(), () -> {
                        player.openInventory(playerGui.getInventory());
                        playerGui.setUpMain(player);
                    });
                } else {
                    String currentDifficulty = PlayerUtil.getDifficultyName(player);
                    message = message.replaceAll("[\\s]", "_");
                    if (DifficultyUtil.hasDifficultyName(message)) {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(editingUUIDToString));
                        String finalMessage = message;
                        Bukkit.getScheduler().runTask(PersonalDifficulty.getPlugin(), ()-> {
                            Bukkit.getPluginManager().callEvent(new PlayerDifficultyChangeEvent(offlinePlayer, currentDifficulty, finalMessage));
                        });
                    } else {
                        player.sendMessage(ChatColor.RED + "Difficulty does not exist");
                    }
                    Bukkit.getScheduler().runTask(PersonalDifficulty.getPlugin(), () -> {
                        player.openInventory(playerGui.getInventory());
                        playerGui.setUpMain(player);
                    });
                }
                setEditDifficultyKey(player, false, null);
            }
        }
    }

    @EventHandler
    public void editDifficultyOption(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        if (hasEditOptionTag(player)) {
            String difficultyName = getEditingOptionKeyByDifficulty(player);
            String optionVariable = getEditingOptionKeyByVariable(player);
            if (difficultyName != null && optionVariable != null) {
                String message = e.getMessage();
                e.setCancelled(true);
                DifficultyEditorGui difficultyEditorGui = new DifficultyEditorGui(player);
                if (message.equals("cancel")) {
                    Bukkit.getScheduler().runTask(PersonalDifficulty.getPlugin(), () -> {
                        player.openInventory(difficultyEditorGui.getInventory());
                        difficultyEditorGui.setEditor(player, difficultyName);
                    });
                } else {
                    CustomDifficulty customDifficulty = DifficultyUtil.getCustomDifficulty(difficultyName);
                    CustomDifficulty.OptionVariables variable = CustomDifficulty.OptionVariables.valueOf(optionVariable);
                    CustomDifficulty.OptionVariablesType type = variable.getType();
                    Object value = null;
                    try {
                        switch (type) {
                            case String:
                                value = message;
                                break;
                            case Double:
                                value = Double.parseDouble(message);
                                break;
                            case Integer:
                                value = Integer.parseInt(message);
                                break;
                            case Boolean:
                                value = Boolean.parseBoolean(message);
                                break;
                        }
                    } catch (NullPointerException | NumberFormatException ex) {
                        player.sendMessage(ChatColor.RED + "Invalid value");
                    }
                    if (value != null) {
                        customDifficulty.setObjectValue(variable, value);
                    }
                    Bukkit.getScheduler().runTask(PersonalDifficulty.getPlugin(), () -> {
                        player.openInventory(difficultyEditorGui.getInventory());
                        difficultyEditorGui.setEditor(player, difficultyName);
                    });
                }
                setEditOptionKey(player, false, null);
            }
        }
    }

}

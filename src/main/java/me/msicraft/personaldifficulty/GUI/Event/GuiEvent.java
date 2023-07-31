package me.msicraft.personaldifficulty.GUI.Event;

import me.msicraft.personaldifficulty.API.Data.CustomDifficulty;
import me.msicraft.personaldifficulty.API.Util.DifficultyUtil;
import me.msicraft.personaldifficulty.GUI.DifficultyEditorGui;
import me.msicraft.personaldifficulty.GUI.MainGui;
import me.msicraft.personaldifficulty.GUI.PlayerGui;
import me.msicraft.personaldifficulty.PersonalDifficulty;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class GuiEvent implements Listener {

    @EventHandler
    public void clickMainGui(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) { return; }
        if (e.getView().getTitle().equalsIgnoreCase("PersonalDifficulty Gui")) {
            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();
            if (e.getCurrentItem() == null) { return; }
            ItemStack selectItem = e.getCurrentItem();
            ItemMeta selectItemMeta = selectItem.getItemMeta();
            if (selectItemMeta == null) { return; }
            PersistentDataContainer selectItemData = selectItemMeta.getPersistentDataContainer();
            if (selectItemData.has(new NamespacedKey(PersonalDifficulty.getPlugin(), "PersonalDifficulty-MainGui"), PersistentDataType.STRING)) {
                String var = selectItemData.get(new NamespacedKey(PersonalDifficulty.getPlugin(), "PersonalDifficulty-MainGui"), PersistentDataType.STRING);
                if (var != null) {
                    switch (var) {
                        case "SettingDifficulty":
                            DifficultyEditorGui difficultyEditorGui = new DifficultyEditorGui(player);
                            player.closeInventory();
                            player.openInventory(difficultyEditorGui.getInventory());
                            difficultyEditorGui.setMain(player);
                            break;
                        case "SettingPlayer":
                            PlayerGui playerGui = new PlayerGui(player);
                            player.closeInventory();
                            player.openInventory(playerGui.getInventory());
                            playerGui.setUpMain(player);
                            break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void clickPlayerSettingGui(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) { return; }
        if (e.getView().getTitle().equalsIgnoreCase("Setting Player")) {
            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();
            if (e.getCurrentItem() == null) {
                return;
            }
            ItemStack selectItem = e.getCurrentItem();
            ItemMeta selectItemMeta = selectItem.getItemMeta();
            if (selectItemMeta == null) {
                return;
            }
            PersistentDataContainer selectItemData = selectItemMeta.getPersistentDataContainer();
            PlayerGui playerGui = new PlayerGui(player);
            if (selectItemData.has(new NamespacedKey(PersonalDifficulty.getPlugin(), "PlayerSetting-Main"), PersistentDataType.STRING)) {
                String var = selectItemData.get(new NamespacedKey(PersonalDifficulty.getPlugin(), "PlayerSetting-Main"), PersistentDataType.STRING);
                if (var != null && e.isLeftClick()) {
                    int maxPage = playerGui.maxPage();
                    int currentPage = playerGui.getPage(player);
                    switch (var) {
                        case "Back":
                            MainGui mainGui = new MainGui();
                            player.openInventory(mainGui.getInventory());
                            mainGui.setUp();
                            break;
                        case "page":
                            PlayerGui.FilterType filterType = playerGui.getFilterType(player);
                            PlayerGui.FilterType nextType = PlayerGui.FilterType.all;
                            switch (filterType) {
                                case all:
                                    nextType = PlayerGui.FilterType.online;
                                    break;
                                case online:
                                    nextType = PlayerGui.FilterType.offline;
                                    break;
                                case offline:
                                    break;
                            }
                            playerGui.setFilterType(player, nextType);
                            player.openInventory(playerGui.getInventory());
                            playerGui.setUpMain(player);
                        case "Next":
                            int next = currentPage + 1;
                            if (next > maxPage) {
                                next = 0;
                            }
                            playerGui.setPage(player, next);
                            player.openInventory(playerGui.getInventory());
                            playerGui.setUpMain(player);
                            break;
                        case "Previous":
                            int previous = currentPage - 1;
                            if (previous < 0) {
                                previous = maxPage;
                            }
                            playerGui.setPage(player, previous);
                            player.openInventory(playerGui.getInventory());
                            playerGui.setUpMain(player);
                            break;
                        case "edit":
                            String targetUUIDToString = playerGui.getEditUUIDToString(selectItem);
                            if (targetUUIDToString != null) {
                                player.sendMessage(ChatColor.YELLOW + "========================================");
                                player.sendMessage(ChatColor.GRAY + " Please enter difficulty");
                                player.sendMessage(ChatColor.GRAY + " Cancel when entering 'cancel'");
                                player.sendMessage(ChatColor.GRAY + " Available difficulties: " + DifficultyUtil.getDifficultyNames());
                                player.sendMessage(ChatColor.YELLOW + "========================================");
                                ChatEditEvent.setEditDifficultyKey(player, true, UUID.fromString(targetUUIDToString));
                                player.closeInventory();
                            }
                            break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void clickDifficultyEditor(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) { return; }
        if (e.getView().getTitle().equalsIgnoreCase("Difficulty Editor")) {
            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();
            if (e.getCurrentItem() == null) {
                return;
            }
            ItemStack selectItem = e.getCurrentItem();
            ItemMeta selectItemMeta = selectItem.getItemMeta();
            if (selectItemMeta == null) {
                return;
            }
            PersistentDataContainer selectItemData = selectItemMeta.getPersistentDataContainer();
            DifficultyEditorGui difficultyEditorGui = new DifficultyEditorGui(player);
            if (selectItemData.has(new NamespacedKey(PersonalDifficulty.getPlugin(), "DifficultyEditor-Main"), PersistentDataType.STRING)) {
                String var = selectItemData.get(new NamespacedKey(PersonalDifficulty.getPlugin(), "DifficultyEditor-Main"), PersistentDataType.STRING);
                if (var != null && e.isLeftClick()) {
                    int maxPage = difficultyEditorGui.maxMainPage();
                    int currentPage = difficultyEditorGui.getMainPage(player);
                    switch (var) {
                        case "Back":
                            MainGui mainGui = new MainGui();
                            player.openInventory(mainGui.getInventory());
                            mainGui.setUp();
                            break;
                        case "Next":
                            int next = currentPage + 1;
                            if (next > maxPage) {
                                next = 0;
                            }
                            difficultyEditorGui.setMainPage(player, next);
                            player.openInventory(difficultyEditorGui.getInventory());
                            difficultyEditorGui.setMain(player);
                            break;
                        case "Previous":
                            int previous = currentPage - 1;
                            if (previous < 0) {
                                previous = maxPage;
                            }
                            difficultyEditorGui.setMainPage(player, previous);
                            player.openInventory(difficultyEditorGui.getInventory());
                            difficultyEditorGui.setMain(player);
                            break;
                    }
                }
            } else if (selectItemData.has(new NamespacedKey(PersonalDifficulty.getPlugin(), "DifficultyEditor-Edit-Book"), PersistentDataType.STRING)) {
                String var = selectItemData.get(new NamespacedKey(PersonalDifficulty.getPlugin(), "DifficultyEditor-Edit-Book"), PersistentDataType.STRING);
                if (var != null && e.isLeftClick()) {
                    player.openInventory(difficultyEditorGui.getInventory());
                    difficultyEditorGui.setEditor(player, var);
                }
            } else if (selectItemData.has(new NamespacedKey(PersonalDifficulty.getPlugin(), "DifficultyEditor-Editor"), PersistentDataType.STRING)) {
                String var = selectItemData.get(new NamespacedKey(PersonalDifficulty.getPlugin(), "DifficultyEditor-Editor"), PersistentDataType.STRING);
                if (var != null && e.isLeftClick()) {
                    String difficultyName = null;
                    ItemStack book = e.getClickedInventory().getItem(4);
                    if (book != null) {
                        PersistentDataContainer data = book.getItemMeta().getPersistentDataContainer();
                        if (data.has(new NamespacedKey(PersonalDifficulty.getPlugin(), "DifficultyEditor-Info"), PersistentDataType.STRING)) {
                            difficultyName = data.get(new NamespacedKey(PersonalDifficulty.getPlugin(), "DifficultyEditor-Info"), PersistentDataType.STRING);
                        }
                    }
                    if (book == null || difficultyName == null) {
                        player.openInventory(difficultyEditorGui.getInventory());
                        difficultyEditorGui.setMain(player);
                        return;
                    }
                    int maxPage = difficultyEditorGui.maxEditorPage();
                    int currentPage = difficultyEditorGui.getEditorPage(player);
                    switch (var) {
                        case "Back":
                            player.openInventory(difficultyEditorGui.getInventory());
                            difficultyEditorGui.setMain(player);
                            break;
                        case "Next":
                            int next = currentPage + 1;
                            if (next > maxPage) {
                                next = 0;
                            }
                            difficultyEditorGui.setEditorPage(player, next);
                            player.openInventory(difficultyEditorGui.getInventory());
                            difficultyEditorGui.setEditor(player, difficultyName);
                            break;
                        case "Previous":
                            int previous = currentPage - 1;
                            if (previous < 0) {
                                previous = maxPage;
                            }
                            difficultyEditorGui.setEditorPage(player, previous);
                            player.openInventory(difficultyEditorGui.getInventory());
                            difficultyEditorGui.setEditor(player, difficultyName);
                            break;
                    }
                }
            } else if (selectItemData.has(new NamespacedKey(PersonalDifficulty.getPlugin(), "DifficultyEditor-Edit-Option"), PersistentDataType.STRING)) {
                String var = selectItemData.get(new NamespacedKey(PersonalDifficulty.getPlugin(), "DifficultyEditor-Edit-Option"), PersistentDataType.STRING);
                if (var != null && e.isLeftClick()) {
                    String[] a = var.split(":");
                    String vars = a[1];
                    CustomDifficulty.OptionVariablesType type = CustomDifficulty.OptionVariables.valueOf(vars).getType();
                    player.sendMessage(ChatColor.YELLOW + "========================================");
                    player.sendMessage(ChatColor.GRAY + " Please enter value");
                    player.sendMessage(ChatColor.GRAY + " Current value type: " + type.name());
                    player.sendMessage(ChatColor.GRAY + " Cancel when entering 'cancel'");
                    player.sendMessage(ChatColor.YELLOW + "========================================");
                    ChatEditEvent.setEditOptionKey(player, true, var);
                    player.closeInventory();
                }
            }
        }
    }

}

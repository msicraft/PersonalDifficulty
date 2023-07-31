package me.msicraft.personaldifficulty.GUI;

import me.msicraft.personaldifficulty.API.Data.CustomDifficulty;
import me.msicraft.personaldifficulty.API.Data.PlayerData;
import me.msicraft.personaldifficulty.API.Util.DifficultyUtil;
import me.msicraft.personaldifficulty.API.Util.PlayerUtil;
import me.msicraft.personaldifficulty.DataFile.PlayerDataFile;
import me.msicraft.personaldifficulty.PersonalDifficulty;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DifficultyEditorGui implements InventoryHolder {

    private Inventory gui;

    public DifficultyEditorGui(Player player) {
        gui = Bukkit.createInventory(player, 54, "Difficulty Editor");
    }

    public void setMain(Player player) {
        setMainPageButton(player);
        List<String> difficulties = DifficultyUtil.getDifficultyNames();
        int maxSize = difficulties.size();
        int pageCount = getMainPage(player);
        int guiCount = 0;
        int lastCount = pageCount*45;
        String name;
        List<String> lore = new ArrayList<>();
        ItemStack itemStack;
        for (int a = lastCount; a<maxSize; a++) {
            CustomDifficulty customDifficulty = DifficultyUtil.getCustomDifficulty(difficulties.get(a));
            name = customDifficulty.getName();
            if (!lore.isEmpty()) {
                lore.clear();
            }
            lore.add("");
            lore.add(ChatColor.YELLOW + "Left Click: Edit");
            lore.add("");
            for (CustomDifficulty.OptionVariables option : CustomDifficulty.OptionVariables.values()) {
                lore.add(ChatColor.GREEN + option.name() + ": " + ChatColor.GRAY + customDifficulty.getObjectValue(option));
            }
            itemStack = createNormalItem(Material.BOOK, name, lore, "DifficultyEditor-Edit-Book", name);
            gui.setItem(guiCount, itemStack);
            guiCount++;
            if (guiCount >= 45) {
                break;
            }
        }
    }

    private final int[] optionSlots = {
            9,10,11,12,13,14,15,16,17,
            18,19,20,21,22,23,24,25,26,
            27,28,29,30,31,32,33,34,35,
            36,37,38,39,40,41,42,43,44};

    public void setEditor(Player player, String difficultyName) {
        setDifficultyInfo(difficultyName);
        setEditorPageButton(player);
        CustomDifficulty.OptionVariables[] optionVariables = CustomDifficulty.OptionVariables.values();
        int maxSize = optionVariables.length;
        int pageCount = getEditorPage(player);
        int guiCount = 0;
        int lastCount = pageCount*36;
        String name;
        List<String> lore = new ArrayList<>();
        ItemStack itemStack;
        for (int a = lastCount; a<maxSize; a++) {
            if (!lore.isEmpty()) {
                lore.clear();
            }
            lore.add(ChatColor.YELLOW + "Left Click: Edit");
            lore.add("");
            CustomDifficulty customDifficulty = DifficultyUtil.getCustomDifficulty(difficultyName);
            CustomDifficulty.OptionVariables optionVariable = optionVariables[a];
            name = optionVariable.name();
            lore.add(ChatColor.GREEN + "Current: " + ChatColor.GRAY + customDifficulty.getObjectValue(optionVariable));
            String key = customDifficulty.getName() + ":" + optionVariable.name();
            itemStack = createNormalItem(Material.PAPER, name, lore, "DifficultyEditor-Edit-Option", key);
            int slot = optionSlots[guiCount];
            gui.setItem(slot, itemStack);
            guiCount++;
            if (guiCount >= 45) {
                break;
            }
        }
    }

    private void setDifficultyInfo(String difficultyName) {
        ItemStack itemStack;
        CustomDifficulty customDifficulty = DifficultyUtil.getCustomDifficulty(difficultyName);
        List<String> lore = new ArrayList<>();
        for (CustomDifficulty.OptionVariables optionVariable : CustomDifficulty.OptionVariables.values()) {
            lore.add(ChatColor.GREEN + optionVariable.name() + ": " + ChatColor.GRAY + customDifficulty.getObjectValue(optionVariable));
        }
        itemStack = createNormalItem(Material.BOOK, difficultyName, lore, "DifficultyEditor-Info", difficultyName);
        gui.setItem(4, itemStack);
    }

    private final List<String> emptyLore = Collections.emptyList();

    private void setMainPageButton(Player player) {
        ItemStack itemStack;
        itemStack = createNormalItem(Material.BARRIER, ChatColor.RED + "Back", emptyLore, "DifficultyEditor-Main", "Back");
        gui.setItem(45, itemStack);
        itemStack = createNormalItem(Material.ARROW, "Next", emptyLore, "DifficultyEditor-Main", "Next");
        gui.setItem(50, itemStack);
        itemStack = createNormalItem(Material.ARROW, "Previous", emptyLore, "DifficultyEditor-Main", "Previous");
        gui.setItem(48, itemStack);
        String page = "Page: " + (getMainPage(player)+1) + "/" + (maxMainPage()+1);
        itemStack = createNormalItem(Material.BOOK, page, emptyLore, "DifficultyEditor-Main", "page");
        gui.setItem(49, itemStack);
    }

    public int maxMainPage() {
        int maxSize = DifficultyUtil.getDifficultyNames().size();
        return maxSize / 45;
    }

    public int getMainPage(Player player) {
        int page = 0;
        PlayerData playerData = PlayerUtil.getPlayerData(player);
        PlayerDataFile playerDataFile = playerData.getPlayerDataFile();
        if (playerDataFile.getConfig().contains("SettingDifficulty.MainPage")) {
            page = playerDataFile.getConfig().getInt("SettingDifficulty.MainPage");
        }
        return page;
    }

    public void setMainPage(Player player, int page) {
        PlayerData playerData = PlayerUtil.getPlayerData(player);
        PlayerDataFile playerDataFile = playerData.getPlayerDataFile();
        playerDataFile.getConfig().set("SettingDifficulty.MainPage", page);
        playerDataFile.saveConfig();
    }

    private void setEditorPageButton(Player player) {
        ItemStack itemStack;
        itemStack = createNormalItem(Material.BARRIER, ChatColor.RED + "Back", emptyLore, "DifficultyEditor-Editor", "Back");
        gui.setItem(45, itemStack);
        itemStack = createNormalItem(Material.ARROW, "Next", emptyLore, "DifficultyEditor-Editor", "Next");
        gui.setItem(50, itemStack);
        itemStack = createNormalItem(Material.ARROW, "Previous", emptyLore, "DifficultyEditor-Editor", "Previous");
        gui.setItem(48, itemStack);
        String page = "Page: " + (getEditorPage(player)+1) + "/" + (maxEditorPage()+1);
        itemStack = createNormalItem(Material.BOOK, page, emptyLore, "DifficultyEditor-Editor", "page");
        gui.setItem(49, itemStack);
    }

    public int maxEditorPage() {
        int maxSize = DifficultyUtil.getDifficultyNames().size();
        return maxSize / 45;
    }

    public int getEditorPage(Player player) {
        int page = 0;
        PlayerData playerData = PlayerUtil.getPlayerData(player);
        PlayerDataFile playerDataFile = playerData.getPlayerDataFile();
        if (playerDataFile.getConfig().contains("SettingDifficulty.EditorPage")) {
            page = playerDataFile.getConfig().getInt("SettingDifficulty.EditorPage");
        }
        return page;
    }

    public void setEditorPage(Player player, int page) {
        PlayerData playerData = PlayerUtil.getPlayerData(player);
        PlayerDataFile playerDataFile = playerData.getPlayerDataFile();
        playerDataFile.getConfig().set("SettingDifficulty.EditorPage", page);
        playerDataFile.saveConfig();
    }

    private ItemStack createNormalItem(Material material, String name, List<String> list, String dataTag, String data) {
        ItemStack itemStack = new ItemStack(material, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(list);
        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        dataContainer.set(new NamespacedKey(PersonalDifficulty.getPlugin(), dataTag), PersistentDataType.STRING, data);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public Inventory getInventory() {
        return gui;
    }
}

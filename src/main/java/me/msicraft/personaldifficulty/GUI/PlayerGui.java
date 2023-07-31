package me.msicraft.personaldifficulty.GUI;

import me.msicraft.personaldifficulty.API.Data.PlayerData;
import me.msicraft.personaldifficulty.API.Util.PlayerUtil;
import me.msicraft.personaldifficulty.DataFile.PlayerDataFile;
import me.msicraft.personaldifficulty.PersonalDifficulty;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PlayerGui implements InventoryHolder {

    private Inventory gui;

    public PlayerGui(Player player) {
        gui = Bukkit.createInventory(player, 54, "Setting Player");
    }

    public void setUpMain(Player player) {
        setPageButton(player);
        File[] files = PlayerDataFile.getDataFileList();
        int maxSize = files.length;
        int pageCount = getPage(player);
        int guiCount = 0;
        int lastCount = pageCount*45;
        String currentDifficulty;
        List<String> loreList = new ArrayList<>();
        ItemStack itemStack;
        for (int a = lastCount; a<maxSize; a++) {
            if (!loreList.isEmpty()) {
                loreList.clear();
            }
            loreList.add(ChatColor.YELLOW + "Left Click: Edit difficulty");
            loreList.add("");
            File file = files[a];
            String fileName = file.getName();
            String[] fileNames = fileName.split("\\.");
            String uuidS = fileNames[0];
            UUID uuid = UUID.fromString(uuidS);
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
            boolean check = false;
            FilterType filterType = getFilterType(player);
            switch (filterType) {
                case all:
                    check = true;
                    break;
                case online:
                    if (offlinePlayer.isOnline()) {
                        check = true;
                    }
                    break;
                case offline:
                    if (!offlinePlayer.isOnline()) {
                        check = true;
                    }
                    break;
            }
            if (check) {
                currentDifficulty = PlayerUtil.getDifficultyName(offlinePlayer);
                if (offlinePlayer.isOnline()) {
                    loreList.add(ChatColor.YELLOW + "Status: " + ChatColor.GREEN + "Online");
                } else {
                    loreList.add(ChatColor.YELLOW + "Status: " + ChatColor.RED + "Offline");
                }
                loreList.add(ChatColor.YELLOW + "Current Difficulty: " + ChatColor.GREEN + currentDifficulty);
                itemStack = createNormalItem(Material.PAPER, offlinePlayer.getName(), loreList, "PlayerSetting-Main", "edit");
                addEditTag(itemStack, uuid);
                gui.setItem(guiCount, itemStack);
                guiCount++;
                if (guiCount >= 45) {
                    break;
                }
            }
        }
    }

    public String getEditUUIDToString(ItemStack itemStack) {
        String s = null;
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            PersistentDataContainer data = itemMeta.getPersistentDataContainer();
            if (data.has(new NamespacedKey(PersonalDifficulty.getPlugin(), "PlayerSetting-Edit"), PersistentDataType.STRING)) {
                s = data.get(new NamespacedKey(PersonalDifficulty.getPlugin(), "PlayerSetting-Edit"), PersistentDataType.STRING);
            }
        }
        return s;
    }

    private void addEditTag(ItemStack itemStack, UUID uuid) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        dataContainer.set(new NamespacedKey(PersonalDifficulty.getPlugin(), "PlayerSetting-Edit"), PersistentDataType.STRING, uuid.toString());
        itemStack.setItemMeta(itemMeta);
    }

    private final List<String> emptyLore = Collections.emptyList();

    public enum FilterType {
        all, online, offline
    }

    public FilterType getFilterType(Player player) {
        FilterType filterType = FilterType.all;
        PlayerDataFile playerDataFile = PlayerUtil.getPlayerData(player).getPlayerDataFile();
        if (playerDataFile.hasConfigFile() && playerDataFile.getConfig().contains("SettingPlayer.FilterType")) {
            String type = playerDataFile.getConfig().getString("SettingPlayer.FilterType");
            if (type != null) {
                try {
                    filterType = FilterType.valueOf(type);
                } catch (IllegalArgumentException ignored) {
                }
            }

        }
        return filterType;
    }

    public void setFilterType(Player player, FilterType filterType) {
        PlayerDataFile playerDataFile = PlayerUtil.getPlayerData(player).getPlayerDataFile();
        if (playerDataFile.hasConfigFile()) {
            playerDataFile.getConfig().set("SettingPlayer.FilterType", filterType.name());
            playerDataFile.saveConfig();
        }
    }

    private void setPageButton(Player player) {
        ItemStack itemStack;
        itemStack = createNormalItem(Material.BARRIER, ChatColor.RED + "Back", emptyLore, "PlayerSetting-Main", "Back");
        gui.setItem(45, itemStack);
        itemStack = createNormalItem(Material.ARROW, "Next", emptyLore, "PlayerSetting-Main", "Next");
        gui.setItem(50, itemStack);
        itemStack = createNormalItem(Material.ARROW, "Previous", emptyLore, "PlayerSetting-Main", "Previous");
        gui.setItem(48, itemStack);
        String page = "Page: " + (getPage(player)+1) + "/" + (maxPage()+1);
        List<String> filterLore = new ArrayList<>();
        filterLore.add(ChatColor.YELLOW + "Left Click: " + ChatColor.GREEN + "change");
        filterLore.add("");
        filterLore.add(ChatColor.YELLOW + "Filter: " + ChatColor.GREEN + getFilterType(player));
        itemStack = createNormalItem(Material.BOOK, page, filterLore, "PlayerSetting-Main", "page");
        gui.setItem(49, itemStack);
    }

    public int maxPage() {
        int maxFileSize = PlayerDataFile.getDataFileList().length;;
        return maxFileSize / 45;
    }

    public int getPage(Player player) {
        int page = 0;
        PlayerData playerData = PlayerUtil.getPlayerData(player);
        PlayerDataFile playerDataFile = playerData.getPlayerDataFile();
        if (playerDataFile.getConfig().contains("SettingPlayer.Page")) {
            page = playerDataFile.getConfig().getInt("SettingPlayer.Page");
        }
        return page;
    }

    public void setPage(Player player, int page) {
        PlayerData playerData = PlayerUtil.getPlayerData(player);
        PlayerDataFile playerDataFile = playerData.getPlayerDataFile();
        playerDataFile.getConfig().set("SettingPlayer.Page", page);
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

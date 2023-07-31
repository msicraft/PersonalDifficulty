package me.msicraft.personaldifficulty.GUI;

import me.msicraft.personaldifficulty.PersonalDifficulty;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class MainGui implements InventoryHolder {

    private Inventory gui;

    public MainGui() {
        gui = Bukkit.createInventory(null, 9, "PersonalDifficulty Gui");
    }

    public void setUp() {
        List<String> lore = new ArrayList<>();
        ItemStack itemStack;
        lore.add(ChatColor.GRAY + "Open gui for editing difficulty");
        itemStack = createNormalItem(Material.WHITE_BANNER, "Setting difficulty", lore, "PersonalDifficulty-MainGui", "SettingDifficulty");
        gui.setItem(3, itemStack);
        lore.clear();
        lore.add(ChatColor.GRAY + "Open gui to set the difficulty for the player");
        itemStack = createNormalItem(Material.PLAYER_HEAD, "Setting player", lore, "PersonalDifficulty-MainGui", "SettingPlayer");
        gui.setItem(5, itemStack);
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

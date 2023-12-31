package me.msicraft.personaldifficulty.API;

import me.msicraft.personaldifficulty.PersonalDifficulty;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

public class UpdateChecker {

    private final PersonalDifficulty plugin;
    private final int id;

    public UpdateChecker(PersonalDifficulty plugin, int id) {
        this.plugin = plugin;
        this.id = id;
    }

    public void getPluginUpdateCheck(Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.id).openStream(); Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    consumer.accept(scanner.next());
                }
            } catch (IOException exception) {
                plugin.getLogger().info("Unable to check for updates: " + exception.getMessage());
            }
        });
    }

}

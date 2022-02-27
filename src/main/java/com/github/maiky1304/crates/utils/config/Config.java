package com.github.maiky1304.crates.utils.config;

import lombok.SneakyThrows;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Config extends YamlConfiguration {

    private final File file;

    /**
     * Sets up the entire config including creating possible missing parent folders
     * and saving the default file from the plugin jar.
     * @param plugin
     * @param fileName
     */
    public Config(JavaPlugin plugin, String fileName) {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        this.file = new File(plugin.getDataFolder(), fileName);
        if (!this.file.exists()) {
            plugin.saveResource(fileName, true);
        }
    }

    /**
     * Loads the configuration
     */
    @SneakyThrows({ IOException.class, InvalidConfigurationException.class})
    public void load() {
        super.load(this.file);
    }

    /**
     * Saves the configuration
     */
    @SneakyThrows({ IOException.class })
    public void save() {
        super.save(this.file);
    }

}

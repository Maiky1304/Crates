package com.github.maiky1304.crates.manager;

import com.github.maiky1304.crates.CratesPlugin;
import com.github.maiky1304.crates.utils.config.models.Crate;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CrateManager {

    private final CratesPlugin instance;

    private final List<Crate> loadedCrates = new ArrayList<>();

    public CrateManager(CratesPlugin instance) {
        this.instance = instance;
    }

    /**
     * Find a crate by name
     * @param name
     * @return crate if found or null
     */
    public Crate findCrate(String name) {
        return loadedCrates.stream()
                .filter(crate -> crate.getName().toLowerCase().equals(name.toLowerCase()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Remove a crate from the cache
     * @param crate
     */
    public void removeCrate(Crate crate) {
        this.loadedCrates.remove(crate);
    }

    /**
     * Add a crate to the cache
     * @param crate
     */
    public void addCrate(Crate crate) {
        this.loadedCrates.add(crate);
    }

    /**
     * Save all the crates asynchronously
     */
    public void saveAllCrates() {
        CompletableFuture.runAsync(() -> {
           for (Crate crate : loadedCrates) {
               MemoryConfiguration crateConfig = crate.toConfig();
               instance.getData().set(crate.getName(), crateConfig);
           }
           instance.getData().save();
        });
    }

    /**
     * Load all crates from the config (flat) file.
     * This is all done asynchronously.
     */
    public void loadAllCrates() {
        CompletableFuture.supplyAsync(() -> {
            List<Crate> crates = new ArrayList<>();

            for (String name : instance.getData().getKeys(false)) {
                ConfigurationSection crateConfig = instance.getData().getConfigurationSection(name);
                Crate crate = Crate.fromConfig(name, crateConfig);
                crates.add(crate);
            }

            return crates;
        }).thenAcceptAsync(loadedCrates::addAll);
    }

}

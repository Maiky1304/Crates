package com.github.maiky1304.crates.loader;

import com.github.maiky1304.crates.utils.command.Command;
import com.github.maiky1304.crates.utils.config.Config;
import com.github.maiky1304.crates.utils.menu.InventoryManager;
import lombok.Getter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

/**
 * Custom Plugin class to extend Main class of plugin with
 * so the configuration files, simple methods I need for listeners
 * and commands are nicely organized.
 */
@Getter
public abstract class ExtendedJavaPlugin extends JavaPlugin {

    private Config configuration;
    private Config messages;
    private Config data;

    private InventoryManager inventoryManager;

    public abstract void enable();
    public abstract void disable();

    @Override
    public void onEnable() {
        // Load all configurations
        this.configuration = new Config(this, "config.yml");
        this.configuration.load();

        this.messages = new Config(this, "messages.yml");
        this.messages.load();

        this.data = new Config(this, "data.yml");
        this.data.load();

        // Install Inventory Manager
        this.inventoryManager = new InventoryManager();
        this.registerListener(this.inventoryManager);

        // Invoke abstract enable method
        this.enable();
    }

    @Override
    public void onDisable() {
        // Invoke abstract disable method
        this.disable();
    }

    /**
     * Registers a Bukkit listener without having to provide the
     * plugin variable as well.
     * @param listener
     * @param <L>
     */
    public <L extends Listener> void registerListener(L listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    /**
     * Registers a command easily without having to provide everything
     * yourself.
     * @param command
     * @param <P>
     */
    public <P extends Command> void registerCommand(P command) {
        Objects.requireNonNull(getCommand(command.getInfo().value()), String.format("It looks like you haven't registered the command /%s in your plugin.yml yet!",
                command.getInfo().value()));
        getCommand(command.getInfo().value()).setExecutor(command);
        getCommand(command.getInfo().value()).setTabCompleter(command);
    }

}

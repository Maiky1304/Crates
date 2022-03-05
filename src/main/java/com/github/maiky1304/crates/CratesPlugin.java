package com.github.maiky1304.crates;

import com.github.maiky1304.crates.commands.CrateCommand;
import com.github.maiky1304.crates.database.manager.UserManager;
import com.github.maiky1304.crates.listeners.CrateOpenListener;
import com.github.maiky1304.crates.listeners.JoinListener;
import com.github.maiky1304.crates.listeners.QuitListener;
import com.github.maiky1304.crates.loader.ExtendedJavaPlugin;
import com.github.maiky1304.crates.manager.CrateManager;
import lombok.Getter;

@Getter
public final class CratesPlugin extends ExtendedJavaPlugin {

    private UserManager userManager;
    private CrateManager crateManager;

    @Override
    public void enable() {
        // Initialize manager(s)
        try {
            this.userManager = new UserManager(this);
        } catch (InstantiationException e) {
            getLogger().severe("Cannot connect or load database, plugin is disabling...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.crateManager = new CrateManager(this);
        this.crateManager.loadAllCrates();

        // Register listener(s)
        this.registerListener(new JoinListener(this));
        this.registerListener(new QuitListener(this));
        this.registerListener(new CrateOpenListener(this));

        // Register command(s)
        this.registerCommand(new CrateCommand(this));
    }

    @Override
    public void disable() {
        this.crateManager.saveAllCrates();
    }

}

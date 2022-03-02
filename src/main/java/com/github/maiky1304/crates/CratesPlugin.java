package com.github.maiky1304.crates;

import com.github.maiky1304.crates.commands.CrateCommand;
import com.github.maiky1304.crates.database.manager.UserManager;
import com.github.maiky1304.crates.database.models.User;
import com.github.maiky1304.crates.listeners.JoinListener;
import com.github.maiky1304.crates.listeners.QuitListener;
import com.github.maiky1304.crates.loader.ExtendedJavaPlugin;
import lombok.Getter;

import java.util.HashMap;
import java.util.UUID;

@Getter
public final class CratesPlugin extends ExtendedJavaPlugin {

    private UserManager userManager;

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

        // Register listener(s)
        this.registerListener(new JoinListener(this));
        this.registerListener(new QuitListener(this));

        // Register command(s)
        this.registerCommand(new CrateCommand());
    }

    @Override
    public void disable() {
    }

}

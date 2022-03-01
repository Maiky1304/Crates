package com.github.maiky1304.crates.listeners;

import com.github.maiky1304.crates.CratesPlugin;
import com.github.maiky1304.crates.database.manager.UserManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@RequiredArgsConstructor
public class JoinListener implements Listener {

    private final CratesPlugin instance;

    /**
     * This all attempts to find the user in the database
     * then if it is found stores it into the cache.
     *
     * Or if it's not found creates a new model saves it, gets the ID that
     * is assigned to it and then store it into the cache as a fresh object.
     * @param event Event that fires when a player joins the server
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        UserManager manager = instance.getUserManager();
        manager.findUser(player.getUniqueId())
                .thenAcceptAsync(user -> {
                    if (user != null) {
                        manager.getCache().put(player.getUniqueId(), user);
                        return;
                    }

                    manager.createUser(player).thenAcceptAsync(u -> {
                        manager.getCache().put(player.getUniqueId(), u);
                    });
                });
    }

}

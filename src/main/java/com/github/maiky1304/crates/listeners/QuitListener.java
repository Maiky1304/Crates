package com.github.maiky1304.crates.listeners;

import com.github.maiky1304.crates.CratesPlugin;
import com.github.maiky1304.crates.database.manager.UserManager;
import com.github.maiky1304.crates.database.models.User;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class QuitListener implements Listener {

    private final CratesPlugin instance;

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        UserManager manager = instance.getUserManager();

        if (!manager.getCache().containsKey(player.getUniqueId()))
            return;

        User user = manager.getCache().get(player.getUniqueId());
        manager
    }

}

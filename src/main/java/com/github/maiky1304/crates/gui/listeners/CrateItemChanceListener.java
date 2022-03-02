package com.github.maiky1304.crates.gui.listeners;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.function.BiConsumer;

/**
 * Class that can be used to allow chat input
 * specifically for the setting of a Crate item's chance.
 */
@Getter @Setter
@RequiredArgsConstructor
public class CrateItemChanceListener implements Listener {

    private final Player player;
    private final BiConsumer<Player, String> consumer;

    private boolean isFinished = false;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (isFinished) {
            // Make sure the current class is unregistered before it fires again.
            HandlerList.unregisterAll(this);
            return;
        }
        if (event.getPlayer() != player) return;

        event.setCancelled(true);

        this.setFinished(true);
        this.consumer.accept(event.getPlayer(), event.getMessage());
    }

}

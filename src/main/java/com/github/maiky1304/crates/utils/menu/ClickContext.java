package com.github.maiky1304.crates.utils.menu;

import com.github.maiky1304.crates.utils.text.Text;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

@Getter
public class ClickContext {

    private final InventoryClickEvent event;

    public ClickContext(InventoryClickEvent event) {
        this.event = event;
    }

    /**
     * @return the player who clicked
     */
    public Player getPlayer() {
        return (Player) event.getWhoClicked();
    }

    /**
     * @return the click type in the event
     */
    public ClickType getClickType() {
        return event.getClick();
    }

    /**
     * Send a message directly to the Clicker that supports
     * color codes using the & character.
     * @param message
    r */
    public void reply(String message) {
        event.getWhoClicked().sendMessage(Text.colors(message));
    }

}

package com.github.maiky1304.crates.utils.menu;

import com.github.maiky1304.crates.utils.text.Text;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

@Getter
public class ClickContext {

    private final InventoryClickEvent event;
    private final ClickType clickType;

    public ClickContext(InventoryClickEvent event) {
        this.event = event;
        this.clickType = event.getClick();
    }

    /**
     * Send a message directly to the Clicker that supports
     * color codes using the & character.
     * @param message
     */
    public void reply(String message) {
        event.getWhoClicked().sendMessage(Text.colors(message));
    }

}

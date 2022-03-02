package com.github.maiky1304.crates.utils.menu;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class InventoryManager implements Listener {

    private final List<Menu> activeMenus = new ArrayList<>();

    /**
     * Registers the menu by adding it to the cache
     * @param menu
     */
    public void registerMenu(Menu menu) {
        this.activeMenus.add(menu);
    }

    /**
     * Destroys the menu by removing it from the cache
     * @param menu
     */
    public void destroyMenu(Menu menu) {
        this.activeMenus.remove(menu);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getWhoClicked().getOpenInventory().getTopInventory();
        if (this.activeMenus.stream().noneMatch(menu -> menu.getInventory()
                .equals(inventory))) return;

        event.setCancelled(true);

        Menu menu = this.activeMenus.stream().filter(m -> m.getInventory()
                .equals(inventory)).findFirst().orElse(null);
        assert menu != null;
        menu.handleClick(new ClickContext(event));
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (this.activeMenus.stream().noneMatch(menu -> menu.getInventory()
                .equals(event.getInventory()))) return;

        Menu menu = this.activeMenus.stream().filter(m -> m.getInventory()
                .equals(event.getInventory())).findFirst().orElse(null);
        assert menu != null;
        menu.setMenuState(MenuState.CLOSED);

        if (ArrayUtils.contains(menu.flags, MenuFlag.DESTROY_ON_CLOSE)) {
            destroyMenu(menu);
        }
    }

}

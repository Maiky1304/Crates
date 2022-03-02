package com.github.maiky1304.crates.utils.menu;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        if (this.activeMenus.stream().noneMatch(menu -> menu.getInventory()
                .equals(event.getClickedInventory()))) return;

        event.setCancelled(true);

        Menu menu = this.activeMenus.stream().filter(m -> m.getInventory()
                .equals(event.getClickedInventory())).findFirst().orElse(null);
        assert menu != null;

        Map<Method, ClickListener> methods = menu.getMethods().stream()
                .filter(method -> method.isAnnotationPresent(ClickListener.class))
                .collect(Collectors.toMap(m -> m, m -> m.getDeclaredAnnotation(ClickListener.class)));
        methods.forEach((method, data) -> {
            if (data.slot() != event.getSlot()) return;
            try {
                if (method.getParameterCount() == 1 &&
                    method.getParameterTypes()[0] == ClickContext.class) {
                    ClickContext context = new ClickContext(event);
                    method.invoke(menu, context);
                } else {
                    method.invoke(menu);
                }
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
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

package com.github.maiky1304.crates.utils.menu;

import com.github.maiky1304.crates.utils.text.Placeholder;
import com.github.maiky1304.crates.utils.text.Text;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Getter
public class Menu {

    protected final Inventory inventory;
    protected final MenuFlag[] flags;

    private final Player player;

    @Setter
    protected MenuState menuState;

    private final HashMap<String, ItemStack> itemMapper = new HashMap<>();

    public Menu(Player player, int rows, String title, MenuFlag... flags) {
        this.player = player;
        this.flags = flags;

        this.inventory = Bukkit.createInventory(null, rows * 9, Text.colors(title));
    }

    /**
     * Draw the menu by adding all items to it occurs before opening the inventory.
     */
    public void draw() {
        Map<Method, ClickListener> methods = getMethods().stream()
                .filter(method -> method.isAnnotationPresent(ClickListener.class))
                .collect(Collectors.toMap(m -> m, m -> m.getDeclaredAnnotation(ClickListener.class)));
        methods.forEach((method, cl) -> {
            ItemStack itemStack = itemMapper.get(cl.itemId());
            if (itemStack == null) {
                throw new IllegalArgumentException(String.format("Item with ID %s cannot be found in item mapper, did you define it using Menu#defineItem?",
                        cl.itemId()));
            }
            this.inventory.setItem(cl.slot(), itemStack);
        });
    }

    /**
     * Defines an item inside the item mapper to access during runtime in the
     * annotation ClickListener
     * @see ClickListener
     * @param itemId
     * @param itemStack
     */
    public void defineItem(String itemId, ItemStack itemStack) {
        if (this.itemMapper.containsKey(itemId)) {
            throw new IllegalArgumentException(String.format("An item with the ID %s is already defined in the mappings.",
                    itemId));
        }

        this.itemMapper.put(itemId, itemStack);
    }

    /**
     * Returns all declared methods that exist in the class
     * @return List of classes
     */
    protected List<Method> getMethods() {
        return Arrays.asList(getClass().getDeclaredMethods());
    }

    /**
     * Opens the menu for the player provided
     * in the constructor
     */
    public void open() {
        this.draw();
        this.player.openInventory(this.inventory);
    }

}

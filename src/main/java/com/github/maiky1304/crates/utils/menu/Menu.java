package com.github.maiky1304.crates.utils.menu;

import com.github.maiky1304.crates.utils.text.Text;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class Menu {

    protected int draws = 0;

    protected Inventory inventory;
    protected final MenuFlag[] flags;
    protected final String title;

    private final Player player;

    @Setter
    protected MenuState menuState;

    private final HashMap<String, ItemStack> itemMapper = new HashMap<>();

    public Menu(Player player, int rows, String title, MenuFlag... flags) {
        this.player = player;
        this.flags = flags;
        this.title = title;

        this.inventory = Bukkit.createInventory(null, rows * 9, Text.colors(title));
    }

    /**
     * Live change the rows of the inventory will change the inventory object
     * @param rows
     */
    public void setRows(int rows) {
        this.inventory = Bukkit.createInventory(null, rows * 9, Text.colors(title));
        this.draw();
        this.open();
    }

    /**
     * Checks if it is the first draw
     * @return true or false
     */
    public boolean isFirstDraw() {
        return draws == 1;
    }

    /**
     * Draw the menu by adding all items to it occurs before opening the inventory.
     */
    public void draw() {
        this.draws++;

        Map<Method, ClickListener> methods = getMethods().stream()
                .filter(method -> method.isAnnotationPresent(ClickListener.class))
                .collect(Collectors.toMap(m -> m, m -> m.getDeclaredAnnotation(ClickListener.class)));
        methods.forEach((method, cl) -> {
            ItemStack itemStack = itemMapper.get(cl.itemId());
            if (itemStack == null) {
                throw new IllegalArgumentException(String.format("Item with ID %s cannot be found in item mapper, did you define it using Menu#defineItem?",
                        cl.itemId()));
            }
            this.drawItem(cl.slot(), itemStack);
        });
    }

    /**
     * Draw an item in the inventory
     * @param slot
     * @param itemStack
     */
    public void drawItem(int slot, ItemStack itemStack) {
        this.inventory.setItem(slot, itemStack);
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
     * Handles the clicks gets executed from InventoryManager but can also be overridden in a class
     * that inherits the Menu class.
     * @param context
     */
    public void handleClick(ClickContext context) {
        Map<Method, ClickListener> methods = getMethods().stream()
                .filter(method -> method.isAnnotationPresent(ClickListener.class))
                .collect(Collectors.toMap(m -> m, m -> m.getDeclaredAnnotation(ClickListener.class)));
        for (Method method : methods.keySet()) {
            ClickListener data = methods.get(method);

            if (data.slot() != context.getEvent().getSlot()) continue;
            try {
                if (method.getParameterCount() == 1 &&
                        method.getParameterTypes()[0] == ClickContext.class) {
                    method.invoke(this, context);
                } else {
                    method.invoke(this);
                }
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void close() {
        setMenuState(MenuState.CLOSED);
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

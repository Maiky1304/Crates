package com.github.maiky1304.crates.utils.menu;

import com.github.maiky1304.crates.utils.text.Text;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Getter
public class Menu {

    private final Inventory inventory;
    private final MenuInfo menuInfo;

    private final HashMap<String, ItemStack> itemMapper = new HashMap<>();

    public Menu() {
        this.menuInfo = this.getClass().getDeclaredAnnotation(MenuInfo.class);
        Objects.requireNonNull(this.menuInfo, "A menu requires a MenuInfo annotation!");
        this.inventory = Bukkit.createInventory(null, this.menuInfo.rows() * 9,
                Text.colors(menuInfo.title()));
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
    private List<Method> getMethods() {
        return Arrays.asList(getClass().getDeclaredMethods());
    }

}

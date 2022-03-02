package com.github.maiky1304.crates.utils.items;

import com.github.maiky1304.crates.utils.text.Text;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ItemBuilder {

    private final ItemStack itemStack;

    private ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    /**
     * Edits the item's durability/data
     * @param data
     * @return instance of current class
     */
    public ItemBuilder setData(int data) {
        return editItem(item -> item.setDurability((short) data));
    }

    /**
     * Edits the item's lore using a vararg
     * @param lore
     * @return instance of current class
     */
    public ItemBuilder setLore(String... lore) {
        return setLore(Arrays.asList(lore));
    }

    /**
     * Edits the item's lore using a list
     * @param lore
     * @return instance of current class
     */
    public ItemBuilder setLore(List<String> lore) {
        return editItemMeta(meta -> meta.setLore(Text.colors(lore)));
    }

    /**
     * Edits the item's name
     * @param name
     * @return instance of current class
     */
    public ItemBuilder setName(String name) {
        return editItemMeta(meta -> meta.setDisplayName(Text.colors(name)));
    }

    /**
     * Edits the ItemStack using a consumer
     * @param consumer
     * @return instance of current class
     */
    public ItemBuilder editItem(Consumer<ItemStack> consumer) {
        consumer.accept(this.itemStack);
        return this;
    }

    /**
     * Edits the ItemMeta using a consumer
     * @param consumer
     * @return instance of current class
     */
    public ItemBuilder editItemMeta(Consumer<ItemMeta> consumer) {
        ItemMeta meta = this.itemStack.getItemMeta();
        consumer.accept(meta);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * @return the fully modified & built ItemStack
     */
    public ItemStack build() {
        return this.itemStack;
    }

    /**
     * Creates a new instance of ItemBuilder using a given Material
     * @param material
     * @return new instance of ItemBuilder
     */
    public static ItemBuilder of(Material material) {
        return new ItemBuilder(new ItemStack(material, 1));
    }

    /**
     * Creates a new instance of ItemBuilder using a given ItemStack
     * @param itemStack
     * @return new instance of ItemBuilder
     */
    public static ItemBuilder of(ItemStack itemStack) {
        return new ItemBuilder(itemStack);
    }

}

package com.github.maiky1304.crates.utils.config.models;

import com.github.maiky1304.crates.utils.items.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class Crate {

    private final String name;
    private final ItemStack boxItem;
    private final List<CrateItem> items;

    /**
     * Checks if the item provided is the same as the current crate's
     * item.
     * @param item
     * @return true or false
     */
    public boolean isCrateItem(ItemStack item) {
        System.out.println(item.isSimilar(createItem(1)));
        return item.isSimilar(createItem(1));
    }

    /**
     * Creates an item to give to a player
     * @return the item created
     */
    public ItemStack createItem(int amount) {
        return ItemBuilder.of(boxItem)
                .setName("&b" + name + " Crate")
                .setAmount(amount)
                .setLore(
                        "",
                        "&7Right-click to open crate."
                ).build();
    }

    public MemoryConfiguration toConfig() {
        MemoryConfiguration config = new MemoryConfiguration();
        config.set("item", boxItem);
        AtomicInteger ai = new AtomicInteger(0);
        config.set("contents", items.stream().collect(Collectors.toMap(u -> ai.getAndIncrement(), CrateItem::toConfig)));
        return config;
    }

    public static Crate fromConfig(String name, ConfigurationSection config) {
        ItemStack itemStack = (ItemStack) config.get("item");
        List<CrateItem> items = config.getConfigurationSection("contents")
                .getKeys(false).stream().map(config.getConfigurationSection("contents")::getConfigurationSection)
                .map(CrateItem::fromConfig).collect(Collectors.toList());
        return new Crate(name, itemStack, new ArrayList<>(items));
    }

}

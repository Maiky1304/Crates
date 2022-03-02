package com.github.maiky1304.crates.utils.config.models;

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

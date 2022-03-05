package com.github.maiky1304.crates.utils.config.models;

import com.github.maiky1304.crates.utils.items.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
@Getter @Setter
public class CrateItem {

    private final ItemStack item;
    private double chance;

    public MemoryConfiguration toConfig() {
        MemoryConfiguration config = new MemoryConfiguration();
        config.set("item", ItemBuilder.of(item).serialize());
        config.set("chance", chance);
        return config;
    }

    public static CrateItem fromConfig(ConfigurationSection config) {
        ItemStack itemStack = ItemBuilder.of(config.getString("item")).build();
        double chance = config.getDouble("chance", 0d);
        return new CrateItem(itemStack, chance);
    }

}

package com.github.maiky1304.crates.utils.config.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
@Getter @Setter
public class CrateItem {

    private final ItemStack item;
    private double chance;

}

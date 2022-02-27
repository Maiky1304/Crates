package com.github.maiky1304.crates.utils.config.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@AllArgsConstructor
@Getter
public class Crate {

    private final ItemStack boxItem;
    private final List<CrateItem> items;

}

package com.github.maiky1304.crates.utils.menu;

import org.bukkit.inventory.ItemStack;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ClickListener {

    int slot();
    String itemId();

}

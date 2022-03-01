package com.github.maiky1304.crates.utils.colors;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

@UtilityClass
public class Text {

    /**
     * Apply colors to a string allowing the & color character
     * @param input
     * @return the string with all colors
     */
    public static String colors(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

}

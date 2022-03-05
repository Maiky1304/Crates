package com.github.maiky1304.crates.utils.text;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * Apply colors to a list of strings allowing the & color character
     * @param input
     * @return the list of strings with all colors
     */
    public static List<String> colors(List<String> input) {
        return input.stream().map(Text::colors).collect(Collectors.toList());
    }

}

package com.github.maiky1304.crates.utils.text;

import lombok.experimental.UtilityClass;

/**
 * Simple Utility Class to convert and check strings for numbers.
 */
@UtilityClass
public class Numbers {

    /**
     * Checks whether a string input is a valid double
     * @param input
     * @return if the string is a valid double
     */
    public static boolean isDouble(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    /**
     * Convert a String to a Double if it wasn't checked or
     * if it's not a valid Double it will cause to throw
     * a IllegalArgumentException.
     * @param input
     * @return the converted integer
     */
    public static double toDouble(String input) {
        if (!isDouble(input)) throw new IllegalArgumentException("String input is not a valid Double");
        return Double.parseDouble(input);
    }

    /**
     * Checks whether a string input is a valid int
     * @param input
     * @return if the string is a valid int
     */
    public static boolean isInt(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    /**
     * Convert a String to an Int if it wasn't checked or
     * if it's not a valid Int it will cause to throw
     * a IllegalArgumentException.
     * @param input
     * @return the converted integer
     */
    public static int toInt(String input) {
        if (!isInt(input)) throw new IllegalArgumentException("String input is not a valid Integer");
        return Integer.parseInt(input);
    }

}

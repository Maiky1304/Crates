package com.github.maiky1304.crates.utils.config.types;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Message {

    public static String INVALID_NUMBER = "errors.invalid-number";
    public static String INVALID_NUMBER_OR_DOUBLE = "errors.invalid-number-or-double";
    public static String CRATE_EXISTS = "errors.crate-exists";
    public static String NEED_ITEM_HAND = "errors.need-item-hand";
    public static String CRATE_NOT_FOUND = "errors.crate-not-found";
    public static String NOT_ONLINE = "errors.not-online";
    public static String INVENTORY_FULL = "errors.inventory-full";
    public static String CRATE_MAX_ITEMS = "errors.crate-max-items";
    public static String CRATE_DAILY_LIMIT = "errors.crate-daily-limit";
    public static String CRATE_COOLDOWN = "errors.crate-cooldown";
    
    public static String CRATE_CREATED = "regular.crate-created";
    public static String CRATE_DELETED = "regular.crate-deleted";
    public static String CRATE_GIVEN = "regular.crate-given";
    public static String ACTION_CANCELLED = "regular.action-cancelled";
    public static String CRATE_ITEM_ADDED = "regular.crate-item-added";
    public static String CRATE_ITEM_REMOVED = "regular.crate-item-removed";
    public static String CRATE_ENTER_NEW_CHANCE = "regular.crate-enter-new-chance";
    public static String CHANCE_SET = "regular.chance-set";
    public static String OPENING_CRATE = "regular.opening-crate";
    public static String REWARD_CRATE = "regular.reward-crate";
    
}

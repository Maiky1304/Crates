package com.github.maiky1304.crates.utils.sounds;

import lombok.experimental.UtilityClass;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@UtilityClass
public class SoundUtil {

    /**
     * Plays a sound only for the player
     * @param player
     * @param sound
     */
    public static void playSound(Player player, Sound sound) {
        player.playSound(player.getLocation(), sound, 0.1f, 1f);
    }

}

package com.github.maiky1304.crates.gui.animations;

import com.cryptomorin.xseries.XSound;
import com.github.maiky1304.crates.CratesPlugin;
import com.github.maiky1304.crates.utils.data.Pair;
import com.github.maiky1304.crates.utils.items.ItemBuilder;
import com.github.maiky1304.crates.utils.menu.Menu;
import com.github.maiky1304.crates.utils.menu.MenuAnimation;
import com.github.maiky1304.crates.utils.random.RandomUtil;
import com.github.maiky1304.crates.utils.sounds.SoundUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public class GlassAnimation extends MenuAnimation {

    private int currentSlot = 0;

    public GlassAnimation(CratesPlugin instance, Menu menu, Runnable runnable) {
        super(instance, menu, 2, runnable);
    }

    @Override
    public void render() {
        Pair<Integer, Integer> frame = nextFrame();

        List<GlassColor> glassColors = Arrays.asList(GlassColor.values());
        GlassColor color = RandomUtil.pickRandom(glassColors);
        GlassColor color2 = RandomUtil.pickRandom(glassColors);

        getMenu().drawItem(frame.getKey(), ItemBuilder.of(Material.STAINED_GLASS_PANE)
                .setData(color.durability).setName(color.color + "...").build());
        getMenu().drawItem(frame.getValue(), ItemBuilder.of(Material.STAINED_GLASS_PANE)
                .setData(color2.durability).setName(color2.color + "...").build());

        SoundUtil.playSound(getMenu().getPlayer(), XSound.BLOCK_NOTE_BLOCK_PLING.parseSound());

        // Means that the animation is 1 item away (empty slot for reward)
        if (frame.getKey() == 21) {
            this.finish();
        }
    }

    public Pair<Integer, Integer> nextFrame() {
        this.currentSlot++;
        return new Pair<>(this.currentSlot - 1, getMenu().getInventory()
                .getSize() - this.currentSlot);
    }

    @RequiredArgsConstructor
    public enum GlassColor {

        WHITE(0, ChatColor.WHITE),
        ORANGE(1, ChatColor.GOLD),
        MAGENTA(2, ChatColor.DARK_PURPLE),
        LIGHT_BLUE(3, ChatColor.BLUE),
        YELLOW(4, ChatColor.YELLOW),
        LIME(5, ChatColor.GREEN),
        PINK(6, ChatColor.LIGHT_PURPLE),
        GRAY(7, ChatColor.DARK_GRAY),
        CYAN(9, ChatColor.DARK_AQUA),
        PURPLE(10, ChatColor.DARK_PURPLE),
        DARK_BLUE(11, ChatColor.DARK_BLUE),
        GREEN(13, ChatColor.DARK_GREEN),
        RED(14, ChatColor.DARK_RED),
        BLACK(15, ChatColor.BLACK);

        private final int durability;
        private final ChatColor color;

    }

}

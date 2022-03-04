package com.github.maiky1304.crates.gui;

import com.github.maiky1304.crates.CratesPlugin;
import com.github.maiky1304.crates.gui.animations.GlassAnimation;
import com.github.maiky1304.crates.utils.config.models.Crate;
import com.github.maiky1304.crates.utils.config.models.CrateItem;
import com.github.maiky1304.crates.utils.items.ItemBuilder;
import com.github.maiky1304.crates.utils.menu.ClickContext;
import com.github.maiky1304.crates.utils.menu.Menu;
import com.github.maiky1304.crates.utils.menu.MenuAnimation;
import com.github.maiky1304.crates.utils.menu.MenuFlag;
import com.github.maiky1304.crates.utils.random.RandomUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class DrawingMenu extends Menu {

    private final CratesPlugin instance;
    private final Crate crate;

    private GlassAnimation animation;

    public DrawingMenu(CratesPlugin instance, Player player, Crate crate) {
        super(player, 5, "Drawing an item...", MenuFlag.DESTROY_ON_CLOSE);
        this.instance = instance;
        this.crate = crate;
    }

    @Override
    public void draw() {
        this.animation = new GlassAnimation(instance, this, this::giveReward);
        animation.start();
    }

    @Override
    public void handleClick(ClickContext context) {
        context.getEvent().setCancelled(true);
    }

    public void giveReward() {
        CrateItem reward = RandomUtil.pickRandomWithChance(crate.getItems(), CrateItem::getChance);
        this.drawItem(22, reward == null ?
                ItemBuilder.of(Material.BARRIER)
                        .setName("&cCrate has no items :(").build()
                : reward.getItem());
    }

    @Override
    public void close() {
        super.close();
        animation.finish();
    }

}

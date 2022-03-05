package com.github.maiky1304.crates.gui;

import com.github.maiky1304.crates.CratesPlugin;
import com.github.maiky1304.crates.gui.animations.GlassAnimation;
import com.github.maiky1304.crates.utils.config.models.Crate;
import com.github.maiky1304.crates.utils.config.models.CrateItem;
import com.github.maiky1304.crates.utils.config.types.Message;
import com.github.maiky1304.crates.utils.items.ItemBuilder;
import com.github.maiky1304.crates.utils.menu.Menu;
import com.github.maiky1304.crates.utils.menu.MenuFlag;
import com.github.maiky1304.crates.utils.random.RandomUtil;
import com.github.maiky1304.crates.utils.text.Text;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class DrawingMenu extends Menu {

    private final CratesPlugin instance;
    private final Crate crate;

    private GlassAnimation animation;
    private boolean rewardHandedOut = false;

    public DrawingMenu(CratesPlugin instance, Player player, Crate crate) {
        super(player, 5, "Drawing an item...", MenuFlag.DESTROY_ON_CLOSE);

        this.instance = instance;
        this.instance.getInventoryManager().registerMenu(this);

        this.crate = crate;
    }

    @Override
    public void draw() {
        this.animation = new GlassAnimation(instance, this, this::giveReward);
        animation.start();
    }

    public void giveReward() {
        if (rewardHandedOut) {
            return;
        }

        rewardHandedOut = true;

        if (this.getPlayer().getInventory().firstEmpty() == -1) {
            this.drawItem(22, ItemBuilder.of(Material.BARRIER).setName("&cYou have not enough inventory space.").build());
        } else {
            CrateItem reward = RandomUtil.pickRandomWithChance(crate.getItems(), CrateItem::getChance);
            this.drawItem(22, reward == null ?
                    ItemBuilder.of(Material.BARRIER)
                            .setName("&cCrate has no items :(").build()
                    : reward.getItem());
            if (reward != null) {
                this.getPlayer().getInventory().addItem(reward.getItem());

                String itemName = reward.getItem().hasItemMeta() && reward.getItem().getItemMeta().hasDisplayName()
                        ? reward.getItem().getItemMeta().getDisplayName() : StringUtils.capitalize(reward.getItem().getType().toString().toLowerCase()
                        .replaceAll("_", " "));
                String chance = new DecimalFormat("0.00").format(reward.getChance());

                this.getPlayer().sendMessage(Text.colors(
                        instance.getMessages().getString(Message.REWARD_CRATE)
                                .replaceAll("%item_name%", itemName)
                                .replaceAll("%item_chance%", chance)
                ));
            }
        }
    }

    @Override
    public void close() {
        super.close();
        animation.finish();
        if (!rewardHandedOut) {
            this.giveReward();
        }
    }

}

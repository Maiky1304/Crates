package com.github.maiky1304.crates.gui;

import com.github.maiky1304.crates.CratesPlugin;
import com.github.maiky1304.crates.utils.config.models.Crate;
import com.github.maiky1304.crates.utils.items.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class OpenConfirmMenu extends ConfirmMenu {

    public OpenConfirmMenu(CratesPlugin instance, Player player, Crate crate, Consumer<Player> consumer) {
        super(instance, player, crate, consumer);
    }

    @Override
    public void draw() {
        super.draw();

        ItemStack icon = ItemBuilder.of(getCrate().getBoxItem())
                .setName(String.format("&b%s Crate", getCrate().getName())).build();
        this.drawItem(13, icon);
    }

}

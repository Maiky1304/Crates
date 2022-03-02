package com.github.maiky1304.crates.gui;

import com.github.maiky1304.crates.CratesPlugin;
import com.github.maiky1304.crates.utils.config.models.Crate;
import com.github.maiky1304.crates.utils.items.ItemBuilder;
import com.github.maiky1304.crates.utils.menu.ClickContext;
import com.github.maiky1304.crates.utils.menu.ClickListener;
import com.github.maiky1304.crates.utils.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ConfirmMenu extends Menu {

    private final Crate crate;
    private final CratesPlugin instance;

    public ConfirmMenu(CratesPlugin instance, Player player, Crate crate) {
        super(
                player,
                3,
                String.format("Create crate %s?", crate.getName())
        );

        this.instance = instance;
        this.instance.getInventoryManager().registerMenu(this);

        this.crate = crate;

        this.defineItem("confirm_item", ItemBuilder
                .of(Material.WOOL)
                .setData(5)
                .setName("&a&lConfirm action")
                .build());
        this.defineItem("cancel_item", ItemBuilder
                .of(Material.WOOL)
                .setData(14)
                .setName("&c&lCancel action")
                .build());
    }

    @ClickListener(
            slot = 12,
            itemId = "confirm_item"
    )
    public void confirmButton(ClickContext context) {
        instance.getData().set(crate.getName(), crate.toConfig());
        instance.getData().save();

        context.reply(String.format("&dYou've successfully created a crate with the name &7%s&d.",
                crate.getName()));
        context.reply(String.format("&dEdit it using &7/crate edit %s&d.", crate.getName()));
        context.getPlayer().closeInventory();
    }

    @ClickListener(
            slot = 14,
            itemId = "cancel_item"
    )
    public void cancelButton(ClickContext context) {
        context.getPlayer().closeInventory();
        context.reply("&cThis action was successfully cancelled.");
    }

}

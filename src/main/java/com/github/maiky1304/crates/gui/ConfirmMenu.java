package com.github.maiky1304.crates.gui;

import com.github.maiky1304.crates.CratesPlugin;
import com.github.maiky1304.crates.utils.config.models.Crate;
import com.github.maiky1304.crates.utils.config.types.Message;
import com.github.maiky1304.crates.utils.items.ItemBuilder;
import com.github.maiky1304.crates.utils.menu.ClickContext;
import com.github.maiky1304.crates.utils.menu.ClickListener;
import com.github.maiky1304.crates.utils.menu.Menu;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

@Getter
public class ConfirmMenu extends Menu {

    private final Crate crate;
    private final CratesPlugin instance;
    private final Consumer<Player> consumer;

    public ConfirmMenu(CratesPlugin instance, Player player, Crate crate, Consumer<Player> consumer) {
        super(
                player,
                4,
                "Confirm action"
        );

        this.instance = instance;
        this.instance.getInventoryManager().registerMenu(this);

        this.crate = crate;

        this.consumer = consumer;

        this.defineItem("confirm_item", ItemBuilder
                .of(Material.WOOL)
                .setData(5)
                .setName("&a&lConfirm")
                .build());
        this.defineItem("cancel_item", ItemBuilder
                .of(Material.WOOL)
                .setData(14)
                .setName("&c&lCancel")
                .build());
    }

    @ClickListener(
            slot = 20,
            itemId = "confirm_item"
    )
    public void confirmButton(ClickContext context) {
        consumer.accept(context.getPlayer());
    }

    @ClickListener(
            slot = 24,
            itemId = "cancel_item"
    )
    public void cancelButton(ClickContext context) {
        context.getPlayer().closeInventory();
        context.reply(instance.getMessages().getString(Message.ACTION_CANCELLED));
    }

}

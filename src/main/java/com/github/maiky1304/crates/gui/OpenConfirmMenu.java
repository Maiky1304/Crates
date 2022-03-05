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
public class OpenConfirmMenu extends Menu {

    private final Crate crate;
    private final CratesPlugin instance;
    private final Consumer<Player> consumer;

    public OpenConfirmMenu(CratesPlugin instance, Player player, Crate crate, Consumer<Player> consumer) {
        super(player, 4, "Confirm action");

        this.crate = crate;

        this.instance = instance;
        this.instance.getInventoryManager().registerMenu(this);

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
        this.defineItem("crate_icon", ItemBuilder
                .of(getCrate().getBoxItem())
                .setName(String.format("&b%s Crate", getCrate().getName()))
                .build());
    }

    @ClickListener(
            slot = 13,
            itemId = "crate_icon"
    )
    public void crateIcon() {
        // Empty block
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

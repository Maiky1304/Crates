package com.github.maiky1304.crates.gui;

import com.github.maiky1304.crates.utils.items.ItemBuilder;
import com.github.maiky1304.crates.utils.menu.ClickContext;
import com.github.maiky1304.crates.utils.menu.ClickListener;
import com.github.maiky1304.crates.utils.menu.Menu;
import com.github.maiky1304.crates.utils.menu.MenuInfo;
import org.bukkit.Material;

@MenuInfo(
        title = "Confirm this action...",
        rows = 3
)
public class ConfirmMenu extends Menu {

    public ConfirmMenu() {
        this.defineItem("confirm_item", ItemBuilder.of(Material.WOOL)
                .setData(5).build());
        this.defineItem("cancel_item", ItemBuilder.of(Material.WOOL)
                .setData(14).build());
    }

    @ClickListener(
            slot = 12,
            itemId = "confirm_item"
    )
    public void confirmButton(ClickContext context) {
        context.reply("You clicked confirm");
    }

    @ClickListener(
            slot = 14,
            itemId = "cancel_item"
    )
    public void cancelButton(ClickContext context) {
        context.reply("You clicked cancel");
    }

}

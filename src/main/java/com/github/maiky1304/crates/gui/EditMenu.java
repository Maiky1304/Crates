package com.github.maiky1304.crates.gui;

import com.github.maiky1304.crates.CratesPlugin;
import com.github.maiky1304.crates.utils.config.models.Crate;
import com.github.maiky1304.crates.utils.menu.Menu;
import com.github.maiky1304.crates.utils.menu.MenuFlag;
import org.bukkit.entity.Player;

public class EditMenu extends Menu {

    private final CratesPlugin instance;
    private final Crate crate;

    public EditMenu(CratesPlugin instance, Player player, Crate crate) {
        super(player, (int) Math.ceil(crate.getItems().size() / 9d), "");
        this.instance = instance;
        this.crate = crate;
    }

}

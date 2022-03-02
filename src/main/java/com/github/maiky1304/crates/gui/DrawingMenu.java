package com.github.maiky1304.crates.gui;

import com.github.maiky1304.crates.utils.menu.Menu;
import com.github.maiky1304.crates.utils.menu.MenuFlag;
import org.bukkit.entity.Player;

public class DrawingMenu extends Menu {

    public DrawingMenu(Player player) {
        super(player, 5, "Drawing an item...", MenuFlag.DESTROY_ON_CLOSE);
    }

    @Override
    public void draw() {
        super.draw();
    }

}

package com.github.maiky1304.crates.listeners;

import com.github.maiky1304.crates.CratesPlugin;
import com.github.maiky1304.crates.gui.DrawingMenu;
import com.github.maiky1304.crates.utils.config.models.Crate;
import com.github.maiky1304.crates.utils.text.Text;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class CrateOpenListener implements Listener {

    private final CratesPlugin instance;

    @EventHandler
    public void onCrateOpen(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (!event.getAction().toString().startsWith("RIGHT_CLICK_")) return;
        if (event.getItem() == null) return;

        ItemStack handItem = event.getItem();
        if (instance.getCrateManager().getLoadedCrates().stream()
                .noneMatch(crate -> crate.isCrateItem(handItem))) return;

        Crate crate = instance.getCrateManager().getLoadedCrates().stream()
                .filter(c -> c.isCrateItem(handItem)).findFirst().orElse(null);
        assert crate != null;

        handItem.setAmount(handItem.getAmount() - 1);

        DrawingMenu menu = new DrawingMenu(instance, event.getPlayer(), crate);
        menu.open();

        event.getPlayer().sendMessage(Text.colors(String.format("&dYou're now opening the crate &7%s&d...",
                crate.getName())));
    }

}

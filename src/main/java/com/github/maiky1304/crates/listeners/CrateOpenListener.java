package com.github.maiky1304.crates.listeners;

import com.github.maiky1304.crates.CratesPlugin;
import com.github.maiky1304.crates.database.models.User;
import com.github.maiky1304.crates.gui.ConfirmMenu;
import com.github.maiky1304.crates.gui.DrawingMenu;
import com.github.maiky1304.crates.utils.config.models.Crate;
import com.github.maiky1304.crates.utils.text.Text;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.time.DateUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Date;

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

        ConfirmMenu confirmMenu = new ConfirmMenu(instance, event.getPlayer(), crate, player -> {
            User user = instance.getUserManager().getCache().get(event.getPlayer().getUniqueId());
            if (!DateUtils.isSameDay(new Date(), new Date(user.getLastCrateTimestamp()))) {
                user.setCratesToday(0);
            }

            if (user.getCratesToday() >= instance.getConfiguration().getInt("settings.per-day")) {
                event.getPlayer().sendMessage(Text.colors("&cYou can't open anymore crates today, try again tomorrow!"));
                return;
            }

            user.setCratesToday(user.getCratesToday() + 1);
            user.setLastCrateTimestamp(System.currentTimeMillis());

            handItem.setAmount(handItem.getAmount() - 1);

            DrawingMenu menu = new DrawingMenu(instance, event.getPlayer(), crate);
            menu.open();

            event.getPlayer().sendMessage(Text.colors(String.format("&dYou're now opening the crate &7%s&d...",
                    crate.getName())));
        });
        confirmMenu.open();
    }

}

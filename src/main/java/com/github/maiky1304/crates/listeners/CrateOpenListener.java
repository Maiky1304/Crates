package com.github.maiky1304.crates.listeners;

import com.github.maiky1304.crates.CratesPlugin;
import com.github.maiky1304.crates.database.models.User;
import com.github.maiky1304.crates.gui.DrawingMenu;
import com.github.maiky1304.crates.gui.OpenConfirmMenu;
import com.github.maiky1304.crates.utils.config.models.Crate;
import com.github.maiky1304.crates.utils.config.types.Message;
import com.github.maiky1304.crates.utils.config.types.Option;
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

        event.setCancelled(true);

        Crate crate = instance.getCrateManager().getLoadedCrates().stream()
                .filter(c -> c.isCrateItem(handItem)).findFirst().orElse(null);
        assert crate != null;

        OpenConfirmMenu confirmMenu = new OpenConfirmMenu(instance, event.getPlayer(), crate, player -> {
            User user = instance.getUserManager().getCache().get(event.getPlayer().getUniqueId());
            if (!DateUtils.isSameDay(new Date(), new Date(user.getLastCrateTimestamp()))) {
                user.setCratesToday(0);
            }

            if (user.getCratesToday() >= instance.getConfiguration().getInt(Option.CRATES_PER_DAY)) {
                event.getPlayer().sendMessage(Text.colors(instance.getMessages().getString(Message.CRATE_DAILY_LIMIT)));
                return;
            }

            if ((user.getLastCrateTimestamp() + (instance.getConfiguration().getInt(Option.CRATES_COOLDOWN)))
            > System.currentTimeMillis()) {
                event.getPlayer().sendMessage(Text.colors(instance.getMessages().getString(Message.CRATE_COOLDOWN)));
                return;
            }

            user.setCratesToday(user.getCratesToday() + 1);
            user.setLastCrateTimestamp(System.currentTimeMillis());

            handItem.setAmount(handItem.getAmount() - 1);

            DrawingMenu menu = new DrawingMenu(instance, event.getPlayer(), crate);
            menu.open();

            event.getPlayer().sendMessage(Text.colors(instance.getMessages().getString(Message.OPENING_CRATE).replaceAll("%crate_name%",
                    crate.getName())));
        });
        confirmMenu.open();
    }

}

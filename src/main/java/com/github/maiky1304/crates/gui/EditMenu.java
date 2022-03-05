package com.github.maiky1304.crates.gui;

import com.github.maiky1304.crates.CratesPlugin;
import com.github.maiky1304.crates.gui.listeners.CrateItemChanceListener;
import com.github.maiky1304.crates.utils.config.models.Crate;
import com.github.maiky1304.crates.utils.config.models.CrateItem;
import com.github.maiky1304.crates.utils.config.types.Message;
import com.github.maiky1304.crates.utils.config.types.Option;
import com.github.maiky1304.crates.utils.data.Pair;
import com.github.maiky1304.crates.utils.items.ItemBuilder;
import com.github.maiky1304.crates.utils.menu.ClickContext;
import com.github.maiky1304.crates.utils.menu.Menu;
import com.github.maiky1304.crates.utils.text.Numbers;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class EditMenu extends Menu {

    private final CratesPlugin instance;
    private final Crate crate;

    private final HashMap<Integer, CrateItem> crateItemMapper = new HashMap<>();

    public EditMenu(CratesPlugin instance, Player player, Crate crate) {
        super(player, calculateRows(crate), String.format("&d&lCrate &8- &7%s", crate.getName()));

        this.instance = instance;
        this.instance.getInventoryManager().registerMenu(this);

        this.crate = crate;
    }

    @Override
    public void draw() {
        this.draws++;

        if (!isFirstDraw()) {
            this.inventory.clear();
            this.crateItemMapper.clear();

            if (this.crate.getItems().size() > getInventory().getSize()
            || this.crate.getItems().size() <= (getInventory().getSize() - 9)) {
                this.setRows(calculateRows(this.crate));
            }
        }

        AtomicInteger slot = new AtomicInteger(0);
        crate.getItems().stream()
                .map(item -> new Pair<ItemStack, CrateItem>(
                        ItemBuilder.of(item.getItem())
                                .setLore(
                                        "",
                                        "&5Right-click &7to &dremove&7.",
                                        "&5Shift-click &7to &dset chance&7.",
                                        "",
                                        String.format("&dCurrent chance: &7%.2f%%", item.getChance())
                                )
                                .build(),
                        item)
                ).forEach(pair -> {
                    final int i = slot.getAndIncrement();

                    this.drawItem(i, pair.getKey());
                    this.crateItemMapper.put(i, pair.getValue());
                });
    }

    @Override
    public void handleClick(ClickContext context) {
        boolean isCrateItem = this.crateItemMapper.containsKey(context.getEvent().getSlot());
        boolean isPlayerInventory = context.getEvent().getClickedInventory() ==
                context.getPlayer().getOpenInventory().getBottomInventory();

        switch (context.getClickType()) {
            case LEFT:
                // Add item
                if (!isPlayerInventory) return;

                if (crate.getItems().size() >= instance.getConfiguration().getInt(Option.CRATES_MAX_ITEMS)) {
                    context.reply(instance.getMessages().getString(Message.CRATE_MAX_ITEMS));
                    return;
                }

                ItemStack itemStack = context.getEvent().getCurrentItem();
                if (itemStack == null || itemStack.getType() == Material.AIR) {
                    return;
                }

                CrateItem itemToAdd = new CrateItem(itemStack, 0);
                this.crate.getItems().add(itemToAdd);
                this.draw();

                context.reply(instance.getMessages().getString(Message.CRATE_ITEM_ADDED).replaceAll("%crate_name%",
                        crate.getName()));
                break;
            case RIGHT:
                // Remove item
                if (isPlayerInventory) return;
                if (!isCrateItem) return;

                CrateItem crateItemToRemove = this.crateItemMapper.get(context.getEvent().getSlot());

                this.crate.getItems().remove(crateItemToRemove);
                this.draw();

                context.reply(instance.getMessages().getString(Message.CRATE_ITEM_REMOVED).replaceAll("%crate_name%",
                        crate.getName()));
                break;
            case SHIFT_LEFT:
                // Set chance
                if (isPlayerInventory) return;
                if (!isCrateItem) return;

                context.getPlayer().closeInventory();
                context.reply(instance.getMessages().getString(Message.CRATE_ENTER_NEW_CHANCE));

                CrateItem crateItemToEdit = this.crateItemMapper.get(context.getEvent().getSlot());

                CrateItemChanceListener listener = new CrateItemChanceListener(context.getPlayer(),
                        (player, input) -> {
                            if (!Numbers.isDouble(input)) {
                                this.open();
                                context.reply(instance.getMessages().getString(Message.INVALID_NUMBER_OR_DOUBLE));
                                return;
                            }

                            double chance = Numbers.toDouble(input);
                            crateItemToEdit.setChance(chance);

                            // Inventory open has to be sync because of Bukkit API limitations.
                            Bukkit.getScheduler().runTask(instance, this::open);

                            context.reply(String.format(instance.getMessages().getString(Message.CHANCE_SET), chance));
                        });
                instance.registerListener(listener);
                break;
        }
    }

    /**
     * Calculate the rows required by dividing the crates by 9.0 and then
     * using Math#ceil to make it the next highest number.
     *
     * Also prevents it from becoming zero with a zero check. (makes it one
     * instead)
     * @return the rows calculated
     */
    private static int calculateRows(Crate crate) {
        return crate.getItems().size() == 0 ? 1 : (int) Math.ceil(crate.getItems().size() / 9d);
    }

}

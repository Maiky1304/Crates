package com.github.maiky1304.crates.commands;

import com.github.maiky1304.crates.CratesPlugin;
import com.github.maiky1304.crates.gui.ConfirmMenu;
import com.github.maiky1304.crates.gui.EditMenu;
import com.github.maiky1304.crates.utils.command.*;
import com.github.maiky1304.crates.utils.config.models.Crate;
import com.github.maiky1304.crates.utils.config.types.Message;
import com.github.maiky1304.crates.utils.text.Numbers;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CommandInfo(value = "crate", permission = "crates.admin", type = CommandType.PLAYERS)
public class CrateCommand extends Command {

    private final CratesPlugin instance;

    public CrateCommand(CratesPlugin instance) {
        this.instance = instance;

        this.defineTabCompletion("crates", ctx -> instance.getCrateManager().getLoadedCrates()
                .stream().map(Crate::getName).collect(Collectors.toList()));
        this.defineTabCompletion("players", ctx -> Bukkit.getOnlinePlayers().stream()
                .map(Player::getName).collect(Collectors.toList()));
    }


    @DefaultCommand
    @SubCommandInfo("help")
    public void onHelp(CommandContext context) {
        int page = 1;

        if (context.getArgs().length != 0) {
            if (!Numbers.isInt(context.getArgs()[0])) {
                context.reply(instance.getMessages().getString(Message.INVALID_NUMBER));
                return;
            } else page = Numbers.toInt(context.getArgs()[0]);
        }

        CommandHelp help = new CommandHelp(this);
        List<SubCommandInfo> range = help.getRange(page - 1);

        context.reply(String.format("&d&lCrates &8- &7%s&8/&7%s", page, help.getPages()));
        if (range.size() == 0) {
            context.reply("    &c&oNo subcommands found :(");
        } else {
            range.stream().map(item -> String.format("&5/&d%s %s %s", context.getLabel(),
                            item.value(), item.usage()))
                    .forEach(context::reply);
        }
    }

    @SubCommandInfo(
            value = "create",
            permission = "crates.admin.create",
            usage = "<name>"
    )
    public void onCreate(CommandContext context) {
        if (context.getArgs().length != 1) {
            context.reply(String.format("&cUsage: /%s create <name>", context.getLabel()));
            return;
        }

        String name = context.getArgs()[0];

        if (instance.getCrateManager().findCrate(name) != null) {
            context.reply(instance.getMessages().getString(Message.CRATE_EXISTS));
            return;
        }

        Player player = context.getPlayer();
        if (player.getInventory().getItemInMainHand() == null
            || player.getInventory().getItemInMainHand().getType() == Material.AIR) {
            context.reply(instance.getMessages().getString(Message.NEED_ITEM_HAND));
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        Crate crate = new Crate(name, item, new ArrayList<>());

        ConfirmMenu confirmMenu = new ConfirmMenu(instance,
                context.getPlayer(), crate, user -> {
            instance.getCrateManager().addCrate(crate);

            context.reply(Arrays.toString(instance.getConfiguration().getStringList(Message.CRATE_CREATED)
                    .stream().map(line -> line.replaceAll("%crate_name%", crate.getName())).toArray()));
            context.getPlayer().closeInventory();
        });
        confirmMenu.open();
    }

    @SubCommandInfo(
            value = "delete",
            permission = "crates.admin.delete",
            usage = "<name>"
    )
    @TabInfo("crates")
    public void onDelete(CommandContext context) {
        if (context.getArgs().length != 1) {
            context.reply(String.format("&cUsage: /%s delete <name>", context.getLabel()));
            return;
        }

        String name = context.getArgs()[0];
        Crate crate = instance.getCrateManager().findCrate(name);

        if (crate == null) {
            context.reply(instance.getMessages().getString(Message.CRATE_NOT_FOUND));
            return;
        }

        ConfirmMenu confirmMenu = new ConfirmMenu(instance,
                context.getPlayer(), crate, user -> {
            instance.getCrateManager().removeCrate(crate);

            context.reply(instance.getMessages().getString(Message.CRATE_DELETED).replaceAll("%crate_name%", crate.getName()));
            context.getPlayer().closeInventory();
        });
        confirmMenu.open();
    }

    @SubCommandInfo(
            value = "edit",
            permission = "crates.admin.edit",
            usage = "<name>"
    )
    @TabInfo("crates")
    public void onEdit(CommandContext context) {
        if (context.getArgs().length != 1) {
            context.reply(String.format("&cUsage: /%s edit <name>", context.getLabel()));
            return;
        }

        String name = context.getArgs()[0];
        Crate crate = instance.getCrateManager().findCrate(name);

        if (crate == null) {
            context.reply(instance.getMessages().getString(Message.CRATE_NOT_FOUND));
            return;
        }

        EditMenu editMenu = new EditMenu(instance, context.getPlayer(), crate);
        editMenu.open();
    }

    @SubCommandInfo(
            value = "give",
            permission = "crates.admin.give",
            usage = "<player> <crate> <amount>"
    )
    @TabInfo("players crates")
    public void onGive(CommandContext context) {
        if (context.getArgs().length != 3) {
            context.reply(String.format("&cUsage: /%s give <player> <crate> <amount>",
                    context.getLabel()));
            return;
        }

        String playerArg = context.getArgs()[0];
        String crateArg = context.getArgs()[1];
        String amountArg = context.getArgs()[2];

        Player player = Bukkit.getPlayer(playerArg);
        if (player == null) {
            context.reply(instance.getMessages().getString(Message.NOT_ONLINE).replaceAll("%name%", playerArg));
            return;
        }

        Crate crate = instance.getCrateManager().findCrate(crateArg);
        if (crate == null) {
            context.reply(instance.getMessages().getString(Message.CRATE_NOT_FOUND));
            return;
        }

        if (!Numbers.isInt(amountArg)) {
            context.reply(instance.getMessages().getString(Message.INVALID_NUMBER));
            return;
        }

        int amount = Numbers.toInt(amountArg);

        if (player.getInventory().firstEmpty() == -1) {
            context.reply(instance.getMessages().getString(Message.INVENTORY_FULL)
                    .replaceAll("%name%", player.getName()));
            return;
        }

        player.getInventory().addItem(crate.createItem(amount));
        context.reply(instance.getMessages().getString(Message.CRATE_GIVEN)
                .replaceAll("%name%", player.getName())
                .replaceAll("%amount%", amountArg)
                .replaceAll("%crate_name%", crate.getName()));
    }

}

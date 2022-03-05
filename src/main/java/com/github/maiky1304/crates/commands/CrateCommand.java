package com.github.maiky1304.crates.commands;

import com.github.maiky1304.crates.CratesPlugin;
import com.github.maiky1304.crates.gui.ConfirmMenu;
import com.github.maiky1304.crates.gui.EditMenu;
import com.github.maiky1304.crates.utils.command.*;
import com.github.maiky1304.crates.utils.config.models.Crate;
import com.github.maiky1304.crates.utils.text.Numbers;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@CommandInfo(value = "crate", permission = "crates.admin", type = CommandType.PLAYERS)
public class CrateCommand extends Command {

    private final CratesPlugin instance;

    @DefaultCommand
    @SubCommandInfo("help")
    public void onHelp(CommandContext context) {
        int page = 1;

        if (context.getArgs().length != 0) {
            if (!Numbers.isInt(context.getArgs()[0])) {
                context.reply(ChatColor.RED + "This is not a valid number.");
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
    @TabInfo("crates")
    public void onCreate(CommandContext context) {
        if (context.getArgs().length != 1) {
            context.reply(String.format("&cUsage: /%s create <name>", context.getLabel()));
            return;
        }

        String name = context.getArgs()[0];

        if (instance.getCrateManager().findCrate(name) != null) {
            context.reply("&cA crate with this name already exists!");
            return;
        }

        Player player = context.getPlayer();
        if (player.getInventory().getItemInMainHand() == null) {
            context.reply("&cYou need an item in your hand to do this.");
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        Crate crate = new Crate(name, item, new ArrayList<>());

        ConfirmMenu confirmMenu = new ConfirmMenu(instance,
                context.getPlayer(), crate, user -> {
            instance.getCrateManager().addCrate(crate);

            context.reply(String.format("&dYou've successfully created a crate with the name &7%s&d.",
                    crate.getName()));
            context.reply(String.format("&dEdit it using &7/crate edit %s&d.", crate.getName()));
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
            context.reply("&cCan't find a crate with that name.");
            return;
        }

        ConfirmMenu confirmMenu = new ConfirmMenu(instance,
                context.getPlayer(), crate, user -> {
            instance.getCrateManager().removeCrate(crate);

            context.reply(String.format("&dYou've successfully deleted a crate with the name &7%s&d.",
                    crate.getName()));
            context.getPlayer().closeInventory();
        });
    }

    @SubCommandInfo(
            value = "edit",
            permission = "crates.admin.edit",
            usage = "<name>"
    )
    public void onEdit(CommandContext context) {
        if (context.getArgs().length != 1) {
            context.reply(String.format("&cUsage: /%s edit <name>", context.getLabel()));
            return;
        }

        String name = context.getArgs()[0];
        Crate crate = instance.getCrateManager().findCrate(name);

        if (crate == null) {
            context.reply("&cCan't find a crate with that name.");
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
            context.reply(String.format("&cThere's no player online with the username %s.",
                    playerArg));
            return;
        }

        Crate crate = instance.getCrateManager().findCrate(crateArg);
        if (crate == null) {
            context.reply(String.format("&cThere's no crate with the name %s.", crateArg));
            return;
        }

        if (!Numbers.isInt(amountArg)) {
            context.reply(String.format("&c%s is not a valid Integer.", amountArg));
            return;
        }

        int amount = Numbers.toInt(amountArg);

        if (player.getInventory().firstEmpty() == -1) {
            context.reply(String.format("&cTell %s to make inventory space, because their inventory is full.",
                    player.getName()));
            return;
        }

        player.getInventory().addItem(crate.createItem(amount));
        context.reply(String.format("&dSuccessfully given &7%s &dthe crate &7%sx %s&d.",
                player.getName(), amount, crate.getName()));
    }

}

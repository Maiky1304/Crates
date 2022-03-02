package com.github.maiky1304.crates.commands;

import com.github.maiky1304.crates.CratesPlugin;
import com.github.maiky1304.crates.gui.ConfirmMenu;
import com.github.maiky1304.crates.utils.command.*;
import com.github.maiky1304.crates.utils.config.models.Crate;
import com.github.maiky1304.crates.utils.text.Numbers;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
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
    public void onCreate(CommandContext context) {
        if (context.getArgs().length != 1) {
            context.reply(String.format("&cUsage: /%s create <name>", context.getLabel()));
            return;
        }

        Player player = context.getPlayer();
        if (player.getInventory().getItemInMainHand() == null) {
            context.reply("&cJe moet een item in je hand hebben om dit te doen.");
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        String name = context.getArgs()[0];
        Crate crate = new Crate(name, item, Collections.emptyList());

        ConfirmMenu confirmMenu = new ConfirmMenu(instance, context.getPlayer(), crate);
        confirmMenu.open();
    }

    @SubCommandInfo(
            value = "delete",
            permission = "crates.admin.delete",
            usage = "<name>"
    )
    public void onDelete(CommandContext context) {
        if (context.getArgs().length != 1) {
            context.reply(String.format("&cUsage: /%s delete <name>", context.getLabel()));
            return;
        }

        String name = context.getArgs()[0];
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
    }

    @SubCommandInfo(
            value = "give",
            permission = "crates.admin.give",
            usage = "<player> <crate> <amount>"
    )
    public void onGive(CommandContext context) {
    }

}

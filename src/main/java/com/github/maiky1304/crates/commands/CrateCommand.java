package com.github.maiky1304.crates.commands;

import com.github.maiky1304.crates.utils.command.*;
import com.github.maiky1304.crates.utils.text.Numbers;
import org.bukkit.ChatColor;

import java.util.List;

@CommandInfo(value = "crate", permission = "crates.admin")
public class CrateCommand extends Command {

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

        String name = context.getArgs()[0];

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

}

package com.github.maiky1304.crates.commands;

import com.github.maiky1304.crates.utils.command.*;
import org.apache.commons.lang.ArrayUtils;

import java.util.List;

@CommandInfo(value = "crate", permission = "crates.admin")
public class CrateCommand extends Command {

    @DefaultCommand
    @SubCommandInfo("help")
    public void onHelp(CommandContext context, int page) {
        CommandHelp help = new CommandHelp(this);
        List<SubCommandInfo> range = help.getRange(page);

        String[] lines = new String[]{
                String.format("&d&lCrates &8- &7%s&8/&7%s", help.getPage(), help.getPages()),
        };
        range.stream().map(item -> String.format("&5/&d%s %s %s"));
        context.reply(lines);
    }

    @SubCommandInfo(
            value = "create",
            permission = "<permission>.create"
    )
    public void onCreate(CommandContext context) {

    }

}

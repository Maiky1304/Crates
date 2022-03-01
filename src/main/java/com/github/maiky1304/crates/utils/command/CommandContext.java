package com.github.maiky1304.crates.utils.command;

import com.github.maiky1304.crates.utils.colors.Text;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
@Getter
public class CommandContext {

    private final CommandSender sender;
    private final String label;
    private final String[] args;

    /**
     * Send multiple lines to the CommandSender at once also
     * supports color codes like the CommandContext#reply method
     * with a single string.
     * @param lines
     */
    public void reply(String... lines) {
        for (String line : lines) {
            reply(line);
        }
    }

    /**
     * Send a message directly to the CommandSender that supports
     * color codes using the & character.
     * @param message
     */
    public void reply(String message) {
        sender.sendMessage(Text.colors(message));
    }

    /**
     * Combines all arguments in the String array
     * seperated by a space
     * @return combined string seperated by space
     */
    public String joinArgs() {
        return StringUtils.join(args, ' ');
    }

}

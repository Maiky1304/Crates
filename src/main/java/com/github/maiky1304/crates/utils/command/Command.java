package com.github.maiky1304.crates.utils.command;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Simple extension of CommandExecutor can be extended in another class
 * needs to be registered the regular way through JavaPlugin.
 */
@Getter
public abstract class Command implements CommandExecutor {

    private final CommandInfo info;

    public Command() {
        this.info = getClass().getDeclaredAnnotation(CommandInfo.class);
        Objects.requireNonNull(this.info, "A command requires a CommandInfo annotation!");
    }

    @SneakyThrows
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (!info.permission().isEmpty() && !sender.hasPermission(info.permission())) {
            sender.sendMessage(ChatColor.RED + "You have no permissions to execute this command.");
            return true;
        }

        if (info.type() == CommandType.PLAYERS && !(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You have to be a player to execute this command.");
            return true;
        }

        if (info.type() == CommandType.CONSOLE && sender instanceof Player) {
            sender.sendMessage(ChatColor.RED + "You can only execute this command from the console.");
            return true;
        }

        CommandContext context = new CommandContext(sender, label, args);
        if (args.length == 0) {
            Method fallback = findFallbackMethod();
            if (fallback == null) {
                sender.sendMessage(ChatColor.RED + "No fallback method was setup for this command.");
                return true;
            }
            fallback.invoke(this, context);
        } else {
            Map<Method, SubCommandInfo> methods = findSubCommands();

            String subCommand = args[0].toLowerCase();
            Method subCommandMethod = methods.keySet().stream()
                    .filter(method -> methods.get(method).value().toLowerCase().equals(subCommand))
                    .findFirst().orElse(null);
            if (subCommandMethod == null) {
                sender.sendMessage(ChatColor.RED + String.format("This sub-command does not exist, try /%s help",
                        label));
                return true;
            }

            SubCommandInfo sci = methods.get(subCommandMethod);
            if (!sender.hasPermission(sci.permission())) {
                sender.sendMessage(ChatColor.RED + "You have no permissions to execute this subcommand.");
                return true;
            }

            subCommandMethod.invoke(this, context);
        }
        return true;
    }

    /**
     * Finds all the methods that have the SubCommandInfo annotation
     * then maps them to the method as the key and the instance of the annotation as
     * the value.
     * @return a key value pair map with the methods and subcommand info data.
     */
    private Map<Method, SubCommandInfo> findSubCommands() {
        return getMethods().stream().filter(method -> method.isAnnotationPresent(SubCommandInfo.class))
                .collect(Collectors.toMap(method -> method, method -> method.getDeclaredAnnotation(SubCommandInfo.class)));

    }

    /**
     * Finds a declared method in the class that have the DefaultCommand annotation
     * and the value set to true.
     * @return the method found or null
     */
    private Method findFallbackMethod() {
        return getMethods().stream().filter(method -> method.isAnnotationPresent(DefaultCommand.class))
                .filter(method -> method.getDeclaredAnnotation(DefaultCommand.class).value())
                .findFirst().orElse(null);
    }

    /**
     * Returns all declared methods that exist in the class
     * @return List of classes
     */
    private List<Method> getMethods() {
        return Arrays.asList(getClass().getDeclaredMethods());
    }

}

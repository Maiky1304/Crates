package com.github.maiky1304.crates.utils.command;

import com.github.maiky1304.crates.utils.data.Pair;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Simple extension of CommandExecutor can be extended in another class
 * needs to be registered the regular way through JavaPlugin.
 */
@Getter
public abstract class Command implements CommandExecutor, TabExecutor {

    private final CommandInfo info;
    private final HashMap<String, Function<CommandContext, List<String>>> tabCompleters = new HashMap<>();

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

        CommandContext context = new CommandContext(sender, label, args.length > 0 ? (String[]) ArrayUtils.remove(args, 0) : args);
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

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        if (args.length == 1) {
            return findSubCommands().values().stream()
                    .map(SubCommandInfo::value).collect(Collectors.toList());
        }

        String argument = args[0];
        return findTabAnnotations()
                .values()
                .stream()
                .filter(pair -> pair.getKey().value().equals(argument))
                .findFirst()
                .map(pair -> {
                    if (pair.getValue().value().split(" ").length <= (args.length - 2))
                        return null;

                    String id = pair.getValue().value().split(" ")[args.length - 2];
                    return this.tabCompleters.containsKey(id) ?
                            this.tabCompleters.get(id).apply(new CommandContext(sender, alias, args))
                            : null;
                }).orElse(null);
    }

    /**
     * Defines a tab completer inside the map
     * @param id
     * @param function
     */
    public void defineTabCompletion(String id, Function<CommandContext, List<String>> function) {
        this.tabCompleters.put(id, function);
    }

    /**
     * Finds all the methods that have the TabInfo and SubCommandInfo annotation
     * then maps them to the method as the key and the instance of the annotation
     * of the annotation as the value.
     * @return a key value pair map with the methods and subcommand info data.
     */
    private Map<Method, Pair<SubCommandInfo, TabInfo>> findTabAnnotations() {
        return getMethods().stream().filter(method -> method.isAnnotationPresent(TabInfo.class) && method.isAnnotationPresent(SubCommandInfo.class))
                .collect(Collectors.toMap(method -> method, method -> new Pair<>(method.getDeclaredAnnotation(SubCommandInfo.class),
                        method.getDeclaredAnnotation(TabInfo.class))));
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

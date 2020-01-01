package io.github.xiione;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.List;

//this.getCommand(XiionePluginClass.NAME).setExecutor(new XiionePluginClass());

public class XiionePluginClass implements Listener, CommandExecutor, TabCompleter {

    public final static String NAME = "anvillore";
    public final static String NAME_FORMATTED = "AnviLore";
    public final static String RESOURCE_ID = "00000";
    public final static double PLUGIN_VERSION = 1.0;
    private final static char PRIMARY_COLOR = 'a';
    private final static char SECONDARY_COLOR = '7';
    private final static char PLAIN_COLOR = 'f';

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender.hasPermission(NAME + ".admin")) {
            if (args.length == 0) {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&" + PRIMARY_COLOR + NAME_FORMATTED + " " + PLUGIN_VERSION + " &" + SECONDARY_COLOR + "by Xiione"));
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7https://www.spigotmc.org/resources/" + NAME + "." + RESOURCE_ID + "/"));
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&" + PRIMARY_COLOR + "Usage: &" + SECONDARY_COLOR + "/" + NAME + " [help|reload]"));
            } else switch (args[0].toLowerCase()) {
                case "reload":
                    if (args.length > 1) {
                        commandSender.sendMessage(ChatColor.RED + "Too many arguments provided!");
                        return true;
                    } else {
                        AnviLoreListener.loadConfigs(true); //change me!!
                        commandSender.sendMessage(ChatColor.GREEN + NAME_FORMATTED + " config reloaded!");
                        return true;
                    }
                case "help":
                    if (args.length > 1) {
                        commandSender.sendMessage(ChatColor.RED + "Too many arguments provided!");
                        return true;
                    } else {
                        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&" + SECONDARY_COLOR + "/" + NAME + "&" + PLAIN_COLOR + ": Show plugin info."));
                        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&" + SECONDARY_COLOR + "/" + NAME + " help&" + PLAIN_COLOR + ": Show command usages."));
                        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&" + SECONDARY_COLOR + "/" + NAME + " reload&" + PLAIN_COLOR + ": Reload the plugin configuration."));
                        return true;
                    }
                default:
                    commandSender.sendMessage(ChatColor.RED + "Unknown subcommand!");
                    return true;
            }
            return false;
        } else {
            commandSender.sendMessage(ChatColor.RED + "No permission!");
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if (command.getName().equalsIgnoreCase(NAME)) {
            List<String> emptyList = Arrays.asList("");
            switch (args.length) {
                case 1:
                    return Arrays.asList("help", "give", "reload");
                case 2:
                    switch (args[0].toLowerCase()) {
                        case "help":
                            return emptyList;
                        case "reload":
                            return emptyList;
                        case "give":
                            return null;
                    }
                default:
                    return emptyList;
            }
        }
        return null;
    }
}

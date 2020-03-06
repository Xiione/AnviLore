package io.github.xiione;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Arrays;
import java.util.List;

//this.getCommand(XiionePluginClass.NAME).setExecutor(new XiionePluginClass());

public class XiionePluginClass implements Listener, CommandExecutor, TabCompleter {

    public XiionePluginClass(AnviLore passedPlugin) { this.plugin = passedPlugin; } //change me!

    private AnviLore plugin;

    public final static String NAME = "anvilore";
    public final static String NAME_FORMATTED = "AnviLore";
    public final static String RESOURCE_ID = "00000";
    public final static double PLUGIN_VERSION = 1.0;

    private final static ChatColor PRIMARY_COLOR = ChatColor.GREEN;
    private final static ChatColor SECONDARY_COLOR = ChatColor.GRAY;
    private final static ChatColor PLAIN_COLOR = ChatColor.WHITE;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender.hasPermission(NAME + ".admin")) {
            if (args.length == 0) {
                commandSender.sendMessage(PRIMARY_COLOR + NAME_FORMATTED + " " + PLUGIN_VERSION + " " + SECONDARY_COLOR + "by Xiione");
                commandSender.sendMessage(SECONDARY_COLOR + "https://www.spigotmc.org/resources/" + NAME + "." + RESOURCE_ID + "/");
                commandSender.sendMessage(PRIMARY_COLOR + "Usage: " + SECONDARY_COLOR + "/" + NAME + " [help|reload]");
            } else switch (args[0].toLowerCase()) {
                case "reload":
                    if (args.length > 1) {
                        commandSender.sendMessage(ChatColor.RED + "Too many arguments provided!");
                        return true;
                    } else {
                        plugin.loadConfigs(true); //change me!
                        commandSender.sendMessage(ChatColor.GREEN + NAME_FORMATTED + " config reloaded!");
                        return true;
                    }
                case "help":
                    if (args.length > 1) {
                        commandSender.sendMessage(ChatColor.RED + "Too many arguments provided!");
                        return true;
                    } else {
                        commandSender.sendMessage(SECONDARY_COLOR + "/" + NAME + "" + PLAIN_COLOR + ": Show plugin info.");
                        commandSender.sendMessage(SECONDARY_COLOR + "/" + NAME + " help" + PLAIN_COLOR + ": Show command usages.");
                        commandSender.sendMessage(SECONDARY_COLOR + "/" + NAME + " reload" + PLAIN_COLOR + ": Reload the plugin configuration.");
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
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) { //TODO find proper tabcompletion api?
        if (command.getName().equalsIgnoreCase(NAME)) {
            List<String> emptyList = Arrays.asList("");
            switch (args.length) {
                case 1:
                    return Arrays.asList("help", "reload");
                case 2:
                    switch (args[0].toLowerCase()) {
                        case "help":
                            return emptyList;
                        case "reload":
                            return emptyList;
                    }
                default:
                    return emptyList;
            }
        }
        return null;
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission(NAME + ".notifyupdate")) { //TODO change me!! (notify_update &&)
            UpdateCheck
                    .of(plugin)
                    .resourceId(Integer.parseInt(RESOURCE_ID))
                    .handleResponse((versionResponse, version) -> {
                        switch (versionResponse) {
                            case FOUND_NEW:
                                p.sendMessage(PRIMARY_COLOR + "A new version of " + NAME_FORMATTED + " is available!" + SECONDARY_COLOR + " (" + SECONDARY_COLOR + version + SECONDARY_COLOR + ")");
                                p.sendMessage(SECONDARY_COLOR + "You can find it here: " + PRIMARY_COLOR + "https://www.spigotmc.org/resources/" + NAME + "." + RESOURCE_ID + "/");
                                break;
                            case LATEST:
                                break;
                            case UNAVAILABLE:
                                p.sendMessage(ChatColor.RED + "Unable to perform a version check for " + NAME_FORMATTED + ".");
                        }
                    }).check();
        }
    }
}

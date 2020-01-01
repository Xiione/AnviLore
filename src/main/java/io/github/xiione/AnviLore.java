package io.github.xiione;

import org.bukkit.plugin.java.JavaPlugin;

public class AnviLore extends JavaPlugin {

    @Override
    public void onEnable() {
        final XiionePluginClass xiionePluginClass = new XiionePluginClass();
        final AnviLoreListener anviLoreListener = new AnviLoreListener();

        getServer().getPluginManager().registerEvents(xiionePluginClass, this);
        getServer().getPluginManager().registerEvents(anviLoreListener, this);

        this.getCommand(XiionePluginClass.NAME).setExecutor(new XiionePluginClass());

    }

    @Override
    public void onDisable() {

    }
}

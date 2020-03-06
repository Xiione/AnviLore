package io.github.xiione;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class AnviLore extends JavaPlugin {

    String version;

    //TODO colored name fixing
    //TODO using a specified item renamed with proper color coding

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.loadConfigs(false);

        final XiionePluginClass xiionePluginClass = new XiionePluginClass(this);
        final AnviLoreListener anviLoreListener = new AnviLoreListener(this);

        getServer().getPluginManager().registerEvents(xiionePluginClass, this);
        getServer().getPluginManager().registerEvents(anviLoreListener, this);

        this.getCommand(XiionePluginClass.NAME).setExecutor(xiionePluginClass);

        try{
            this.getLogger().info("Package name = " + Bukkit.getServer().getClass().getPackage().getName());
            version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            this.getLogger().info("Loading NMS support for version " + version + "...");
        } catch(ArrayIndexOutOfBoundsException e) {
            this.getLogger().warning("Failed to load NMS support!");
        }

    }

    @Override
    public void onDisable() {

    }

    //TODO level cost sum system

    protected char color_code_prefix;
    protected boolean apply_vanilla_repairs, use_whitelist_item, use_whitelist_lore_item, read_from_displayname, erase_old_lore;
    protected List<String> whitelist_item, blacklist_item, whitelist_lore_item, blacklist_lore_item;

    void loadConfigs(boolean reload) { //load config values
        FileConfiguration config = this.getConfig();
        this.saveDefaultConfig(); //create the config if it does not exist
        if (reload) {
            this.reloadConfig(); //if being issued via command, reload config values
        }

        color_code_prefix = config.getString("color-code-prefix").charAt(0);

        apply_vanilla_repairs = config.getBoolean("enable-vanilla-result");
        use_whitelist_item = config.getBoolean("use-whitelist-item");
        use_whitelist_lore_item = config.getBoolean("use-whitelist-lore-item");
        read_from_displayname = config.getBoolean("read-from-displayname"); //TODO add note that this obviously makes adding lore in full vanilla impossible
        erase_old_lore = config.getBoolean("erase-old-lore");

        whitelist_item = config.getStringList("whitelist-item");
        blacklist_item = config.getStringList("blacklist-item");
        whitelist_lore_item = config.getStringList("whitelist-lore-item");
        blacklist_lore_item = config.getStringList("blacklist-lore-item");
    }

    int getChatColorLevelCost(char c) {
        return this.getConfig().getInt("level-cost-" + c);
    }
}

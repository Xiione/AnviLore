package io.github.xiione.api;


import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.plugin.java.JavaPlugin;

public interface CostSetter {
    void setCost(JavaPlugin plugin, PrepareAnvilEvent e, int i);
}

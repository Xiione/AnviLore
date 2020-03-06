package io.github.xiione.nms.v1_15_R1;

import net.minecraft.server.v1_15_R1.ContainerAnvil;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftInventoryView;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CostSetter implements io.github.xiione.api.CostSetter {

    public void setCost(JavaPlugin plugin, PrepareAnvilEvent e, int i) {
        CraftInventoryView view = (CraftInventoryView) e.getView();
        ContainerAnvil anvil = (ContainerAnvil) view.getHandle();
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            anvil.levelCost.set(i);
        }, 1);

    }
}

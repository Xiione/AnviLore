package io.github.xiione;

import io.github.xiione.api.CostSetter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;



public class AnviLoreListener implements Listener {

    public AnviLoreListener(AnviLore passedPlugin) { this.plugin = passedPlugin; }

    private AnviLore plugin;

    @EventHandler
    public void prepareAnvil(PrepareAnvilEvent e) {
        ItemStack itemA = e.getInventory().getItem(0);
        ItemStack itemB = e.getInventory().getItem(1);
        if (itemA == null || itemB == null) return; //no changes to items can be carried out if either item is not present
        ItemMeta metaA = itemA.getItemMeta();
        ItemMeta metaB = itemB.getItemMeta();

        if(!isValidToModify(itemA, itemB)) return; //exit if no changes are allowed to/can be made to be begin with

        List<String> loreResult;

        if(metaA.getLore() == null || plugin.erase_old_lore) { //start empty if the itemA has no lore, or if the option is configured to clear
            loreResult = new ArrayList<>();
        } else {
            loreResult = metaA.getLore();
        }

        if(plugin.read_from_displayname) { //loreresult is now ready...
            loreResult.add(ChatColor.translateAlternateColorCodes('&', metaB.getDisplayName()));
        } else {
            loreResult.addAll(metaB.getLore());
        }


        ItemStack itemResult;
        ItemMeta metaResult;
        if(e.getResult() == null || !plugin.apply_vanilla_repairs) { //does the repair have vanilla behavior associated with it?
            itemResult = itemA.clone();
            metaResult = metaA;
        } else { //if it does (and is allowed to have it apply), let it be the result item
            itemResult = e.getResult();
            metaResult = e.getResult().getItemMeta(); //TODO richer implementation to handle which vanilla changes do/don't go thru
        }

        metaResult.setLore(loreResult); //use new lore
        itemResult.setItemMeta(metaResult);

        plugin.getServer().getScheduler().runTask(plugin, new ItemProvider(e.getInventory(), 2, itemResult)); //use itemprovider regardless of whether result existed or not
        getCostSetter().setCost(plugin, e, 1);

    }

    private static class ItemProvider implements Runnable {
        private Inventory inv;
        private int slot;
        private ItemStack item;
        public ItemProvider(Inventory inv, int slot, ItemStack item) {
            this.inv = inv;
            this.slot = slot;
            this.item = item;
        }
        @Override
        public void run() {
            this.inv.setItem(this.slot, this.item);
        }
    }

//    private int calculateLevelCost(String lore){
        //TODO calculating costs from name-read or not lore, using config :)
//    }

    private boolean isValidToModify(ItemStack itemA, ItemStack itemB) { //seperate method cuz clutter
        boolean isValidItemA = false;
        boolean isValidItemB = false;

        if(plugin.use_whitelist_item) {
            for(String material : plugin.whitelist_item) { //loop whitelist
                if (itemA.getType().equals(Material.matchMaterial(material))) {
                    isValidItemA = true; //all clear!
                    break; //no need to continue loop
                }
            }
        } else {
            isValidItemA = true; //assume item is valid
            for(String material : plugin.blacklist_item) { //loop blacklist
                if (itemA.getType().equals(Material.matchMaterial(material))) {
                    isValidItemA = false; //item is blacklisted!
                    break; //no need to continue loop
                }
            }
        }

        if(plugin.use_whitelist_lore_item) {
            for(String material : plugin.whitelist_lore_item) {
                if (itemB.getType().equals(Material.matchMaterial(material))) {
                    isValidItemB = true;
                    break;
                }
            }
        } else {
            isValidItemB = true;
            for(String material : plugin.blacklist_lore_item) {
                if (itemB.getType().equals(Material.matchMaterial(material))) {
                    isValidItemB = false;
                    break;
                }
            }
        }

        boolean isModificationMade; //will any lore-related modifications be made?
        ItemMeta metaB = itemB.getItemMeta();

        if(plugin.read_from_displayname) {
            //empty displayname?
            isModificationMade = !metaB.getDisplayName().equals("");
        } else {
            //empty lore
            isModificationMade = metaB.getLore() != null;
        }

        return isValidItemA && isValidItemB && isModificationMade; //teeeechnically could be replaced entirely with "return false"s, but keep this way for the sake of readability
    }

    private CostSetter getCostSetter() {
        try {
            final Class<?> aClass = Class.forName("io.github.xiione.nms." + (plugin.version + ".CostSetter"));
            // Check if we have a NMSHandler class at that location.
            if (CostSetter.class.isAssignableFrom(aClass)) { // Make sure it actually implements NMS
                return (CostSetter) aClass.getConstructor().newInstance(); // Set our handler
            }
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}



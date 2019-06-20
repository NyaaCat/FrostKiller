package cat.nyaa.frostkiller;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class FrostKillerEvent implements Listener {
    Configuration configuration;

    FrostKillerEvent(){
        configuration = FrostKillerPlugin.plugin.configuration;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent ev) {
        ItemStack[] armorContents = ev.getPlayer().getInventory().getArmorContents();
        if (armorContents.length == 0) return;
        for (int i = 0; i < armorContents.length; i++) {
            ItemStack armorContent = armorContents[i];
            if (armorContent != null) {
                if (!armorContent.containsEnchantment(Enchantment.FROST_WALKER)) continue;
                if (configuration.removeItem) {
                    armorContents[i] = new ItemStack(Material.AIR);
                }else if (configuration.removeEnchant){
                    armorContent.removeEnchantment(Enchantment.FROST_WALKER);
                }else {
                    return;
                }
            }
        }
        ev.getPlayer().getInventory().setArmorContents(armorContents);
    }

    @EventHandler
    public void onFrostForm(EntityBlockFormEvent ev){
        if (configuration.enabled){
            ev.setCancelled(true);
        }
    }
}

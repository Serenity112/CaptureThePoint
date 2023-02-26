package minecraftevent.capturethepoint.listeners;

import minecraftevent.capturethepoint.CaptureThePoint;
import minecraftevent.capturethepoint.randomdrops.FallingReward;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.Bukkit.getServer;

public class GameListeners implements Listener {
    @EventHandler
    public void onInventoryOpenEvent(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.CHEST) {
            Block b = e.getClickedBlock();

            if (FallingReward.summonedDrops.containsKey(b.getLocation())) {
                int[] timers = FallingReward.summonedDrops.get(b.getLocation());
                Bukkit.getScheduler().cancelTask(timers[0]);
                Bukkit.getScheduler().cancelTask(timers[1]);
                FallingReward.summonedDrops.remove(b.getLocation());
            }

            Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureThePoint.getInstance(), () -> {
                b.setType(Material.AIR);
            }, 100);

        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        Item item = e.getItemDrop();
        int id = item.getItemStack().getTypeId();

        // Аним пушки
        if(id == 4784 || id == 4770 || id == 4766 || id == 4781 || id == 4762 || id == 4790 || id == 4756 || id == 4758) {
            e.setCancelled(true);
        }

        if (id == 4299 || id == 4222 || id == 4223 || id == 4132 || id < 4000 || (id >= 4169 && id <= 4196) || (id >= 4254 && id <= 4271) || (id >= 4300)) {
            return;
        }

        e.setCancelled(true);
    }
}
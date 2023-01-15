package minecraftevent.capturethepoint.listeners;

import minecraftevent.capturethepoint.CaptureThePoint;
import minecraftevent.capturethepoint.randomdrops.FallingReward;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ChestListener implements Listener {
    @EventHandler
    public void onInventoryOpenEvent(PlayerInteractEvent e) {
        //e.getPlayer().sendMessage("chest opened");
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.CHEST) {
            //e.getPlayer().sendMessage("chest opened");
            Block b = e.getClickedBlock();

            if (FallingReward.summonedDrops.containsKey(b.getLocation())) {
                //e.getPlayer().sendMessage("chest from drop");
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
        //Bukkit.getPlayer("VoidOfExtinction").sendMessage("Id: " + id);
        if (id < 4000 || (id >= 4169 && id <= 4196) || (id >= 4254 && id <= 4271)) {
            return;
        }

        e.setCancelled(true);
    }
}

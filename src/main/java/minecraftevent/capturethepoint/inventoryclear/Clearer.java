package minecraftevent.capturethepoint.inventoryclear;

import minecraftevent.capturethepoint.CaptureThePoint;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Clearer {
    private static int celear_id = 0;

    public static void startClear() {
        celear_id = Bukkit.getScheduler().scheduleSyncRepeatingTask(CaptureThePoint.getInstance(), () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Inventory inv = player.getInventory();
                for(ItemStack item : player.getInventory().getContents()){
                    if (item != null) {
                        int id = item.getTypeId();
                        if ((id >= 4169 && id <= 4196) || (id >= 4254 && id <= 4271)) {
                            if(item.getDurability() >= 21) {
                                item.setAmount(0);
                            }
                        }
                    }
                }
            }
        }, 0, 300);
    }
}
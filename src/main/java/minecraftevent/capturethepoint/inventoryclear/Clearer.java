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

                        switch (id) {
                            case 4183: // M4A1
                            case 4185: // AK109
                            case 4176: // MP5K
                            case 4265: // Aug PARA
                                if(item.getDurability() >= 21) {
                                    item.setAmount(0);
                                }
                                break;
                            case 4181: // узи
                                if(item.getDurability() >= 17) {
                                    item.setAmount(0);
                                }
                                break;
                            case 4254: // Grach
                            case 4267: // Glock
                                if(item.getDurability() >= 13) {
                                    item.setAmount(0);
                                }
                                break;
                            case 4179: // Mosin
                            case 4186: // L11
                                if(item.getDurability() >= 4) {
                                    item.setAmount(0);
                                }
                            case 4180: // Винторез
                                if(item.getDurability() >= 8) {
                                    item.setAmount(0);
                                }
                            case 4260: // Пулик
                                if(item.getDurability() >= 85) {
                                    item.setAmount(0);
                                }
                            case 4175: // Миниган
                                if(item.getDurability() >= 135) {
                                    //item.setAmount(0); // Миниган имба, все патроны нужны
                                }
                            case 4266: // свдм
                                if(item.getDurability() >= 8) {
                                    item.setAmount(0);
                                }
                        }
                    }
                }
            }
        }, 0, 300);
    }
}
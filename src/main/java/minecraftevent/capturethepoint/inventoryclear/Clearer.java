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
                            case 4138: // автоматы и ППшки
                            case 4185:
                            case 4176:
                            case 4265:
                                if(item.getDurability() >= 21) {
                                    item.setAmount(0);
                                }
                                break;
                            case 4163: // узи
                                if(item.getDurability() >= 17) {
                                    item.setAmount(0);
                                }
                                break;
                            case 4236: // пистолеты
                            case 4254:
                                if(item.getDurability() >= 13) {
                                    item.setAmount(0);
                                }
                                break;
                            case 4179: // Снайпы
                            case 4186:
                                if(item.getDurability() >= 4) {
                                    item.setAmount(0);
                                }
                            case 4157: // Винторез
                                if(item.getDurability() >= 7) {
                                    item.setAmount(0);
                                }
                            case 4260: // Пулик
                                if(item.getDurability() >= 85) {
                                    item.setAmount(0);
                                }
                            case 4148: // Миниган
                                if(item.getDurability() >= 135) {
                                    item.setAmount(0);
                                }
                            case 4266: // свдм
                                if(item.getDurability() >= 7) {
                                    item.setAmount(0);
                                }
                        }
                    }
                }
            }
        }, 0, 300);
    }
}
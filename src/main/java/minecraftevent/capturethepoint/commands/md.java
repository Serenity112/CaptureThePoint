package minecraftevent.capturethepoint.commands;

import com.google.common.collect.Lists;
import minecraftevent.capturethepoint.Devices.Device;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static org.bukkit.Bukkit.getPlayer;

public class md extends AbstractCommand {
    public md() {
        super("md");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) throws CommandException {
        if(!sender.isOp())
            return;

        if (args.length > 0) {
            Player player = getPlayer(args[0]);
            Device device = Device.valueOf(args[1]);

            ItemStack item = getDeviceItem(device, 1);
            player.getInventory().addItem(item);
            player.updateInventory();
        }
    }

    public static ItemStack getDeviceItem(Device device, int amount) {
        ItemStack item = null;
        ItemMeta meta = null;

        switch (device) {
            case AirstrikeArea:
                item = new ItemStack(Material.MAGMA_CREAM, amount);
                meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&4&lArea AirStrike"));
                meta.setLore(Lists.newArrayList("Right-click to call Area Airstrike"));
                break;
            case AirstrikeCluster:
                item = new ItemStack(Material.BLAZE_POWDER, amount);
                meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&4&lCluster AirStrike"));
                meta.setLore(Lists.newArrayList("Right-click to call Cluster Airstrike"));
                break;
            case AirstrikeBreakdown:
                item = new ItemStack(Material.FERMENTED_SPIDER_EYE, amount);
                meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&4&lBreakdown AirStrike"));
                meta.setLore(Lists.newArrayList("Right-click to call Breakdown Airstrike"));
                break;
            case Injection: // Быстрый хил + слепота
                item = new ItemStack(Material.SLIME_BALL, amount);
                meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&2&lInjection"));
                meta.setLore(Lists.newArrayList("Right-click to take Injection"));
                break;
            case Adrenaline: // Скорость + прыгучесть
                item = new ItemStack(Material.GHAST_TEAR, amount);
                meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lAdrenaline"));
                meta.setLore(Lists.newArrayList("Right-click to take Adrenaline"));
                break;
            case BulletproofVest: // Временная броня
                item = new ItemStack(Material.PAPER, amount);
                meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7&lBulletproofVest"));
                meta.setLore(Lists.newArrayList("Right-click to get temporary defence"));
                break;
            case Scanner:
                item = new ItemStack(Material.SULPHUR, amount);
                meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&9&lScanner"));
                meta.setLore(Lists.newArrayList("Right-click to scan enemies"));
                break;
            case Stimulator:
                item = new ItemStack(Material.SUGAR, amount);
                meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lStimulator"));
                meta.setLore(Lists.newArrayList("Right-click to get temporary regeneration"));
                break;
            case AllySummon:
                item = new ItemStack(Material.BOOK, amount);
                meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lSummon ally"));
                meta.setLore(Lists.newArrayList("Right-click to summon allies on your points"));
                break;
        }

        item.setItemMeta(meta);
        return item;
    }

}
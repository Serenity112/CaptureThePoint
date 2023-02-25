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
                meta.setLore(Lists.newArrayList(ChatColor.DARK_PURPLE + "Right-click to call Area Airstrike on the location you look at",
                        ChatColor.WHITE + "Causes 13 consecutive blows on a small area",
                        ChatColor.WHITE + "Blows deal huge damage and break blocks"));
                break;
            case AirstrikeCluster:
                item = new ItemStack(Material.BLAZE_POWDER, amount);
                meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&4&lCluster AirStrike"));
                meta.setLore(Lists.newArrayList(ChatColor.DARK_PURPLE + "Right-click to call Cluster Airstrike on the location you look at",
                        ChatColor.WHITE + "Causes 5 cluster blows on a decent area",
                        ChatColor.WHITE + "Each cluster blow consists of 25 small explosions ",
                        ChatColor.WHITE + "Explosions deal medium damage and don't break blocks"));
                break;
            case AirstrikeBreakdown:
                item = new ItemStack(Material.FERMENTED_SPIDER_EYE, amount);
                meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&4&lBreakdown AirStrike"));
                meta.setLore(Lists.newArrayList(ChatColor.DARK_PURPLE + "Right-click to call Breakdown Airstrike on the location you look at",
                        ChatColor.WHITE + "Causes 2 breakdown blows on a small area",
                        ChatColor.WHITE + "Blows deal massive damage, break blocks and penetrate through surfaces"));
                break;
            case Injection: // Быстрый хил + слепота
                item = new ItemStack(Material.SLIME_BALL, amount);
                meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&2&lInjection"));
                meta.setLore(Lists.newArrayList(ChatColor.DARK_PURPLE + "Right-click to take Injection",
                        ChatColor.WHITE + "• Heals 5 full hearts (instant)",
                        ChatColor.WHITE + "• Blindness II (4 seconds)",
                        ChatColor.WHITE + "• Slowness II (2 seconds)"));
                break;
            case Adrenaline: // Скорость + прыгучесть
                item = new ItemStack(Material.GHAST_TEAR, amount);
                meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lAdrenaline"));
                meta.setLore(Lists.newArrayList(ChatColor.DARK_PURPLE + "Right-click to take Adrenaline",
                        ChatColor.WHITE + "• Speed II (30 seconds)",
                        ChatColor.WHITE + "• Jump Boost II (30 seconds)"));
                break;
            case KevlarPlate: // Временная броня
                item = new ItemStack(Material.PAPER, amount);
                meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7&lKevlar Plate"));
                meta.setLore(Lists.newArrayList(ChatColor.DARK_PURPLE + "Right-click to get temporary defence",
                        ChatColor.WHITE + "• Resistance II (10 seconds)"));
                break;
            case Scanner:
                item = new ItemStack(Material.SULPHUR, amount);
                meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&9&lScanner"));
                meta.setLore(Lists.newArrayList(ChatColor.DARK_PURPLE + "Right-click to scan enemies"));
                break;
            case Stimulator:
                item = new ItemStack(Material.SUGAR, amount);
                meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lStimulator"));
                meta.setLore(Lists.newArrayList(ChatColor.DARK_PURPLE + "Right-click to get temporary regeneration",
                        ChatColor.WHITE + "• Regeneration II (20 seconds)"));
                break;
        }

        item.setItemMeta(meta);
        return item;
    }

}
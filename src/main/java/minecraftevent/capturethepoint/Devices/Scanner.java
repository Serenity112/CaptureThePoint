package minecraftevent.capturethepoint.Devices;

import minecraftevent.capturethepoint.CaptureThePoint;
import net.minecraft.server.v1_12_R1.DataWatcher;
import net.minecraft.server.v1_12_R1.DataWatcherObject;
import net.minecraft.server.v1_12_R1.DataWatcherRegistry;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityMetadata;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Objects;

import static org.bukkit.Bukkit.getScoreboardManager;

public class Scanner {
    public static void ScanEnemy(Player observer, double radius) {
        Bukkit.getScheduler().runTaskAsynchronously(CaptureThePoint.getInstance(), () -> {
            Scoreboard board = getScoreboardManager().getMainScoreboard();

            Team observerTeam = board.getEntryTeam(observer.getName());

            if (observerTeam != null) {
                Team oppositeTeam = Objects.equals(observerTeam.getName(), "Red") ? board.getTeam("Blue") : board.getTeam("Red");

                if (oppositeTeam != null) {
                    // Подготовка
                    observer.sendMessage(ChatColor.BLUE + "Scanning start...");

                    for (Entity nearby : observer.getWorld().getNearbyEntities(observer.getLocation(), radius, radius, radius)) {
                        if (nearby instanceof Player) {
                            if (Objects.equals(board.getEntryTeam(nearby.getName()).getName(), oppositeTeam.getName())) {
                                nearby.sendMessage(ChatColor.DARK_RED + "You will be scanned in 2 seconds!");
                            }
                        }
                    }

                    // Задержка
                    sleep(2000);

                    // Скан
                    boolean found = false;

                    for (Entity nearby : observer.getWorld().getNearbyEntities(observer.getLocation(), radius, radius, radius)) {
                        if (nearby instanceof Player) {
                            if (Objects.equals(board.getEntryTeam(nearby.getName()).getName(), oppositeTeam.getName())) {
                                found = true;
                                nearby.sendMessage(ChatColor.DARK_RED + "You got scanned!");

                                final int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(CaptureThePoint.getInstance(), () -> {
                                    DataWatcher dw = ((CraftPlayer) nearby).getHandle().getDataWatcher();
                                    dw.set(new DataWatcherObject<Byte>(0, DataWatcherRegistry.a), (byte) 0x40);
                                    PacketPlayOutEntityMetadata data = new PacketPlayOutEntityMetadata(nearby.getEntityId(), dw, false);
                                    ((CraftPlayer) observer).getHandle().playerConnection.sendPacket(data);
                                }, 0, 1);

                                Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureThePoint.getInstance(), () -> {
                                    Bukkit.getScheduler().cancelTask(id);
                                }, 80);
                            }
                        }
                    }

                    if (found)
                        observer.sendMessage(ChatColor.DARK_GREEN + "Targets highlighted!");
                    else
                        observer.sendMessage(ChatColor.DARK_RED + "Targets not found");
                }
            }
        });
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

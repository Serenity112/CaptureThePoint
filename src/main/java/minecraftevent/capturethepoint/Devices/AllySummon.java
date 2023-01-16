package minecraftevent.capturethepoint.Devices;

import minecraftevent.capturethepoint.CaptureThePoint;
import minecraftevent.capturethepoint.capture.Point;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandException;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.Locale;
import java.util.Objects;

import static java.lang.Math.*;

public class AllySummon {
    private static float spawnRadius = 4f;
    private static int mobCount = 4;
    private static int spawnDelayTicks = 100;

    public static void spawnAllyOnPoints(Player player) {
        Team summoningTeam = CaptureThePoint.board.getEntryTeam(player.getName());
        Team oppositeTeam = Objects.equals(summoningTeam.getName(), "Red") ? CaptureThePoint.blueTeam : CaptureThePoint.redTeam;

        // Подготовка
        boolean havePoints = false;
        for (Point point : CaptureThePoint.PointsArray) {
            if (point.state.toString().toLowerCase(Locale.ROOT).equals(summoningTeam.getName().toLowerCase(Locale.ROOT))) {
                havePoints = true;
            }
        }

        if (!havePoints) {
            player.sendMessage(ChatColor.DARK_RED + "You don't control any point!");
            throw new CommandException();
        }

        // Предупреждение

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            String teamname = CaptureThePoint.board.getEntryTeam(p.getName()).getName();

            if(Objects.equals(teamname, summoningTeam.getName())) {
                p.sendMessage(ChatColor.DARK_GREEN + "You will get recruitment in " + spawnDelayTicks / 20 + " seconds!");
            }

            if(Objects.equals(teamname, oppositeTeam.getName())) {
                p.sendMessage(ChatColor.DARK_RED + "Enemy will get recruitment in " + spawnDelayTicks / 20 + " seconds!");
            }
        }

        // Саммон
        Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureThePoint.getInstance(), () -> {
            for (Point point : CaptureThePoint.PointsArray) {
                if (point.state.toString().toLowerCase(Locale.ROOT).equals(summoningTeam.getName().toLowerCase(Locale.ROOT))) {
                    Location center = point.location;
                    for (int i = 0; i < mobCount; i++) {
                        double r = spawnRadius * sqrt(random());
                        double theta = random() * 2 * PI;
                        double x = center.getX() + r * cos(theta);
                        double z = center.getZ() + r * sin(theta);

                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "summon techguns:commando " + x + " " + center.getY() + " " + z);

                        for (Entity nearby : CaptureThePoint.world.getNearbyEntities(center, spawnRadius + 1, spawnRadius + 1, spawnRadius + 1)) {
                            if (nearby instanceof Monster) {
                                summoningTeam.addEntry(nearby.getUniqueId().toString());
                            }
                        }
                    }
                }
            }
        }, spawnDelayTicks);
    }
}

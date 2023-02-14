package minecraftevent.capturethepoint.eventcontrols;

import minecraftevent.capturethepoint.CaptureThePoint;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import static java.lang.Math.abs;

public class PreEvent {
    private static World world;

    private static Location blue_spawn;
    private static Location red_spawn;

    private static Team red_team;
    private static Team blue_team;
    private static Scoreboard board;


    private static Location red_meetup;
    private static Location blue_meetup;
    private static float meetup_radius = 6f;

    public PreEvent(World world) {
        PreEvent.world = world;

        board = CaptureThePoint.board;
        red_team = CaptureThePoint.redTeam;
        blue_team = CaptureThePoint.blueTeam;

        blue_spawn = new Location(world, 25, 65, -620);
        red_spawn = new Location(world, -297, 65, -643);

        red_meetup = new Location(world, -153, 165, -645);
        blue_meetup = new Location(world, -140 , 165, -654);
    }

    public static void teamStart() {
        Bukkit.getScheduler().runTaskAsynchronously(CaptureThePoint.getInstance(), () -> {

            for (int timer = 10; timer > 0; timer--) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendTitle(ChatColor.GREEN + Integer.toString(timer), "", 5, 20, 1);
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            int red_count = 0;
            for (Entity nearby : world.getNearbyEntities(red_meetup, meetup_radius, meetup_radius, meetup_radius)) {
                if (nearby instanceof Player) {
                    red_count++;
                }
            }

            int blue_count = 0;
            for (Entity nearby : world.getNearbyEntities(blue_meetup, meetup_radius, meetup_radius, meetup_radius)) {
                if (nearby instanceof Player) {
                    blue_count++;
                }
            }



            if (abs(red_count - blue_count) > 1 || red_count == 0 || blue_count == 0) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendTitle(ChatColor.RED + "Unequal players number!", "", 10, 35, 20);
                }
                return;
            }


            for (Entity nearby : world.getNearbyEntities(red_meetup, meetup_radius, meetup_radius, meetup_radius)) {
                if (nearby instanceof Player) {
                    red_team.addEntry(nearby.getName());

                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "noppes faction " + nearby.getName() + " 4 set 2000");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "noppes faction " + nearby.getName() + " 3 set -2000");

                    nearby.sendMessage(ChatColor.WHITE + "You joined " + ChatColor.RED + "Red " + ChatColor.WHITE + "team");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawnpoint " + nearby.getName() + " " + red_spawn.getX() + " " + red_spawn.getY() + " " + red_spawn.getZ());
                    nearby.teleport(red_spawn);
                }
            }

            for (Entity nearby : world.getNearbyEntities(blue_meetup, meetup_radius, meetup_radius, meetup_radius)) {
                if (nearby instanceof Player) {
                    blue_team.addEntry(nearby.getName());

                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "noppes faction " + nearby.getName() + " 4 set -2000");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "noppes faction " + nearby.getName() + " 3 set 2000");

                    nearby.sendMessage(ChatColor.WHITE + "You joined " + ChatColor.BLUE + "Blue " + ChatColor.WHITE + "team");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawnpoint " + nearby.getName() + " " + blue_spawn.getX() + " " + blue_spawn.getY() + " " + blue_spawn.getZ());
                    nearby.teleport(blue_spawn);
                }
            }
        });

    }


    public static void teleportTeams() {
        for (String player : red_team.getEntries()) {
            Player p = Bukkit.getPlayer(player);
            if (p != null && p.isOnline()) {
                Bukkit.getPlayer(player).teleport(red_spawn);
            }
        }

        for (String player : blue_team.getEntries()) {
            Player p = Bukkit.getPlayer(player);
            if (p != null && p.isOnline()) {
                Bukkit.getPlayer(player).teleport(blue_spawn);
            }
        }
    }


}

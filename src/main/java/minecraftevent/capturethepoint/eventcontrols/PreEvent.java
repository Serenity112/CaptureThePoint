package minecraftevent.capturethepoint.eventcontrols;

import minecraftevent.capturethepoint.CaptureThePoint;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.event.EventHandler;

import java.util.ArrayList;

public class PreEvent implements Listener {
    private static Location blue_spawn;
    private static Location red_spawn;

    private static Scoreboard board;

    private static Location red_meetup;
    private static Location blue_meetup;
    private static Location spawn_location;

    private static boolean event_active = false;
    public static boolean joining_mode = false;
    private static boolean timer = false;

    public static final ArrayList<String> red_players = new ArrayList<>();
    public static final ArrayList<String> blue_players = new ArrayList<>();

    private static Team red_team;
    private static Team blue_team;

    public PreEvent(World world) {
        board = CaptureThePoint.board;

        spawn_location = new Location(world, -147, 165, -654);

        red_meetup = new Location(world, -152, 165, -654);
        blue_meetup = new Location(world, -140, 165, -654);

        red_spawn = new Location(world, -297, 65, -643);
        blue_spawn = new Location(world, 25, 65, -620);

        red_team = CaptureThePoint.redTeam;
        blue_team = CaptureThePoint.blueTeam;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        String playername = player.getName();

        if (joining_mode) {
            player.teleport(spawn_location);
        }

        Team team = board.getEntryTeam(player.getName());

        if (team != null) {
            player.sendMessage("You were removed from " + team.getName() + " team! Reselect.");

            switch (team.getName()) {
                case "Red":
                    red_players.remove(playername);
                    red_team.removeEntry(playername);
                    break;
                case "Blue":
                    blue_players.remove(playername);
                    blue_team.removeEntry(playername);
                    break;
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
    }

    private static void processPlayer(Player player, String team) {
        switch (team) {
            case "Red":
                red_team.addEntry(player.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawnpoint " + player.getName() + " " + blue_spawn.getX() + " " + blue_spawn.getY() + " " + blue_spawn.getZ());
                player.teleport(red_spawn);
                break;
            case "Blue":
                blue_team.addEntry(player.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawnpoint " + player.getName() + " " + blue_spawn.getX() + " " + blue_spawn.getY() + " " + blue_spawn.getZ());
                player.teleport(blue_spawn);
                break;
        }
    }

    public static void addPlayer(String playername, String team) {
        Player player = Bukkit.getPlayer(playername);

        if (!joining_mode) {
            player.sendMessage(ChatColor.RED + "Game was not started yet!");
            return;
        }

        if (timer) {
            player.sendMessage(ChatColor.RED + "Game is starting, please wait! You will be able to choose a team after the start");
            return;
        }

        if (event_active) { // Для опоздавших
            switch (team) {
                case "Red":
                    if (red_players.size() - blue_players.size() >= 1) {
                        player.sendMessage(ChatColor.RED + "Team is full!");
                        return;
                    }

                    player.sendMessage(ChatColor.WHITE + "You joined " + ChatColor.RED + "Red " + ChatColor.WHITE + "team");
                    blue_players.remove(playername);
                    red_players.add(playername);
                    processPlayer(player, "Red");
                    break;
                case "Blue":
                    if (blue_players.size() - red_players.size() >= 1) {
                        player.sendMessage(ChatColor.RED + "Team is full!");
                        return;
                    }
                    player.sendMessage(ChatColor.WHITE + "You joined " + ChatColor.BLUE + "Blue " + ChatColor.WHITE + "team");
                    red_players.remove(playername);
                    blue_players.add(playername);
                    processPlayer(player, "Blue");
                    break;
            }
        } else { // Мейн распределение
            switch (team) {
                case "Red":
                    player.sendMessage(ChatColor.WHITE + "You joined " + ChatColor.RED + "Red " + ChatColor.WHITE + "team");
                    blue_players.remove(playername);
                    red_players.add(playername);
                    player.teleport(red_meetup);
                    break;
                case "Blue":
                    player.sendMessage(ChatColor.WHITE + "You joined " + ChatColor.BLUE + "Blue " + ChatColor.WHITE + "team");
                    red_players.remove(playername);
                    blue_players.add(playername);
                    player.teleport(blue_meetup);
                    break;
            }
        }
    }

    public static void delPlayer(String playername, String team) {
        Player player = Bukkit.getPlayer(playername);

        if (!joining_mode) {
            player.sendMessage(ChatColor.RED + "Game was not started yet!");
            return;
        }

        if (timer) {
            player.sendMessage(ChatColor.RED + "Game is starting, you can't leave a team!");
            return;
        }

        switch (team) {
            case "Red":
                player.sendMessage(ChatColor.WHITE + "You left " + ChatColor.RED + "Red " + ChatColor.WHITE + "team");
                red_players.remove(playername);
                player.teleport(spawn_location);
                break;
            case "Blue":
                player.sendMessage(ChatColor.WHITE + "You left " + ChatColor.BLUE + "Blue " + ChatColor.WHITE + "team");
                blue_players.remove(playername);
                player.teleport(spawn_location);
                break;
        }
    }


    // Запуск отсчёта до игры
    public static void teamStart() {
        Bukkit.getScheduler().runTaskAsynchronously(CaptureThePoint.getInstance(), () -> {
            if (Math.abs(red_players.size() - blue_players.size()) > 1) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    String subtitle = ChatColor.RED + " " + red_players.size() + ChatColor.BLUE + " " + blue_players.size();
                    player.sendTitle(ChatColor.DARK_RED + "Unequal number of players!", subtitle, 5, 60, 5);
                }
                return;
            }

            timer = true;

            for (int i = 10; i > 0; i--) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendTitle(ChatColor.GREEN + Integer.toString(i), "", 5, 20, 5);

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            for (String playername : red_players) {
                Player player = Bukkit.getPlayer(playername);
                processPlayer(player, "Red");
            }

            for (String playername : blue_players) {
                Player player = Bukkit.getPlayer(playername);
                processPlayer(player, "Blue");
            }

            timer = false;
            event_active = true;
        });
    }

    public static void teamstats(String playername) {
        Player player = Bukkit.getPlayer(playername);
        player.sendMessage(ChatColor.RED + " " + red_players.size() + ChatColor.BLUE + " " + blue_players.size());
    }
}
package minecraftevent.capturethepoint.capture;

import minecraftevent.capturethepoint.CaptureThePoint;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class PreEvent {
    private static World world;

    private static Location blue_spawn;
    private static Location red_spawn;

    private static Team red_team;
    private static Team blue_team;
    private static Scoreboard board;

    public PreEvent(World world) {
        PreEvent.world = world;

        board = CaptureThePoint.board;
        red_team = CaptureThePoint.redTeam;
        blue_team = CaptureThePoint.blueTeam;

        blue_spawn = new Location(PreEvent.world, 31, 65, -601);
        red_spawn = new Location(PreEvent.world, -308, 65, -631);
    }

    public static void teleportTeams() {
        for (String player : red_team.getEntries()) {
            Player p = Bukkit.getPlayer(player);
            if(p != null && p.isOnline()) {
                Bukkit.getPlayer(player).teleport(red_spawn);
            }
        }

        for (String player : blue_team.getEntries()) {
            Player p = Bukkit.getPlayer(player);
            if(p != null && p.isOnline()) {
                Bukkit.getPlayer(player).teleport(blue_spawn);
            }
        }
    }
}

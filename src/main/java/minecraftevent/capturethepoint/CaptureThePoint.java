package minecraftevent.capturethepoint;

import minecraftevent.capturethepoint.capture.Point;
import minecraftevent.capturethepoint.commands.killstreak;
import minecraftevent.capturethepoint.eventcontrols.PreEvent;
import minecraftevent.capturethepoint.capture.ProcessCapture;
import minecraftevent.capturethepoint.commands.ctp;
import minecraftevent.capturethepoint.commands.md;
import minecraftevent.capturethepoint.commands.team;
import minecraftevent.capturethepoint.inventoryclear.Clearer;
import minecraftevent.capturethepoint.listeners.GameListeners;
import minecraftevent.capturethepoint.listeners.ItemTrigger;
import minecraftevent.capturethepoint.KillStreak.PlayerKillListener;
import minecraftevent.capturethepoint.randomdrops.FallingReward;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;


public final class CaptureThePoint extends JavaPlugin {
    public enum State {
        RED,
        BLUE,
        NEU
    }

    private static CaptureThePoint instance;
    public static CaptureThePoint getInstance() {
        return instance;
    }
    public static ArrayList<Point> PointsArray = new ArrayList<>();

    public static Scoreboard board;
    public static Team redTeam;
    public static Team blueTeam;
    public static World world;

    @Override
    public void onEnable() {
        instance = this;

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        board = manager.getMainScoreboard();
        redTeam = board.getTeam("Red");
        blueTeam = board.getTeam("Blue");

        world = getServer().getWorlds().get(0);
        initPoints(world);

        new team();
        new ctp();
        new md();
        new killstreak();

        new FallingReward();
        new ProcessCapture(world);

        getServer().getPluginManager().registerEvents(new PlayerKillListener(), this);
        getServer().getPluginManager().registerEvents(new GameListeners(), this);
        getServer().getPluginManager().registerEvents(new ItemTrigger(), this);
        getServer().getPluginManager().registerEvents(new PreEvent(world), this);

        Clearer.startClear();
    }

    @Override
    public void onDisable() {

    }

    private void initPoints(World world) {
        PointsArray.add(new Point(
                "Middle",
                new Location(world, -141, 65, -616),
                State.NEU
        ));

        PointsArray.add(new Point(
                "Statue",
                new Location(world, -136, 65, -548),
                State.NEU
        ));

        PointsArray.add(new Point(
                "Electro",
                new Location(world, -138, 65, -684),
                State.NEU
        ));

        PointsArray.add(new Point(
                "Archway",
                new Location(world, -204, 65, -616),
                State.NEU
        ));

        PointsArray.add(new Point(
                "Shop",
                new Location(world, -81, 65, -613),
                State.NEU
        ));

        PointsArray.add(new Point(
                "Parking",
                new Location(world, -138, 52, -677),
                State.NEU
        ));
    }
}
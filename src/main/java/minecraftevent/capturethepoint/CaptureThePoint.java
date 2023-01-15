package minecraftevent.capturethepoint;

import minecraftevent.capturethepoint.capture.Point;
import minecraftevent.capturethepoint.capture.ProcessCapture;
import minecraftevent.capturethepoint.commands.ctp;
import minecraftevent.capturethepoint.commands.md;
import minecraftevent.capturethepoint.commands.team;
import minecraftevent.capturethepoint.listeners.ChestListener;
import minecraftevent.capturethepoint.randomdrops.FallingReward;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;


public final class CaptureThePoint extends JavaPlugin {
    public enum State {
        RED,
        REDTRANSITION,
        BLUE,
        BLUETRANSITION,
        NEU
    }

    private static CaptureThePoint instance;

    public static CaptureThePoint getInstance() {
        return instance;
    }


    public ArrayList<Point> PointsArray = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;

        World world = getServer().getWorlds().get(0);
        initPoints(world);

        new FallingReward();
        new ProcessCapture(world);

        getServer().getPluginManager().registerEvents(new ChestListener(), this);

        ProcessCapture.startCapturing();

        new team();
        new ctp();
        new md();
    }

    @Override
    public void onDisable() {

    }

    private void initPoints(World world) {
        PointsArray.add(new Point(
                "Middle",
                new Location(world, -136, 65, -614),
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
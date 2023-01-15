package minecraftevent.capturethepoint.capture;

import org.bukkit.Location;
import minecraftevent.capturethepoint.CaptureThePoint.State;


public class Point {
    public String displayName;
    public Location location;
    public State state;
    public int timer;
    public boolean playerFoundLastIteration;
    public int numberInScoreboard;

    public Point(String displayName, Location location, State state) {
        this.displayName = displayName;
        this.location = location;
        this.state = state;
        timer = 0;
        playerFoundLastIteration = false;
        numberInScoreboard = 0;
    }
}
package minecraftevent.capturethepoint.capture;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import minecraftevent.capturethepoint.CaptureThePoint;
import minecraftevent.capturethepoint.randomdrops.Rewards;
import minecraftevent.capturethepoint.randomdrops.FallingReward;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.HashMap;
import java.util.Random;

import static java.lang.Math.*;
import static minecraftevent.capturethepoint.CaptureThePoint.State.*;

public class ProcessCapture {
    private static int capturingId = 0;

    private static World world;
    private static final double radius = 2.5;

    // Scoreboard
    private static Scoreboard board = CaptureThePoint.board;
    private static Objective pointObjective;
    private static Team redTeam = CaptureThePoint.redTeam;
    private static Team blueTeam = CaptureThePoint.blueTeam;
    private static int redscore = 0;
    private static int bluescore = 0;

    // Timer
    private static final int pointsToCapture = 10;
    private static final int scoreUpdatePeriodTics = 10;

    // Prefix
    private static final String redprefix = "&4&l";
    private static final String blueprefix = "&3&l";
    private static final String whiteprefix = "&f&l";
    private static final String goldprefix = "&6&l";
    private static final String grayprefix = "&7&l";

    // Points names
    private static HashMap<String, String> pointsNames = new HashMap<>();

    private static final int maxscore = 10000;

    public ProcessCapture(World world) {
        ProcessCapture.world = world;
        pointObjective = board.getObjective(ChatColor.translateAlternateColorCodes('&', "&6&lPOINTS"));
        resetScoreboard();
    }

    private static void resetScoreboard() {
        for (String entry : board.getEntries()) {
            board.resetScores(entry);
        }

        pointsNames = new HashMap<>();

        redscore = 0;
        bluescore = 0;

        pointObjective.getScore("").setScore(15);
        pointObjective.getScore(ChatColor.translateAlternateColorCodes('&', redprefix + "RED: " + whiteprefix + redscore + goldprefix + "/" + maxscore)).setScore(14);
        pointObjective.getScore(ChatColor.translateAlternateColorCodes('&', blueprefix + "BLUE: " + whiteprefix + bluescore + goldprefix + "/" + maxscore)).setScore(13);
        pointObjective.getScore(" ").setScore(12);

        int num = 11;

        for (Point point : CaptureThePoint.getInstance().PointsArray) {
            point.timer = 0;
            point.state = NEU;
            point.numberInScoreboard = num;

            pointObjective.getScore(ChatColor.translateAlternateColorCodes('&', whiteprefix + point.displayName + ": " + whiteprefix + "NEU")).setScore(num);
            pointsNames.put(point.displayName, whiteprefix + point.displayName + ": " + whiteprefix + "NEU");

            Location loc = point.location.clone().add(0, -1, 0);
            loc.getBlock().setType(Material.STAINED_GLASS);

            num--;
        }
    }

    private static void updateRedScore() {
        board.resetScores(ChatColor.translateAlternateColorCodes('&', redprefix + "RED: " + whiteprefix + redscore + goldprefix + "/" + maxscore));
        redscore++;
        pointObjective.getScore(ChatColor.translateAlternateColorCodes('&', redprefix + "RED: " + whiteprefix + redscore + goldprefix + "/" + maxscore)).setScore(14);
    }

    private static void updateBlueScore() {
        board.resetScores(ChatColor.translateAlternateColorCodes('&', blueprefix + "BLUE: " + whiteprefix + bluescore + goldprefix + "/" + maxscore));
        bluescore++;
        pointObjective.getScore(ChatColor.translateAlternateColorCodes('&', blueprefix + "BLUE: " + whiteprefix + bluescore + goldprefix + "/" + maxscore)).setScore(13);
    }

    private static String getPrefixByState(CaptureThePoint.State state) {
        String prefix = "";

        switch (state) {
            case NEU:
                prefix = whiteprefix + "NEU";
                break;
            case RED:
                prefix = redprefix + "RED";
                break;
            case BLUE:
                prefix = blueprefix + "BLUE";
                break;
        }

        return prefix;
    }

    private static void scoreboardSetPointState(Point point, CaptureThePoint.State newstate) {
        board.resetScores(ChatColor.translateAlternateColorCodes('&', pointsNames.get(point.displayName)));
        String newname = whiteprefix + point.displayName + ": " + getPrefixByState(newstate);
        pointObjective.getScore(ChatColor.translateAlternateColorCodes('&', newname)).setScore(point.numberInScoreboard);
        point.state = newstate;
        pointsNames.put(point.displayName, newname);
    }

    private static void scoreboardTransitionPointState(Point point, CaptureThePoint.State transitionState, int timer) {
        timer = abs(timer);
        board.resetScores(ChatColor.translateAlternateColorCodes('&', pointsNames.get(point.displayName)));
        switch (timer % 2) {
            case 0:
                String newname = whiteprefix + point.displayName + ": " + getPrefixByState(point.state);
                pointObjective.getScore(ChatColor.translateAlternateColorCodes('&', newname)).setScore(point.numberInScoreboard);
                pointsNames.put(point.displayName, newname);
                break;
            case 1:
                String newname1 = "";

                if (point.state == NEU) {
                    switch (transitionState) {
                        case RED:
                            newname1 = whiteprefix + point.displayName + ": " + redprefix + "NEU";
                            break;
                        case BLUE:
                            newname1 = whiteprefix + point.displayName + ": " + blueprefix + "NEU";
                            break;
                    }
                } else {
                    switch (point.state) {
                        case RED:
                            newname1 = whiteprefix + point.displayName + ": " + grayprefix + "RED";
                            break;
                        case BLUE:
                            newname1 = whiteprefix + point.displayName + ": " + grayprefix + "BLUE";
                            break;
                    }
                }

                pointObjective.getScore(ChatColor.translateAlternateColorCodes('&', newname1)).setScore(point.numberInScoreboard);
                pointsNames.put(point.displayName, newname1);
                break;
        }
    }

    public static void startCapturing() {
        FallingReward.startTimerDrop();

        capturingId = Bukkit.getScheduler().scheduleSyncRepeatingTask(CaptureThePoint.getInstance(), () -> {
            for (Point point : CaptureThePoint.getInstance().PointsArray) {

                boolean foundRedPlayer = false;
                boolean foundBluePlayer = false;

                for (Entity nearbyEntity : world.getNearbyEntities(point.location, radius, radius, radius)) {
                    if (nearbyEntity instanceof Player) {
                        point.playerFoundLastIteration = true;

                        Player player = (Player) nearbyEntity;
                        Team team = board.getEntryTeam(player.getName());

                        if (team != null) {
                            switch (team.getName()) {
                                case "Red":
                                    foundRedPlayer = true;

                                    if (abs(point.timer) < pointsToCapture) {
                                        if (point.timer < 0) {
                                            ActionBarAPI.sendActionBar(player, ChatColor.translateAlternateColorCodes('&', whiteprefix + "Capturing: " + blueprefix + " " + abs(point.timer)));
                                        } else {
                                            ActionBarAPI.sendActionBar(player, ChatColor.translateAlternateColorCodes('&', whiteprefix + "Capturing: " + redprefix + " " + abs(point.timer)));
                                        }
                                    }
                                    break;
                                case "Blue":
                                    foundBluePlayer = true;

                                    if (abs(point.timer) < pointsToCapture) {
                                        if (point.timer <= 0) {
                                            ActionBarAPI.sendActionBar(player, ChatColor.translateAlternateColorCodes('&', whiteprefix + "Capturing: " + blueprefix + " " + abs(point.timer)));
                                        } else {
                                            ActionBarAPI.sendActionBar(player, ChatColor.translateAlternateColorCodes('&', whiteprefix + "Capturing: " + redprefix + " " + abs(point.timer)));
                                        }
                                    }
                                    break;
                            }
                        }
                    }
                }

                if (foundRedPlayer && !foundBluePlayer) {
                    if (point.timer == pointsToCapture) {
                        if (point.state != RED) {
                            Rewards.giveRandomRewardOnCapture(world.getNearbyEntities(point.location, radius, radius, radius), redTeam);
                            scoreboardSetPointState(point, RED);

                            if ((new Random().nextInt(100)) < 15) {
                                spawnAlly(point.location, RED);
                            }
                        }


                        for (Entity nearbyEntity : world.getNearbyEntities(point.location, 3 * radius, 3 * radius, 3 * radius)) {
                            if (nearbyEntity instanceof Player) {
                                ActionBarAPI.sendActionBar((Player) nearbyEntity, "");
                            }
                        }
                    }

                    if (point.timer < pointsToCapture) {
                        point.timer++;
                        scoreboardTransitionPointState(point, RED, point.timer);
                    }
                }

                if (foundBluePlayer && !foundRedPlayer) {
                    if (point.timer == -pointsToCapture) {
                        if (point.state != BLUE) {
                            Rewards.giveRandomRewardOnCapture(world.getNearbyEntities(point.location, radius, radius, radius), blueTeam);
                            scoreboardSetPointState(point, BLUE);

                            if ((new Random().nextInt(100)) < 15) {
                                spawnAlly(point.location, BLUE);
                            }
                        }

                        for (Entity nearbyEntity : world.getNearbyEntities(point.location, 3 * radius, 3 * radius, 3 * radius)) {
                            if (nearbyEntity instanceof Player) {
                                ActionBarAPI.sendActionBar((Player) nearbyEntity, "");
                            }
                        }
                    }

                    if (point.timer > -pointsToCapture) {
                        point.timer--;
                        scoreboardTransitionPointState(point, BLUE, point.timer);
                    }
                }

                if (!(foundBluePlayer || foundRedPlayer)) {
                    if (point.playerFoundLastIteration) {
                        switch (point.state) {
                            case RED:
                                if (point.timer != 10) {
                                    scoreboardSetPointState(point, RED);
                                    point.timer = 10;
                                }
                                break;
                            case BLUE:
                                if (point.timer != -10) {
                                    scoreboardSetPointState(point, BLUE);
                                    point.timer = -10;
                                }
                                break;
                            case NEU:
                                if (point.timer != 0) {
                                    scoreboardSetPointState(point, NEU);
                                    point.timer = 0;
                                }
                                break;
                        }

                        for (Entity nearbyEntity : world.getNearbyEntities(point.location, 3 * radius, 3 * radius, 3 * radius)) {
                            if (nearbyEntity instanceof Player) {
                                ActionBarAPI.sendActionBar((Player) nearbyEntity, "");
                            }
                        }
                        point.playerFoundLastIteration = false;
                    }
                }

                switch (point.state) {
                    case RED:
                        updateRedScore();
                        point.location.clone().add(0, -1, 0).getBlock().setType(Material.STAINED_GLASS);
                        point.location.clone().add(0, -1, 0).getBlock().setData((byte) 14);
                        point.location.clone().add(0, -2, 0).getBlock().setType(Material.BEACON);
                        break;
                    case BLUE:
                        updateBlueScore();
                        point.location.clone().add(0, -1, 0).getBlock().setType(Material.STAINED_GLASS);
                        point.location.clone().add(0, -1, 0).getBlock().setData((byte) 11);
                        point.location.clone().add(0, -2, 0).getBlock().setType(Material.BEACON);
                        break;
                }

                if(redscore >= maxscore ) {
                    stopCapturing();

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendTitle("Team " + ChatColor.RED + "RED" + ChatColor.WHITE + " wins!", "", 10, 2400, 20);
                    }
                }

                if(bluescore >= maxscore) {
                    stopCapturing();

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendTitle("Team " + ChatColor.BLUE + "BLUE" + ChatColor.WHITE + " wins!", "", 10, 2400, 20);
                    }
                }
            }


        }, 0, scoreUpdatePeriodTics);
    }

    public static void stopCapturing() {
        FallingReward.stopTimerDrop();

        Bukkit.getScheduler().cancelTask(capturingId);
    }

    public static void resetCapturing() {
        resetScoreboard();
    }

    private static void spawnAlly(Location location, CaptureThePoint.State state) {
        Random rand = new Random();

        int allynum = getRandomNumberInRange(1, 3, rand);

        for (int i = 0; i < allynum; i++) {
            double r = 4 * sqrt(random());
            double theta = random() * 2 * PI;
            double x = location.getX() + r * cos(theta);
            double y = location.getY();
            double z = location.getZ() + r * sin(theta);

            switch (state) {
                case RED:
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "noppes clone spawn Red 2 0:" + x + "," + y + "," + z);
                    break;
                case BLUE:
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "noppes clone spawn Blue 1 0:" + x + "," + y + "," + z);
                    break;
            }
        }
    }

    private static int getRandomNumberInRange(int min, int max, Random rand) {
        return rand.nextInt((max - min) + 1) + min;
    }
}
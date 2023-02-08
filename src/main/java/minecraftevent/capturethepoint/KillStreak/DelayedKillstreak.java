package minecraftevent.capturethepoint.KillStreak;

public class DelayedKillstreak {
    public DelayedKillstreak(int scheduleId, String killerName) {
        this.scheduleId = scheduleId;
        this.killerName = killerName;
    }

    public int scheduleId;
    public String killerName;
}

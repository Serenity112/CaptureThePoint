package minecraftevent.capturethepoint.randomdrops;

import minecraftevent.capturethepoint.Devices.Device;
import minecraftevent.capturethepoint.commands.md;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.Collection;
import java.util.Random;
import java.util.Set;

import static org.bukkit.Bukkit.getPlayer;

public class Rewards {
    public static void giveRandomRewardOnCapture(Collection<Entity> entities, Team team) {
        Set<String> teamMembers = team.getEntries();

        for (String entry : teamMembers) {
            Player player = getPlayer(entry);
            Random rand = new Random();

            if (player != null && player.isOnline()) {
                if (entities.contains((Entity) player)) {
                    // 75% тем, кто был в списке
                    if (rand.nextInt(4) == 0)
                        continue;
                } else {
                    // 33% тем, кто не был в списке, но был в команде.
                    if (rand.nextInt(3) != 0)
                        continue;
                }

                //Bukkit.getPlayer("VoidOfExtinction").sendMessage("Проверка пройдена");

                // 100% всем кто прошёл проверку
                int rewardType = rand.nextInt(10);

                switch (rewardType) {
                    case 0:
                        Device strike = getRandomAirStrike();
                        player.getInventory().addItem(md.getDeviceItem(strike, 1));
                        break;
                    case 1:
                        Device special = getRandomSpecialItem();
                        player.getInventory().addItem(md.getDeviceItem(special, 1));
                        break;
                    default:
                        Device buffitem = getRandomBuffItem();
                        player.getInventory().addItem(md.getDeviceItem(buffitem, 1));
                        break;
                }
            }
        }
    }

    public static void giveRandomRewardOnKill(Player player) {
        Random rand = new Random();
        int rewardType = rand.nextInt(10);

        switch (rewardType) {
            case 0:
                Device strike = getRandomAirStrike();
                player.getInventory().addItem(md.getDeviceItem(strike, 1));
                break;
            case 1:
                Device special = getRandomSpecialItem();
                player.getInventory().addItem(md.getDeviceItem(special, 1));
                break;
            default:
                Device buffitem = getRandomBuffItem();
                player.getInventory().addItem(md.getDeviceItem(buffitem, 1));
                break;
        }
    }

    public static Device getRandomAirStrike() {
        Random rand = new Random();
        int type = rand.nextInt(3);
        switch (type) {
            case 0:
                return Device.AirstrikeArea;
            case 1:
                return Device.AirstrikeCluster;
            case 2:
                return Device.AirstrikeBreakdown;
        }

        return Device.AirstrikeArea;
    }

    public static Device getRandomBuffItem() {
        Random rand = new Random();
        int type = rand.nextInt(4);
        switch (type) {
            case 0:
                return Device.Injection;
            case 1:
                return Device.Adrenaline;
            case 2:
                return Device.KevlarPlate;
            case 3:
                return Device.Stimulator;
        }

        return Device.Injection;
    }

    public static Device getRandomSpecialItem() {
        return Device.Scanner;
    }
}
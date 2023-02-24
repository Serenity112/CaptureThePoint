package minecraftevent.capturethepoint.randomdrops;


import minecraftevent.capturethepoint.CaptureThePoint;
import minecraftevent.capturethepoint.Devices.Device;

import minecraftevent.capturethepoint.capture.Point;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static java.lang.Math.abs;
import static minecraftevent.capturethepoint.commands.md.getDeviceItem;


public class FallingReward {
    private static World world;

    private static Location pos1;
    private static Location pos2;

    private static final ArrayList<Location> shulker_locations = new ArrayList<>();
    public static final HashMap<Location, int[]> summonedDrops = new HashMap<>();

    public static int id = 0;

    public FallingReward() {
        world = Bukkit.getServer().getWorlds().get(0);
        pos1 = new Location(world, -76, 65, -724); // у ботофермы
        pos2 = new Location(world, -210, 65, -511); // на парковке

        shulker_locations.add(new Location(world, -129, 32, -612)); //vss
        shulker_locations.add(new Location(world, -129, 31, -612)); //serbu
        shulker_locations.add(new Location(world, -129, 30, -612)); //pkm

        shulker_locations.add(new Location(world, -129, 32, -611)); //svdm
        shulker_locations.add(new Location(world, -129, 31, -611)); //minigun
        shulker_locations.add(new Location(world, -129, 30, -611)); //taurus

        shulker_locations.add(new Location(world, -129, 30, -610)); //rocket
    }

    public static void startTimerDrop() {
        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(CaptureThePoint.getInstance(), () -> {
            summonDrop();
        }, 2400, 2400);
    }

    public static void stopTimerDrop() {
       if(id != 0) {
           Bukkit.getScheduler().cancelTask(id);
       }
    }

    public static void summonDrop(Location loc) {
        Random rand = new Random();

        Location dropLoc = raycastFromUp(loc, 120);
        Location newloc = dropLoc.add(0, 1, 0);

        double dist = newloc.distance(CaptureThePoint.PointsArray.get(0).location);
        Point closestPoint = CaptureThePoint.PointsArray.get(0);

        for (Point point: CaptureThePoint.PointsArray) {
            double newdist = newloc.distance(point.location);
            if (newdist < dist) {
                closestPoint = point;
                dist = newdist;
            }
        }

        world.spawnParticle(Particle.CLOUD, newloc.getX(), newloc.getY() + 50, newloc.getZ(), 150, 0, 25, 0, 0.01);


        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(ChatColor.BOLD + "" + ChatColor.AQUA + "Random drop from the sky at " + newloc.getX() + " " + newloc.getY() + " " + newloc.getZ() + " at " + closestPoint.displayName);
        }

        Block block = newloc.getBlock();
        block.setType(Material.CHEST);

        // Маркер
        int chestMarkerTimer = Bukkit.getScheduler().scheduleSyncRepeatingTask(CaptureThePoint.getInstance(), () -> {
            //world.spawnParticle(Particle.REDSTONE, block.getX(), block.getY(), block.getZ(), 100, 0.5, 2, 0.5, 0.02);

            world.spawnParticle(Particle.SPELL_WITCH, block.getX(), block.getY(), block.getZ(), 300, 0.5, 8, 0.5, 10);
        }, 0, 40);

        // Удаление сундука через 3 минуты
        int chestDespawnTimer = Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureThePoint.getInstance(), () -> {
            if (block.getType() == Material.CHEST) {
                block.setType(Material.AIR);
            }

            Bukkit.getScheduler().cancelTask(chestMarkerTimer);
            summonedDrops.remove(block.getLocation());
        }, 3600);

        summonedDrops.put(block.getLocation(), new int[]{chestDespawnTimer, chestMarkerTimer});

        putRandomDropInChest(block, rand);
    }


    public static void summonDrop() {
        Random rand = new Random();

        int pos1x = (int) abs(pos1.getX());
        int pos1z = (int) abs(pos1.getZ());

        int pos2x = (int) abs(pos2.getX());
        int pos2z = (int) abs(pos2.getZ());

        double x = -getRandomNumberInRange(pos1x, pos2x, rand);
        double z = -getRandomNumberInRange(pos2z, pos1z, rand);

        Location dropLoc = raycastFromUp(new Location(world, x, 50, z), 120);
        Location newloc = dropLoc.add(0, 1, 0);

        double dist = newloc.distance(CaptureThePoint.PointsArray.get(0).location);
        Point closestPoint = CaptureThePoint.PointsArray.get(0);

        for (Point point: CaptureThePoint.PointsArray) {
            double newdist = newloc.distance(point.location);
            if (newdist < dist) {
                closestPoint = point;
                dist = newdist;
            }
        }

        world.spawnParticle(Particle.CLOUD, x, newloc.getY() + 50, z, 150, 0, 25, 0, 0.01);


        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(ChatColor.BOLD + "" + ChatColor.AQUA + "Random drop from the sky at " + closestPoint.displayName);
        }

        Block block = newloc.getBlock();
        block.setType(Material.CHEST);

        // Маркер
        int chestMarkerTimer = Bukkit.getScheduler().scheduleSyncRepeatingTask(CaptureThePoint.getInstance(), () -> {
            //world.spawnParticle(Particle.REDSTONE, block.getX(), block.getY(), block.getZ(), 100, 0.5, 2, 0.5, 0.02);

            world.spawnParticle(Particle.SPELL_WITCH, block.getX(), block.getY(), block.getZ(), 300, 0.5, 8, 0.5, 10);
        }, 0, 40);

        // Удаление сундука через 3 минуты
        int chestDespawnTimer = Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureThePoint.getInstance(), () -> {
            if (block.getType() == Material.CHEST) {
                block.setType(Material.AIR);
            }

            Bukkit.getScheduler().cancelTask(chestMarkerTimer);
            summonedDrops.remove(block.getLocation());
        }, 3600);

        summonedDrops.put(block.getLocation(), new int[]{chestDespawnTimer, chestMarkerTimer});

        putRandomDropInChest(block, rand);
    }

    static void putRandomDropInChest(Block block, Random rand) {
        int reward = rand.nextInt(8);

        Chest chest = (Chest) block.getState();
        Inventory dropInv = chest.getInventory();
        chest.update(true);

        // Пушки
        if (reward < 7) {
            Inventory inv = ((ShulkerBox) shulker_locations.get(reward).getBlock().getState()).getInventory();

            for (ItemStack i : inv.getContents()) {
                if (i != null) {
                    dropInv.addItem(i);
                }
            }
        }

        // Эирстрайки
        if (reward == 7) {
            for (int i = 0; i < 3; i++) {
                switch (i) {
                    case 1:
                        if (rand.nextInt(100) >= 75)
                            continue;
                        break;
                    case 2:
                        if (rand.nextInt(100) >= 50)
                            continue;
                        break;
                }

                dropInv.addItem(getDeviceItem(Rewards.getRandomAirStrike(), 1));
            }
        }

        addRandomBuffsToChest(dropInv);
    }

    private static void addRandomBuffsToChest(Inventory chestinv) {
        Random rand = new Random();

        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 1:
                    if (rand.nextInt(100) >= 95)
                        return;
                    break;
                case 2:
                    if (rand.nextInt(100) >= 90)
                        return;
                    break;
                case 3:
                    if (rand.nextInt(100) >= 85)
                        return;
                    break;
            }

            int item = rand.nextInt(10);
            Device device = item == 0 ? Rewards.getRandomSpecialItem() : Rewards.getRandomBuffItem();
            chestinv.addItem(getDeviceItem(device, 1));
        }
    }

    private static int getRandomNumberInRange(int min, int max, Random rand) {
        return rand.nextInt((max - min) + 1) + min;
    }

    private static Location raycastFromUp(Location curr, int height) {
        for (int i = height; i > 0; i--) {
            curr.setY(i);
            if (curr.getBlock().getType() != Material.AIR) {
                return curr;
            }
        }
        return curr;
    }
}

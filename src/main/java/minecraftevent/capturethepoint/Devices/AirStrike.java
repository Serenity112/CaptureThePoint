package minecraftevent.capturethepoint.Devices;

import com.github.yeetmanlord.raycast_util.BlockRayCastResult;
import com.github.yeetmanlord.raycast_util.RayCastUtility;
import com.github.yeetmanlord.raycast_util.ResultType;
import minecraftevent.capturethepoint.CaptureThePoint;
import net.minecraft.server.v1_12_R1.BlockBarrier;
import org.bukkit.*;
import org.bukkit.command.CommandException;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.*;
import static java.lang.Math.random;

public class AirStrike {
    private static final int maxHeight = 120;

    public static void performAreaStrike(Player player) throws CommandException {
        World world = player.getWorld();

        float blowCount = 13;
        float blowPower = 4.5f;
        float explosionRadius = 6;
        int blowDelay = 500;
        float maxDistance = 200;
        float alertRadius = 15;

        // Мейн рейкаст
        BlockRayCastResult target = RayCastUtility.rayCastBlocks(player, maxDistance, true, RayCastUtility.Precision.ACCURATE_BLOCK);

        if (target.getType() != ResultType.BLOCK || target.getBlock().getType() == Material.BARRIER) {
            player.sendMessage(ChatColor.DARK_RED + "Target is out of range.");
            throw new CommandException();
        } else {
            player.sendMessage(ChatColor.DARK_GREEN + "Coordinates received, expect airstrikes.");
        }

        List<Location> targetList = new ArrayList<>();
        Location targetLocation = target.getBlock().getLocation();

        // Пресет точек
        targetList.add(targetLocation);

        for (int i = 1; i < blowCount; i++) {
            double r = explosionRadius * sqrt(random());
            double theta = random() * 2 * PI;

            double x = targetLocation.getX() + r * cos(theta);
            double z = targetLocation.getZ() + r * sin(theta);

            Location randomTarget = new Location(world, x, targetLocation.getY(), z);
            targetList.add(randomTarget);
        }


        // Удары
        Bukkit.getScheduler().runTaskAsynchronously(CaptureThePoint.getInstance(), () -> {
            for (Entity nearby : world.getNearbyEntities(targetLocation, alertRadius, alertRadius, alertRadius)) {
                if (nearby instanceof Player) {
                    Player entity = (Player) nearby;
                    entity.sendMessage(ChatColor.DARK_RED + "WATCH OUT FOR AIRSTRIKE!");
                }
            }

            for (int i = 0; i < 1; i++) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                        "playsound techguns:guns.locked master " + player.getName() + " " +
                                player.getLocation().getX() + " " + player.getLocation().getY() + " " + player.getLocation().getZ() + " 1 1");

                sleep(2000);
            }


            for (Location loc : targetList) {
                Location finalLoc = raycastFromUp(loc, maxHeight);

                // Симуляция удара на 0.1 урона чтобы внести в ивент смерти от игрока
                for (Entity nearby : world.getNearbyEntities(finalLoc, explosionRadius, explosionRadius, explosionRadius)) {
                    if (nearby instanceof Player) {
                        Player entity = (Player) nearby;
                        entity.damage(0.1, player);
                    }
                }

                Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureThePoint.getInstance(), () -> {
                    world.spawnParticle(Particle.CLOUD, finalLoc.getX(), finalLoc.getY() + 50, finalLoc.getZ(), 1000, 0, 25, 0, 0.05);
                    world.spawnParticle(Particle.FLAME, finalLoc.getX(), finalLoc.getY() + 1, finalLoc.getZ(), 100, 1, 1, 1, 0.2);
                    world.createExplosion(finalLoc.getX(), finalLoc.getY() + 1, finalLoc.getZ(), blowPower, true, true);
                });

                sleep(blowDelay);
            }
        });
    }

    public static void performClusterStrike(Player player) throws CommandException {
        World world = player.getWorld();

        float mainBlowCount = 5;
        float clusterCount = 25;
        float clusterRadius = 7;
        float clusterDamage = 15f;

        float clusterBlowRange = 7;

        int blowDelay = 650;
        float maxDistance = 200;

        float aimChance = 0.70f;
        float aimSlice = 0.33f;

        // Мейн рейкаст
        BlockRayCastResult target = RayCastUtility.rayCastBlocks(player, maxDistance, true, RayCastUtility.Precision.ACCURATE_BLOCK);

        if (target.getType() != ResultType.BLOCK || target.getBlock().getType() == Material.BARRIER) {
            player.sendMessage(ChatColor.DARK_RED + "Target is out of range.");
            throw new CommandException();
        } else {
            player.sendMessage(ChatColor.DARK_GREEN + "Coordinates received, expect airstrikes.");
        }

        Location targetBlowLocation = target.getBlock().getLocation();

        // Последовательные удары
        Bukkit.getScheduler().runTaskAsynchronously(CaptureThePoint.getInstance(), () -> {
            // Предупреждение
            double alertRadius = 16 + clusterRadius;
            for (Entity nearby : world.getNearbyEntities(targetBlowLocation, alertRadius, alertRadius, alertRadius)) {
                if (nearby instanceof LivingEntity) {
                    LivingEntity entity = (LivingEntity) nearby;
                    entity.sendMessage(ChatColor.DARK_RED + "WATCH OUT FOR CLUSTER AIRSTRIKE!");
                }
            }

            for (int i = 0; i < 1; i++) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                        "playsound techguns:guns.locked master " + player.getName() + " " +
                                player.getLocation().getX() + " " + player.getLocation().getY() + " " + player.getLocation().getZ() + " 1 1");

                sleep(1000);
            }

            // Сами удары
            for (int maincounter = 0; maincounter < mainBlowCount; maincounter++) {
                double radius = maincounter == 0 ? 4 : 16;
                double r = radius * sqrt(random());
                double theta = random() * 2 * PI;
                double x = targetBlowLocation.getX() + r * cos(theta);
                double z = targetBlowLocation.getZ() + r * sin(theta);

                Location newLoc = new Location(world, x, targetBlowLocation.getY(), z);

                // Аим
                if (maincounter > 0 && aimChance > 0f) {
                    ArrayList<Location> playerLocationList = new ArrayList<>();

                    for (Entity nearby : world.getNearbyEntities(targetBlowLocation, radius, radius, radius)) {
                        if (nearby instanceof Player) {
                            Player targetplayer = (Player) nearby;
                            playerLocationList.add(targetplayer.getLocation());
                        }
                    }

                    if (playerLocationList.size() > 0) {
                        Location aimLoc = playerLocationList.get(ThreadLocalRandom.current().nextInt(0, playerLocationList.size()));

                        if (aimChance == 1f) {
                            newLoc = aimLoc;
                        } else {
                            while (true) {
                                if (aimChance > random()) {
                                    Location one = newLoc.clone().multiply(1 - aimSlice);
                                    Location two = aimLoc.clone().multiply(aimSlice);

                                    newLoc = one.add(two);
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                }

                raycastFromUp(newLoc, maxHeight);
                newLoc.setY(newLoc.getY() + 16);

                double centerX = newLoc.getX();
                double centerY = newLoc.getY();
                double centerZ = newLoc.getZ();

                Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureThePoint.getInstance(), () -> {
                    world.spawnParticle(Particle.CLOUD, centerX, centerY + 75, centerZ, 750, 0, 25, 0, 0.05);
                    world.spawnParticle(Particle.SMOKE_NORMAL, centerX, centerY, centerZ, 1500, 2, 2, 2, 0.05);
                    world.spawnParticle(Particle.EXPLOSION_NORMAL, centerX, centerY, centerZ, 750, 1, 1, 1, 0.05);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                            "playsound minecraft:entity.enderdragon_fireball.explode master @a " + centerX + " " + centerY + " " + centerZ + " 5 1");
                });

                sleep(500);

                for (int i = 0; i < clusterCount; i++) {
                    double r2 = clusterRadius * sqrt(random());
                    double theta2 = random() * 2 * PI;

                    double x2 = centerX + r2 * cos(theta2);
                    double z2 = centerZ + r2 * sin(theta2);

                    Location randomTarget = new Location(world, x2, centerY, z2);
                    raycastFromUp(randomTarget, (int) centerY);

                    Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureThePoint.getInstance(), () -> {
                        world.spawnParticle(Particle.EXPLOSION_NORMAL, randomTarget.getX(), randomTarget.getY() + 2, randomTarget.getZ(), 25, 0.3, 0.3, 0.3, 0.01);
                        world.spawnParticle(Particle.FLAME, randomTarget.getX(), randomTarget.getY() + 2, randomTarget.getZ(), 25, 0.3, 0.3, 0.3, 0.01);
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                                "playsound modularwarfare:explosions.distant master @a " + randomTarget.getX() + " " + randomTarget.getY() + 2 + " " + randomTarget.getZ() + " 5 1");

                        for (Entity nearby : world.getNearbyEntities(randomTarget, clusterBlowRange, 4, clusterBlowRange)) {
                            if (nearby instanceof LivingEntity) {
                                LivingEntity entity = (LivingEntity) nearby;
                                double distance = randomTarget.distance(entity.getLocation());
                                entity.damage(clusterDamage * (1 - distance / clusterBlowRange), player);


                                // entity.setLastDamageCause(new EntityDamageEvent(player, ENTITY_ATTACK, 0));
                            }
                        }
                    });

                    int clusterDelay = ThreadLocalRandom.current().nextInt(0, 100);
                    sleep(clusterDelay);
                }

                // Между разными кластерными взрывами
                sleep(blowDelay);
            }
        });
    }

    public static void performBreakdownStrike(Player player) throws CommandException {
        World world = player.getWorld();

        float blowCount = 3f;
        float areaRadius = 5f;
        float explosionPower = 5f;

        int thickness = 4;
        int breakdownCount = 3;
        float breakdownChance = 0.75f;

        int blowDelay = 500;
        float maxDistance = 200;

        float alertRadius = 15;

        // Мейн рейкаст
        BlockRayCastResult target = RayCastUtility.rayCastBlocks(player, maxDistance, true, RayCastUtility.Precision.ACCURATE_BLOCK);

        if (target.getType() != ResultType.BLOCK || target.getBlock().getType() == Material.BARRIER) {
            player.sendMessage(ChatColor.DARK_RED + "Target is out of range.");
            throw new CommandException();
        } else {
            player.sendMessage(ChatColor.DARK_GREEN + "Coordinates received, expect airstrikes.");
        }

        Location targetBlowLocation = target.getBlock().getLocation();
        List<Location> targetList = new ArrayList<>();
        targetList.add(targetBlowLocation);

        // Пресет точек
        for (int maincounter = 1; maincounter < blowCount; maincounter++) {
            double r = areaRadius * sqrt(random());
            double theta = random() * 2 * PI;
            double x = targetBlowLocation.getX() + r * cos(theta);
            double z = targetBlowLocation.getZ() + r * sin(theta);

            Location newLoc = new Location(world, x, targetBlowLocation.getY(), z);
            targetList.add(newLoc);
        }

        // Взрывы
        Bukkit.getScheduler().runTaskAsynchronously(CaptureThePoint.getInstance(), () -> {
            for (Entity nearby : world.getNearbyEntities(targetBlowLocation, alertRadius, alertRadius, alertRadius)) {
                if (nearby instanceof LivingEntity) {
                    LivingEntity entity = (LivingEntity) nearby;
                    entity.sendMessage(ChatColor.DARK_RED + "БОЙСЯ ПРОБИВАЮЩЕГО АВИАУДАРА!");
                }
            }

            for (int i = 0; i < 2; i++) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                        "playsound techguns:guns.locked master " + player.getName() + " " +
                                player.getLocation().getX() + " " + player.getLocation().getY() + " " + player.getLocation().getZ() + " 1 1");

                sleep(1000);
            }


            for (Location loc : targetList) {
                ArrayList<Location> breakdownLocs = raycastFromUpBreakdown(loc, maxHeight, thickness, breakdownCount, breakdownChance);

                float powerDowngrade = 1.0f;
                for (Location breakdown : breakdownLocs) {
                    float finalPowerDowngrade = powerDowngrade;

                    // Симуляция удара на 0 урона чтобы внести в ивент смерти от игрока
                    for (Entity nearby : world.getNearbyEntities(breakdown, areaRadius, areaRadius, areaRadius)) {
                        if (nearby instanceof Player) {
                            Player entity = (Player) nearby;
                            entity.damage(0.1, player);
                        }
                    }

                    Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureThePoint.getInstance(), () -> {
                        world.spawnParticle(Particle.CLOUD, breakdown.getX(), breakdown.getY() + 50, breakdown.getZ(), 1000, 0, 25, 0, 0.05);
                        world.spawnParticle(Particle.FLAME, breakdown.getX(), breakdown.getY() + 1, breakdown.getZ(), 100, 1, 1, 1, 0.2);
                        world.createExplosion(breakdown.getX(), breakdown.getY() + 1, breakdown.getZ(), explosionPower* finalPowerDowngrade, true, true);
                    });

                    powerDowngrade = powerDowngrade * 0.85f;
                    // Задержка пробойных уровней
                    sleep(100);
                }

                sleep(blowDelay);
            }
        });

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

    private static ArrayList<Location> raycastFromUpBreakdown(Location target, int height, int thickness, int breakdownCount, float breakdownChance) {
        int i_breakdown = 0;

        ArrayList<Location> targets = new ArrayList<>();

        target.setY(height);

        while (height > 0) {
            while (target.getBlock().getType() == Material.AIR && height > 0 ) {
                height--;
                target.setY(height);
            }

            // Новый удар в пол
            i_breakdown++;
            targets.add(target.clone());
            if(i_breakdown >= breakdownCount) {
                return targets;
            }


            // Шанс пробоя
            if (breakdownChance < random())
                return targets;


            // Подсчёт толщины
            int i_thickness = 0;
            while (target.getBlock().getType() != Material.AIR && height > 0) {
                i_thickness++;
                if (i_thickness > thickness)
                    return targets;
                height--;
                target.setY(height);
            }
        }

        return targets;
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

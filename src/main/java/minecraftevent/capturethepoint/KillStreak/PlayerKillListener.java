package minecraftevent.capturethepoint.KillStreak;
import minecraftevent.capturethepoint.CaptureThePoint;
import minecraftevent.capturethepoint.commands.killstreak;
import minecraftevent.capturethepoint.randomdrops.Rewards;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Random;

import static org.bukkit.Bukkit.*;


public class PlayerKillListener implements Listener {
    public static HashMap<String, PlayerData> UserData = new HashMap<>();

    public static HashMap<String, DelayedKillstreak> temporaryPlayerKillers = new HashMap<>();


    public PlayerKillListener() {
        for(Player player: getOnlinePlayers()) {
            setPlayerSpeciality(player.getName(), "assault", killstreak.default_level);
        }
    }

    public static void setPlayerSpeciality(String player, String speciality, int level) {
        UserData.put(player, new PlayerData(speciality, level));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        setPlayerSpeciality(player.getName(), "assault", killstreak.default_level);

        Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureThePoint.getInstance(), () -> {
            givePlayerSaturation(e.getPlayer());
        }, 20);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeathEvent(PlayerDeathEvent e) {
        Player killed = e.getEntity();
        Player killer = e.getEntity().getKiller();


        String killedname = killed.getName();
        String killername = killer == null ? null : killer.getName();

        if (temporaryPlayerKillers.containsKey(killedname)) {
            Bukkit.getScheduler().cancelTask(temporaryPlayerKillers.get(killedname).scheduleId);
            temporaryPlayerKillers.remove(killedname);
        }

        if (killer != null) {
            int level = UserData.get(killername).level + 1;
            String speciality = UserData.get(killername).speciality;
            UserData.put(killername, new PlayerData(speciality, level));

            killstreak.giveKillstreak(killer, speciality, level);

            // Награда за убийство с шансом 10%
            if (new Random().nextInt(10) == 0)
                Rewards.giveRandomRewardOnKill(killer);

            killer.playSound(killer.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3.0F, 0.5F);
        }

        UserData.put(killedname, new PlayerData(UserData.get(killedname).speciality, killstreak.default_level));
        killed.getInventory().clear();
        killed.getInventory().setHeldItemSlot(0);
        Bukkit.dispatchCommand(getConsoleSender(), "mw-clear " + killedname);
    }

    @EventHandler()
    public void onEntityDamageEntity(EntityDamageByEntityEvent e) {
        Entity damaged = e.getEntity();
        Entity damager = e.getDamager();
        setPlayerKiller(damaged, damager);
    }

    public static void setPlayerKiller(Entity damaged, Entity damager) {
        if (damaged instanceof Player && damager instanceof Player) {
            if(temporaryPlayerKillers.containsKey(damaged.getName())) {
                int id = temporaryPlayerKillers.get(damaged.getName()).scheduleId;
                Bukkit.getScheduler().cancelTask(id);
            }

            int newId = Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureThePoint.getInstance(), () -> {
                temporaryPlayerKillers.remove(damaged.getName());
            }, 200);

            temporaryPlayerKillers.put(damaged.getName(), new DelayedKillstreak(newId, damager.getName()));
        }
    }

    @EventHandler()
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureThePoint.getInstance(), () -> {
            givePlayerSaturation(e.getPlayer());
        }, 20);
    }

    private void givePlayerSaturation(Player player){
        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 999999, 0, true, false));
    }
}

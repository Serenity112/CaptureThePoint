package minecraftevent.capturethepoint.listeners;

import minecraftevent.capturethepoint.Devices.AirStrike;
import minecraftevent.capturethepoint.Devices.Scanner;
import org.bukkit.command.CommandException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ItemTrigger implements Listener {
    public ItemTrigger() {

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if ((e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR)) {
            if (e.getHand().equals(EquipmentSlot.HAND)) {
                try {
                    ItemStack item = player.getInventory().getItemInMainHand();
                    if (item == null)
                        return;

                    switch (item.getType()) {
                        case MAGMA_CREAM:
                            AirStrike.performAreaStrike(player);
                            item.setAmount(item.getAmount() - 1);
                            break;
                        case BLAZE_POWDER:
                            AirStrike.performClusterStrike(player);
                            item.setAmount(item.getAmount() - 1);
                            break;
                        case FERMENTED_SPIDER_EYE:
                            AirStrike.performBreakdownStrike(player);
                            item.setAmount(item.getAmount() - 1);
                            break;
                        case SLIME_BALL: // Injection
                            double healed = player.getHealth() + 10.0f;
                            if (healed > 20.0f) {
                                player.setHealth(20.0f);
                            } else {
                                player.setHealth(healed);
                            }
                            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, 1, true, false));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 1, true, false));
                            item.setAmount(item.getAmount() - 1);
                            break;
                        case GHAST_TEAR: // Adrenaline
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 600, 1, true, false));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 600, 1, true, false));
                            item.setAmount(item.getAmount() - 1);
                            break;
                        case PAPER: // Bulletproof
                            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 1, true, false));
                            item.setAmount(item.getAmount() - 1);
                            break;
                        case SULPHUR: // Scanner
                            Scanner.ScanEnemy(player, 65);
                            item.setAmount(item.getAmount() - 1);
                            break;
                        case SUGAR: // Stimulator
                            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 400, 1, true, false));
                            item.setAmount(item.getAmount() - 1);
                            break;
                    }
                } catch (CommandException exception) {
                }
            }
        }
    }
}

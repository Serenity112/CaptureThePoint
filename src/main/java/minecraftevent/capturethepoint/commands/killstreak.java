package minecraftevent.capturethepoint.commands;

import minecraftevent.capturethepoint.KillStreak.PlayerKillListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.ShulkerBox;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.Objects;

public class killstreak extends AbstractCommand {

    public killstreak() {
        super("killstreak");

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        board = manager.getMainScoreboard();
    }

    private static Scoreboard board;

    // -1 для вхождение в цикл киллстриков. Он идут: 0-4 (модуль 5)
    public static int default_level = -1;

    @Override
    public void execute(CommandSender sender, String label, String[] args) throws CommandException {
        if(!sender.isOp())
            return;

        if (args.length > 0) {
            String option = args[1];

            switch (option) {
                case "give":
                    Player player1 = Bukkit.getPlayer(args[0]);
                    String kit1 = args[2];
                    int level1 = Integer.parseInt(args[3]);
                    giveKillstreak(player1, kit1, level1);
                    break;
                case "set":
                    Player player2 = Bukkit.getPlayer(args[0]);
                    String kit2 = args[2];
                    int level2 = Objects.equals(args[3], "default") ? default_level : Integer.parseInt(args[3]);
                    PlayerKillListener.setPlayerSpeciality(player2.getName(), kit2, level2);
                    break;
            }
        }
    }

    public static void giveKillstreak(Player player, String kit, int level) {
        if (level < 0)
            return;

        int modlevel = (level % 5) + 1; //0 1 2 3 4 5 6 7 8 9 -> 1 2 3 4 5 1 2 3 4 5
        String streakname = "";

        if(modlevel == 1) {
            streakname = "ks1"; // Универсальный
        }
        else {
            streakname = "ks" + modlevel + "_" + kit;

            // Особенные случаи
            if(Objects.equals(kit, "assault") && modlevel == 5) {
                streakname += "_" + board.getEntryTeam(player.getName()).getName();
            }

            if(Objects.equals(kit, "sniper") && modlevel == 4) {
                if(level > 4) {
                    streakname += "_ammo";
                }
            }

            if(Objects.equals(kit, "bomber") && modlevel == 2) {
                streakname += "_" + board.getEntryTeam(player.getName()).getName();
            }

            if(Objects.equals(kit, "bomber") && modlevel == 3) {
                if(level <= 3) {
                    ksBomberRocket(player);
                } else {
                    ksBomberAmmo(player);
                }
                return;
            }

            if(Objects.equals(kit, "bomber") && modlevel == 5) {
                ksBomberAmmo(player);
                return;
            }
        }


        String command = "kit give " + streakname + " " + player.getName();
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }

    private static void ksBomberRocket(Player player) {
        Location loc = new Location(player.getWorld(), -129, 30, -608);
        Inventory inv = player.getInventory();
        Inventory ks_inv = ((ShulkerBox) loc.getBlock().getState()).getInventory();
        for (ItemStack i : ks_inv.getContents()) {
            if (i != null) {
                inv.addItem(i);
            }
        }
    }

    private static void ksBomberAmmo(Player player) {
        Location loc = new Location(player.getWorld(), -129, 30, -607);
        Inventory inv = player.getInventory();
        Inventory ks_inv = ((ShulkerBox) loc.getBlock().getState()).getInventory();
        for (ItemStack i : ks_inv.getContents()) {
            if (i != null) {
                inv.addItem(i);
            }
        }
    }


}
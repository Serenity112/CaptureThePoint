package minecraftevent.capturethepoint.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.Locale;

public class team extends AbstractCommand {
    Scoreboard board;

    public team() {
        super("team");

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        board = manager.getMainScoreboard();
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) throws CommandException {
        if (args.length > 0) {
            switch (args[0].toLowerCase(Locale.ROOT)) {
                case "red":
                    Team redteam = board.getTeam("Red");
                    redteam.addEntry(sender.getName());
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "noppes faction " + sender.getName() + " 4 set 2000");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "noppes faction " + sender.getName() + " 3 set -2000");
                    break;
                case "blue":
                    Team blueteam = board.getTeam("Blue");
                    blueteam.addEntry(sender.getName());
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "noppes faction " + sender.getName() + " 4 set -2000");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "noppes faction " + sender.getName() + " 3 set 2000");
                    break;
                default:
                    sender.sendMessage(ChatColor.RED + "Такая командая не участвует в ивенте!");
                    break;
            }
        }
    }
}

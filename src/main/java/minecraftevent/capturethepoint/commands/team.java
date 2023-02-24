package minecraftevent.capturethepoint.commands;

import minecraftevent.capturethepoint.eventcontrols.PreEvent;
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
        if(!sender.isOp())
            return;

        if (args.length > 0) {
            switch (args[0].toLowerCase(Locale.ROOT)) {
                case "red":
                    Team redteam = board.getTeam("Red");
                    redteam.addEntry(sender.getName());

                    PreEvent.blue_players.remove(sender.getName());
                    PreEvent.red_players.add(sender.getName());
                    break;
                case "blue":
                    Team blueteam = board.getTeam("Blue");
                    blueteam.addEntry(sender.getName());

                    PreEvent.red_players.remove(sender.getName());
                    PreEvent.blue_players.add(sender.getName());
                    break;
                default:
                    sender.sendMessage(ChatColor.RED + "Такая командая не участвует в ивенте!");
                    break;
            }
        }
    }
}

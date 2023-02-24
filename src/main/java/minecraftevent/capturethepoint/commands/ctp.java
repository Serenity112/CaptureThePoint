package minecraftevent.capturethepoint.commands;

import minecraftevent.capturethepoint.KillStreak.PlayerKillListener;
import minecraftevent.capturethepoint.eventcontrols.PreEvent;
import minecraftevent.capturethepoint.capture.ProcessCapture;
import minecraftevent.capturethepoint.randomdrops.FallingReward;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ctp extends AbstractCommand {
    public ctp() {
        super("ctp");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) throws CommandException {
        if(!sender.isOp())
            return;

        if (args.length > 0) {
            switch (args[0]) {
                case "start":
                    ProcessCapture.stopCapturing();
                    ProcessCapture.startCapturing();
                    break;
                case "stop":
                    ProcessCapture.stopCapturing();
                    break;
                case "reset":
                    ProcessCapture.resetCapturing();
                    break;
                case "teamstart":
                    PreEvent.teamStart();
                    break;
                case "drop":
                    FallingReward.summonDrop(((Player) sender).getLocation());
                    break;
                case "stats":
                    PlayerKillListener.PrintKD();
                    break;
                case "jointeam":
                    PreEvent.addPlayer(args[1], args[2]);
                    break;
                case "leaveteam":
                    PreEvent.delPlayer(args[1], args[2]);
                    break;
                case "teamstats":
                    PreEvent.teamstats(sender.getName());
                    break;
                case "joining":
                    PreEvent.joining_mode = true;
                    break;
            }
        }
    }
}
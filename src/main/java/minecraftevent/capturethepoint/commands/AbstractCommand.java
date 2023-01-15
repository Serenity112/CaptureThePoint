package minecraftevent.capturethepoint.commands;

import minecraftevent.capturethepoint.CaptureThePoint;
import org.bukkit.command.*;

public abstract class AbstractCommand implements CommandExecutor {

    public AbstractCommand(String command) {
        PluginCommand pluginCommand = CaptureThePoint.getInstance().getCommand(command);
        if(pluginCommand != null) {
            pluginCommand.setExecutor(this);
        }
    }

    public abstract void execute(CommandSender sender, String label, String[] args) throws CommandException;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) throws CommandException {
        execute(sender, label, args);
        return true;
    }
}

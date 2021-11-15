package dansplugins.easylinks.commands;

import dansplugins.easylinks.EasyLinks;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import preponderous.ponder.misc.AbstractCommand;

/**
 * @author Daniel Stephenson
 */
public class DefaultCommand extends AbstractCommand {

    @Override
    public boolean execute(CommandSender commandSender) {
        commandSender.sendMessage(ChatColor.AQUA + "Easy Links " + EasyLinks.getInstance().getVersion());
        commandSender.sendMessage(ChatColor.AQUA + "Developed by: DanTheTechMan");
        commandSender.sendMessage(ChatColor.AQUA + "Wiki: https://github.com/dmccoystephenson/Easy-Links/wiki");
        return true;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] strings) {
        return execute(commandSender);
    }
}
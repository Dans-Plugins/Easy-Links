package dansplugins.easylinks.commands;

import dansplugins.easylinks.EasyLinks;
import dansplugins.easylinks.data.PersistentData;
import dansplugins.easylinks.objects.Link;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import preponderous.ponder.misc.AbstractCommand;

import java.util.ArrayList;
import java.util.Collections;

public class DeleteCommand extends AbstractCommand {

    private ArrayList<String> names = new ArrayList<>(Collections.singletonList("delete"));
    private ArrayList<String> permissions = new ArrayList<>(Collections.singletonList("el.delete"));

    @Override
    public ArrayList<String> getNames() {
        return names;
    }

    @Override
    public ArrayList<String> getPermissions() {
        return permissions;
    }

    @Override
    public boolean execute(CommandSender commandSender) {
        commandSender.sendMessage(ChatColor.RED + "Usage: /el delete \"label\"");
        return false;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        ArrayList<String> doubleQuoteArgs = EasyLinks.getInstance().getToolbox().getArgumentParser().getArgumentsInsideDoubleQuotes(args);
        if (doubleQuoteArgs.size() != 1) {
            execute(commandSender); // send usage message
        }

        String label = doubleQuoteArgs.get(0);
        boolean success = PersistentData.getInstance().removeLink(label);

        if (success) {
            commandSender.sendMessage(ChatColor.GREEN + "Link deleted.");
            return true;
        }
        else {
            commandSender.sendMessage(ChatColor.GREEN + "Something went wrong.");
            return false;
        }
    }
}

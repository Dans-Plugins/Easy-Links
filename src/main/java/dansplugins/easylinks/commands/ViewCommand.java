package dansplugins.easylinks.commands;

import dansplugins.easylinks.EasyLinks;
import dansplugins.easylinks.data.PersistentData;
import dansplugins.easylinks.objects.Link;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import preponderous.ponder.misc.AbstractCommand;

import java.util.ArrayList;
import java.util.Collections;

public class ViewCommand extends AbstractCommand {

    private ArrayList<String> names = new ArrayList<>(Collections.singletonList("view"));
    private ArrayList<String> permissions = new ArrayList<>(Collections.singletonList("el.view"));

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
        commandSender.sendMessage(ChatColor.RED + "Usage: /el view \"label\"");
        return false;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        ArrayList<String> doubleQuoteArgs = EasyLinks.getInstance().getToolbox().getArgumentParser().getArgumentsInsideDoubleQuotes(args);
        if (doubleQuoteArgs.size() != 1) {
            execute(commandSender); // send usage message
            return false;
        }

        String label = doubleQuoteArgs.get(0);
        Link link = PersistentData.getInstance().getLink(label);
        if (link == null) {
            commandSender.sendMessage(ChatColor.RED + "That link wasn't found.");
            return false;
        }

        commandSender.sendMessage(ChatColor.AQUA + " === " + link.getLabel() + " === ");
        commandSender.sendMessage(ChatColor.AQUA + link.getUrl());
        return true;
    }
}

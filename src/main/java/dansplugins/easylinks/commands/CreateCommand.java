package dansplugins.easylinks.commands;

import dansplugins.easylinks.EasyLinks;
import dansplugins.easylinks.data.PersistentData;
import dansplugins.easylinks.objects.Link;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import preponderous.ponder.misc.AbstractCommand;

import java.util.ArrayList;
import java.util.Collections;

public class CreateCommand extends AbstractCommand {

    private ArrayList<String> names = new ArrayList<>(Collections.singletonList("create"));
    private ArrayList<String> permissions = new ArrayList<>(Collections.singletonList("el.create"));

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
        commandSender.sendMessage(ChatColor.RED + "Usage: /el create \"label\" \"link\"");
        return false;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        ArrayList<String> doubleQuoteArgs = EasyLinks.getInstance().getToolbox().getArgumentParser().getArgumentsInsideDoubleQuotes(args);
        if (doubleQuoteArgs.size() != 2) {
            execute(commandSender); // send usage message
        }

        String label = doubleQuoteArgs.get(0);
        String link = doubleQuoteArgs.get(1);
        Link newLink = new Link(label, link);
        PersistentData.getInstance().addLink(newLink);
        commandSender.sendMessage(ChatColor.GREEN + "Link created.");
        return true;
    }
}

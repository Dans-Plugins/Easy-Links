package dansplugins.easylinks.commands;

import dansplugins.easylinks.data.PersistentData;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import preponderous.ponder.misc.AbstractCommand;

import java.util.ArrayList;
import java.util.Collections;

public class StatsCommand extends AbstractCommand {

    private ArrayList<String> names = new ArrayList<>(Collections.singletonList("stats"));
    private ArrayList<String> permissions = new ArrayList<>(Collections.singletonList("el.stats"));

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
        commandSender.sendMessage(ChatColor.AQUA + "Number of Links: " + PersistentData.getInstance().getLinks().size());
        commandSender.sendMessage(ChatColor.AQUA + "Total number of uses: " + PersistentData.getInstance().getTotalUses());
        commandSender.sendMessage(ChatColor.AQUA + "Most popular link: " + PersistentData.getInstance().getMostPopularLink());
        return true;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] strings) {
        return execute(commandSender);
    }
}

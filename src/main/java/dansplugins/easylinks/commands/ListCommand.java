package dansplugins.easylinks.commands;

import dansplugins.easylinks.data.PersistentData;
import dansplugins.easylinks.objects.Link;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import preponderous.ponder.misc.AbstractCommand;

import java.util.ArrayList;
import java.util.Collections;

public class ListCommand extends AbstractCommand {

    private ArrayList<String> names = new ArrayList<>(Collections.singletonList("list"));
    private ArrayList<String> permissions = new ArrayList<>(Collections.singletonList("el.list"));

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
        commandSender.sendMessage(ChatColor.AQUA + "=== Links ===");
        for (Link link : PersistentData.getInstance().getLinks()) {
            commandSender.sendMessage(ChatColor.AQUA + link.getLabel());
        }
        return true;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] strings) {
        return execute(commandSender);
    }
}

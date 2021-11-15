package dansplugins.easylinks.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import preponderous.ponder.misc.AbstractCommand;

import java.util.ArrayList;
import java.util.Collections;

public class HelpCommand extends AbstractCommand {

    private ArrayList<String> names = new ArrayList<>(Collections.singletonList("help"));
    private ArrayList<String> permissions = new ArrayList<>(Collections.singletonList("el.help"));

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
        commandSender.sendMessage(ChatColor.AQUA + "/el list");
        commandSender.sendMessage(ChatColor.AQUA + "/el help");
        commandSender.sendMessage(ChatColor.AQUA + "/el view");
        commandSender.sendMessage(ChatColor.AQUA + "/el create");
        commandSender.sendMessage(ChatColor.AQUA + "/el delete");
        commandSender.sendMessage(ChatColor.AQUA + "/el stats");
        return true;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] strings) {
        return execute(commandSender);
    }
}

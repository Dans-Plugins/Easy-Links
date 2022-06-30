package dansplugins.easylinks.commands;

import dansplugins.easylinks.data.PersistentData;
import dansplugins.easylinks.objects.Link;
import preponderous.ponder.minecraft.bukkit.abs.AbstractPluginCommand;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Daniel McCoy Stephenson
 */
public class ListCommand extends AbstractPluginCommand {
    private final PersistentData persistentData;

    public ListCommand(PersistentData persistentData) {
        super(new ArrayList<>(Arrays.asList("list")), new ArrayList<>(Arrays.asList("el.list")));
        this.persistentData = persistentData;
    }

    @Override
    public boolean execute(CommandSender commandSender) {
        if (persistentData.getLinks().size() == 0) {
            commandSender.sendMessage(ChatColor.AQUA + "There are no links set at this time.");
            return true;
        }
        commandSender.sendMessage(ChatColor.AQUA + "=== Links ===");
        for (Link link : persistentData.getLinks()) {
            commandSender.sendMessage(ChatColor.AQUA + link.getLabel());
        }
        return true;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] strings) {
        return execute(commandSender);
    }
}
package dansplugins.easylinks.commands;

import dansplugins.easylinks.data.PersistentData;
import preponderous.ponder.minecraft.bukkit.abs.AbstractPluginCommand;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Daniel McCoy Stephenson
 */
public class StatsCommand extends AbstractPluginCommand {
    private final PersistentData persistentData;

    public StatsCommand(PersistentData persistentData) {
        super(new ArrayList<>(Arrays.asList("stats")), new ArrayList<>(Arrays.asList("el.stats")));
        this.persistentData = persistentData;
    }

    @Override
    public boolean execute(CommandSender commandSender) {
        commandSender.sendMessage(ChatColor.AQUA + "Number of Links: " + persistentData.getLinks().size());
        commandSender.sendMessage(ChatColor.AQUA + "Total number of uses: " + persistentData.getTotalUses());
        commandSender.sendMessage(ChatColor.AQUA + "Most popular link: " + persistentData.getMostPopularLink());
        return true;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] strings) {
        return execute(commandSender);
    }
}
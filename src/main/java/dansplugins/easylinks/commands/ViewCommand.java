package dansplugins.easylinks.commands;

import dansplugins.easylinks.data.PersistentData;
import dansplugins.easylinks.objects.Link;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import preponderous.ponder.minecraft.abs.AbstractPluginCommand;
import preponderous.ponder.misc.ArgumentParser;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Daniel McCoy Stephenson
 */
public class ViewCommand extends AbstractPluginCommand {

    public ViewCommand() {
        super(new ArrayList<>(Arrays.asList("view")), new ArrayList<>(Arrays.asList("el.view")));
    }

    @Override
    public boolean execute(CommandSender commandSender) {
        commandSender.sendMessage(ChatColor.RED + "Usage: /el view \"label\"");
        return false;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        ArgumentParser argumentParser = new ArgumentParser();
        ArrayList<String> doubleQuoteArgs = argumentParser.getArgumentsInsideDoubleQuotes(args);
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
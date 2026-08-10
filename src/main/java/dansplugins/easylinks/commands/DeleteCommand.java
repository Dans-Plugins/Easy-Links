package dansplugins.easylinks.commands;

import dansplugins.easylinks.data.PersistentData;
import dansplugins.easylinks.services.Storage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import preponderous.ponder.minecraft.bukkit.abs.AbstractPluginCommand;
import preponderous.ponder.misc.ArgumentParser;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Daniel McCoy Stephenson
 */
public class DeleteCommand extends AbstractPluginCommand {
    private final PersistentData persistentData;
    private final Storage storage;

    public DeleteCommand(PersistentData persistentData, Storage storage) {
        super(new ArrayList<>(Arrays.asList("delete")), new ArrayList<>(Arrays.asList("el.delete")));
        this.persistentData = persistentData;
        this.storage = storage;
    }

    @Override
    public boolean execute(CommandSender commandSender) {
        commandSender.sendMessage(ChatColor.RED + "Usage: /el delete \"label\"");
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
        boolean success = persistentData.removeLink(label);

        if (success) {
            storage.save();
            commandSender.sendMessage(ChatColor.GREEN + "Link deleted.");
            return true;
        }
        else {
            commandSender.sendMessage(ChatColor.GREEN + "Something went wrong.");
            return false;
        }
    }
}
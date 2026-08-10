package dansplugins.easylinks.commands;

import dansplugins.easylinks.data.PersistentData;
import dansplugins.easylinks.objects.Link;
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
public class CreateCommand extends AbstractPluginCommand {
    private final PersistentData persistentData;
    private final Storage storage;

    public CreateCommand(PersistentData persistentData, Storage storage) {
        super(new ArrayList<>(Arrays.asList("create")), new ArrayList<>(Arrays.asList("el.create")));
        this.persistentData = persistentData;
        this.storage = storage;
    }

    @Override
    public boolean execute(CommandSender commandSender) {
        commandSender.sendMessage(ChatColor.RED + "Usage: /el create \"label\" \"link\"");
        return false;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        ArgumentParser argumentParser = new ArgumentParser();
        ArrayList<String> doubleQuoteArgs = argumentParser.getArgumentsInsideDoubleQuotes(args);
        if (doubleQuoteArgs.size() != 2) {
            execute(commandSender); // send usage message
            return false;
        }

        String label = doubleQuoteArgs.get(0);
        String link = doubleQuoteArgs.get(1);
        Link newLink = new Link(label, link);
        persistentData.addLink(newLink);
        storage.save();
        commandSender.sendMessage(ChatColor.GREEN + "Link created.");
        return true;
    }
}
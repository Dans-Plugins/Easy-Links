package dansplugins.easylinks.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import preponderous.ponder.minecraft.bukkit.abs.AbstractPluginCommand;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Daniel McCoy Stephenson
 */
public class HelpCommand extends AbstractPluginCommand {

    public HelpCommand() {
        super(new ArrayList<>(Arrays.asList("help")), new ArrayList<>(Arrays.asList("el.help")));
    }

    @Override
    public boolean execute(CommandSender commandSender) {
        commandSender.sendMessage(ChatColor.AQUA + "/el help");
        commandSender.sendMessage(ChatColor.AQUA + "/el list");
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
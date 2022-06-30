package dansplugins.easylinks.commands;

import dansplugins.easylinks.EasyLinks;
import preponderous.ponder.minecraft.bukkit.abs.AbstractPluginCommand;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Daniel McCoy Stephenson
 */
public class DefaultCommand extends AbstractPluginCommand {
    private final EasyLinks easyLinks;

    public DefaultCommand(EasyLinks easyLinks) {
        super(new ArrayList<>(Arrays.asList("default")), new ArrayList<>(Arrays.asList("el.default")));
        this.easyLinks = easyLinks;
    }

    @Override
    public boolean execute(CommandSender commandSender) {
        commandSender.sendMessage(ChatColor.AQUA + "=== Easy Links " + easyLinks.getVersion() + " ===");
        commandSender.sendMessage(ChatColor.AQUA + "Developed by: DanTheTechMan");
        commandSender.sendMessage(ChatColor.AQUA + "Wiki: https://github.com/dmccoystephenson/Easy-Links/wiki");
        return true;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] strings) {
        return execute(commandSender);
    }
}
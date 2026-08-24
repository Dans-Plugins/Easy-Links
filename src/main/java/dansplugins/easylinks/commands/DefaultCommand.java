package dansplugins.easylinks.commands;

import preponderous.ponder.minecraft.bukkit.abs.AbstractPluginCommand;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Daniel McCoy Stephenson
 */
public class DefaultCommand extends AbstractPluginCommand {
    private static final String REPOSITORY_URL = "https://github.com/Dans-Plugins/Easy-Links";

    private final String version;

    public DefaultCommand(String version) {
        super(new ArrayList<>(Arrays.asList("default")), new ArrayList<>(Arrays.asList("el.default")));
        this.version = version;
    }

    @Override
    public boolean execute(CommandSender commandSender) {
        commandSender.sendMessage(ChatColor.AQUA + "=== Easy Links " + version + " ===");
        commandSender.sendMessage(ChatColor.AQUA + "Developed by: DanTheTechMan");
        commandSender.sendMessage(ChatColor.AQUA + "Repository: " + REPOSITORY_URL);
        return true;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] strings) {
        return execute(commandSender);
    }
}
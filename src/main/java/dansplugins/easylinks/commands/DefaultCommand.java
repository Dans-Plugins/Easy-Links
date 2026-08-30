package dansplugins.easylinks.commands;

import preponderous.ponder.minecraft.bukkit.abs.AbstractPluginCommand;
import preponderous.ponder.minecraft.bukkit.tools.PermissionChecker;

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

    /**
     * Shows the banner to senders holding this command's permission, and refuses everyone else.
     *
     * This command is dispatched straight from the plugin's zero-argument branch rather than through
     * Ponder's command service, so the permission declared in the constructor is not checked for it
     * the way it is for the registered subcommands. The same {@link PermissionChecker} the command
     * service uses is applied here so that a negated node is honoured and the refusal reads the same.
     *
     * @param commandSender The sender of the command.
     * @param permissionChecker The checker used to test the sender against this command's permissions.
     * @return A boolean indicating whether the command was handled.
     */
    public boolean executeIfPermitted(CommandSender commandSender, PermissionChecker permissionChecker) {
        if (!permissionChecker.checkPermission(commandSender, getPermissions())) {
            return true;
        }
        return execute(commandSender);
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
package dansplugins.easylinks;

import dansplugins.easylinks.commands.*;
import dansplugins.easylinks.data.PersistentData;
import dansplugins.easylinks.objects.Link;
import dansplugins.easylinks.services.ConfigService;
import dansplugins.easylinks.services.StorageService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import preponderous.ponder.minecraft.bukkit.abs.AbstractPluginCommand;
import preponderous.ponder.minecraft.bukkit.abs.PonderBukkitPlugin;
import preponderous.ponder.minecraft.bukkit.services.CommandService;
import preponderous.ponder.minecraft.bukkit.tools.PermissionChecker;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Daniel McCoy Stephenson
 */
public class EasyLinks extends PonderBukkitPlugin {
    private final String pluginVersion = "v" + getDescription().getVersion();

    private final CommandService commandService = new CommandService(getPonder());
    private final PersistentData persistentData = new PersistentData();
    private final StorageService storageService = new StorageService(this, persistentData);
    private final ConfigService configService = new ConfigService(this);
    private final PermissionChecker permissionChecker = new PermissionChecker();

    /**
     * This runs when the server starts.
     */
    @Override
    public void onEnable() {
        initializeConfig();
        initializeCommandService();

        // create link
        persistentData.addLink(new Link("Easy Links", "https://github.com/dmccoystephenson/Easy-Links"));

        storageService.load();
    }

    /**
     * This runs when the server stops. Link data is written back to storage so that links created
     * or deleted during the session, along with their use counts, survive the restart.
     */
    @Override
    public void onDisable() {
        storageService.save();
    }

    /**
     * This method handles commands sent to the minecraft server and interprets them if the label matches one of the core commands.
     * @param sender The sender of the command.
     * @param cmd The command that was sent. This is unused.
     * @param label The core command that has been invoked.
     * @param args Arguments of the core command. Often sub-commands.
     * @return A boolean indicating whether the execution of the command was successful.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            DefaultCommand defaultCommand = new DefaultCommand(getVersion());
            return defaultCommand.executeIfPermitted(sender, permissionChecker);
        }

        return commandService.interpretAndExecuteCommand(sender, label, args);
    }

    /**
     * This can be used to get the version of the plugin.
     * @return A string containing the version preceded by 'v'
     */
    public String getVersion() {
        return pluginVersion;
    }

    /**
     * Checks if the version is mismatched.
     * @return A boolean indicating if the version is mismatched.
     */
    public boolean isVersionMismatched() {
        String configVersion = this.getConfig().getString("version");
        if (configVersion == null || this.getVersion() == null) {
            return false;
        } else {
            return !configVersion.equalsIgnoreCase(this.getVersion());
        }
    }

    /**
     * Logs a message to the console if the debugMode config option is enabled.
     * @param message The message to log.
     */
    public void debug(String message) {
        if (configService.getBoolean("debugMode")) {
            getLogger().info("[Debug] " + message);
        }
    }

    private void initializeConfig() {
        if (configFileExists()) {
            performCompatibilityChecks();
        }
        else {
            configService.saveMissingConfigDefaultsIfNotPresent();
        }
    }

    private boolean configFileExists() {
        return new File("./plugins/" + getName() + "/config.yml").exists();
    }

    private void performCompatibilityChecks() {
        if (isVersionMismatched()) {
            configService.saveMissingConfigDefaultsIfNotPresent();
        }
        reloadConfig();
    }

    /**
     * Initializes Ponder's command service with the plugin's commands.
     */
    private void initializeCommandService() {
        ArrayList<AbstractPluginCommand> commands = new ArrayList<>(Arrays.asList(
                new HelpCommand(), new CreateCommand(persistentData, storageService),
                new DeleteCommand(persistentData, storageService), new ViewCommand(persistentData),
                new ListCommand(persistentData), new StatsCommand(persistentData)
        ));
        getPonder().getCommandService().initialize(commands, "That command wasn't found.");
    }
}
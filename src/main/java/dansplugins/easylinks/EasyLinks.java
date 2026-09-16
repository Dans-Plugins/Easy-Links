package dansplugins.easylinks;

import dansplugins.easylinks.commands.*;
import dansplugins.easylinks.data.PersistentData;
import dansplugins.easylinks.objects.Link;
import dansplugins.easylinks.services.ConfigService;
import dansplugins.easylinks.services.StorageService;
import dansplugins.easylinks.trace.TraceClient;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import preponderous.ponder.minecraft.bukkit.abs.AbstractPluginCommand;
import preponderous.ponder.minecraft.bukkit.abs.PonderBukkitPlugin;
import preponderous.ponder.minecraft.bukkit.tools.PermissionChecker;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author Daniel McCoy Stephenson
 */
public class EasyLinks extends PonderBukkitPlugin {
    private final String pluginVersion = "v" + getDescription().getVersion();

    private final PersistentData persistentData = new PersistentData();
    private final StorageService storageService = new StorageService(this, persistentData);
    private final ConfigService configService = new ConfigService(this);
    private final PermissionChecker permissionChecker = new PermissionChecker();

    // A no-op until the config has been read, so a command arriving before
    // onEnable() finishes has something safe to report to.
    private TraceClient trace = TraceClient.disabled();

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

        // usage reporting: one event now, one per command; see config.yml
        trace = TraceClient.builder(configService.getUsageReportingEndpoint(), getName())
                .key(configService.getUsageReportingKey())
                .enabled(configService.isUsageReportingEnabled())
                .serverWideConfig(getDataFolder().getParentFile())
                .logger(getLogger())
                .build();
        logUsageReportingStatus();
        trace.report("startup", null, Collections.singletonMap("version", getDescription().getVersion()));
    }

    /** Says on every start whether usage reporting is on, and why not when it is off. */
    private void logUsageReportingStatus() {
        if (trace.isEnabled()) {
            getLogger().info("Usage reporting is on: " + getName() + " sends its name, version and command names to "
                    + configService.getUsageReportingEndpoint() + " - nothing about players or the server. "
                    + "Turn it off with usage-reporting.enabled: false in this plugin's config.yml, "
                    + "or for every plugin with enabled: false in plugins/trace/config.yml. "
                    + "Details: https://github.com/Stephenson-Software/trace#usage-reporting");
        } else {
            getLogger().info("Usage reporting is off (" + trace.disabledReason() + ").");
        }
    }

    /**
     * This runs when the server stops. Link data is written back to storage so that links created
     * or deleted during the session, along with their use counts, survive the restart.
     */
    @Override
    public void onDisable() {
        trace.close();
        storageService.save();
    }

    /**
     * This method handles commands sent to the minecraft server and interprets them if the label matches one of the core commands.
     * Subcommands are dispatched through the command service Ponder owns, which is the one
     * {@link #initializeCommandService()} hands the plugin's commands to.
     * @param sender The sender of the command.
     * @param cmd The command that was sent. This is unused.
     * @param label The core command that has been invoked.
     * @param args Arguments of the core command. Often sub-commands.
     * @return A boolean indicating whether the execution of the command was successful.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        trace.report("command", null, Collections.singletonMap("name", cmd.getName()));
        if (args.length == 0) {
            DefaultCommand defaultCommand = new DefaultCommand(getVersion());
            return defaultCommand.executeIfPermitted(sender, permissionChecker);
        }

        return getPonder().getCommandService().interpretAndExecuteCommand(sender, label, args);
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
        boolean firstRun = !configFileExists();

        // Writes the bundled config.yml, comments and all, when there is none on disk. It never
        // rewrites a file that already exists, so an installation that predates a bundled key
        // reads it through the defaults instead; see ConfigService.
        saveDefaultConfig();

        if (firstRun) {
            configService.saveMissingConfigDefaultsIfNotPresent();
        } else {
            performCompatibilityChecks();
        }
    }

    private boolean configFileExists() {
        return new File("./plugins/" + getName() + "/config.yml").exists();
    }

    private void performCompatibilityChecks() {
        // A config.yml from before usage reporting has no usage-reporting block on disk; writing
        // the defaults out puts the switch where the operator can see it, rather than only in
        // the jar. See ConfigServiceTest for the isSet()/copyDefaults semantics this relies on.
        if (isVersionMismatched() || !configService.isSet("usage-reporting")) {
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
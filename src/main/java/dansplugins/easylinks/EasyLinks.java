package dansplugins.easylinks;

import dansplugins.easylinks.commands.*;
import dansplugins.easylinks.data.PersistentData;
import dansplugins.easylinks.managers.StorageManager;
import dansplugins.easylinks.objects.Link;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import preponderous.ponder.AbstractPonderPlugin;
import preponderous.ponder.misc.PonderAPI_Integrator;
import preponderous.ponder.misc.specification.ICommand;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class EasyLinks extends AbstractPonderPlugin {

    private static EasyLinks instance;

    public static EasyLinks getInstance() {
        return instance;
    }

    private String version = "v0.2";

    @Override
    public void onEnable() {
        instance = this;
        ponderAPI_integrator = new PonderAPI_Integrator(this);
        toolbox = getPonderAPI().getToolbox();
        initializeConfigService();
        initializeConfigFile();
        registerEventHandlers();
        initializeCommandService();
        getPonderAPI().setDebug(false);

        // create link
        PersistentData.getInstance().addLink(new Link("Easy Links", "https://github.com/dmccoystephenson/Easy-Links"));

        if (getPonderAPI().getConfigService().getBoolean("unleashedLinksEnabled")) {
            PersistentData.getInstance().addLink(new Link("Rules", "https://discord.com/channels/906452086550581278/906455394128371753/906455421353603094"));
            PersistentData.getInstance().addLink(new Link("Discord", "https://discord.gg/pYrwp3eggx \n"));
            PersistentData.getInstance().addLink(new Link("PMC", "https://www.planetminecraft.com/server/kingdom-anarchy/"));
            PersistentData.getInstance().addLink(new Link("Custom Plugins", "https://www.spigotmc.org/resources/authors/danthetechman.659208/"));
        }

        StorageManager.getInstance().load();
    }

    @Override
    public void onDisable() {
        StorageManager.getInstance().save();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            DefaultCommand defaultCommand = new DefaultCommand();
            return defaultCommand.execute(sender);
        }

        return getPonderAPI().getCommandService().interpretCommand(sender, label, args);
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public boolean isVersionMismatched() {
        String configVersion = this.getConfig().getString("version");
        if (configVersion == null || this.getVersion() == null) {
            return false;
        } else {
            return !configVersion.equalsIgnoreCase(this.getVersion());
        }
    }

    private void initializeConfigService() {
        HashMap<String, Object> configOptions = new HashMap<>();
        configOptions.put("debugMode", false);
        configOptions.put("unleashedLinksEnabled", false);
        getPonderAPI().getConfigService().initialize(configOptions);
    }

    private void initializeConfigFile() {
        if (!(new File("./plugins/LanguageBarriers/config.yml").exists())) {
            getPonderAPI().getConfigService().saveMissingConfigDefaultsIfNotPresent();
        }
        else {
            // pre load compatibility checks
            if (isVersionMismatched()) {
                getPonderAPI().getConfigService().saveMissingConfigDefaultsIfNotPresent();
            }
            reloadConfig();
        }
    }

    private void registerEventHandlers() {
        /*
        ArrayList<Listener> listeners = new ArrayList<>();
        getToolbox().getEventHandlerRegistry().registerEventHandlers(listeners, this);
        */
    }

    private void initializeCommandService() {
        ArrayList<ICommand> commands = new ArrayList<>(Arrays.asList(
                new HelpCommand(), new CreateCommand(),
                new DeleteCommand(), new ViewCommand(),
                new ListCommand(), new StatsCommand()
        ));
        getPonderAPI().getCommandService().initialize(commands, "That command wasn't found.");
    }

}

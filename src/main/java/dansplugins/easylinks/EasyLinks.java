package dansplugins.easylinks;

import dansplugins.easylinks.commands.*;
import org.bukkit.event.Listener;
import preponderous.ponder.AbstractPonderPlugin;
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

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable() {

    }

    private void initializeConfigService() {
        HashMap<String, Object> configOptions = new HashMap<>();
        configOptions.put("debugMode", false);
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
        ArrayList<Listener> listeners = new ArrayList<>();
        getToolbox().getEventHandlerRegistry().registerEventHandlers(listeners, this);
    }

    private void initializeCommandService() {
        ArrayList<ICommand> commands = new ArrayList<>(Arrays.asList(
                new HelpCommand(), new CreateCommand(),
                new DeleteCommand(), new EditCommand(),
                new ViewCommand(), new ListCommand(),
                new StatsCommand()
        ));
        getPonderAPI().getCommandService().initialize(commands, "That command wasn't found.");
    }

}

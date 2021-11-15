package dansplugins.easylinks.commands;

import org.bukkit.command.CommandSender;
import preponderous.ponder.misc.AbstractCommand;

import java.util.ArrayList;
import java.util.Collections;

public class ListCommand extends AbstractCommand {

    private ArrayList<String> names = new ArrayList<>(Collections.singletonList("list"));
    private ArrayList<String> permissions = new ArrayList<>(Collections.singletonList("el.list"));

    @Override
    public ArrayList<String> getNames() {
        return names;
    }

    @Override
    public ArrayList<String> getPermissions() {
        return permissions;
    }

    @Override
    public boolean execute(CommandSender commandSender) {
        // TODO: implement
        return false;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] strings) {
        return execute(commandSender);
    }
}

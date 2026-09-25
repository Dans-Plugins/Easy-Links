package dansplugins.easylinks.commands;

import dansplugins.easylinks.data.PersistentData;
import dansplugins.easylinks.objects.Link;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ListCommandTest {
    private PersistentData persistentData;
    private FakeCommandSender commandSender;
    private ListCommand listCommand;

    @BeforeEach
    void setUp() {
        persistentData = new PersistentData();
        commandSender = new FakeCommandSender();
        listCommand = new ListCommand(persistentData);
    }

    @Test
    void execute_reportsWhenThereAreNoLinks() {
        boolean result = listCommand.execute(commandSender.asCommandSender());

        assertTrue(result);
        assertEquals(1, commandSender.getMessages().size());
        assertTrue(commandSender.getMessages().get(0).endsWith("There are no links set at this time."));
    }

    @Test
    void execute_sendsAHeaderFollowedByEveryLabel() {
        persistentData.addLink(new Link("discord", "https://discord.gg/example"));
        persistentData.addLink(new Link("wiki", "https://example.com/wiki"));

        boolean result = listCommand.execute(commandSender.asCommandSender());

        List<String> messages = commandSender.getMessages();
        assertTrue(result);
        assertEquals(3, messages.size());
        assertTrue(messages.get(0).endsWith("=== Links ==="));
        // Links are held in a HashSet, so the order of the labels is not fixed.
        assertTrue(messages.subList(1, 3).stream().anyMatch(message -> message.endsWith("discord")));
        assertTrue(messages.subList(1, 3).stream().anyMatch(message -> message.endsWith("wiki")));
    }

    @Test
    void execute_ignoresArguments() {
        persistentData.addLink(new Link("discord", "https://discord.gg/example"));

        boolean result = listCommand.execute(commandSender.asCommandSender(), new String[]{"unexpected"});

        assertTrue(result);
        assertEquals(2, commandSender.getMessages().size());
        assertTrue(commandSender.getMessages().get(1).endsWith("discord"));
    }
}

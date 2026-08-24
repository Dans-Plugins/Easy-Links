package dansplugins.easylinks.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultCommandTest {
    private FakeCommandSender commandSender;
    private DefaultCommand defaultCommand;

    @BeforeEach
    void setUp() {
        commandSender = new FakeCommandSender();
        defaultCommand = new DefaultCommand("v9.9.9");
    }

    @Test
    void execute_showsTheVersionAndTheDansPluginsRepository() {
        boolean result = defaultCommand.execute(commandSender.asCommandSender());

        assertTrue(result);
        List<String> messages = commandSender.getMessages();
        assertEquals(3, messages.size());
        assertTrue(messages.get(0).endsWith("=== Easy Links v9.9.9 ==="));
        assertTrue(messages.get(2).endsWith("Repository: https://github.com/Dans-Plugins/Easy-Links"));
    }

    @Test
    void execute_doesNotAdvertiseThePreTransferOwnerOrTheEmptyWiki() {
        defaultCommand.execute(commandSender.asCommandSender());

        for (String message : commandSender.getMessages()) {
            assertTrue(!message.contains("dmccoystephenson/Easy-Links"),
                    "The banner still advertises the pre-transfer repository owner: " + message);
            assertTrue(!message.contains("/wiki"),
                    "The banner still advertises a wiki, and no wiki has been created: " + message);
        }
    }
}

package dansplugins.easylinks.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import preponderous.ponder.minecraft.bukkit.tools.PermissionChecker;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultCommandTest {
    private FakeCommandSender commandSender;
    private DefaultCommand defaultCommand;
    private PermissionChecker permissionChecker;

    @BeforeEach
    void setUp() {
        commandSender = new FakeCommandSender();
        defaultCommand = new DefaultCommand("v9.9.9");
        permissionChecker = new PermissionChecker();
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
            assertFalse(message.contains("dmccoystephenson/Easy-Links"),
                    "The banner still advertises the pre-transfer repository owner: " + message);
            assertFalse(message.contains("/wiki"),
                    "The banner still advertises a wiki, and no wiki has been created: " + message);
        }
    }

    @Test
    void executeIfPermitted_withoutTheNode_refusesInsteadOfShowingTheBanner() {
        boolean result = defaultCommand.executeIfPermitted(commandSender.asCommandSender(), permissionChecker);

        assertTrue(result);
        for (String message : commandSender.getMessages()) {
            assertFalse(message.contains("=== Easy Links"),
                    "The banner was shown to a sender without el.default: " + message);
        }
        assertTrue(commandSender.getMessages().stream().anyMatch(message -> message.contains("el.default")),
                "No refusal naming the missing permission was sent: " + commandSender.getMessages());
    }

    @Test
    void executeIfPermitted_withTheNode_showsTheBanner() {
        commandSender.grantPermission("el.default");

        boolean result = defaultCommand.executeIfPermitted(commandSender.asCommandSender(), permissionChecker);

        assertTrue(result);
        List<String> messages = commandSender.getMessages();
        assertEquals(3, messages.size());
        assertTrue(messages.get(0).endsWith("=== Easy Links v9.9.9 ==="));
    }
}

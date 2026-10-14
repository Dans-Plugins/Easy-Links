package dansplugins.easylinks.commands;

import dansplugins.easylinks.data.PersistentData;
import dansplugins.easylinks.objects.Link;
import org.bukkit.ChatColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeleteCommandTest {
    private PersistentData persistentData;
    private RecordingStorage storage;
    private FakeCommandSender commandSender;
    private DeleteCommand deleteCommand;

    @BeforeEach
    void setUp() {
        persistentData = new PersistentData();
        persistentData.addLink(new Link("discord", "https://discord.gg/example"));
        storage = new RecordingStorage();
        commandSender = new FakeCommandSender();
        deleteCommand = new DeleteCommand(persistentData, storage);
    }

    @Test
    void execute_savesTheRemovalToStorage() {
        boolean result = deleteCommand.execute(commandSender.asCommandSender(), new String[]{"\"discord\""});

        assertTrue(result);
        assertNull(persistentData.getLink("discord"));
        assertEquals(1, storage.getSaveCount());
        assertTrue(commandSender.getMessages().get(0).endsWith("Link deleted."));
    }

    @Test
    void execute_doesNotSaveWhenTheLabelIsNotFound() {
        boolean result = deleteCommand.execute(commandSender.asCommandSender(), new String[]{"\"nonexistent\""});

        assertFalse(result);
        assertEquals(1, persistentData.getLinks().size());
        assertEquals(0, storage.getSaveCount());
    }

    @Test
    void execute_reportsAnUnknownLabelAsNotFound() {
        deleteCommand.execute(commandSender.asCommandSender(), new String[]{"\"nonexistent\""});

        assertEquals(1, commandSender.getMessages().size());
        assertEquals(ChatColor.RED + "That link wasn't found.", commandSender.getMessages().get(0));
    }

    @Test
    void execute_matchesTheLabelCaseInsensitively() {
        boolean result = deleteCommand.execute(commandSender.asCommandSender(), new String[]{"\"DISCORD\""});

        assertTrue(result);
        assertEquals(0, persistentData.getLinks().size());
        assertEquals(1, storage.getSaveCount());
    }

    @Test
    void execute_leavesOtherLinksInPlace() {
        persistentData.addLink(new Link("wiki", "https://example.com/wiki"));

        deleteCommand.execute(commandSender.asCommandSender(), new String[]{"\"discord\""});

        assertEquals(1, persistentData.getLinks().size());
        assertNull(persistentData.getLink("discord"));
        assertNotNull(persistentData.getLink("wiki"));
    }

    @Test
    void execute_sendsUsageWhenTheLabelIsNotQuoted() {
        boolean result = deleteCommand.execute(commandSender.asCommandSender(), new String[]{"discord"});

        assertFalse(result);
        assertNotNull(persistentData.getLink("discord"));
        assertEquals(0, storage.getSaveCount());
        assertTrue(commandSender.getMessages().get(0).endsWith("Usage: /el delete \"label\""));
    }

    @Test
    void execute_sendsUsageWhenNoArgumentsAreGiven() {
        boolean result = deleteCommand.execute(commandSender.asCommandSender());

        assertFalse(result);
        assertNotNull(persistentData.getLink("discord"));
        assertEquals(0, storage.getSaveCount());
        assertEquals(1, commandSender.getMessages().size());
        assertEquals(ChatColor.RED + "Usage: /el delete \"label\"", commandSender.getMessages().get(0));
    }
}

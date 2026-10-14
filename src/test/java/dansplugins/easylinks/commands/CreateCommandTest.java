package dansplugins.easylinks.commands;

import dansplugins.easylinks.data.PersistentData;
import dansplugins.easylinks.objects.Link;
import org.bukkit.ChatColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateCommandTest {
    private PersistentData persistentData;
    private RecordingStorage storage;
    private FakeCommandSender commandSender;
    private CreateCommand createCommand;

    @BeforeEach
    void setUp() {
        persistentData = new PersistentData();
        storage = new RecordingStorage();
        commandSender = new FakeCommandSender();
        createCommand = new CreateCommand(persistentData, storage);
    }

    @Test
    void execute_savesTheNewLinkToStorage() {
        boolean result = createCommand.execute(commandSender.asCommandSender(),
                new String[]{"\"discord\"", "\"https://discord.gg/example\""});

        assertTrue(result);
        assertNotNull(persistentData.getLink("discord"));
        assertEquals(1, storage.getSaveCount());
        assertTrue(commandSender.getMessages().get(0).endsWith("Link created."));
    }

    @Test
    void execute_doesNotSaveWhenArgumentsAreMalformed() {
        boolean result = createCommand.execute(commandSender.asCommandSender(),
                new String[]{"discord", "https://discord.gg/example"});

        assertFalse(result);
        assertEquals(0, persistentData.getLinks().size());
        assertEquals(0, storage.getSaveCount());
        assertTrue(commandSender.getMessages().get(0).endsWith("Usage: /el create \"label\" \"link\""));
    }

    @Test
    void execute_storesTheUrlWithZeroUses() {
        createCommand.execute(commandSender.asCommandSender(),
                new String[]{"\"discord\"", "\"https://discord.gg/example\""});

        Link link = persistentData.getLink("discord");
        assertEquals("https://discord.gg/example", link.getUrl());
        assertEquals(0, link.getUses());
    }

    @Test
    void execute_keepsSpacesInsideAQuotedLabel() {
        boolean result = createCommand.execute(commandSender.asCommandSender(),
                new String[]{"\"Server", "Wiki\"", "\"https://example.com/wiki\""});

        assertTrue(result);
        Link link = persistentData.getLink("Server Wiki");
        assertNotNull(link);
        assertEquals("Server Wiki", link.getLabel());
        assertEquals("https://example.com/wiki", link.getUrl());
    }

    @Test
    void execute_sendsUsageWhenOnlyALabelIsGiven() {
        boolean result = createCommand.execute(commandSender.asCommandSender(), new String[]{"\"discord\""});

        assertFalse(result);
        assertEquals(0, persistentData.getLinks().size());
        assertEquals(0, storage.getSaveCount());
        assertTrue(commandSender.getMessages().get(0).endsWith("Usage: /el create \"label\" \"link\""));
    }

    @Test
    void execute_sendsUsageWhenNoArgumentsAreGiven() {
        boolean result = createCommand.execute(commandSender.asCommandSender());

        assertFalse(result);
        assertEquals(0, persistentData.getLinks().size());
        assertEquals(0, storage.getSaveCount());
        assertEquals(1, commandSender.getMessages().size());
        assertEquals(ChatColor.RED + "Usage: /el create \"label\" \"link\"", commandSender.getMessages().get(0));
    }
}

package dansplugins.easylinks.commands;

import dansplugins.easylinks.data.PersistentData;
import dansplugins.easylinks.objects.Link;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
}

package dansplugins.easylinks.commands;

import dansplugins.easylinks.data.PersistentData;
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
    }

    @Test
    void execute_doesNotSaveWhenArgumentsAreMalformed() {
        boolean result = createCommand.execute(commandSender.asCommandSender(),
                new String[]{"discord", "https://discord.gg/example"});

        assertFalse(result);
        assertEquals(0, persistentData.getLinks().size());
        assertEquals(0, storage.getSaveCount());
    }
}

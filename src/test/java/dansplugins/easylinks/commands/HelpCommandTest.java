package dansplugins.easylinks.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HelpCommandTest {
    // Every subcommand registered in EasyLinks.initializeCommandService(), in the order the help lists them,
    // with the argument syntax of the command's own usage message and the description from COMMANDS.md.
    private static final List<String> EXPECTED_LINES = Arrays.asList(
            "/el help - View a list of commands.",
            "/el list - List all saved links.",
            "/el view \"label\" - View the URL for a saved link.",
            "/el create \"label\" \"link\" - Create a new link.",
            "/el delete \"label\" - Delete a link.",
            "/el stats - View plugin statistics.");

    private FakeCommandSender commandSender;
    private HelpCommand helpCommand;

    @BeforeEach
    void setUp() {
        commandSender = new FakeCommandSender();
        helpCommand = new HelpCommand();
    }

    @Test
    void execute_listsEverySubcommand() {
        boolean result = helpCommand.execute(commandSender.asCommandSender());

        List<String> messages = commandSender.getMessages();
        assertTrue(result);
        assertEquals(EXPECTED_LINES.size(), messages.size());
        for (int i = 0; i < EXPECTED_LINES.size(); i++) {
            assertTrue(messages.get(i).endsWith(EXPECTED_LINES.get(i)), "line " + i + " was " + messages.get(i));
        }
    }

    @Test
    void execute_ignoresArguments() {
        boolean result = helpCommand.execute(commandSender.asCommandSender(), new String[]{"unexpected"});

        assertTrue(result);
        assertEquals(EXPECTED_LINES.size(), commandSender.getMessages().size());
    }
}

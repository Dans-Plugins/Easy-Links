package dansplugins.easylinks.commands;

import dansplugins.easylinks.data.PersistentData;
import dansplugins.easylinks.objects.Link;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StatsCommandTest {
    private PersistentData persistentData;
    private FakeCommandSender commandSender;
    private StatsCommand statsCommand;

    @BeforeEach
    void setUp() {
        persistentData = new PersistentData();
        commandSender = new FakeCommandSender();
        statsCommand = new StatsCommand(persistentData);
    }

    @Test
    void execute_reportsZeroesAndNoMostPopularLinkWhenThereAreNoLinks() {
        boolean result = statsCommand.execute(commandSender.asCommandSender());

        List<String> messages = commandSender.getMessages();
        assertTrue(result);
        assertEquals(3, messages.size());
        assertTrue(messages.get(0).endsWith("Number of Links: 0"));
        assertTrue(messages.get(1).endsWith("Total number of uses: 0"));
        assertTrue(messages.get(2).endsWith("Most popular link: N/A"));
    }

    @Test
    void execute_reportsTheLinkCountTheSummedUsesAndTheMostUsedLink() {
        Link discord = new Link("discord", "https://discord.gg/example");
        discord.setUses(3);
        Link wiki = new Link("wiki", "https://example.com/wiki");
        wiki.setUses(7);
        persistentData.addLink(discord);
        persistentData.addLink(wiki);

        boolean result = statsCommand.execute(commandSender.asCommandSender());

        List<String> messages = commandSender.getMessages();
        assertTrue(result);
        assertEquals(3, messages.size());
        assertTrue(messages.get(0).endsWith("Number of Links: 2"));
        assertTrue(messages.get(1).endsWith("Total number of uses: 10"));
        assertTrue(messages.get(2).endsWith("Most popular link: wiki"));
    }

    @Test
    void execute_ignoresArguments() {
        Link discord = new Link("discord", "https://discord.gg/example");
        discord.setUses(2);
        persistentData.addLink(discord);

        boolean result = statsCommand.execute(commandSender.asCommandSender(), new String[]{"unexpected"});

        List<String> messages = commandSender.getMessages();
        assertTrue(result);
        assertEquals(3, messages.size());
        assertTrue(messages.get(0).endsWith("Number of Links: 1"));
        assertTrue(messages.get(1).endsWith("Total number of uses: 2"));
        assertTrue(messages.get(2).endsWith("Most popular link: discord"));
    }
}

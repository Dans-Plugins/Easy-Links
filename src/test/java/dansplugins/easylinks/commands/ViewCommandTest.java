package dansplugins.easylinks.commands;

import dansplugins.easylinks.data.PersistentData;
import dansplugins.easylinks.objects.Link;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ViewCommandTest {
    private PersistentData persistentData;
    private Link link;
    private FakeCommandSender commandSender;
    private ViewCommand viewCommand;

    @BeforeEach
    void setUp() {
        persistentData = new PersistentData();
        link = new Link("discord", "https://discord.gg/example");
        persistentData.addLink(link);
        commandSender = new FakeCommandSender();
        viewCommand = new ViewCommand(persistentData);
    }

    @Test
    void execute_sendsTheLabelAndUrlAndCountsAUse() {
        boolean result = viewCommand.execute(commandSender.asCommandSender(), new String[]{"\"discord\""});

        assertTrue(result);
        assertEquals(1, link.getUses());
        assertEquals(2, commandSender.getMessages().size());
        assertTrue(commandSender.getMessages().get(0).endsWith(" === discord === "));
        assertTrue(commandSender.getMessages().get(1).endsWith("https://discord.gg/example"));
    }

    @Test
    void execute_matchesTheLabelCaseInsensitively() {
        boolean result = viewCommand.execute(commandSender.asCommandSender(), new String[]{"\"DISCORD\""});

        assertTrue(result);
        assertEquals(1, link.getUses());
        assertTrue(commandSender.getMessages().get(0).endsWith(" === discord === "));
    }

    @Test
    void execute_reportsAnUnknownLabelWithoutCountingAUse() {
        boolean result = viewCommand.execute(commandSender.asCommandSender(), new String[]{"\"nonexistent\""});

        assertFalse(result);
        assertEquals(0, link.getUses());
        assertEquals(1, commandSender.getMessages().size());
        assertTrue(commandSender.getMessages().get(0).endsWith("That link wasn't found."));
    }

    @Test
    void execute_sendsUsageWhenTheLabelIsNotQuoted() {
        boolean result = viewCommand.execute(commandSender.asCommandSender(), new String[]{"discord"});

        assertFalse(result);
        assertEquals(0, link.getUses());
        assertTrue(commandSender.getMessages().get(0).endsWith("Usage: /el view \"label\""));
    }

    @Test
    void execute_sendsUsageWhenNoArgumentsAreGiven() {
        boolean result = viewCommand.execute(commandSender.asCommandSender());

        assertFalse(result);
        assertTrue(commandSender.getMessages().get(0).endsWith("Usage: /el view \"label\""));
    }
}

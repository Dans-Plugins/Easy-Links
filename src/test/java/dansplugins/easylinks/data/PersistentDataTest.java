package dansplugins.easylinks.data;

import dansplugins.easylinks.objects.Link;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PersistentDataTest {
    private PersistentData persistentData;

    @BeforeEach
    void setUp() {
        persistentData = new PersistentData();
    }

    @Test
    void removeLink_removesMatchingLinkFromStorage() {
        persistentData.addLink(new Link("discord", "https://discord.gg/example"));

        boolean result = persistentData.removeLink("discord");

        assertTrue(result);
        assertEquals(0, persistentData.getLinks().size());
        assertNull(persistentData.getLink("discord"));
    }

    @Test
    void removeLink_isCaseInsensitive() {
        persistentData.addLink(new Link("Discord", "https://discord.gg/example"));

        boolean result = persistentData.removeLink("discord");

        assertTrue(result);
        assertEquals(0, persistentData.getLinks().size());
    }

    @Test
    void removeLink_returnsFalseWhenLabelNotFound() {
        persistentData.addLink(new Link("discord", "https://discord.gg/example"));

        boolean result = persistentData.removeLink("nonexistent");

        assertFalse(result);
        assertEquals(1, persistentData.getLinks().size());
    }

    @Test
    void removeLink_onlyRemovesMatchingLink() {
        persistentData.addLink(new Link("discord", "https://discord.gg/example"));
        persistentData.addLink(new Link("wiki", "https://example.com/wiki"));

        boolean result = persistentData.removeLink("discord");

        assertTrue(result);
        assertEquals(1, persistentData.getLinks().size());
        assertNull(persistentData.getLink("discord"));
        assertNotNull(persistentData.getLink("wiki"));
    }

    @Test
    void getTotalUses_returnsZeroWhenNoLinksExist() {
        assertEquals(0, persistentData.getTotalUses());
    }

    @Test
    void getTotalUses_sumsUsesAcrossAllLinks() {
        Link discord = new Link("discord", "https://discord.gg/example");
        discord.setUses(3);
        Link wiki = new Link("wiki", "https://example.com/wiki");
        wiki.setUses(5);
        persistentData.addLink(discord);
        persistentData.addLink(wiki);

        assertEquals(8, persistentData.getTotalUses());
    }

    @Test
    void getMostPopularLink_returnsPlaceholderWhenNoLinksExist() {
        assertEquals("N/A", persistentData.getMostPopularLink());
    }

    @Test
    void getMostPopularLink_returnsLabelOfLinkWithHighestUses() {
        Link discord = new Link("discord", "https://discord.gg/example");
        discord.setUses(3);
        Link wiki = new Link("wiki", "https://example.com/wiki");
        wiki.setUses(5);
        persistentData.addLink(discord);
        persistentData.addLink(wiki);

        assertEquals("wiki", persistentData.getMostPopularLink());
    }
}

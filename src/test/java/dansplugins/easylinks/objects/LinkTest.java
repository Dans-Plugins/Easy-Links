package dansplugins.easylinks.objects;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LinkTest {

    @Test
    void constructor_startsWithZeroUses() {
        Link link = new Link("discord", "https://discord.gg/example");

        assertEquals("discord", link.getLabel());
        assertEquals("https://discord.gg/example", link.getUrl());
        assertEquals(0, link.getUses());
    }

    @Test
    void save_writesEachFieldAsJson() {
        Link link = new Link("discord", "https://discord.gg/example");
        link.setUses(7);

        Map<String, String> data = link.save();

        assertEquals(3, data.size());
        assertEquals("\"discord\"", data.get("label"));
        assertEquals("\"https://discord.gg/example\"", data.get("url"));
        assertEquals("7", data.get("uses"));
    }

    @Test
    void saveThenLoad_roundTripsAllFields() {
        Link original = new Link("Server Wiki", "https://example.com/wiki?page=1&lang=en");
        original.setUses(42);

        Link restored = new Link(original.save());

        assertEquals("Server Wiki", restored.getLabel());
        assertEquals("https://example.com/wiki?page=1&lang=en", restored.getUrl());
        assertEquals(42, restored.getUses());
    }

    @Test
    void load_readsQuotedUsesValue() {
        Map<String, String> data = new HashMap<>();
        data.put("label", "\"discord\"");
        data.put("url", "\"https://discord.gg/example\"");
        data.put("uses", "\"5\"");

        Link link = new Link(data);

        assertEquals(5, link.getUses());
    }

    @Test
    void load_throwsWhenUsesIsMissing() {
        Map<String, String> data = new HashMap<>();
        data.put("label", "\"discord\"");
        data.put("url", "\"https://discord.gg/example\"");

        assertThrows(NumberFormatException.class, () -> new Link(data));
    }

    @Test
    void links_withIdenticalFieldsAreDistinctInASet() {
        Link first = new Link("discord", "https://discord.gg/example");
        Link second = new Link("discord", "https://discord.gg/example");

        HashSet<Link> links = new HashSet<>();
        links.add(first);
        links.add(second);

        assertNotEquals(first, second);
        assertEquals(2, links.size());
    }
}

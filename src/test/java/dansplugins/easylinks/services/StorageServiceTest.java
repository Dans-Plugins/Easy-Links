package dansplugins.easylinks.services;

import dansplugins.easylinks.data.PersistentData;
import dansplugins.easylinks.objects.Link;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pins how links are written to and read back from {@code links.json}, against a real file in a
 * temporary directory rather than the server's {@code ./plugins/EasyLinks/}.
 */
class StorageServiceTest {
    @TempDir
    Path directory;

    private final List<String> debugMessages = new ArrayList<>();

    private StorageService storageServiceFor(PersistentData persistentData) {
        return new StorageService(persistentData, directory.toString() + File.separator, debugMessages::add);
    }

    @Test
    void saveThenLoad_restoresEveryLinkWithItsUses() {
        PersistentData saved = new PersistentData();
        Link discord = new Link("discord", "https://discord.gg/example");
        discord.setUses(3);
        saved.addLink(discord);
        saved.addLink(new Link("Server Wiki", "https://example.com/wiki?page=1&lang=en"));
        storageServiceFor(saved).save();

        PersistentData loaded = new PersistentData();
        storageServiceFor(loaded).load();

        assertEquals(2, loaded.getLinks().size());
        Link restoredDiscord = loaded.getLink("discord");
        assertNotNull(restoredDiscord);
        assertEquals("https://discord.gg/example", restoredDiscord.getUrl());
        assertEquals(3, restoredDiscord.getUses());
        Link restoredWiki = loaded.getLink("Server Wiki");
        assertNotNull(restoredWiki);
        assertEquals("https://example.com/wiki?page=1&lang=en", restoredWiki.getUrl());
        assertEquals(0, restoredWiki.getUses());
    }

    @Test
    void save_writesLinksJsonIntoTheGivenDirectory() {
        PersistentData persistentData = new PersistentData();
        persistentData.addLink(new Link("discord", "https://discord.gg/example"));

        storageServiceFor(persistentData).save();

        assertTrue(directory.resolve("links.json").toFile().isFile());
    }

    @Test
    void load_discardsLinksThatWereInMemoryBeforehand() {
        PersistentData saved = new PersistentData();
        saved.addLink(new Link("discord", "https://discord.gg/example"));
        storageServiceFor(saved).save();

        // EasyLinks.onEnable() adds a link before loading; this is why it never survives (#16).
        PersistentData persistentData = new PersistentData();
        persistentData.addLink(new Link("Easy Links", "https://github.com/dmccoystephenson/Easy-Links"));
        storageServiceFor(persistentData).load();

        assertEquals(1, persistentData.getLinks().size());
        assertNull(persistentData.getLink("Easy Links"));
        assertNotNull(persistentData.getLink("discord"));
    }

    @Test
    void load_withNoLinksJson_leavesNoLinks() {
        PersistentData persistentData = new PersistentData();
        persistentData.addLink(new Link("Easy Links", "https://github.com/dmccoystephenson/Easy-Links"));

        storageServiceFor(persistentData).load();

        assertFalse(directory.resolve("links.json").toFile().exists());
        assertTrue(persistentData.getLinks().isEmpty());
    }

    @Test
    void saveThenLoad_ofNoLinks_leavesNoLinks() {
        storageServiceFor(new PersistentData()).save();

        PersistentData loaded = new PersistentData();
        loaded.addLink(new Link("discord", "https://discord.gg/example"));
        storageServiceFor(loaded).load();

        assertTrue(loaded.getLinks().isEmpty());
    }

    @Test
    void saveAndLoad_reportTheLinkCountToDebug() {
        PersistentData persistentData = new PersistentData();
        persistentData.addLink(new Link("discord", "https://discord.gg/example"));
        persistentData.addLink(new Link("wiki", "https://example.com/wiki"));
        StorageService storageService = storageServiceFor(persistentData);

        storageService.save();
        storageService.load();

        assertEquals(2, debugMessages.size());
        assertEquals("Saved 2 link(s) to storage.", debugMessages.get(0));
        assertEquals("Loaded 2 link(s) from storage.", debugMessages.get(1));
    }
}

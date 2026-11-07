package dansplugins.easylinks.services;

import dansplugins.easylinks.data.PersistentData;
import dansplugins.easylinks.objects.Link;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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
    private final List<String> warnings = new ArrayList<>();

    private StorageService storageServiceFor(PersistentData persistentData) {
        return new StorageService(persistentData, directory.toString() + File.separator, debugMessages::add, warnings::add);
    }

    private void writeLinksJson(String content) throws IOException {
        Files.write(directory.resolve("links.json"), content.getBytes(StandardCharsets.UTF_8));
    }

    private String readLinksJson() throws IOException {
        return new String(Files.readAllBytes(directory.resolve("links.json")), StandardCharsets.UTF_8);
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
        assertTrue(warnings.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "[{\"label\":\"\\\"discord\\\"\",\"url\":\"\\\"https://discord.gg/example\\\"\",\"uses\":\"3\"},{\"label\":", // cut off part-way
            "[{\"label\":\"\\\"discord\\\"\",\"url\":\"\\\"https://discord.gg/example\\\"\",\"uses\":\"3\",}]", // stray comma
            "", // no content at all
            "null",
            "{\"label\":\"\\\"discord\\\"\"}", // not a list
            "[{\"label\":\"\\\"discord\\\"\",\"url\":\"\\\"https://discord.gg/example\\\"\"}]", // no uses
            "[{\"label\":\"\\\"discord\\\"\",\"url\":\"\\\"https://discord.gg/example\\\"\",\"uses\":\"many\"}]", // uses not a number
            "[{\"url\":\"\\\"https://discord.gg/example\\\"\",\"uses\":\"3\"}]", // no label
            "[{\"label\":\"\\\"discord\\\"\",\"uses\":\"3\"}]" // no URL
    })
    void save_afterAFailedLoad_leavesTheUnreadableFileAsItWas(String unreadable) throws IOException {
        writeLinksJson(unreadable);
        PersistentData persistentData = new PersistentData();
        StorageService storageService = storageServiceFor(persistentData);

        storageService.load();
        persistentData.addLink(new Link("created afterwards", "https://example.com"));
        storageService.save();

        assertEquals(unreadable, readLinksJson());
    }

    @Test
    void load_ofAnUnreadableFile_warnsAndLeavesNoLinks() throws IOException {
        writeLinksJson("[{\"label\":");
        PersistentData persistentData = new PersistentData();
        persistentData.addLink(new Link("Easy Links", "https://github.com/dmccoystephenson/Easy-Links"));

        storageServiceFor(persistentData).load();

        assertTrue(persistentData.getLinks().isEmpty());
        assertEquals(1, warnings.size());
        assertTrue(warnings.get(0).contains("Could not read"));
        assertTrue(debugMessages.isEmpty());
    }

    @Test
    void save_afterAFailedLoad_warnsThatNothingWasSaved() throws IOException {
        writeLinksJson("[{\"label\":");
        StorageService storageService = storageServiceFor(new PersistentData());
        storageService.load();
        warnings.clear();

        boolean saved = storageService.save();

        assertFalse(saved);
        assertEquals(1, warnings.size());
        assertTrue(warnings.get(0).startsWith("links.json was not saved"));
        assertTrue(debugMessages.isEmpty());
    }

    @Test
    void save_reportsAWriteThatHappened() {
        assertTrue(storageServiceFor(new PersistentData()).save());
    }

    @Test
    void save_afterTheFileIsRepairedAndLoaded_writesAgain() throws IOException {
        writeLinksJson("[{\"label\":");
        PersistentData persistentData = new PersistentData();
        StorageService storageService = storageServiceFor(persistentData);
        storageService.load();

        writeLinksJson("[]");
        storageService.load();
        persistentData.addLink(new Link("discord", "https://discord.gg/example"));
        storageService.save();

        PersistentData reloaded = new PersistentData();
        storageServiceFor(reloaded).load();
        assertNotNull(reloaded.getLink("discord"));
    }
}

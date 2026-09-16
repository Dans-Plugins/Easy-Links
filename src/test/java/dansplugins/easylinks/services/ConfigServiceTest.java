package dansplugins.easylinks.services;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pins how the usage-reporting settings are read, against a real {@link YamlConfiguration} rather
 * than a mock, because the point being pinned is Bukkit's own behaviour: {@code saveDefaultConfig()}
 * never rewrites a {@code config.yml} that already exists, so an installation upgraded from before
 * the {@code usage-reporting} block has none of it on disk. The jar's {@code config.yml} is
 * registered as that file's defaults, and only the one-argument getters fall through to them.
 */
class ConfigServiceTest {
    private static final String BUNDLED_KEY = "6uCKPycLUOUIk2v_hnYb-EazXmMRvpKuP1PXFadSwUw";

    /** A config whose on-disk contents are {@code onDisk}, with the bundled config.yml as its defaults. */
    private static ConfigService configServiceFor(String onDisk) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(onDisk);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
        config.setDefaults(bundledDefaults());
        return new ConfigService(null) {
            @Override
            public FileConfiguration getConfig() {
                return config;
            }
        };
    }

    private static YamlConfiguration bundledDefaults() {
        InputStream bundled = ConfigServiceTest.class.getResourceAsStream("/config.yml");
        assertNotNull(bundled, "src/main/resources/config.yml must be bundled in the jar");
        return YamlConfiguration.loadConfiguration(new InputStreamReader(bundled, StandardCharsets.UTF_8));
    }

    @Test
    void bundledConfigCarriesTheBlock() {
        YamlConfiguration bundled = bundledDefaults();

        assertTrue(bundled.getBoolean("usage-reporting.enabled"));
        assertEquals("https://trace.danielstephenson.dev", bundled.getString("usage-reporting.endpoint"));
        assertEquals(BUNDLED_KEY, bundled.getString("usage-reporting.key"));
    }

    @Test
    void usageReporting_readsThroughToTheBundledDefaultsWhenTheFileHasNoBlock() {
        // The config.yml of a server that ran a version before usage reporting existed.
        ConfigService configService = configServiceFor("version: v0.4.0\ndebugMode: false\n");

        assertTrue(configService.isUsageReportingEnabled());
        assertEquals("https://trace.danielstephenson.dev", configService.getUsageReportingEndpoint());
        assertEquals(BUNDLED_KEY, configService.getUsageReportingKey());
    }

    @Test
    void twoArgumentGettersWouldNotFallThrough_whichIsWhyTheyAreNotUsed() {
        // Documents the trap the one-argument getters avoid: the explicit fallback wins over the
        // bundled defaults, so reading the key this way would turn reporting off on every
        // installation whose config.yml predates the block.
        ConfigService configService = configServiceFor("version: v0.4.0\ndebugMode: false\n");

        assertEquals("", configService.getConfig().getString("usage-reporting.key", ""));
        assertEquals(BUNDLED_KEY, configService.getConfig().getString("usage-reporting.key"));
    }

    @Test
    void usageReporting_isOffWithNoKeyAnywhere() {
        YamlConfiguration config = new YamlConfiguration();
        ConfigService configService = new ConfigService(null) {
            @Override
            public FileConfiguration getConfig() {
                return config;
            }
        };

        assertEquals("", configService.getUsageReportingKey(), "no key anywhere must read as off, not as null");
        assertEquals("https://trace.danielstephenson.dev", configService.getUsageReportingEndpoint());
        assertFalse(configService.isUsageReportingEnabled());
    }

    @Test
    void usageReporting_readsTheConfiguredValuesOverTheDefaults() {
        ConfigService configService = configServiceFor(
                "usage-reporting:\n  enabled: false\n  endpoint: http://localhost:8080\n  key: abc\n");

        assertFalse(configService.isUsageReportingEnabled());
        assertEquals("http://localhost:8080", configService.getUsageReportingEndpoint());
        assertEquals("abc", configService.getUsageReportingKey());
    }

    // The block-on-disk write EasyLinks.performCompatibilityChecks() triggers: isSet() must
    // not count the defaults, or the write would never happen; copyDefaults(true) + save
    // (saveMissingConfigDefaultsIfNotPresent) must be what puts the block into the file.

    @Test
    void anOlderConfigDoesNotCountTheDefaultsAsTheBlockBeingOnDisk() {
        ConfigService configService = configServiceFor("version: v0.1\ndebugMode: false\n");

        assertFalse(configService.isSet("usage-reporting"), "the on-enable write would never trigger");
        assertTrue(configService.isUsageReportingEnabled(), "...while the getters still read through");
    }

    @Test
    void aConfigThatHasTheBlockIsRecognisedAsSuch() {
        ConfigService configService = configServiceFor("usage-reporting:\n  enabled: false\n");

        assertTrue(configService.isSet("usage-reporting"));
    }

    @Test
    void copyingTheDefaultsWritesTheBlockWithTheBundledValues() throws Exception {
        YamlConfiguration onDisk = new YamlConfiguration();
        onDisk.loadFromString("version: v0.1\ndebugMode: false\n");
        onDisk.setDefaults(bundledDefaults());
        onDisk.options().copyDefaults(true);

        YamlConfiguration written = new YamlConfiguration();
        written.loadFromString(onDisk.saveToString());

        assertTrue(written.isSet("usage-reporting"));
        assertEquals(true, written.get("usage-reporting.enabled", null));
        assertEquals("https://trace.danielstephenson.dev", written.get("usage-reporting.endpoint", null));
        assertEquals(BUNDLED_KEY, written.get("usage-reporting.key", null));
        assertEquals("v0.1", written.getString("version"), "the existing keys survive the write");
    }
}

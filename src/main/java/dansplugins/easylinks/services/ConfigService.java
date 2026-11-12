package dansplugins.easylinks.services;

import dansplugins.easylinks.EasyLinks;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.function.Supplier;

/**
 * @author Daniel McCoy Stephenson
 */
public class ConfigService {
    private static final String USAGE_REPORTING_ENABLED_KEY = "usage-reporting.enabled";
    private static final String USAGE_REPORTING_ENDPOINT_KEY = "usage-reporting.endpoint";
    private static final String USAGE_REPORTING_KEY_KEY = "usage-reporting.key";
    private static final String DEFAULT_USAGE_REPORTING_ENDPOINT = "https://trace.danielstephenson.dev";

    private final Supplier<FileConfiguration> config;
    private final Supplier<String> version;
    private final Runnable saveConfig;

    public ConfigService(EasyLinks easyLinks) {
        // Lambdas rather than method references: a bound reference dereferences the plugin here,
        // and nothing of the plugin is touched until a method of this service is called.
        this(() -> easyLinks.getConfig(), () -> easyLinks.getVersion(), () -> easyLinks.saveConfig());
    }

    /**
     * @param config Supplies the plugin's config.yml.
     * @param version Supplies the running plugin version, written to the {@code version} key.
     * @param saveConfig Writes the config back to disk.
     */
    ConfigService(Supplier<FileConfiguration> config, Supplier<String> version, Runnable saveConfig) {
        this.config = config;
        this.version = version;
        this.saveConfig = saveConfig;
    }

    public void saveMissingConfigDefaultsIfNotPresent() {
        // set version
        if (!getConfig().isString("version")) {
            getConfig().addDefault("version", version.get());
        } else {
            getConfig().set("version", version.get());
        }

        // save config options
        if (!isSet("debugMode")) { getConfig().set("debugMode", false); }

        getConfig().options().copyDefaults(true);
        saveConfig.run();
    }

    // The one-argument getters, deliberately. Bukkit registers the jar's config.yml
    // as the defaults for the file on disk, and the one-argument getters fall
    // through to them -- but the two-argument getters return their explicit
    // fallback instead, which for the key would be "" and would turn reporting
    // off wherever the block is missing from disk. EasyLinks writes the block out
    // on enable when it is missing (see performCompatibilityChecks), so this only
    // matters for a hand-trimmed config.yml or a plugins directory that could not
    // be written to. Verified against YamlConfiguration, not assumed; see
    // ConfigServiceTest.

    public boolean isUsageReportingEnabled() {
        return getConfig().getBoolean(USAGE_REPORTING_ENABLED_KEY);
    }

    public String getUsageReportingEndpoint() {
        String endpoint = getConfig().getString(USAGE_REPORTING_ENDPOINT_KEY);
        return endpoint != null ? endpoint : DEFAULT_USAGE_REPORTING_ENDPOINT;
    }

    /** Empty when no key is configured or bundled, which the client treats as "off". */
    public String getUsageReportingKey() {
        String key = getConfig().getString(USAGE_REPORTING_KEY_KEY);
        return key != null ? key : "";
    }

    public FileConfiguration getConfig() {
        return config.get();
    }

    public boolean isSet(String option) {
        return getConfig().isSet(option);
    }

    public int getInt(String option) {
        return getConfig().getInt(option);
    }

    public int getIntOrDefault(String option, int defaultValue) {
        int toReturn = getInt(option);
        if (toReturn == 0) {
            return defaultValue;
        }
        return toReturn;
    }

    public boolean getBoolean(String option) {
        return getConfig().getBoolean(option);
    }

    public double getDouble(String option) {
        return getConfig().getDouble(option);
    }

    public double getDoubleOrDefault(String option, double defaultValue) {
        double toReturn = getDouble(option);
        if (toReturn == 0) {
            return defaultValue;
        }
        return toReturn;
    }

    public String getString(String option) {
        return getConfig().getString(option);
    }

    public String getStringOrDefault(String option, String defaultValue) {
        String toReturn = getString(option);
        if (toReturn == null) {
            return defaultValue;
        }
        return toReturn;
    }
}
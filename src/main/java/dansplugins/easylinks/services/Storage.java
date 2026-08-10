package dansplugins.easylinks.services;

/**
 * The persistence operations that the rest of the plugin depends on.
 *
 * @author Daniel McCoy Stephenson
 */
public interface Storage {
    /**
     * Writes the current state of the plugin's data to disk.
     */
    void save();

    /**
     * Replaces the plugin's data with the state stored on disk.
     */
    void load();
}

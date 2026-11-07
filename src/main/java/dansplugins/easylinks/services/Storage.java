package dansplugins.easylinks.services;

/**
 * The persistence operations that the rest of the plugin depends on.
 *
 * @author Daniel McCoy Stephenson
 */
public interface Storage {
    /**
     * Writes the current state of the plugin's data to disk.
     *
     * @return false if the write was skipped because the stored data could not be read at startup,
     *         in which case the current state will be lost at the next restart.
     */
    boolean save();

    /**
     * Replaces the plugin's data with the state stored on disk.
     */
    void load();
}

package dansplugins.easylinks.commands;

import dansplugins.easylinks.services.Storage;

/**
 * A {@link Storage} stand-in that counts calls instead of touching the file system.
 */
class RecordingStorage implements Storage {
    private int saveCount = 0;
    private boolean saveSucceeds = true;

    @Override
    public boolean save() {
        saveCount++;
        return saveSucceeds;
    }

    @Override
    public void load() {
        throw new UnsupportedOperationException("Commands are not expected to load from storage.");
    }

    int getSaveCount() {
        return saveCount;
    }

    /** Makes every later {@link #save()} report that the write was skipped. */
    void skipSaves() {
        saveSucceeds = false;
    }
}

package dansplugins.easylinks.services;

import dansplugins.easylinks.EasyLinks;
import dansplugins.easylinks.data.PersistentData;
import dansplugins.easylinks.objects.Link;
import preponderous.ponder.misc.JsonWriterReader;

import java.util.*;
import java.util.function.Consumer;

/**
 * @author Daniel McCoy Stephenson
 */
public class StorageService implements Storage {
    private final PersistentData persistentData;
    private final String filePath;
    private final Consumer<String> debug;
    private final Consumer<String> warning;

    // Set when links.json exists but could not be read. Saving then would replace the operator's
    // links with whatever is in memory, so the file is left alone until a load succeeds.
    private boolean linksFileUnreadable = false;

    private final static String FILE_PATH = "./plugins/EasyLinks/";
    private final static String LINKS_FILE_NAME = "links.json";
    private final JsonWriterReader jsonWriterReader = new JsonWriterReader();

    public StorageService(EasyLinks easyLinks, PersistentData persistentData) {
        this(persistentData, FILE_PATH, easyLinks::debug, message -> easyLinks.getLogger().warning(message));
    }

    /**
     * @param persistentData The data that is saved and replaced on load.
     * @param filePath The directory links.json is kept in, ending in a separator.
     * @param debug Receives a message after each save and load.
     * @param warning Receives a message when links.json cannot be read, and for each save that is
     *                skipped because of it.
     */
    StorageService(PersistentData persistentData, String filePath, Consumer<String> debug, Consumer<String> warning) {
        this.persistentData = persistentData;
        this.filePath = filePath;
        this.debug = debug;
        this.warning = warning;
        jsonWriterReader.initialize(filePath);
    }

    @Override
    public void save() {
        if (linksFileUnreadable) {
            warning.accept(LINKS_FILE_NAME + " was not saved, because it could not be read at startup and saving would replace it."
                    + " Repair or remove it and restart the server. Changes made until then are not kept.");
            return;
        }
        saveLinks();
    }

    @Override
    public void load() {
        loadLinks();
    }

    private void saveLinks() {
        List<Map<String, String>> links = new ArrayList<>();
        for (Link link : persistentData.getLinks()){
            links.add(link.save());
        }
        jsonWriterReader.writeOutFiles(links, LINKS_FILE_NAME);
        debug.accept("Saved " + links.size() + " link(s) to storage.");
    }


    private void loadLinks() {
        persistentData.getLinks().clear();
        HashSet<Link> links;
        try {
            links = readLinks();
        } catch (RuntimeException e) {
            linksFileUnreadable = true;
            warning.accept("Could not read " + filePath + LINKS_FILE_NAME + " (" + e.getMessage() + ")."
                    + " No links are loaded, and the file will not be changed. Repair or remove it and restart the server.");
            return;
        }
        linksFileUnreadable = false;
        persistentData.setLinks(links);
        debug.accept("Loaded " + links.size() + " link(s) from storage.");
    }

    /** @throws RuntimeException if links.json holds anything but a list of complete links. */
    private HashSet<Link> readLinks() {
        ArrayList<HashMap<String, String>> data = jsonWriterReader.loadDataFromFilename(filePath + LINKS_FILE_NAME);
        if (data == null) {
            throw new IllegalStateException("the file is empty");
        }
        HashSet<Link> links = new HashSet<>();
        for (Map<String, String> linkData : data){
            Link link = new Link(linkData);
            if (link.getLabel() == null || link.getUrl() == null) {
                throw new IllegalStateException("a link has no label or no URL");
            }
            links.add(link);
        }
        return links;
    }
}
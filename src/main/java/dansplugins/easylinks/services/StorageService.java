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

    private final static String FILE_PATH = "./plugins/EasyLinks/";
    private final static String LINKS_FILE_NAME = "links.json";
    private final JsonWriterReader jsonWriterReader = new JsonWriterReader();

    public StorageService(EasyLinks easyLinks, PersistentData persistentData) {
        this(persistentData, FILE_PATH, easyLinks::debug);
    }

    /**
     * @param persistentData The data that is saved and replaced on load.
     * @param filePath The directory links.json is kept in, ending in a separator.
     * @param debug Receives a message after each save and load.
     */
    StorageService(PersistentData persistentData, String filePath, Consumer<String> debug) {
        this.persistentData = persistentData;
        this.filePath = filePath;
        this.debug = debug;
        jsonWriterReader.initialize(filePath);
    }

    @Override
    public void save() {
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
        ArrayList<HashMap<String, String>> data = jsonWriterReader.loadDataFromFilename(filePath + LINKS_FILE_NAME);
        HashSet<Link> links = new HashSet<>();
        for (Map<String, String> linkData : data){
            Link link = new Link(linkData);
            links.add(link);
        }
        persistentData.setLinks(links);
        debug.accept("Loaded " + links.size() + " link(s) from storage.");
    }
}
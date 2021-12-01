package dansplugins.easylinks.managers;

import dansplugins.easylinks.data.PersistentData;
import dansplugins.easylinks.objects.Link;
import preponderous.ponder.misc.JsonWriterReader;

import java.util.*;

/**
 * @author Daniel Stephenson
 */
public class StorageManager {
    private static StorageManager instance;
    private final static String FILE_PATH = "./plugins/SimpleSkills/";
    private final static String PLAYER_RECORDS_FILE_NAME = "playerRecords.json";
    private final JsonWriterReader jsonWriterReader = new JsonWriterReader();

    private StorageManager() {
        jsonWriterReader.initialize(FILE_PATH);
    }

    public static StorageManager getInstance() {
        if (instance == null) {
            instance = new StorageManager();
        }
        return instance;
    }

    public void save() {
        saveLinks();
    }

    public void load() {
        loadLinks();
    }

    private void saveLinks() {
        List<Map<String, String>> playerRecords = new ArrayList<>();
        for (Link link : PersistentData.getInstance().getLinks()){
            playerRecords.add(link.save());
        }
        jsonWriterReader.writeOutFiles(playerRecords, PLAYER_RECORDS_FILE_NAME);
    }


    private void loadLinks() {
        PersistentData.getInstance().getLinks().clear();
        ArrayList<HashMap<String, String>> data = jsonWriterReader.loadDataFromFilename(FILE_PATH + PLAYER_RECORDS_FILE_NAME);
        HashSet<Link> links = new HashSet<>();
        for (Map<String, String> linkData : data){
            Link link = new Link(linkData);
            links.add(link);
        }
        PersistentData.getInstance().setLinks(links);
    }
}
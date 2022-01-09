package dansplugins.easylinks.services;

import dansplugins.easylinks.data.PersistentData;
import dansplugins.easylinks.objects.Link;
import preponderous.ponder.misc.JsonWriterReader;

import java.util.*;

/**
 * @author Daniel McCoy Stephenson
 */
public class LocalStorageService {
    private static LocalStorageService instance;
    private final static String FILE_PATH = "./plugins/EasyLinks/";
    private final static String LINKS_FILE_NAME = "links.json";
    private final JsonWriterReader jsonWriterReader = new JsonWriterReader();

    private LocalStorageService() {
        jsonWriterReader.initialize(FILE_PATH);
    }

    public static LocalStorageService getInstance() {
        if (instance == null) {
            instance = new LocalStorageService();
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
        List<Map<String, String>> links = new ArrayList<>();
        for (Link link : PersistentData.getInstance().getLinks()){
            links.add(link.save());
        }
        jsonWriterReader.writeOutFiles(links, LINKS_FILE_NAME);
    }


    private void loadLinks() {
        PersistentData.getInstance().getLinks().clear();
        ArrayList<HashMap<String, String>> data = jsonWriterReader.loadDataFromFilename(FILE_PATH + LINKS_FILE_NAME);
        HashSet<Link> links = new HashSet<>();
        for (Map<String, String> linkData : data){
            Link link = new Link(linkData);
            links.add(link);
        }
        PersistentData.getInstance().setLinks(links);
    }
}
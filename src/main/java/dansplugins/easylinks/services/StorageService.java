package dansplugins.easylinks.services;

import dansplugins.easylinks.EasyLinks;
import dansplugins.easylinks.data.PersistentData;
import dansplugins.easylinks.objects.Link;
import preponderous.ponder.misc.JsonWriterReader;

import java.util.*;

/**
 * @author Daniel McCoy Stephenson
 */
public class StorageService implements Storage {
    private final EasyLinks easyLinks;
    private final PersistentData persistentData;

    private final static String FILE_PATH = "./plugins/EasyLinks/";
    private final static String LINKS_FILE_NAME = "links.json";
    private final JsonWriterReader jsonWriterReader = new JsonWriterReader();

    public StorageService(EasyLinks easyLinks, PersistentData persistentData) {
        this.easyLinks = easyLinks;
        this.persistentData = persistentData;
        jsonWriterReader.initialize(FILE_PATH);
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
        easyLinks.debug("Saved " + links.size() + " link(s) to storage.");
    }


    private void loadLinks() {
        persistentData.getLinks().clear();
        ArrayList<HashMap<String, String>> data = jsonWriterReader.loadDataFromFilename(FILE_PATH + LINKS_FILE_NAME);
        HashSet<Link> links = new HashSet<>();
        for (Map<String, String> linkData : data){
            Link link = new Link(linkData);
            links.add(link);
        }
        persistentData.setLinks(links);
        easyLinks.debug("Loaded " + links.size() + " link(s) from storage.");
    }
}
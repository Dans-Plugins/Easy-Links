package dansplugins.easylinks.objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import preponderous.ponder.misc.abs.Savable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel McCoy Stephenson
 */
public class Link implements Savable {
    private String label;
    private String url;
    private int uses;

    public Link(String label, String url) {
        this.label = label;
        this.url = url;
        uses = 0;
    }

    public Link(Map<String, String> data) {
        this.load(data);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getUses() {
        return uses;
    }

    public void setUses(int uses) {
        this.uses = uses;
    }

    @Override()
    public Map<String, String> save() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Map<String, String> saveMap = new HashMap<>();
        saveMap.put("label", gson.toJson(label));
        saveMap.put("url", gson.toJson(url));
        saveMap.put("uses", gson.toJson(uses));

        return saveMap;
    }

    @Override()
    public void load(Map<String, String> data) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        label = gson.fromJson(data.get("label"), String.class);
        url = gson.fromJson(data.get("url"), String.class);
        uses = Integer.parseInt(gson.fromJson(data.get("uses"), String.class));
    }
}
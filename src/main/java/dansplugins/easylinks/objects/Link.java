package dansplugins.easylinks.objects;

import preponderous.ponder.modifiers.Savable;

import java.util.Map;
import java.util.UUID;

public class Link implements Savable {
    private String label;
    private String url;
    private UUID setter;
    private int uses;

    public Link(String label, String url, UUID setter) {
        this.label = label;
        this.url = url;
        this.setter = setter;
        uses = 0;
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

    public UUID getSetter() {
        return setter;
    }

    public void setSetter(UUID setter) {
        this.setter = setter;
    }

    public int getUses() {
        return uses;
    }

    public void setUses(int uses) {
        this.uses = uses;
    }

    @Override
    public Map<String, String> save() {
        // TODO: implement
        return null;
    }

    @Override
    public void load(Map<String, String> map) {
        // TODO: implement
    }
}
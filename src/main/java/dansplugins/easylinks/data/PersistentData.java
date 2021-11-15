package dansplugins.easylinks.data;

import dansplugins.easylinks.objects.Link;

import java.util.HashSet;

public class PersistentData {

    private static PersistentData instance;

    private HashSet<Link> links = new HashSet<>();

    private PersistentData() {

    }

    public PersistentData getInstance() {
        if (instance == null) {
            instance = new PersistentData();
        }
        return instance;
    }

    public HashSet<Link> getLinks() {
        return links;
    }

    public Link getLink(String label) {
        for (Link link : links) {
            if (link.getLabel().equalsIgnoreCase(label)) {
                return link;
            }
        }
        return null;
    }

    public boolean addLink(Link link) {
        return links.add(link);
    }

    public boolean removeLink(Link link) {
        return links.remove(link);
    }

}

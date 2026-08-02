package dansplugins.easylinks.data;

import dansplugins.easylinks.objects.Link;

import java.util.HashSet;

/**
 * @author Daniel McCoy Stephenson
 */
public class PersistentData {
    private HashSet<Link> links = new HashSet<>();

    public HashSet<Link> getLinks() {
        return links;
    }

    public void setLinks(HashSet<Link> links) {
        this.links = links;
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

    public boolean removeLink(String label) {
        Link linkToRemove = getLink(label);
        if (linkToRemove == null) {
            return false;
        }
        return links.remove(linkToRemove);
    }

    public int getTotalUses() {
        return -1;
    }

    public String getMostPopularLink() {
        return "(TBD)";
    }
}
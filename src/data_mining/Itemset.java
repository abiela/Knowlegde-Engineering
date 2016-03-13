package data_mining;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by biela.arek@gmail.com (Arek Biela) on 12.03.2016.
 */
public class Itemset {

    private Set<String> items;

    public Itemset(String[] rawValues) {
        items = new HashSet<>();
        for(int i = 0; i < rawValues.length ; i++) {
            items.add(rawValues[i]);
        }
    }

    public Itemset(String onlyItem) {
        items = new HashSet<>();
        items.add(onlyItem);
    }

    public Itemset(Set<String> stringSet) {
        this.items = stringSet;
    }

    public Set<String> getItems() {
        return items;
    }
}

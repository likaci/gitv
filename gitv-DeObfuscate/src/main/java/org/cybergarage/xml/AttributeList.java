package org.cybergarage.xml;

import java.util.Vector;

public class AttributeList extends Vector {
    public Attribute getAttribute(int n) {
        return (Attribute) get(n);
    }

    public Attribute getAttribute(String name) {
        if (name == null) {
            return null;
        }
        int nLists = size();
        for (int n = 0; n < nLists; n++) {
            Attribute elem = getAttribute(n);
            if (name.compareTo(elem.getName()) == 0) {
                return elem;
            }
        }
        return null;
    }
}

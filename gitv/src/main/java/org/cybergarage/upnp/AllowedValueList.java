package org.cybergarage.upnp;

import java.util.Iterator;
import java.util.Vector;

public class AllowedValueList extends Vector {
    public static final String ELEM_NAME = "allowedValueList";

    public AllowedValueList(String[] values) {
        for (String allowedValue : values) {
            add(new AllowedValue(allowedValue));
        }
    }

    public AllowedValue getAllowedValue(int n) {
        return (AllowedValue) get(n);
    }

    public boolean isAllowed(String v) {
        Iterator i = iterator();
        while (i.hasNext()) {
            if (((AllowedValue) i.next()).getValue().equals(v)) {
                return true;
            }
        }
        return false;
    }
}

package org.cybergarage.util;

import java.util.Vector;

public class ListenerList extends Vector {
    public boolean add(Object obj) {
        if (indexOf(obj) >= 0) {
            return false;
        }
        return super.add(obj);
    }
}

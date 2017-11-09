package org.cybergarage.upnp;

import java.util.Vector;

public class IconList extends Vector<Icon> {
    public static final String ELEM_NAME = "iconList";
    private static final long serialVersionUID = 1;

    public Icon getIcon(int n) {
        try {
            return (Icon) get(n);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}

package org.cybergarage.upnp;

import java.util.Vector;

public class ServiceList extends Vector<Service> {
    public static final String ELEM_NAME = "serviceList";
    private static final long serialVersionUID = 1;

    public Service getService(int n) {
        try {
            return (Service) get(n);
        } catch (Exception e) {
            return null;
        }
    }
}

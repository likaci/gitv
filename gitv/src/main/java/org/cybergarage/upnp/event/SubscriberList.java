package org.cybergarage.upnp.event;

import java.util.Vector;

public class SubscriberList extends Vector<Subscriber> {
    private static final long serialVersionUID = 1;

    public Subscriber getSubscriber(int n) {
        try {
            return (Subscriber) get(n);
        } catch (ArrayIndexOutOfBoundsException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}

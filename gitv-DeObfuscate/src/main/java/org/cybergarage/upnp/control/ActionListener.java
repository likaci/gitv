package org.cybergarage.upnp.control;

import org.cybergarage.upnp.Action;

public interface ActionListener {
    boolean actionControlReceived(Action action);
}

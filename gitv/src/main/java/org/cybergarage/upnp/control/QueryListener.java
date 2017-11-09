package org.cybergarage.upnp.control;

import org.cybergarage.upnp.StateVariable;

public interface QueryListener {
    boolean queryControlReceived(StateVariable stateVariable);
}

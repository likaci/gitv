package org.cybergarage.upnp;

import java.util.Vector;

public class ServiceStateTable extends Vector {
    public static final String ELEM_NAME = "serviceStateTable";

    public StateVariable getStateVariable(int n) {
        return (StateVariable) get(n);
    }
}

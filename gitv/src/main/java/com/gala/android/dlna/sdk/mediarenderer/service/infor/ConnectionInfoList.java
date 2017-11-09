package com.gala.android.dlna.sdk.mediarenderer.service.infor;

import java.util.Vector;

public class ConnectionInfoList extends Vector {
    public ConnectionInfo getConnectionInfo(int n) {
        return (ConnectionInfo) get(n);
    }
}

package com.gala.android.dlna.sdk.mediarenderer.service.infor;

import java.util.Vector;

public class AVTransportInfoList extends Vector {
    public AVTransportInfo getAVTransportInfo(int n) {
        return (AVTransportInfo) get(n);
    }
}

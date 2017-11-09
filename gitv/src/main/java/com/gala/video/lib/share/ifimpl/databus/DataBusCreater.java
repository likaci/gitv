package com.gala.video.lib.share.ifimpl.databus;

import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus;

public class DataBusCreater {
    public static IDataBus create() {
        return new DataBus();
    }
}

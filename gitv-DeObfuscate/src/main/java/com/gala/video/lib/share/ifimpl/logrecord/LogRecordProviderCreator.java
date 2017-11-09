package com.gala.video.lib.share.ifimpl.logrecord;

import com.gala.video.lib.share.ifmanager.bussnessIF.logrecord.ILogRecordProvider;

public class LogRecordProviderCreator {
    public static ILogRecordProvider create() {
        return new LogRecordProvider();
    }
}

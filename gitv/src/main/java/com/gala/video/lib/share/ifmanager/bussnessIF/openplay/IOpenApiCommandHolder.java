package com.gala.video.lib.share.ifmanager.bussnessIF.openplay;

import android.content.Context;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;

public interface IOpenApiCommandHolder extends IInterfaceWrapper {
    public static final long OAA_CEIL = 3600000;
    public static final int OAA_CHANNEL_LEVEL1_COUNT = 6;
    public static final int OAA_CHANNEL_LEVEL2_COUNT = 6;
    public static final long OAA_CONNECT_DURATION = 900000;
    public static final long OAA_CONNECT_INTERVAL = 5000;
    public static final long OAA_CONNECT_MAX_COUNT = 3;
    public static final long OAA_DEFAULT_INTERVAL = 0;
    public static final long OAA_DURATION = 3600000;
    public static final long OAA_HISTORY_MAX_COUNT = 60;
    public static final long OAA_MAX_COUNT = 8;
    public static final int OAA_MAX_DATA = 60;
    public static final long OAA_NO_LIMIT = Long.MAX_VALUE;
    public static final int OAA_RESOURCE_ID_COUNT = 5;

    public static abstract class Wrapper implements IOpenApiCommandHolder {
        public Object getInterface() {
            return this;
        }

        public static IOpenApiCommandHolder asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IOpenApiCommandHolder)) {
                return null;
            }
            return (IOpenApiCommandHolder) wrapper;
        }
    }

    IAddInstanceHolder[] getCommandHolder(Context context);
}

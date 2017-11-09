package com.gala.video.app.epg.home.view;

import android.util.Log;
import com.gala.video.app.epg.HomeDebug;

public class ViewDebug {
    public static final boolean DBG;

    static {
        boolean z = HomeDebug.DEBUG_LOG || Log.isLoggable("ViewDebug", 3);
        DBG = z;
    }
}

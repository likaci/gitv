package com.gala.video.crosswalkinterface;

import android.content.Context;

public interface IXWalkPlugin {
    IXWalkView create(Context context);

    boolean isPluginReady();
}

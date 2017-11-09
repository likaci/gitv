package com.gala.sdk.plugin;

import android.content.Context;

public interface IFeature {
    Context getContext();

    String getDescription();

    String getName();

    AbsPluginProvider getProvider();

    int getType();
}

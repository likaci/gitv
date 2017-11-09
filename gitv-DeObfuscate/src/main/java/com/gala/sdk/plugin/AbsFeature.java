package com.gala.sdk.plugin;

import android.content.Context;

public abstract class AbsFeature implements IFeature {
    private Context mContext;
    private final AbsPluginProvider mProvider;
    private final int mType;

    public AbsFeature(Context context, AbsPluginProvider provider, int type) {
        this.mContext = context;
        this.mProvider = provider;
        this.mType = type;
    }

    public int getType() {
        return this.mType;
    }

    public String getName() {
        return null;
    }

    public String getDescription() {
        return null;
    }

    public Context getContext() {
        return this.mContext;
    }

    public final AbsPluginProvider getProvider() {
        return this.mProvider;
    }
}

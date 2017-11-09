package com.gala.video.lib.share.ifimpl.openplay.service.feature;

import android.content.Context;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;

public abstract class MaxCommand<T> extends ServerCommand<T> {
    private int mMaxCount;

    public MaxCommand(Context context, int target, int operation, int dataType, int maxCount) {
        super(context, target, operation, dataType);
        this.mMaxCount = maxCount;
    }

    public int getMaxCount() {
        return this.mMaxCount;
    }
}

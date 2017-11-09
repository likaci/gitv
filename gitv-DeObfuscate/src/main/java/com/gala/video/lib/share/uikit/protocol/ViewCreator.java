package com.gala.video.lib.share.uikit.protocol;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public interface ViewCreator<V extends View> {
    V createView(Context context, ViewGroup viewGroup);
}

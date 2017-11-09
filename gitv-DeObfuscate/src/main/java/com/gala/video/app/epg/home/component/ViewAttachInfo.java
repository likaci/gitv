package com.gala.video.app.epg.home.component;

import android.util.SparseIntArray;

public class ViewAttachInfo {
    public int initialVisibility;
    SparseIntArray mViewsVisibility;
    public int visibility;

    public String toString() {
        StringBuilder sb = new StringBuilder(32);
        sb.append("ViewAttatchInfo{");
        sb.append(this.visibility);
        sb.append(" ");
        sb.append(this.initialVisibility);
        sb.append('}');
        return sb.toString();
    }
}

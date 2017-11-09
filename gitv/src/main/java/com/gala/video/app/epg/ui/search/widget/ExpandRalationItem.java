package com.gala.video.app.epg.ui.search.widget;

import android.util.Log;
import android.widget.TextView;

public class ExpandRalationItem {
    public TextView mKeyItemView;
    public String mRelationKeys;

    public String getUpKeyChar() {
        if (this.mKeyItemView != null) {
            return converIdToChar(this.mKeyItemView.getNextFocusUpId());
        }
        return "";
    }

    public String getLeftKeyChar() {
        if (this.mKeyItemView != null) {
            return converIdToChar(this.mKeyItemView.getNextFocusLeftId());
        }
        return "";
    }

    public String getRightKeyChar() {
        if (this.mKeyItemView != null) {
            return converIdToChar(this.mKeyItemView.getNextFocusRightId());
        }
        return "";
    }

    public String getDownKeyChar() {
        if (this.mKeyItemView != null) {
            return converIdToChar(this.mKeyItemView.getNextFocusDownId());
        }
        return "";
    }

    private String converIdToChar(int id) {
        String resStr = "";
        if (id >= 65 && id <= 90) {
            resStr = String.valueOf((char) id);
        } else if (id == 100) {
            resStr = "0";
        } else {
            resStr = String.valueOf(id % 90);
        }
        Log.e("jaunce", "resStr:" + resStr);
        return resStr;
    }
}

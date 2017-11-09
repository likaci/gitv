package com.gala.android.dlna.sdk.stddmrcontroller.data;

import com.gala.android.dlna.sdk.stddmrcontroller.enums.ACTION;
import com.gala.android.dlna.sdk.stddmrcontroller.enums.ACTION_ARGUMENT;
import java.util.Hashtable;

public class ActionContent {
    private ACTION mAction;
    private Hashtable<ACTION_ARGUMENT, String> mArgumentValues;

    public ActionContent(ACTION action, Hashtable<ACTION_ARGUMENT, String> argumentValues) {
        this.mAction = action;
        this.mArgumentValues = argumentValues;
    }

    public ACTION getAction() {
        return this.mAction;
    }

    public Hashtable<ACTION_ARGUMENT, String> getArgumentValues() {
        return this.mArgumentValues;
    }
}

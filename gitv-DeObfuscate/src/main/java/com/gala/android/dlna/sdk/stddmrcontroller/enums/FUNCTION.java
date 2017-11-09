package com.gala.android.dlna.sdk.stddmrcontroller.enums;

import com.gala.android.dlna.sdk.stddmrcontroller.Util;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import org.json.JSONException;
import org.json.JSONObject;

public enum FUNCTION {
    PLAY(null),
    PAUSE(null),
    STOP(null),
    GETPOSITION(null),
    GETTRANSPORTSTATE(null),
    GETMEDIADURATION(null),
    GETVOLUME(null),
    SEEK(new C00901()),
    PUSHURL(new C00912()),
    PLAYMEDIA(new C00923()),
    SETVOLUME(new C00934()),
    GETMEDIAURI(null);
    
    private Vector<FUNCTION_ARGUMENT> mArguments;

    class C00901 extends Vector<FUNCTION_ARGUMENT> {
        C00901() {
            add(FUNCTION_ARGUMENT.PositionInSecond);
        }
    }

    class C00912 extends Vector<FUNCTION_ARGUMENT> {
        C00912() {
            add(FUNCTION_ARGUMENT.Path);
            add(FUNCTION_ARGUMENT.Title);
            add(FUNCTION_ARGUMENT.MediaType);
        }
    }

    class C00923 extends Vector<FUNCTION_ARGUMENT> {
        C00923() {
            add(FUNCTION_ARGUMENT.Path);
            add(FUNCTION_ARGUMENT.Title);
            add(FUNCTION_ARGUMENT.MediaType);
        }
    }

    class C00934 extends Vector<FUNCTION_ARGUMENT> {
        C00934() {
            add(FUNCTION_ARGUMENT.Percentage);
        }
    }

    private FUNCTION(Vector<FUNCTION_ARGUMENT> arguments) {
        this.mArguments = arguments;
    }

    public Vector<FUNCTION_ARGUMENT> getArguments() {
        return this.mArguments;
    }

    public static Vector<String> getArgumentValues(FUNCTION function, JSONObject jObj) {
        if (function == null || jObj == null || function.getArguments() == null || function.getArguments().isEmpty()) {
            return null;
        }
        Vector<String> argumentValues = new Vector();
        Iterator it = function.getArguments().iterator();
        while (it.hasNext()) {
            FUNCTION_ARGUMENT argument = (FUNCTION_ARGUMENT) it.next();
            if (argument == null) {
                return null;
            }
            argumentValues.add(jObj.optString(argument.name()));
        }
        return argumentValues;
    }

    public static String BuildJsonWithArgumentValues(FUNCTION function, Hashtable<FUNCTION_ARGUMENT, String> argumentValues) {
        if (function == null) {
            return null;
        }
        try {
            JSONObject functionContent = new JSONObject();
            functionContent.put(Util.FUNCTION_TAG_FLAVOR, Util.FUNCTION_TAG_DLNA);
            JSONObject content = new JSONObject();
            content.put(Util.FUNCTION_TAG_ACTION, function.name());
            if (!(function.getArguments() == null || function.getArguments().isEmpty())) {
                if (argumentValues == null || argumentValues.isEmpty()) {
                    return null;
                }
                Iterator it = function.getArguments().iterator();
                while (it.hasNext()) {
                    FUNCTION_ARGUMENT argument = (FUNCTION_ARGUMENT) it.next();
                    if (argument == null) {
                        return null;
                    }
                    String argumentValue = (String) argumentValues.get(argument);
                    if (argumentValue == null) {
                        return null;
                    }
                    content.put(argument.name(), argumentValue);
                }
            }
            functionContent.put(Util.FUNCTION_TAG_CONTENT, content);
            return functionContent.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}

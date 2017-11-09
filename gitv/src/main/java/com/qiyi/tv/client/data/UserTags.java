package com.qiyi.tv.client.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UserTags implements Parcelable, Serializable {
    public static final Creator<UserTags> CREATOR = new Creator<UserTags>() {
        public final UserTags createFromParcel(Parcel parcel) {
            Map readHashMap = parcel.readHashMap(HashMap.class.getClassLoader());
            UserTags userTags = new UserTags();
            userTags.mMap.putAll(readHashMap);
            return userTags;
        }

        public final UserTags[] newArray(int size) {
            return new UserTags[size];
        }
    };
    private static final String TAG = "UserTags";
    private static final long serialVersionUID = 1;
    private final HashMap<String, Object> mMap = new HashMap();

    public Map<String, Object> getMap() {
        return this.mMap;
    }

    public void copy(UserTags tags) {
        this.mMap.clear();
        if (tags != null) {
            this.mMap.putAll(tags.getMap());
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int i) {
        dest.writeMap(this.mMap);
    }

    public Set<String> keySet() {
        return this.mMap.keySet();
    }

    public Object get(String key) {
        return this.mMap.get(key);
    }

    public int size() {
        return this.mMap.size();
    }

    public String getString(String key) {
        Object obj = this.mMap.get(key);
        if (obj == null) {
            return null;
        }
        try {
            return (String) obj;
        } catch (ClassCastException e) {
            typeWarning(key, obj, "String", e);
            return null;
        }
    }

    public ArrayList<String> getStringArrayList(String key) {
        Object obj = this.mMap.get(key);
        if (obj == null || !(obj instanceof ArrayList)) {
            return null;
        }
        try {
            return (ArrayList) obj;
        } catch (ClassCastException e) {
            typeWarning(key, obj, "ArrayList<String>", e);
            return null;
        }
    }

    public void putStringArrayList(String key, ArrayList<String> value) {
        this.mMap.put(key, value);
    }

    public void putString(String key, String value) {
        this.mMap.put(key, value);
    }

    public void putInt(String key, int value) {
        this.mMap.put(key, Integer.valueOf(value));
    }

    private void typeWarning(String key, Object value, String className, Object defaultValue, ClassCastException e) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Key ");
        stringBuilder.append(key);
        stringBuilder.append(" expected ");
        stringBuilder.append(className);
        stringBuilder.append(" but value was a ");
        stringBuilder.append(value.getClass().getName());
        stringBuilder.append(".  The default value ");
        stringBuilder.append(defaultValue);
        stringBuilder.append(" was returned.");
        Log.w(TAG, stringBuilder.toString());
        Log.w(TAG, "Attempt to cast generated internal exception:", e);
    }

    private void typeWarning(String key, Object value, String className, ClassCastException e) {
        typeWarning(key, value, className, "<null>", e);
    }
}

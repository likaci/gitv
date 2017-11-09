package com.gala.tv.voice.service;

import android.content.Context;
import com.gala.tv.voice.core.Log;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ResourceSemanticTranslator implements ISemanticTranslator {
    private static final List<String[]> a = new ArrayList();
    private static boolean f411a = true;
    private final Context f412a;
    private Filter f413a;
    private final Class f414a;

    public interface Filter {
        boolean access(Field field);
    }

    public ResourceSemanticTranslator(Context context, Class cls, Filter filter) {
        this.f412a = context;
        this.f414a = cls;
        this.f413a = filter;
    }

    public synchronized void prepare() {
        Class cls = this.f414a;
        Field[] declaredFields = cls.getDeclaredFields();
        int length = declaredFields.length;
        int i = 0;
        while (i < length) {
            try {
                Field field = declaredFields[i];
                if (this.f413a != null && this.f413a.access(field)) {
                    a.add(this.f412a.getResources().getStringArray(field.getInt(cls)));
                }
                i++;
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e2) {
                e2.printStackTrace();
            }
        }
        if (f411a) {
            for (int i2 = 0; i2 < a.size(); i2++) {
                for (String str : (String[]) a.get(i2)) {
                    Log.d("ResourceSemanticTranslator", "standStr(" + i2 + ") " + str);
                }
            }
        }
    }

    public synchronized String getStandard(String str) {
        String str2;
        if (f411a) {
            Log.d("ResourceSemanticTranslator", "getStandardKeywords(" + str + ")");
        }
        int size = a.size();
        int i = 0;
        Object obj = null;
        String str3 = str;
        while (i < size) {
            Object obj2;
            String[] strArr = (String[]) a.get(i);
            for (String equals : strArr) {
                if (equals.equals(str)) {
                    str2 = strArr[0];
                    obj2 = 1;
                    break;
                }
            }
            str2 = str3;
            obj2 = obj;
            if (obj2 != null) {
                break;
            }
            i++;
            obj = obj2;
            str3 = str2;
        }
        str2 = str3;
        if (f411a) {
            Log.d("ResourceSemanticTranslator", "getStandardKeywords(" + str + ") return " + str2);
        }
        return str2;
    }
}

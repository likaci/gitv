package com.gala.imageprovider.private;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import com.gala.afinal.FinalDb;
import com.gala.afinal.exception.DbException;
import com.gala.download.base.FileRequest;
import com.gala.imageprovider.base.ImageRequest;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import org.cybergarage.upnp.std.av.server.object.SearchCriteria;

@TargetApi(16)
public final class b {
    private HashMap<String, Object> a = new HashMap();

    public static boolean a(String... strArr) {
        if (strArr == null) {
            return true;
        }
        for (String str : strArr) {
            if (str != null && !str.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private static String c(String str) {
        int i = 0;
        char[] cArr = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] bytes = str.getBytes();
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(bytes);
            byte[] digest = instance.digest();
            int length = digest.length;
            char[] cArr2 = new char[(length << 1)];
            int i2 = 0;
            while (i < length) {
                byte b = digest[i];
                int i3 = i2 + 1;
                cArr2[i2] = cArr[(b >>> 4) & 15];
                i2 = i3 + 1;
                cArr2[i3] = cArr[b & 15];
                i++;
            }
            return new String(cArr2);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap a(Bitmap bitmap, float f) {
        if (G.a) {
            G.a("ImageProvider/BitmapTool", ">>>>> toRoundBitmap(): radius=" + f);
        }
        Config config = bitmap.getConfig();
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, f, f, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        bitmap.recycle();
        if (createBitmap.getConfig() == config) {
            return createBitmap;
        }
        Bitmap createBitmap2 = Bitmap.createBitmap(createBitmap.getWidth(), createBitmap.getHeight(), config);
        new Canvas(createBitmap2).drawBitmap(createBitmap, 0.0f, 0.0f, new Paint(a(false, false, config)));
        return createBitmap2;
    }

    public static String a(FileRequest fileRequest) {
        if (fileRequest != null) {
            if (!a(fileRequest.getUrl())) {
                String url = fileRequest.getUrl();
                if (q.a().isEnableFullPathCacheKey()) {
                    url = url.replaceAll("://", "_").replaceAll("/", "_");
                    if (G.a) {
                        G.a("IDownloader/FileTool", ">>>>> getFileNameFromRequest: formatted url=" + url);
                    }
                    url = c(url);
                } else {
                    url = url.substring(url.lastIndexOf(47) + 1, url.length());
                }
                int lastIndexOf = url.lastIndexOf(46);
                if (lastIndexOf >= 0) {
                    url = url.substring(0, lastIndexOf) + url.substring(lastIndexOf);
                }
                if (!G.a) {
                    return url;
                }
                G.a("IDownloader/FileTool", ">>>>> getFileNameFromRequest() returns " + url);
                return url;
            }
        }
        return null;
    }

    public static String a() {
        Reader inputStreamReader;
        Throwable e;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            char[] cArr = new char[20];
            inputStreamReader = new InputStreamReader(new FileInputStream("/sys/class/net/eth0/address"));
            while (true) {
                try {
                    int read = inputStreamReader.read(cArr);
                    if (read == -1) {
                        try {
                            break;
                        } catch (Throwable e2) {
                            G.a("Downloader/SysUtils", ">>>>>getEtherMac: exception happened when close:", e2);
                        }
                    } else if (read != cArr.length || cArr[cArr.length - 1] == '\r') {
                        for (int i = 0; i < read; i++) {
                            if (cArr[i] != '\r') {
                                stringBuffer.append(cArr[i]);
                            }
                        }
                    }
                } catch (Exception e3) {
                    e2 = e3;
                }
            }
            inputStreamReader.close();
        } catch (Exception e4) {
            e2 = e4;
            inputStreamReader = null;
            try {
                G.a("Downloader/SysUtils", ">>>>>getEtherMac: exception happened:", e2);
                if (inputStreamReader != null) {
                    try {
                        inputStreamReader.close();
                    } catch (Throwable e22) {
                        G.a("Downloader/SysUtils", ">>>>>getEtherMac: exception happened when close:", e22);
                    }
                }
                return stringBuffer.toString().trim();
            } catch (Throwable th) {
                e22 = th;
                if (inputStreamReader != null) {
                    try {
                        inputStreamReader.close();
                    } catch (Throwable e5) {
                        G.a("Downloader/SysUtils", ">>>>>getEtherMac: exception happened when close:", e5);
                    }
                }
                throw e22;
            }
        } catch (Throwable th2) {
            e22 = th2;
            inputStreamReader = null;
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            throw e22;
        }
        return stringBuffer.toString().trim();
    }

    public final Object m3a(String str) {
        return this.a.get(str);
    }

    public static <T> T a(Cursor cursor, Class<T> cls, FinalDb finalDb) {
        if (cursor != null) {
            try {
                k a = k.a(cls);
                int columnCount = cursor.getColumnCount();
                if (columnCount > 0) {
                    T newInstance = cls.newInstance();
                    for (int i = 0; i < columnCount; i++) {
                        String columnName = cursor.getColumnName(i);
                        j jVar = (j) a.f19a.get(columnName);
                        if (jVar != null) {
                            jVar.a(newInstance, cursor.getString(i));
                        } else if (a.a().a().equals(columnName)) {
                            a.a().a(newInstance, cursor.getString(i));
                        }
                    }
                    for (i iVar : a.b.values()) {
                        if (iVar.b() == d.class) {
                            iVar.a();
                            iVar.a(newInstance, new d());
                        }
                    }
                    for (h hVar : a.c.values()) {
                        if (hVar.b() == c.class) {
                            c cVar = new c(newInstance, cls, hVar.a(), finalDb);
                            cVar.b(Integer.valueOf(cursor.getInt(cursor.getColumnIndex(hVar.a()))));
                            hVar.a(newInstance, cVar);
                        }
                    }
                    return newInstance;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Bitmap a(Bitmap bitmap, int i, int i2, String str) {
        if (G.a) {
            G.a("ImageProvider/BitmapTool", ">>>>> scaleBitmap: original=" + G.a(bitmap) + ", target w/h=" + i + "/" + i2 + " ,  url:" + str);
        }
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width == i && height == i2) {
            return bitmap;
        }
        Rect rect;
        float f = ((float) i) / ((float) i2);
        int round;
        if (f >= ((float) width) / ((float) height)) {
            round = Math.round(((float) width) / f);
            rect = new Rect(0, Math.round(((float) (height - round)) / 2.0f), width, Math.round((((float) height) / 2.0f) + (((float) round) / 2.0f)));
        } else {
            round = Math.round(f * ((float) height));
            rect = new Rect(Math.round(((float) (width - round)) / 2.0f), 0, Math.round((((float) width) / 2.0f) + (((float) round) / 2.0f)), height);
        }
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, bitmap.getConfig());
        new Canvas(createBitmap).drawBitmap(bitmap, rect, new Rect(0, 0, createBitmap.getWidth(), createBitmap.getHeight()), new Paint(a(true, true, bitmap.getConfig())));
        if (G.a) {
            G.a("ImageProvider/BitmapTool", ">>>>> scaleBitmap: returned bitmap=" + G.a(createBitmap) + " ,  url:" + str);
        }
        return createBitmap;
    }

    public static String a(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
            WifiInfo connectionInfo = wifiManager == null ? null : wifiManager.getConnectionInfo();
            if (connectionInfo != null) {
                return connectionInfo.getMacAddress();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<g> m2a(Object obj) {
        List<g> arrayList = new ArrayList();
        k a = k.a(obj.getClass());
        Object a2 = a.a().a(obj);
        if (!((a2 instanceof Integer) || !(a2 instanceof String) || a2 == null)) {
            arrayList.add(new g(a.a().a(), a2));
        }
        for (j a3 : a.f19a.values()) {
            g a4 = a(a3, obj);
            if (a4 != null) {
                arrayList.add(a4);
            }
        }
        for (h a5 : a.c.values()) {
            a4 = a(a5, obj);
            if (a4 != null) {
                arrayList.add(a4);
            }
        }
        return arrayList;
    }

    private static int a(boolean z, boolean z2, Config config) {
        int i = 0;
        if (z) {
            i = 1;
        }
        if (z2) {
            i |= 2;
        }
        if (config != Config.ARGB_8888) {
            return i | 4;
        }
        return i;
    }

    public static b a(Cursor cursor) {
        if (cursor == null || cursor.getColumnCount() <= 0) {
            return null;
        }
        b bVar = new b();
        int columnCount = cursor.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            bVar.a.put(cursor.getColumnName(i), cursor.getString(i));
        }
        return bVar;
    }

    public static <T> T a(b bVar, Class<?> cls) {
        if (bVar != null) {
            HashMap hashMap = bVar.a;
            try {
                T newInstance = cls.newInstance();
                for (Entry entry : hashMap.entrySet()) {
                    String str = (String) entry.getKey();
                    k a = k.a(cls);
                    j jVar = (j) a.f19a.get(str);
                    if (jVar != null) {
                        jVar.a(newInstance, entry.getValue() == null ? null : entry.getValue().toString());
                    } else if (a.a().a().equals(str)) {
                        a.a().a(newInstance, entry.getValue() == null ? null : entry.getValue().toString());
                    }
                }
                return newInstance;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String a(ImageRequest imageRequest) {
        StringBuilder stringBuilder = new StringBuilder(imageRequest.getUrl());
        if (imageRequest.getTargetHeight() > 0 && imageRequest.getTargetWidth() > 0) {
            stringBuilder.append("_").append(imageRequest.getTargetWidth()).append("_").append(imageRequest.getTargetHeight());
        }
        return stringBuilder.toString();
    }

    private static String a(String str) {
        return "DELETE FROM " + str;
    }

    public static e a(Object obj) {
        k a = k.a(obj.getClass());
        f a2 = a.a();
        Object a3 = a2.a(obj);
        if (a3 == null) {
            throw new DbException("getDeleteSQL:" + obj.getClass() + " id value is null");
        }
        StringBuffer stringBuffer = new StringBuffer(a(a.a()));
        stringBuffer.append(" WHERE ").append(a2.a()).append("=?");
        e eVar = new e();
        eVar.a(stringBuffer.toString());
        eVar.a(a3);
        return eVar;
    }

    public static e a(Class<?> cls, Object obj) {
        k a = k.a(cls);
        f a2 = a.a();
        if (obj == null) {
            throw new DbException("getDeleteSQL:idValue is null");
        }
        StringBuffer stringBuffer = new StringBuffer(a(a.a()));
        stringBuffer.append(" WHERE ").append(a2.a()).append("=?");
        e eVar = new e();
        eVar.a(stringBuffer.toString());
        eVar.a(obj);
        return eVar;
    }

    public static String a(Class<?> cls, String str) {
        StringBuffer stringBuffer = new StringBuffer(a(k.a(cls).a()));
        if (!TextUtils.isEmpty(str)) {
            stringBuffer.append(" WHERE ");
            stringBuffer.append(str);
        }
        return stringBuffer.toString();
    }

    private static String b(String str) {
        return new StringBuffer("SELECT * FROM ").append(str).toString();
    }

    public static String m1a(Class<?> cls, Object obj) {
        k a = k.a(cls);
        StringBuffer stringBuffer = new StringBuffer(b(a.a()));
        stringBuffer.append(" WHERE ");
        StringBuffer append = new StringBuffer(a.a().a()).append(SearchCriteria.EQ);
        if ((obj instanceof String) || (obj instanceof Date) || (obj instanceof java.sql.Date)) {
            append.append("'").append(obj).append("'");
        } else {
            append.append(obj);
        }
        stringBuffer.append(append.toString());
        return stringBuffer.toString();
    }

    public static e b(Class<?> cls, Object obj) {
        k a = k.a(cls);
        StringBuffer stringBuffer = new StringBuffer(b(a.a()));
        stringBuffer.append(" WHERE ").append(a.a().a()).append("=?");
        e eVar = new e();
        eVar.a(stringBuffer.toString());
        eVar.a(obj);
        return eVar;
    }

    public static String a(Class<?> cls) {
        return b(k.a(cls).a());
    }

    public static String b(Class<?> cls, String str) {
        StringBuffer stringBuffer = new StringBuffer(b(k.a(cls).a()));
        if (!TextUtils.isEmpty(str)) {
            stringBuffer.append(" WHERE ").append(str);
        }
        return stringBuffer.toString();
    }

    public static e b(Object obj) {
        k a = k.a(obj.getClass());
        Object a2 = a.a().a(obj);
        if (a2 == null) {
            throw new DbException("this entity[" + obj.getClass() + "]'s id value is null");
        }
        List<g> arrayList = new ArrayList();
        for (j a3 : a.f19a.values()) {
            g a4 = a(a3, obj);
            if (a4 != null) {
                arrayList.add(a4);
            }
        }
        for (h a5 : a.c.values()) {
            a4 = a(a5, obj);
            if (a4 != null) {
                arrayList.add(a4);
            }
        }
        if (arrayList.size() == 0) {
            return null;
        }
        e eVar = new e();
        StringBuffer stringBuffer = new StringBuffer("UPDATE ");
        stringBuffer.append(a.a());
        stringBuffer.append(" SET ");
        for (g a42 : arrayList) {
            stringBuffer.append(a42.a()).append("=?,");
            eVar.a(a42.a());
        }
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        stringBuffer.append(" WHERE ").append(a.a().a()).append("=?");
        eVar.a(a2);
        eVar.a(stringBuffer.toString());
        return eVar;
    }

    public static e a(Object obj, String str) {
        k a = k.a(obj.getClass());
        List<g> arrayList = new ArrayList();
        for (j a2 : a.f19a.values()) {
            g a3 = a(a2, obj);
            if (a3 != null) {
                arrayList.add(a3);
            }
        }
        for (h a4 : a.c.values()) {
            a3 = a(a4, obj);
            if (a3 != null) {
                arrayList.add(a3);
            }
        }
        if (arrayList.size() == 0) {
            throw new DbException("this entity[" + obj.getClass() + "] has no property");
        }
        e eVar = new e();
        StringBuffer stringBuffer = new StringBuffer("UPDATE ");
        stringBuffer.append(a.a());
        stringBuffer.append(" SET ");
        for (g a32 : arrayList) {
            stringBuffer.append(a32.a()).append("=?,");
            eVar.a(a32.a());
        }
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        if (!TextUtils.isEmpty(str)) {
            stringBuffer.append(" WHERE ").append(str);
        }
        eVar.a(stringBuffer.toString());
        return eVar;
    }

    private static g a(j jVar, Object obj) {
        String a = jVar.a();
        Object a2 = jVar.a(obj);
        if (a2 != null) {
            return new g(a, a2);
        }
        if (jVar.b() == null || jVar.b().trim().length() == 0) {
            return null;
        }
        return new g(a, jVar.b());
    }

    private static g a(h hVar, Object obj) {
        String a = hVar.a();
        Object a2 = hVar.a(obj);
        if (a2 != null) {
            Object a3;
            if (a2.getClass() == c.class) {
                a3 = k.a(hVar.a()).a().a(((c) a2).a());
            } else {
                a3 = k.a(a2.getClass()).a().a(a2);
            }
            if (!(a == null || a3 == null)) {
                return new g(a, a3);
            }
        }
        return null;
    }
}

package com.gala.imageprovider.p000private;

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
public final class C0126b {
    private HashMap<String, Object> f545a = new HashMap();

    public static boolean m307a(String... strArr) {
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

    private static String m312c(String str) {
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

    public static Bitmap m288a(Bitmap bitmap, float f) {
        if (C0123G.f541a) {
            C0123G.m279a("ImageProvider/BitmapTool", ">>>>> toRoundBitmap(): radius=" + f);
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
        new Canvas(createBitmap2).drawBitmap(createBitmap, 0.0f, 0.0f, new Paint(C0126b.m287a(false, false, config)));
        return createBitmap2;
    }

    public static String m300a(FileRequest fileRequest) {
        if (fileRequest != null) {
            if (!C0126b.m307a(fileRequest.getUrl())) {
                String url = fileRequest.getUrl();
                if (C0142q.m363a().isEnableFullPathCacheKey()) {
                    url = url.replaceAll("://", "_").replaceAll("/", "_");
                    if (C0123G.f541a) {
                        C0123G.m279a("IDownloader/FileTool", ">>>>> getFileNameFromRequest: formatted url=" + url);
                    }
                    url = C0126b.m312c(url);
                } else {
                    url = url.substring(url.lastIndexOf(47) + 1, url.length());
                }
                int lastIndexOf = url.lastIndexOf(46);
                if (lastIndexOf >= 0) {
                    url = url.substring(0, lastIndexOf) + url.substring(lastIndexOf);
                }
                if (!C0123G.f541a) {
                    return url;
                }
                C0123G.m279a("IDownloader/FileTool", ">>>>> getFileNameFromRequest() returns " + url);
                return url;
            }
        }
        return null;
    }

    public static String m298a() {
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
                            C0123G.m280a("Downloader/SysUtils", ">>>>>getEtherMac: exception happened when close:", e2);
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
                C0123G.m280a("Downloader/SysUtils", ">>>>>getEtherMac: exception happened:", e2);
                if (inputStreamReader != null) {
                    try {
                        inputStreamReader.close();
                    } catch (Throwable e22) {
                        C0123G.m280a("Downloader/SysUtils", ">>>>>getEtherMac: exception happened when close:", e22);
                    }
                }
                return stringBuffer.toString().trim();
            } catch (Throwable th) {
                e22 = th;
                if (inputStreamReader != null) {
                    try {
                        inputStreamReader.close();
                    } catch (Throwable e5) {
                        C0123G.m280a("Downloader/SysUtils", ">>>>>getEtherMac: exception happened when close:", e5);
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

    public final Object m313a(String str) {
        return this.f545a.get(str);
    }

    public static <T> T m296a(Cursor cursor, Class<T> cls, FinalDb finalDb) {
        if (cursor != null) {
            try {
                C0135k a = C0135k.m341a(cls);
                int columnCount = cursor.getColumnCount();
                if (columnCount > 0) {
                    T newInstance = cls.newInstance();
                    for (int i = 0; i < columnCount; i++) {
                        String columnName = cursor.getColumnName(i);
                        C0130j c0130j = (C0130j) a.f568a.get(columnName);
                        if (c0130j != null) {
                            c0130j.m326a(newInstance, cursor.getString(i));
                        } else if (a.m342a().mo652a().equals(columnName)) {
                            a.m342a().m326a(newInstance, cursor.getString(i));
                        }
                    }
                    for (C0134i c0134i : a.f570b.values()) {
                        if (c0134i.m330b() == C0128d.class) {
                            c0134i.mo652a();
                            c0134i.m326a(newInstance, new C0128d());
                        }
                    }
                    for (C0133h c0133h : a.f571c.values()) {
                        if (c0133h.m330b() == C0127c.class) {
                            C0127c c0127c = new C0127c(newInstance, cls, c0133h.mo652a(), finalDb);
                            c0127c.m317b(Integer.valueOf(cursor.getInt(cursor.getColumnIndex(c0133h.mo652a()))));
                            c0133h.m326a(newInstance, c0127c);
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

    public static Bitmap m289a(Bitmap bitmap, int i, int i2, String str) {
        if (C0123G.f541a) {
            C0123G.m279a("ImageProvider/BitmapTool", ">>>>> scaleBitmap: original=" + C0123G.m281a(bitmap) + ", target w/h=" + i + "/" + i2 + " ,  url:" + str);
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
        new Canvas(createBitmap).drawBitmap(bitmap, rect, new Rect(0, 0, createBitmap.getWidth(), createBitmap.getHeight()), new Paint(C0126b.m287a(true, true, bitmap.getConfig())));
        if (C0123G.f541a) {
            C0123G.m279a("ImageProvider/BitmapTool", ">>>>> scaleBitmap: returned bitmap=" + C0123G.m281a(createBitmap) + " ,  url:" + str);
        }
        return createBitmap;
    }

    public static String m299a(Context context) {
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

    public static List<C0132g> m306a(Object obj) {
        List<C0132g> arrayList = new ArrayList();
        C0135k a = C0135k.m341a(obj.getClass());
        Object a2 = a.m342a().m324a(obj);
        if (!((a2 instanceof Integer) || !(a2 instanceof String) || a2 == null)) {
            arrayList.add(new C0132g(a.m342a().mo652a(), a2));
        }
        for (C0130j a3 : a.f568a.values()) {
            C0132g a4 = C0126b.m295a(a3, obj);
            if (a4 != null) {
                arrayList.add(a4);
            }
        }
        for (C0133h a5 : a.f571c.values()) {
            a4 = C0126b.m294a(a5, obj);
            if (a4 != null) {
                arrayList.add(a4);
            }
        }
        return arrayList;
    }

    private static int m287a(boolean z, boolean z2, Config config) {
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

    public static C0126b m290a(Cursor cursor) {
        if (cursor == null || cursor.getColumnCount() <= 0) {
            return null;
        }
        C0126b c0126b = new C0126b();
        int columnCount = cursor.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            c0126b.f545a.put(cursor.getColumnName(i), cursor.getString(i));
        }
        return c0126b;
    }

    public static <T> T m297a(C0126b c0126b, Class<?> cls) {
        if (c0126b != null) {
            HashMap hashMap = c0126b.f545a;
            try {
                T newInstance = cls.newInstance();
                for (Entry entry : hashMap.entrySet()) {
                    String str = (String) entry.getKey();
                    C0135k a = C0135k.m341a(cls);
                    C0130j c0130j = (C0130j) a.f568a.get(str);
                    if (c0130j != null) {
                        c0130j.m326a(newInstance, entry.getValue() == null ? null : entry.getValue().toString());
                    } else if (a.m342a().mo652a().equals(str)) {
                        a.m342a().m326a(newInstance, entry.getValue() == null ? null : entry.getValue().toString());
                    }
                }
                return newInstance;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String m301a(ImageRequest imageRequest) {
        StringBuilder stringBuilder = new StringBuilder(imageRequest.getUrl());
        if (imageRequest.getTargetHeight() > 0 && imageRequest.getTargetWidth() > 0) {
            stringBuilder.append("_").append(imageRequest.getTargetWidth()).append("_").append(imageRequest.getTargetHeight());
        }
        return stringBuilder.toString();
    }

    private static String m305a(String str) {
        return "DELETE FROM " + str;
    }

    public static C0129e m292a(Object obj) {
        C0135k a = C0135k.m341a(obj.getClass());
        C0131f a2 = a.m342a();
        Object a3 = a2.m324a(obj);
        if (a3 == null) {
            throw new DbException("getDeleteSQL:" + obj.getClass() + " id value is null");
        }
        StringBuffer stringBuffer = new StringBuffer(C0126b.m305a(a.m342a()));
        stringBuffer.append(" WHERE ").append(a2.mo652a()).append("=?");
        C0129e c0129e = new C0129e();
        c0129e.m321a(stringBuffer.toString());
        c0129e.m320a(a3);
        return c0129e;
    }

    public static C0129e m291a(Class<?> cls, Object obj) {
        C0135k a = C0135k.m341a(cls);
        C0131f a2 = a.m342a();
        if (obj == null) {
            throw new DbException("getDeleteSQL:idValue is null");
        }
        StringBuffer stringBuffer = new StringBuffer(C0126b.m305a(a.m342a()));
        stringBuffer.append(" WHERE ").append(a2.mo652a()).append("=?");
        C0129e c0129e = new C0129e();
        c0129e.m321a(stringBuffer.toString());
        c0129e.m320a(obj);
        return c0129e;
    }

    public static String m304a(Class<?> cls, String str) {
        StringBuffer stringBuffer = new StringBuffer(C0126b.m305a(C0135k.m341a(cls).m342a()));
        if (!TextUtils.isEmpty(str)) {
            stringBuffer.append(" WHERE ");
            stringBuffer.append(str);
        }
        return stringBuffer.toString();
    }

    private static String m311b(String str) {
        return new StringBuffer("SELECT * FROM ").append(str).toString();
    }

    public static String m303a(Class<?> cls, Object obj) {
        C0135k a = C0135k.m341a(cls);
        StringBuffer stringBuffer = new StringBuffer(C0126b.m311b(a.m342a()));
        stringBuffer.append(" WHERE ");
        StringBuffer append = new StringBuffer(a.m342a().mo652a()).append(SearchCriteria.EQ);
        if ((obj instanceof String) || (obj instanceof Date) || (obj instanceof java.sql.Date)) {
            append.append("'").append(obj).append("'");
        } else {
            append.append(obj);
        }
        stringBuffer.append(append.toString());
        return stringBuffer.toString();
    }

    public static C0129e m308b(Class<?> cls, Object obj) {
        C0135k a = C0135k.m341a(cls);
        StringBuffer stringBuffer = new StringBuffer(C0126b.m311b(a.m342a()));
        stringBuffer.append(" WHERE ").append(a.m342a().mo652a()).append("=?");
        C0129e c0129e = new C0129e();
        c0129e.m321a(stringBuffer.toString());
        c0129e.m320a(obj);
        return c0129e;
    }

    public static String m302a(Class<?> cls) {
        return C0126b.m311b(C0135k.m341a(cls).m342a());
    }

    public static String m310b(Class<?> cls, String str) {
        StringBuffer stringBuffer = new StringBuffer(C0126b.m311b(C0135k.m341a(cls).m342a()));
        if (!TextUtils.isEmpty(str)) {
            stringBuffer.append(" WHERE ").append(str);
        }
        return stringBuffer.toString();
    }

    public static C0129e m309b(Object obj) {
        C0135k a = C0135k.m341a(obj.getClass());
        Object a2 = a.m342a().m324a(obj);
        if (a2 == null) {
            throw new DbException("this entity[" + obj.getClass() + "]'s id value is null");
        }
        List<C0132g> arrayList = new ArrayList();
        for (C0130j a3 : a.f568a.values()) {
            C0132g a4 = C0126b.m295a(a3, obj);
            if (a4 != null) {
                arrayList.add(a4);
            }
        }
        for (C0133h a5 : a.f571c.values()) {
            a4 = C0126b.m294a(a5, obj);
            if (a4 != null) {
                arrayList.add(a4);
            }
        }
        if (arrayList.size() == 0) {
            return null;
        }
        C0129e c0129e = new C0129e();
        StringBuffer stringBuffer = new StringBuffer("UPDATE ");
        stringBuffer.append(a.m342a());
        stringBuffer.append(" SET ");
        for (C0132g a42 : arrayList) {
            stringBuffer.append(a42.m335a()).append("=?,");
            c0129e.m320a(a42.m335a());
        }
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        stringBuffer.append(" WHERE ").append(a.m342a().mo652a()).append("=?");
        c0129e.m320a(a2);
        c0129e.m321a(stringBuffer.toString());
        return c0129e;
    }

    public static C0129e m293a(Object obj, String str) {
        C0135k a = C0135k.m341a(obj.getClass());
        List<C0132g> arrayList = new ArrayList();
        for (C0130j a2 : a.f568a.values()) {
            C0132g a3 = C0126b.m295a(a2, obj);
            if (a3 != null) {
                arrayList.add(a3);
            }
        }
        for (C0133h a4 : a.f571c.values()) {
            a3 = C0126b.m294a(a4, obj);
            if (a3 != null) {
                arrayList.add(a3);
            }
        }
        if (arrayList.size() == 0) {
            throw new DbException("this entity[" + obj.getClass() + "] has no property");
        }
        C0129e c0129e = new C0129e();
        StringBuffer stringBuffer = new StringBuffer("UPDATE ");
        stringBuffer.append(a.m342a());
        stringBuffer.append(" SET ");
        for (C0132g a32 : arrayList) {
            stringBuffer.append(a32.m335a()).append("=?,");
            c0129e.m320a(a32.m335a());
        }
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        if (!TextUtils.isEmpty(str)) {
            stringBuffer.append(" WHERE ").append(str);
        }
        c0129e.m321a(stringBuffer.toString());
        return c0129e;
    }

    private static C0132g m295a(C0130j c0130j, Object obj) {
        String a = c0130j.mo652a();
        Object a2 = c0130j.m324a(obj);
        if (a2 != null) {
            return new C0132g(a, a2);
        }
        if (c0130j.m330b() == null || c0130j.m330b().trim().length() == 0) {
            return null;
        }
        return new C0132g(a, c0130j.m330b());
    }

    private static C0132g m294a(C0133h c0133h, Object obj) {
        String a = c0133h.mo652a();
        Object a2 = c0133h.m324a(obj);
        if (a2 != null) {
            Object a3;
            if (a2.getClass() == C0127c.class) {
                a3 = C0135k.m341a(c0133h.mo652a()).m342a().m324a(((C0127c) a2).m314a());
            } else {
                a3 = C0135k.m341a(a2.getClass()).m342a().m324a(a2);
            }
            if (!(a == null || a3 == null)) {
                return new C0132g(a, a3);
            }
        }
        return null;
    }
}

package com.gala.afinal.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

public class PreferencesCookieStore implements CookieStore {
    private static final String COOKIE_NAME_PREFIX = "cookie_";
    private static final String COOKIE_NAME_STORE = "names";
    private static final String COOKIE_PREFS = "CookiePrefsFile";
    private final SharedPreferences cookiePrefs;
    private final ConcurrentHashMap<String, Cookie> cookies = new ConcurrentHashMap();

    public class SerializableCookie implements Serializable {
        private static final long serialVersionUID = 6374381828722046732L;
        private transient BasicClientCookie clientCookie;
        private final transient Cookie cookie;

        public SerializableCookie(Cookie cookie) {
            this.cookie = cookie;
        }

        public Cookie getCookie() {
            Cookie cookie = this.cookie;
            if (this.clientCookie != null) {
                return this.clientCookie;
            }
            return cookie;
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.writeObject(this.cookie.getName());
            out.writeObject(this.cookie.getValue());
            out.writeObject(this.cookie.getComment());
            out.writeObject(this.cookie.getDomain());
            out.writeObject(this.cookie.getExpiryDate());
            out.writeObject(this.cookie.getPath());
            out.writeInt(this.cookie.getVersion());
            out.writeBoolean(this.cookie.isSecure());
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            this.clientCookie = new BasicClientCookie((String) in.readObject(), (String) in.readObject());
            this.clientCookie.setComment((String) in.readObject());
            this.clientCookie.setDomain((String) in.readObject());
            this.clientCookie.setExpiryDate((Date) in.readObject());
            this.clientCookie.setPath((String) in.readObject());
            this.clientCookie.setVersion(in.readInt());
            this.clientCookie.setSecure(in.readBoolean());
        }
    }

    public PreferencesCookieStore(Context context) {
        int i = 0;
        this.cookiePrefs = context.getSharedPreferences(COOKIE_PREFS, 0);
        String string = this.cookiePrefs.getString(COOKIE_NAME_STORE, null);
        if (string != null) {
            String[] split = TextUtils.split(string, ",");
            int length = split.length;
            while (i < length) {
                String str = split[i];
                String string2 = this.cookiePrefs.getString(new StringBuilder(COOKIE_NAME_PREFIX).append(str).toString(), null);
                if (string2 != null) {
                    Cookie decodeCookie = decodeCookie(string2);
                    if (decodeCookie != null) {
                        this.cookies.put(str, decodeCookie);
                    }
                }
                i++;
            }
            clearExpired(new Date());
        }
    }

    public void addCookie(Cookie cookie) {
        String name = cookie.getName();
        if (cookie.isExpired(new Date())) {
            this.cookies.remove(name);
        } else {
            this.cookies.put(name, cookie);
        }
        Editor edit = this.cookiePrefs.edit();
        edit.putString(COOKIE_NAME_STORE, TextUtils.join(",", this.cookies.keySet()));
        edit.putString(new StringBuilder(COOKIE_NAME_PREFIX).append(name).toString(), encodeCookie(new SerializableCookie(cookie)));
        edit.commit();
    }

    public void clear() {
        this.cookies.clear();
        Editor edit = this.cookiePrefs.edit();
        for (String append : this.cookies.keySet()) {
            edit.remove(new StringBuilder(COOKIE_NAME_PREFIX).append(append).toString());
        }
        edit.remove(COOKIE_NAME_STORE);
        edit.commit();
    }

    public boolean clearExpired(Date date) {
        Editor edit = this.cookiePrefs.edit();
        boolean z = false;
        for (Entry entry : this.cookies.entrySet()) {
            boolean z2;
            String str = (String) entry.getKey();
            if (((Cookie) entry.getValue()).isExpired(date)) {
                this.cookies.remove(str);
                edit.remove(new StringBuilder(COOKIE_NAME_PREFIX).append(str).toString());
                z2 = true;
            } else {
                z2 = z;
            }
            z = z2;
        }
        if (z) {
            edit.putString(COOKIE_NAME_STORE, TextUtils.join(",", this.cookies.keySet()));
        }
        edit.commit();
        return z;
    }

    public List<Cookie> getCookies() {
        return new ArrayList(this.cookies.values());
    }

    protected String encodeCookie(SerializableCookie cookie) {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            new ObjectOutputStream(byteArrayOutputStream).writeObject(cookie);
            return byteArrayToHexString(byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            return null;
        }
    }

    protected Cookie decodeCookie(String cookieStr) {
        try {
            return ((SerializableCookie) new ObjectInputStream(new ByteArrayInputStream(hexStringToByteArray(cookieStr))).readObject()).getCookie();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected String byteArrayToHexString(byte[] b) {
        StringBuffer stringBuffer = new StringBuffer(b.length << 1);
        for (byte b2 : b) {
            int i = b2 & 255;
            if (i < 16) {
                stringBuffer.append('0');
            }
            stringBuffer.append(Integer.toHexString(i));
        }
        return stringBuffer.toString().toUpperCase();
    }

    protected byte[] hexStringToByteArray(String s) {
        int length = s.length();
        byte[] bArr = new byte[(length / 2)];
        for (int i = 0; i < length; i += 2) {
            bArr[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return bArr;
    }
}

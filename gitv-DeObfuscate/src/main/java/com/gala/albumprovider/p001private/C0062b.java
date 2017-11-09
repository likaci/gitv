package com.gala.albumprovider.p001private;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import com.gala.albumprovider.logic.source.SourceTool;
import com.gala.albumprovider.model.QChannel;
import com.gala.albumprovider.model.Tag;
import com.gala.albumprovider.util.USALog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class C0062b {
    private int f261a = -1;
    private final long f262a = 600;
    private C0061a f263a;
    private C0065e f264a;
    private C0066f f265a;
    private String f266a = "";
    private List<Tag> f267a;
    private Map<String, C0063c> f268a;
    private boolean f269a = false;
    private final long f270b = 300;
    private C0061a f271b;
    private boolean f272b = false;
    private long f273c = -1;
    private C0061a f274c;

    public C0062b(String str) {
        this.f266a = str;
        QChannel a = C0067g.m139a().m141a(str);
        if (a != null) {
            this.f265a = new C0066f(a.focus, a.recRes);
            this.f269a = a.hasRecommendList();
            this.f272b = a.hasPlayList();
        }
        this.f263a = new C0061a();
        this.f271b = new C0061a();
        this.f274c = new C0061a();
        this.f264a = new C0065e();
        this.f267a = new ArrayList();
        this.f268a = new HashMap();
        this.f261a = m101a();
    }

    public C0066f m106a() {
        return this.f265a;
    }

    public C0061a m103a() {
        return this.f263a;
    }

    public C0061a m112b() {
        return this.f271b;
    }

    public C0061a m113c() {
        return this.f274c;
    }

    public C0065e m105a() {
        return this.f264a;
    }

    public C0063c m104a(String str) {
        return (C0063c) this.f268a.get(str);
    }

    public void m109a(String str, C0063c c0063c) {
        this.f268a.put(str, c0063c);
    }

    public void m110a(List<Tag> list) {
        this.f267a.addAll(list);
    }

    public List<Tag> m107a() {
        return this.f267a;
    }

    public boolean m111a(String str) {
        int i;
        int i2;
        if (this.f269a) {
            i = 1;
        } else {
            i = 0;
        }
        if (this.f272b) {
            i++;
        }
        if (this.f267a != null && this.f267a.size() > 0) {
            i2 = i;
            for (Tag tag : this.f267a) {
                if (tag.getID().equals("11;sort")) {
                    i2++;
                }
                if (tag.getID().equals("4;sort")) {
                    i2++;
                }
                if (tag.getID().equals("0") || tag.getID().equals("")) {
                    i = i2 + 1;
                } else {
                    i = i2;
                }
                i2 = i;
            }
            i = i2;
        }
        if (this.f267a != null && this.f267a.size() > 0) {
            int i3;
            i2 = this.f261a - i;
            if (i + i2 < this.f267a.size()) {
                i3 = i2 + i;
            } else {
                i3 = this.f267a.size() - 1;
            }
            if (i3 >= i) {
                for (i2 = i; i2 <= i3; i2++) {
                    if (str.equals(((Tag) this.f267a.get(i2)).getID())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public int m101a() {
        long b = m100b();
        if (this.f261a == -1) {
            int i = 0;
            if (b > 600) {
                i = 20;
            }
            if (b <= 600 && b > 300) {
                i = 10;
            }
            if (b <= 300) {
                i = 6;
            }
            this.f261a = i;
        }
        USALog.m147d("cache tags size= " + this.f261a);
        return this.f261a;
    }

    private long m100b() {
        if (SourceTool.gContext == null) {
            return 0;
        }
        ActivityManager activityManager = (ActivityManager) SourceTool.gContext.getSystemService("activity");
        MemoryInfo memoryInfo = new MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        long j = (memoryInfo.availMem / 1024) / 1024;
        USALog.m147d("free memory = " + j);
        return j;
    }

    public long m102a() {
        return this.f273c;
    }

    public void m108a(long j) {
        this.f273c = j;
    }
}

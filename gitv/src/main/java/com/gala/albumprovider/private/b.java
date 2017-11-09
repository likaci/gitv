package com.gala.albumprovider.private;

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

public class b {
    private int a = -1;
    private final long f87a = 600;
    private a f88a;
    private e f89a;
    private f f90a;
    private String f91a = "";
    private List<Tag> f92a;
    private Map<String, c> f93a;
    private boolean f94a = false;
    private final long b = 300;
    private a f95b;
    private boolean f96b = false;
    private long c = -1;
    private a f97c;

    public b(String str) {
        this.f91a = str;
        QChannel a = g.a().a(str);
        if (a != null) {
            this.f90a = new f(a.focus, a.recRes);
            this.f94a = a.hasRecommendList();
            this.f96b = a.hasPlayList();
        }
        this.f88a = new a();
        this.f95b = new a();
        this.f97c = new a();
        this.f89a = new e();
        this.f92a = new ArrayList();
        this.f93a = new HashMap();
        this.a = a();
    }

    public f m21a() {
        return this.f90a;
    }

    public a m19a() {
        return this.f88a;
    }

    public a m24b() {
        return this.f95b;
    }

    public a c() {
        return this.f97c;
    }

    public e m20a() {
        return this.f89a;
    }

    public c a(String str) {
        return (c) this.f93a.get(str);
    }

    public void a(String str, c cVar) {
        this.f93a.put(str, cVar);
    }

    public void a(List<Tag> list) {
        this.f92a.addAll(list);
    }

    public List<Tag> m22a() {
        return this.f92a;
    }

    public boolean m23a(String str) {
        int i;
        int i2;
        if (this.f94a) {
            i = 1;
        } else {
            i = 0;
        }
        if (this.f96b) {
            i++;
        }
        if (this.f92a != null && this.f92a.size() > 0) {
            i2 = i;
            for (Tag tag : this.f92a) {
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
        if (this.f92a != null && this.f92a.size() > 0) {
            int i3;
            i2 = this.a - i;
            if (i + i2 < this.f92a.size()) {
                i3 = i2 + i;
            } else {
                i3 = this.f92a.size() - 1;
            }
            if (i3 >= i) {
                for (i2 = i; i2 <= i3; i2++) {
                    if (str.equals(((Tag) this.f92a.get(i2)).getID())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public int a() {
        long b = b();
        if (this.a == -1) {
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
            this.a = i;
        }
        USALog.d("cache tags size= " + this.a);
        return this.a;
    }

    private long b() {
        if (SourceTool.gContext == null) {
            return 0;
        }
        ActivityManager activityManager = (ActivityManager) SourceTool.gContext.getSystemService("activity");
        MemoryInfo memoryInfo = new MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        long j = (memoryInfo.availMem / 1024) / 1024;
        USALog.d("free memory = " + j);
        return j;
    }

    public long m18a() {
        return this.c;
    }

    public void a(long j) {
        this.c = j;
    }
}

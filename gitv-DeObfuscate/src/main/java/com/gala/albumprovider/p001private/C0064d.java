package com.gala.albumprovider.p001private;

import android.util.SparseArray;
import com.gala.albumprovider.util.ParseUtils;
import com.gala.albumprovider.util.USALog;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class C0064d {
    private static final C0064d f279a = new C0064d();
    private static int[] f280a = new int[]{1, 2, 4, 5, 6};
    private int f281a = 0;
    private long f282a = 3600000;
    private final SparseArray<C0062b> f283a = new SparseArray();
    private boolean f284a = false;

    private C0064d() {
    }

    public static C0064d m123a() {
        return f279a;
    }

    public void m129a(boolean z) {
        if (z && m122a()) {
            this.f284a = z;
            USALog.m147d("isNeedCache =  " + this.f284a);
        }
        if (!this.f284a) {
            this.f283a.clear();
        }
    }

    private boolean m125a() {
        if (m122a() <= 512) {
            USALog.m147d((Object) "memory is not enough for channel cache");
            return false;
        }
        USALog.m147d((Object) "memory is enough for channel cache");
        return true;
    }

    private int m122a() {
        Exception e;
        Throwable th;
        String str = null;
        if (this.f281a > 0) {
            USALog.m147d("total memory is " + this.f281a);
            return this.f281a;
        }
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new FileReader("/proc/meminfo"), 8);
            try {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    str = readLine;
                }
                this.f281a = (int) (((long) Integer.parseInt(str.substring(str.indexOf(58) + 1, str.indexOf(107)).trim())) / 1024);
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            } catch (Exception e3) {
                e = e3;
                try {
                    e.printStackTrace();
                    this.f281a = 0;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                    }
                    USALog.m147d("total memory is " + this.f281a);
                    return this.f281a;
                } catch (Throwable th2) {
                    th = th2;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e4) {
                            e4.printStackTrace();
                        }
                    }
                    throw th;
                }
            }
        } catch (Exception e5) {
            e = e5;
            bufferedReader = null;
            e.printStackTrace();
            this.f281a = 0;
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            USALog.m147d("total memory is " + this.f281a);
            return this.f281a;
        } catch (Throwable th3) {
            th = th3;
            bufferedReader = null;
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            throw th;
        }
        USALog.m147d("total memory is " + this.f281a);
        return this.f281a;
    }

    private void m124a() {
        if (f280a != null) {
            for (int i : f280a) {
                if (C0067g.m139a().m141a(i + "") != null) {
                    this.f283a.put(i, new C0062b(i + ""));
                }
            }
        }
    }

    public boolean m130a(String str) {
        USALog.m147d("checkNeedCacheChannel channelId = " + str + "  ;mChannelCaheTime = " + this.f282a + " ;mIsNeedCache=" + this.f284a);
        if (!this.f284a || this.f282a <= 0) {
            this.f283a.clear();
            return false;
        }
        if (this.f283a.size() == 0) {
            m122a();
        }
        if (this.f283a.indexOfKey(ParseUtils.str2Int(str)) >= 0) {
            return true;
        }
        return false;
    }

    public C0062b m126a(String str) {
        return (C0062b) this.f283a.get(ParseUtils.str2Int(str));
    }

    public C0062b m127a(String str, boolean z) {
        C0062b c0062b = (C0062b) this.f283a.get(ParseUtils.str2Int(str));
        long a = c0062b.m101a();
        long currentTimeMillis = System.currentTimeMillis();
        Object obj = ((a > -1 ? 1 : (a == -1 ? 0 : -1)) == 0 ? -1 : currentTimeMillis - a) > this.f282a ? 1 : null;
        if (obj != null) {
            c0062b = new C0062b(str);
            USALog.m147d("clear the channelData. channelId = " + str);
        }
        if (z && (a == -1 || obj != null)) {
            c0062b.m108a(currentTimeMillis);
            USALog.m147d("channelData.setLastCacheTime().channelId = " + str);
        }
        this.f283a.put(ParseUtils.str2Int(str), c0062b);
        return c0062b;
    }

    public void m128a(long j) {
        this.f282a = j;
        if (j <= 0) {
            this.f283a.clear();
        }
    }
}

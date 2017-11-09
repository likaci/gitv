package com.gala.albumprovider.private;

import android.util.SparseArray;
import com.gala.albumprovider.util.ParseUtils;
import com.gala.albumprovider.util.USALog;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class d {
    private static final d a = new d();
    private static int[] f50a = new int[]{1, 2, 4, 5, 6};
    private int f51a = 0;
    private long f52a = 3600000;
    private final SparseArray<b> f53a = new SparseArray();
    private boolean f54a = false;

    private d() {
    }

    public static d m13a() {
        return a;
    }

    public void a(boolean z) {
        if (z && a()) {
            this.f54a = z;
            USALog.d("isNeedCache =  " + this.f54a);
        }
        if (!this.f54a) {
            this.f53a.clear();
        }
    }

    private boolean m15a() {
        if (a() <= 512) {
            USALog.d((Object) "memory is not enough for channel cache");
            return false;
        }
        USALog.d((Object) "memory is enough for channel cache");
        return true;
    }

    private int a() {
        BufferedReader bufferedReader;
        Exception e;
        Throwable th;
        String str = null;
        if (this.f51a > 0) {
            USALog.d("total memory is " + this.f51a);
            return this.f51a;
        }
        try {
            bufferedReader = new BufferedReader(new FileReader("/proc/meminfo"), 8);
            try {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    str = readLine;
                }
                this.f51a = (int) (((long) Integer.parseInt(str.substring(str.indexOf(58) + 1, str.indexOf(107)).trim())) / 1024);
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
                    this.f51a = 0;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                    }
                    USALog.d("total memory is " + this.f51a);
                    return this.f51a;
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
            this.f51a = 0;
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            USALog.d("total memory is " + this.f51a);
            return this.f51a;
        } catch (Throwable th3) {
            th = th3;
            bufferedReader = null;
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            throw th;
        }
        USALog.d("total memory is " + this.f51a);
        return this.f51a;
    }

    private void m14a() {
        if (f50a != null) {
            for (int i : f50a) {
                if (g.a().a(i + "") != null) {
                    this.f53a.put(i, new b(i + ""));
                }
            }
        }
    }

    public boolean m16a(String str) {
        USALog.d("checkNeedCacheChannel channelId = " + str + "  ;mChannelCaheTime = " + this.f52a + " ;mIsNeedCache=" + this.f54a);
        if (!this.f54a || this.f52a <= 0) {
            this.f53a.clear();
            return false;
        }
        if (this.f53a.size() == 0) {
            a();
        }
        if (this.f53a.indexOfKey(ParseUtils.str2Int(str)) >= 0) {
            return true;
        }
        return false;
    }

    public b a(String str) {
        return (b) this.f53a.get(ParseUtils.str2Int(str));
    }

    public b a(String str, boolean z) {
        b bVar = (b) this.f53a.get(ParseUtils.str2Int(str));
        long a = bVar.a();
        long currentTimeMillis = System.currentTimeMillis();
        Object obj = ((a > -1 ? 1 : (a == -1 ? 0 : -1)) == 0 ? -1 : currentTimeMillis - a) > this.f52a ? 1 : null;
        if (obj != null) {
            bVar = new b(str);
            USALog.d("clear the channelData. channelId = " + str);
        }
        if (z && (a == -1 || obj != null)) {
            bVar.a(currentTimeMillis);
            USALog.d("channelData.setLastCacheTime().channelId = " + str);
        }
        this.f53a.put(ParseUtils.str2Int(str), bVar);
        return bVar;
    }

    public void a(long j) {
        this.f52a = j;
        if (j <= 0) {
            this.f53a.clear();
        }
    }
}

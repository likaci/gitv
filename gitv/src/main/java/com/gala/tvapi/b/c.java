package com.gala.tvapi.b;

import com.gala.tvapi.b.c.a;
import com.gala.tvapi.b.h.b;
import com.gala.tvapi.b.n.d;
import com.gala.tvapi.b.n.e;
import com.gala.tvapi.b.n.f;
import com.gala.tvapi.b.n.g;
import com.gala.tvapi.b.n.h;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.type.PlatformType;

public final class c {
    private static b a = null;

    public static b a(PlatformType platformType) {
        if (a != null) {
            return a;
        }
        b aVar;
        if (platformType == PlatformType.HUAWEI) {
            aVar = new a();
            a = aVar;
            return aVar;
        } else if (platformType == PlatformType.HAIXIN) {
            aVar = new com.gala.tvapi.b.b.a();
            a = aVar;
            return aVar;
        } else if (platformType == PlatformType.SKYWORTH_VIPPROJECT) {
            aVar = new b();
            a = aVar;
            return aVar;
        } else if (platformType == PlatformType.ALIVIP) {
            aVar = new com.gala.tvapi.b.a.a();
            a = aVar;
            return aVar;
        } else if (platformType == PlatformType.TCL_GOLIVE) {
            aVar = new com.gala.tvapi.b.j.b();
            a = aVar;
            return aVar;
        } else if (platformType == PlatformType.YINHESHANDONG) {
            aVar = new e();
            a = aVar;
            return aVar;
        } else if (platformType == PlatformType.YINHEVIP) {
            aVar = new com.gala.tvapi.b.n.a();
            a = aVar;
            return aVar;
        } else if (platformType == PlatformType.ANDROID_PHONE) {
            if (TVApiBase.getTVApiProperty().getPlatform() == PlatformType.TAIWAN) {
                aVar = new com.gala.tvapi.b.k.a();
                a = aVar;
                return aVar;
            }
            aVar = new com.gala.tvapi.b.g.a();
            a = aVar;
            return aVar;
        } else if (platformType == PlatformType.IPHONE) {
            if (TVApiBase.getTVApiProperty().getPlatform() == PlatformType.TAIWAN) {
                aVar = new com.gala.tvapi.b.k.c();
                a = aVar;
                return aVar;
            }
            aVar = new com.gala.tvapi.b.g.b();
            a = aVar;
            return aVar;
        } else if (platformType == PlatformType.STORMTV) {
            aVar = new com.gala.tvapi.b.i.a();
            a = aVar;
            return aVar;
        } else if (platformType == PlatformType.YINHEBOYUAN) {
            aVar = new d();
            a = aVar;
            return aVar;
        } else if (platformType == PlatformType.BOYUANANHUI) {
            aVar = new com.gala.tvapi.b.n.c();
            a = aVar;
            return aVar;
        } else if (platformType == PlatformType.YIDONGANHUI) {
            aVar = new g();
            a = aVar;
            return aVar;
        } else if (platformType == PlatformType.YIDONGZHEJIANG) {
            aVar = new h();
            a = aVar;
            return aVar;
        } else if (platformType == PlatformType.VR) {
            aVar = new com.gala.tvapi.b.l.d();
            a = aVar;
            return aVar;
        } else if (platformType == PlatformType.YINHEBOYUANNATION) {
            aVar = new com.gala.tvapi.b.n.b();
            a = aVar;
            return aVar;
        } else if (platformType == PlatformType.HOLATEKVIP) {
            aVar = new com.gala.tvapi.b.d.a();
            a = aVar;
            return aVar;
        } else if (platformType == PlatformType.LENOVOSHIYUNVIP) {
            aVar = new com.gala.tvapi.b.e.a();
            a = aVar;
            return aVar;
        } else if (platformType == PlatformType.TAIWAN) {
            aVar = new com.gala.tvapi.b.k.b();
            a = aVar;
            return aVar;
        } else if (platformType == PlatformType.CVTE) {
            aVar = new com.gala.tvapi.b.f.a();
            a = aVar;
            return aVar;
        } else if (platformType == PlatformType.DRPENG) {
            aVar = new com.gala.tvapi.b.f.b();
            a = aVar;
            return aVar;
        } else if (platformType == PlatformType.VR_ANDROID_PHONE) {
            aVar = new com.gala.tvapi.b.l.c();
            a = aVar;
            return aVar;
        } else if (platformType == PlatformType.VR_ANDROID_ALLINONE) {
            aVar = new com.gala.tvapi.b.l.a();
            a = aVar;
            return aVar;
        } else if (platformType == PlatformType.VR_ANDROID_GAME) {
            aVar = new com.gala.tvapi.b.l.b();
            a = aVar;
            return aVar;
        } else if (platformType == PlatformType.YINHESHANXIYD) {
            aVar = new f();
            a = aVar;
            return aVar;
        } else if (platformType == PlatformType.FOXCONN) {
            aVar = new com.gala.tvapi.b.f.c();
            a = aVar;
            return aVar;
        } else if (platformType == PlatformType.SKYWORTH_DIGITAL) {
            aVar = new com.gala.tvapi.b.h.a();
            a = aVar;
            return aVar;
        } else if (platformType == PlatformType.WSTV) {
            aVar = new com.gala.tvapi.b.f.e();
            a = aVar;
            return aVar;
        } else if (platformType == PlatformType.XIAOMI_CNTV) {
            aVar = new com.gala.tvapi.b.m.a();
            a = aVar;
            return aVar;
        } else if (platformType == PlatformType.XIAOMI_GITV) {
            aVar = new com.gala.tvapi.b.m.b();
            a = aVar;
            return aVar;
        } else if (platformType == PlatformType.MIFENG) {
            aVar = new com.gala.tvapi.b.f.d();
            a = aVar;
            return aVar;
        } else if (platformType == PlatformType.TCLGLOBALPLAY) {
            aVar = new com.gala.tvapi.b.j.a();
            a = aVar;
            return aVar;
        } else {
            aVar = new a();
            a = aVar;
            return aVar;
        }
    }

    public static void a() {
        a = null;
    }
}

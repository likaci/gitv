package com.gala.tvapi.p008b;

import com.gala.tvapi.p008b.p009a.C0215a;
import com.gala.tvapi.p008b.p010b.C0216a;
import com.gala.tvapi.p008b.p011c.C0217a;
import com.gala.tvapi.p008b.p012d.C0219a;
import com.gala.tvapi.p008b.p013e.C0220a;
import com.gala.tvapi.p008b.p014f.C0221a;
import com.gala.tvapi.p008b.p014f.C0222b;
import com.gala.tvapi.p008b.p014f.C0223c;
import com.gala.tvapi.p008b.p014f.C0224d;
import com.gala.tvapi.p008b.p014f.C0225e;
import com.gala.tvapi.p008b.p015g.C0226a;
import com.gala.tvapi.p008b.p015g.C0227b;
import com.gala.tvapi.p008b.p016h.C0228a;
import com.gala.tvapi.p008b.p016h.C0229b;
import com.gala.tvapi.p008b.p017i.C0230a;
import com.gala.tvapi.p008b.p018j.C0231a;
import com.gala.tvapi.p008b.p018j.C0232b;
import com.gala.tvapi.p008b.p019k.C0233a;
import com.gala.tvapi.p008b.p019k.C0234b;
import com.gala.tvapi.p008b.p019k.C0235c;
import com.gala.tvapi.p008b.p020l.C0236a;
import com.gala.tvapi.p008b.p020l.C0237b;
import com.gala.tvapi.p008b.p020l.C0238c;
import com.gala.tvapi.p008b.p020l.C0239d;
import com.gala.tvapi.p008b.p021m.C0240a;
import com.gala.tvapi.p008b.p021m.C0241b;
import com.gala.tvapi.p008b.p022n.C0242a;
import com.gala.tvapi.p008b.p022n.C0243b;
import com.gala.tvapi.p008b.p022n.C0244c;
import com.gala.tvapi.p008b.p022n.C0245d;
import com.gala.tvapi.p008b.p022n.C0246e;
import com.gala.tvapi.p008b.p022n.C0247f;
import com.gala.tvapi.p008b.p022n.C0248g;
import com.gala.tvapi.p008b.p022n.C0249h;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.type.PlatformType;

public final class C0218c {
    private static C0213b f888a = null;

    public static C0213b m605a(PlatformType platformType) {
        if (f888a != null) {
            return f888a;
        }
        C0213b c0217a;
        if (platformType == PlatformType.HUAWEI) {
            c0217a = new C0217a();
            f888a = c0217a;
            return c0217a;
        } else if (platformType == PlatformType.HAIXIN) {
            c0217a = new C0216a();
            f888a = c0217a;
            return c0217a;
        } else if (platformType == PlatformType.SKYWORTH_VIPPROJECT) {
            c0217a = new C0229b();
            f888a = c0217a;
            return c0217a;
        } else if (platformType == PlatformType.ALIVIP) {
            c0217a = new C0215a();
            f888a = c0217a;
            return c0217a;
        } else if (platformType == PlatformType.TCL_GOLIVE) {
            c0217a = new C0232b();
            f888a = c0217a;
            return c0217a;
        } else if (platformType == PlatformType.YINHESHANDONG) {
            c0217a = new C0246e();
            f888a = c0217a;
            return c0217a;
        } else if (platformType == PlatformType.YINHEVIP) {
            c0217a = new C0242a();
            f888a = c0217a;
            return c0217a;
        } else if (platformType == PlatformType.ANDROID_PHONE) {
            if (TVApiBase.getTVApiProperty().getPlatform() == PlatformType.TAIWAN) {
                c0217a = new C0233a();
                f888a = c0217a;
                return c0217a;
            }
            c0217a = new C0226a();
            f888a = c0217a;
            return c0217a;
        } else if (platformType == PlatformType.IPHONE) {
            if (TVApiBase.getTVApiProperty().getPlatform() == PlatformType.TAIWAN) {
                c0217a = new C0235c();
                f888a = c0217a;
                return c0217a;
            }
            c0217a = new C0227b();
            f888a = c0217a;
            return c0217a;
        } else if (platformType == PlatformType.STORMTV) {
            c0217a = new C0230a();
            f888a = c0217a;
            return c0217a;
        } else if (platformType == PlatformType.YINHEBOYUAN) {
            c0217a = new C0245d();
            f888a = c0217a;
            return c0217a;
        } else if (platformType == PlatformType.BOYUANANHUI) {
            c0217a = new C0244c();
            f888a = c0217a;
            return c0217a;
        } else if (platformType == PlatformType.YIDONGANHUI) {
            c0217a = new C0248g();
            f888a = c0217a;
            return c0217a;
        } else if (platformType == PlatformType.YIDONGZHEJIANG) {
            c0217a = new C0249h();
            f888a = c0217a;
            return c0217a;
        } else if (platformType == PlatformType.VR) {
            c0217a = new C0239d();
            f888a = c0217a;
            return c0217a;
        } else if (platformType == PlatformType.YINHEBOYUANNATION) {
            c0217a = new C0243b();
            f888a = c0217a;
            return c0217a;
        } else if (platformType == PlatformType.HOLATEKVIP) {
            c0217a = new C0219a();
            f888a = c0217a;
            return c0217a;
        } else if (platformType == PlatformType.LENOVOSHIYUNVIP) {
            c0217a = new C0220a();
            f888a = c0217a;
            return c0217a;
        } else if (platformType == PlatformType.TAIWAN) {
            c0217a = new C0234b();
            f888a = c0217a;
            return c0217a;
        } else if (platformType == PlatformType.CVTE) {
            c0217a = new C0221a();
            f888a = c0217a;
            return c0217a;
        } else if (platformType == PlatformType.DRPENG) {
            c0217a = new C0222b();
            f888a = c0217a;
            return c0217a;
        } else if (platformType == PlatformType.VR_ANDROID_PHONE) {
            c0217a = new C0238c();
            f888a = c0217a;
            return c0217a;
        } else if (platformType == PlatformType.VR_ANDROID_ALLINONE) {
            c0217a = new C0236a();
            f888a = c0217a;
            return c0217a;
        } else if (platformType == PlatformType.VR_ANDROID_GAME) {
            c0217a = new C0237b();
            f888a = c0217a;
            return c0217a;
        } else if (platformType == PlatformType.YINHESHANXIYD) {
            c0217a = new C0247f();
            f888a = c0217a;
            return c0217a;
        } else if (platformType == PlatformType.FOXCONN) {
            c0217a = new C0223c();
            f888a = c0217a;
            return c0217a;
        } else if (platformType == PlatformType.SKYWORTH_DIGITAL) {
            c0217a = new C0228a();
            f888a = c0217a;
            return c0217a;
        } else if (platformType == PlatformType.WSTV) {
            c0217a = new C0225e();
            f888a = c0217a;
            return c0217a;
        } else if (platformType == PlatformType.XIAOMI_CNTV) {
            c0217a = new C0240a();
            f888a = c0217a;
            return c0217a;
        } else if (platformType == PlatformType.XIAOMI_GITV) {
            c0217a = new C0241b();
            f888a = c0217a;
            return c0217a;
        } else if (platformType == PlatformType.MIFENG) {
            c0217a = new C0224d();
            f888a = c0217a;
            return c0217a;
        } else if (platformType == PlatformType.TCLGLOBALPLAY) {
            c0217a = new C0231a();
            f888a = c0217a;
            return c0217a;
        } else {
            c0217a = new C0214a();
            f888a = c0217a;
            return c0217a;
        }
    }

    public static void m606a() {
        f888a = null;
    }
}

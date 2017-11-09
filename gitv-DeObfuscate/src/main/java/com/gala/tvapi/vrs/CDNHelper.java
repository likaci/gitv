package com.gala.tvapi.vrs;

import com.gala.tvapi.p008b.C0214a;
import com.gala.tvapi.vrs.core.C0365a;
import com.gala.tvapi.vrs.core.IVrsServer;
import com.gala.tvapi.vrs.p031a.C0336k;
import com.gala.tvapi.vrs.p032b.C0360e;
import com.gala.tvapi.vrs.result.ApiResultF4v;

public class CDNHelper extends BaseHelper {
    public static final IVrsServer<ApiResultF4v> testStress = C0214a.m581a(new C0360e(C0365a.ar), new C0336k(), ApiResultF4v.class, "cdnInfo", false, true);
}

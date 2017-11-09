package com.gala.tvapi.vrs;

import com.gala.tvapi.b.a;
import com.gala.tvapi.vrs.a.k;
import com.gala.tvapi.vrs.b.e;
import com.gala.tvapi.vrs.core.IVrsServer;
import com.gala.tvapi.vrs.result.ApiResultF4v;

public class CDNHelper extends BaseHelper {
    public static final IVrsServer<ApiResultF4v> testStress = a.a(new e(com.gala.tvapi.vrs.core.a.ar), new k(), ApiResultF4v.class, "cdnInfo", false, true);
}

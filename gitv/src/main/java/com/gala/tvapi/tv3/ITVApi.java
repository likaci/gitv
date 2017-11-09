package com.gala.tvapi.tv3;

import com.gala.tvapi.tv3.a.a;
import com.gala.tvapi.tv3.a.b;
import com.gala.tvapi.tv3.a.c;
import com.gala.tvapi.tv3.a.d;
import com.gala.tvapi.tv3.a.e;
import com.gala.tvapi.tv3.result.HostUpgradeResult;
import com.gala.tvapi.tv3.result.PluginUpdateResult;
import com.gala.tvapi.tv3.result.RegisterResult;
import com.gala.tvapi.tv3.result.SignResult;
import com.gala.tvapi.tv3.result.TimeResult;

public class ITVApi {
    private static final IApi a = new c(RegisterResult.class);
    private static final IApi b = new e(TimeResult.class);
    private static final IApi c = new b(PluginUpdateResult.class);
    private static final IApi d = new d(SignResult.class);
    private static final IApi e = new a(HostUpgradeResult.class);

    public static final IApi<RegisterResult> registerApi() {
        return a;
    }

    public static final IApi<TimeResult> timeApi() {
        return b;
    }

    public static final IApi<PluginUpdateResult> pluginUpdateApi() {
        return c;
    }

    public static final IApi<SignResult> signApi() {
        return d;
    }

    public static final IApi<HostUpgradeResult> hostUpgradeApi() {
        return e;
    }
}

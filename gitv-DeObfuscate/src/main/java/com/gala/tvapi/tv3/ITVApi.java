package com.gala.tvapi.tv3;

import com.gala.tvapi.tv3.p027a.C0298a;
import com.gala.tvapi.tv3.p027a.C0300b;
import com.gala.tvapi.tv3.p027a.C0303c;
import com.gala.tvapi.tv3.p027a.C0305d;
import com.gala.tvapi.tv3.p027a.C0307e;
import com.gala.tvapi.tv3.result.HostUpgradeResult;
import com.gala.tvapi.tv3.result.PluginUpdateResult;
import com.gala.tvapi.tv3.result.RegisterResult;
import com.gala.tvapi.tv3.result.SignResult;
import com.gala.tvapi.tv3.result.TimeResult;

public class ITVApi {
    private static final IApi f1060a = new C0303c(RegisterResult.class);
    private static final IApi f1061b = new C0307e(TimeResult.class);
    private static final IApi f1062c = new C0300b(PluginUpdateResult.class);
    private static final IApi f1063d = new C0305d(SignResult.class);
    private static final IApi f1064e = new C0298a(HostUpgradeResult.class);

    public static final IApi<RegisterResult> registerApi() {
        return f1060a;
    }

    public static final IApi<TimeResult> timeApi() {
        return f1061b;
    }

    public static final IApi<PluginUpdateResult> pluginUpdateApi() {
        return f1062c;
    }

    public static final IApi<SignResult> signApi() {
        return f1063d;
    }

    public static final IApi<HostUpgradeResult> hostUpgradeApi() {
        return f1064e;
    }
}

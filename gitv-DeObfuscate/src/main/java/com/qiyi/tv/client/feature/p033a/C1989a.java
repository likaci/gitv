package com.qiyi.tv.client.feature.p033a;

import android.os.Bundle;
import com.qiyi.tv.client.impl.Command;
import com.qiyi.tv.client.impl.Log;
import com.qiyi.tv.client.impl.ParamsHelper;
import com.qiyi.tv.client.impl.Utils;

public final class C1989a {
    public static Bundle m1618a(Bundle bundle, Command... commandArr) {
        if (commandArr == null || commandArr.length == 0) {
            return Utils.createResultBundle(6);
        }
        int i;
        Bundle bundle2;
        int i2;
        int length = commandArr.length;
        boolean z = false;
        int i3 = 1;
        Bundle bundle3 = bundle;
        for (int i4 = 0; i4 < length; i4++) {
            try {
                bundle3 = commandArr[i4].process(bundle3);
                z = ParamsHelper.parseCommandContinue(bundle3);
                i3 = ParamsHelper.parseResultCode(bundle3);
            } catch (Throwable e) {
                Log.m1625w("CommandRunner", "executeCommand() error!", e);
                i3 = Utils.parseErrorCode(e);
            }
            if (i3 != 0 || !z) {
                Log.m1624w("CommandRunner", "executeCommand() [" + i4 + "] break for code=" + i3 + ", serverContinue=" + z);
                Utils.dumpBundle("executeCommand()", bundle3);
                i = i3;
                bundle2 = bundle3;
                i2 = i;
                break;
            }
        }
        i = i3;
        bundle2 = bundle3;
        i2 = i;
        if (bundle2 == null) {
            bundle2 = new Bundle();
        }
        ParamsHelper.setResultCode(bundle2, i2);
        return bundle2;
    }

    public C1989a() {
        Object obj = new Object();
    }
}

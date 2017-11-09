package com.qiyi.tv.client.impl.a;

import android.content.Context;
import android.os.Bundle;
import com.qiyi.tv.client.impl.Command;
import com.qiyi.tv.client.impl.Log;
import com.qiyi.tv.client.impl.ParamsHelper;
import com.qiyi.tv.client.impl.Utils;

final class j extends Command {
    public j(Context context, int i, int i2, int i3) {
        super(context, i, i2, i3);
    }

    public final Bundle process(Bundle params) {
        Bundle bundle = new Bundle();
        Utils.copyBundle(bundle, params);
        ParamsHelper.setOperationTarget(bundle, getTarget());
        ParamsHelper.setOperationType(bundle, getOperationType());
        ParamsHelper.setOperationDataType(bundle, getDataType());
        try {
            return c.a().a(bundle);
        } catch (Throwable e) {
            Log.w("SimpleInvokeCommand", "process() error!", e);
            return Utils.createResultBundle(Utils.parseErrorCode(e));
        }
    }
}

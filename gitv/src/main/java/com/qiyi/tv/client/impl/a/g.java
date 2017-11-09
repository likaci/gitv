package com.qiyi.tv.client.impl.a;

import android.content.Context;
import android.os.Bundle;
import com.qiyi.tv.client.impl.Command;
import com.qiyi.tv.client.impl.Log;
import com.qiyi.tv.client.impl.ParamsHelper;
import com.qiyi.tv.client.impl.Utils;

final class g extends Command {
    public g(Context context, int i) {
        super(context, i, 20001, 30000);
    }

    public final Bundle process(Bundle params) {
        Bundle bundle = new Bundle();
        ParamsHelper.setOperationTarget(bundle, getTarget());
        ParamsHelper.setOperationType(bundle, getOperationType());
        ParamsHelper.setOperationDataType(bundle, getDataType());
        Utils.copyBundle(bundle, params);
        try {
            return c.a().a(bundle);
        } catch (Throwable e) {
            Log.w("GetIntentCommand", "process() error!", e);
            return Utils.createResultBundle(Utils.parseErrorCode(e));
        }
    }
}

package com.gala.video.app.epg.openapi.feature.account;

import android.content.Context;
import android.os.Bundle;
import com.gala.video.lib.share.ifimpl.openplay.service.OpenApiUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.qiyi.tv.client.feature.account.VipInfo;
import com.qiyi.tv.client.impl.Params.DataType;

public class GetVipInfoCommand extends ServerCommand<Void> {
    private static final String TAG = "GetVipInfoCommand";

    public GetVipInfoCommand(Context context) {
        super(context, 10002, 20003, DataType.DATA_VIP_INFO);
    }

    public Bundle onProcess(Bundle params) {
        VipInfo vipInfo;
        Bundle bundle = OpenApiResultCreater.createResultBundle(0);
        boolean isvip = OpenApiUtils.isVipUser(getContext());
        String vipDate = GetInterfaceTools.getIGalaAccountManager().getVipDate();
        if (isvip) {
            vipInfo = new VipInfo(vipDate, isvip);
        } else {
            vipInfo = new VipInfo("", isvip);
        }
        increaseAccessCount();
        ServerParamsHelper.setVipInfo(bundle, vipInfo);
        return bundle;
    }
}

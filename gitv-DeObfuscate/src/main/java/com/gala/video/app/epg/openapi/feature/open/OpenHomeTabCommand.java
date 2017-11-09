package com.gala.video.app.epg.openapi.feature.open;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseIntArray;
import com.gala.video.app.epg.HomeActivity;
import com.gala.video.app.epg.home.data.provider.TabProvider;
import com.gala.video.lib.share.common.configs.IntentConfig;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.TabModel;
import com.push.mqttv3.internal.ClientDefaults;
import com.qiyi.tv.client.impl.Params.TargetType;
import java.util.List;

public class OpenHomeTabCommand extends ServerCommand<Intent> {
    private static final int ANIMATION_HOUSE_GROUP_RESOURCE_ID = 212697412;
    private static final int CAROUSEL_GROUP_RESOURCE_ID = 212860012;
    private static final int CARTOON_GROUP_RESOURCE_ID = 211660512;
    private static final int CHANNEL_GROUP_RESOURCE_ID = 211660612;
    private static final int EPISODE_GROUP_RESOURCE_ID = 211660212;
    private static final int HOME_GROUP_RESOURCE_ID = 211660012;
    private static final int KIDS_GROUP_RESOURCE_ID = 212001312;
    private static final int MOVIE_GROUP_RESOURCE_ID = 211660312;
    private static final int VARIETY_GROUP_RESOURCE_ID = 211660412;
    private static final int VIP_GROUP_RESOURCE_ID = 211660112;
    private static SparseIntArray sTabBinder = new SparseIntArray(12);

    static {
        sTabBinder.append(2, HOME_GROUP_RESOURCE_ID);
        sTabBinder.append(1, CAROUSEL_GROUP_RESOURCE_ID);
        sTabBinder.append(3, VIP_GROUP_RESOURCE_ID);
        sTabBinder.append(4, ANIMATION_HOUSE_GROUP_RESOURCE_ID);
        sTabBinder.append(5, EPISODE_GROUP_RESOURCE_ID);
        sTabBinder.append(6, MOVIE_GROUP_RESOURCE_ID);
        sTabBinder.append(7, VARIETY_GROUP_RESOURCE_ID);
        sTabBinder.append(8, KIDS_GROUP_RESOURCE_ID);
        sTabBinder.append(9, CARTOON_GROUP_RESOURCE_ID);
        sTabBinder.append(10, CHANNEL_GROUP_RESOURCE_ID);
    }

    public OpenHomeTabCommand(Context context) {
        super(context, TargetType.TARGET_HOME_TAB, 20001, 30000);
        setNeedNetwork(false);
    }

    protected Bundle onProcess(Bundle params) {
        int tab = ServerParamsHelper.parseHomeTabType(params);
        if (tab == 0 || -1 == sTabBinder.get(tab, -1)) {
            return OpenApiResultCreater.createResultBundle(6);
        }
        openHomePage(tab, params);
        Bundle result = OpenApiResultCreater.createResultBundle(0);
        ServerParamsHelper.setCommandContinue(result, false);
        increaseAccessCount();
        return result;
    }

    private void openHomePage(int tab, Bundle params) {
        int targetPage = -1;
        List<TabModel> tabModels = TabProvider.getInstance().getTabInfo();
        int targetResourceId = sTabBinder.get(tab, -1);
        if (tabModels != null) {
            int index = 0;
            for (TabModel model : tabModels) {
                if (String.valueOf(targetResourceId).equals(model.getResourceGroupId())) {
                    targetPage = index;
                    break;
                }
                index++;
            }
        }
        Intent intent = new Intent(getContext(), HomeActivity.class);
        intent.putExtra(IntentConfig.DISABLE_START_PREVIEW, true);
        intent.putExtra(IntentConfig.OPENAPI_HOME_TARGET_PAGE, targetPage);
        intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
        getContext().startActivity(intent);
    }
}

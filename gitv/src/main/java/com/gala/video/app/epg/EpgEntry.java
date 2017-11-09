package com.gala.video.app.epg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.gala.multiscreen.dmr.util.StringUtils;
import com.gala.video.app.epg.ui.albumlist.AlbumUtils;
import com.gala.video.lib.share.common.configs.IntentConfig;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.IEpgEntry.Wrapper;
import com.gala.video.lib.share.utils.PageIOUtils;
import com.push.mqttv3.internal.ClientDefaults;

public class EpgEntry extends Wrapper {
    public void startSearchResultPage(Context context, int channelId, String keyword, int clickType, String qpId, String channelName) {
        AlbumUtils.startSearchResultPage(context, channelId, keyword, clickType, qpId, channelName);
    }

    public void startHomeActivity(Context context, boolean disablePreview) {
        Intent intent = new Intent(context, HomeActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(ClientDefaults.MAX_MSG_SIZE);
        }
        intent.addFlags(67108864);
        intent.putExtra(IntentConfig.DISABLE_START_PREVIEW, disablePreview);
        PageIOUtils.activityIn(context, intent);
    }

    public void search(Context context, String value, boolean isActor) {
        int enterType = 6;
        if (isActor) {
            enterType = 5;
        } else if (StringUtils.isAllChar(value)) {
            enterType = 7;
        }
        CreateInterfaceTools.createEpgEntry().startSearchResultPage(context, -1, value, enterType, null, null);
    }
}

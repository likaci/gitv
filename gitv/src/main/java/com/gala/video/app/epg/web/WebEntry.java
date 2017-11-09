package com.gala.video.app.epg.web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.JsonUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.activity.QBaseActivity;
import com.gala.video.lib.share.ifimpl.interaction.ActionSet;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.IWebEntry.Wrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model.WebIntentModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model.WebIntentParams;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.IPlayerFeatureProxy.OnStateChangedListener;
import com.gala.video.lib.share.utils.PageIOUtils;
import com.qiyi.tv.client.impl.Utils;
import java.util.ArrayList;
import java.util.List;

public class WebEntry extends Wrapper {
    private static final String TAG = "EPG/WebEntry";

    private class MyPluginStateListener implements OnStateChangedListener {
        private Context mContext;
        private Intent mIntent;

        public MyPluginStateListener(Context context, Intent intent) {
            this.mContext = context;
            this.mIntent = intent;
        }

        public void onSuccess() {
            PageIOUtils.activityIn(this.mContext, this.mIntent);
        }

        public void onFailed() {
            LogUtils.e(WebEntry.TAG, "onFailed: player plugin loaded onCanceled.");
        }

        public void onCanceled() {
        }

        public void onLoading() {
        }
    }

    public void gotoSubject(Context context, WebIntentParams params) {
        if (params == null) {
            params = new WebIntentParams();
        }
        WebIntentModel intentModel = WebIntentModel.build(params);
        intentModel.setType(-1);
        startSubject(context, intentModel);
    }

    public void gotoSubject(Context context, String id, String name, String from) {
        gotoSubject(context, id, name, from, null);
    }

    @Deprecated
    public void gotoSubject(Context context, String id, String name, String from, String buySource) {
        WebIntentModel intentModel = WebIntentModel.build(2);
        intentModel.setType(-1);
        intentModel.setId(id);
        intentModel.setFrom(from);
        intentModel.setName(name);
        startSubject(context, intentModel);
    }

    private void startSubject(Context context, WebIntentModel intentModel) {
        Intent intent = new Intent(ActionSet.ACT_WEBSUBJECT);
        intent.putExtra("intent_model", intentModel);
        if (context instanceof QBaseActivity) {
            QBaseActivity baseActivity = (QBaseActivity) context;
            QBaseActivity.isLoaderWEBActivity = true;
        }
        if (!(context instanceof Activity)) {
            intent.addFlags(Utils.INTENT_FLAG_DEFAULT);
        }
        onClickPlayerAsync(context, intent);
    }

    public void gotoSubjectNotice(Context context, List<Album> list, String from) {
        gotoSubjectNotice(context, list, from, null);
    }

    @Deprecated
    public void gotoSubjectNotice(Context context, List<Album> list, String from, String buySource) {
        WebIntentModel intentModel = WebIntentModel.build();
        intentModel.setFrom(from);
        intentModel.setAlbumJson(JsonUtils.toJson(list));
        startSubject(context, intentModel);
    }

    public void gotoSubjectForLive(Context context, Album album, ArrayList<Album> flowerList, String from) {
        gotoSubjectForLive(context, album, flowerList, from, null);
    }

    public void gotoSubjectForLive(Context context, Album album, ArrayList<Album> flowerList, String from, String buySource) {
        LogUtils.d(TAG, "gotoSubjectForLive() -> ,from:" + from);
        WebIntentModel intentModel = WebIntentModel.build();
        intentModel.setType(1);
        intentModel.setFrom(from);
        intentModel.setAlbum(album);
        intentModel.setFlowerList(flowerList);
        intentModel.setEventId(PingBackUtils.createEventId());
        String albumJson = JsonUtils.toJson(album);
        String flowerListjson = ListUtils.isEmpty((List) flowerList) ? "" : JsonUtils.toJson(flowerList);
        intentModel.setAlbumJson(albumJson);
        intentModel.setFlowerListJson(flowerListjson);
        startSubject(context, intentModel);
    }

    public void startPurchasePage(Context context, WebIntentParams params) {
        if (params == null) {
            params = new WebIntentParams();
        }
        WebIntentModel intentModel = WebIntentModel.build(params);
        intentModel.setAlbumJson(JsonUtils.toJson(params.albumInfo));
        intentModel.setCurrentPageType(1);
        gotoCommonWebActivity(context, intentModel, params.requestCode);
    }

    public void startPurchasePage(Context context) {
        gotoCommonWebActivity(context, WebIntentModel.build(1));
    }

    public void startMemberRightsPage(Context context, WebIntentParams params) {
        WebIntentModel intentModel = WebIntentModel.build(params);
        intentModel.setCurrentPageType(4);
        gotoCommonWebActivity(context, intentModel);
    }

    public void startMemberRightsPage(Context context) {
        startMemberRightsPage(context, null);
    }

    public void gotoMultiscreenActivity(Context context) {
        WebIntentModel intentModel = WebIntentModel.build();
        intentModel.setCurrentPageType(6);
        gotoCommonWebActivity(context, intentModel);
    }

    public void gotoGetGoldActivity(Context context) {
        WebIntentModel intentModel = WebIntentModel.build();
        intentModel.setCurrentPageType(5);
        gotoCommonWebActivity(context, intentModel);
    }

    public void gotoCommonWebActivity(Context context, String pageUrl) {
        WebIntentModel intentModel = WebIntentModel.build();
        intentModel.setPageUrl(pageUrl);
        gotoCommonWebActivity(context, intentModel);
    }

    public void gotoCommonWebActivity(Context context, WebIntentParams params) {
        gotoCommonWebActivity(context, WebIntentModel.build(params));
    }

    private void gotoCommonWebActivity(Context context, WebIntentModel intentModel) {
        gotoCommonWebActivity(context, intentModel, -2);
    }

    private void gotoCommonWebActivity(Context context, WebIntentModel intentModel, int requestCode) {
        Intent intent = new Intent(ActionSet.ACT_WEBCOMMON);
        intent.putExtra("intent_model", intentModel);
        if (!(context instanceof Activity)) {
            intent.addFlags(Utils.INTENT_FLAG_DEFAULT);
        }
        if (context instanceof QBaseActivity) {
            QBaseActivity baseActivity = (QBaseActivity) context;
            QBaseActivity.isLoaderWEBActivity = true;
        }
        startActivity(context, intent, requestCode);
    }

    public void startFaqActivity(Context context) {
        gotoCommonWebActivity(context, WebIntentModel.build(0));
    }

    public void startSignInActivity(Context context) {
        gotoCommonWebActivity(context, WebIntentModel.build(7));
    }

    public void startCouponActivity(Context context, WebIntentParams params) {
        if (params == null) {
            params = new WebIntentParams();
        }
        WebIntentModel intentModel = WebIntentModel.build(params);
        intentModel.setCurrentPageType(8);
        gotoCommonWebActivity(context, intentModel);
    }

    public void gotoRoleWebActivity(Context context) {
        gotoCommonWebActivity(context, WebIntentModel.build(3));
    }

    public void gotoSearch(Context context) {
        if (context != null) {
            PageIOUtils.activityIn(context, new Intent(ActionSet.ACT_SEARCH));
        }
    }

    public void onClickWebURI(Context context, String uri) {
        new WebOverrideUrl().onClickWebURI(context, uri);
    }

    private void startActivity(Context context, Intent intent, int requestCode) {
        PageIOUtils.activityIn(context, intent, requestCode);
    }

    private void onClickPlayerAsync(Context context, Intent intent) {
        GetInterfaceTools.getPlayerFeatureProxy().loadPlayerFeatureAsync(context, new MyPluginStateListener(context, intent), true);
    }
}

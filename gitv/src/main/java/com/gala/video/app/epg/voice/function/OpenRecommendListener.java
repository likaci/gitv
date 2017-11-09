package com.gala.video.app.epg.voice.function;

import android.content.Context;
import com.gala.tv.voice.service.AbsVoiceAction;
import com.gala.tv.voice.service.VoiceManager;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.app.epg.voice.utils.EntryUtils;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.coreservice.voice.VoiceListener;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import java.util.ArrayList;
import java.util.List;

public class OpenRecommendListener extends VoiceListener {
    private static final String TAG = "OpenRecommendHelper";

    public OpenRecommendListener(Context context, int priority) {
        super(context, priority);
    }

    protected List<AbsVoiceAction> doOpenAction() {
        return new ArrayList();
    }

    private void startAlbumDetailActivity(ChannelLabel albumPhoto, int position) {
        String from = "homerec[" + position + AlbumEnterFactory.SIGN_STR;
        PingBackParams params = new PingBackParams();
        params.add(Keys.T, "20").add("block", "rec").add("rt", "i").add("rseat", from).add("rpage", "rec");
        PingBack.getInstance().postPingBackToLongYuan(params.build());
        EntryUtils.startDetailActivity(VoiceManager.instance().getSmartContext(), albumPhoto, from + "[1]");
    }
}

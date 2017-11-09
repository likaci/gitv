package com.gala.video.app.player.data;

import android.content.Context;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.FeedBackModel;
import java.util.Map;
import org.cybergarage.upnp.std.av.server.object.SearchCriteria;

public class PlayerFeedbackModel extends FeedBackModel {
    private static final String TAG = "Player/App/PlayerFeedbackModel";
    private String mCustomQrMessage;

    public void setQrMessage(String message) {
        this.mCustomQrMessage = message;
    }

    public PlayerFeedbackModel() {
        setApiName("player");
    }

    public String getQRString() {
        LogUtils.d(TAG, "getQRString, mCustomQrMessage = " + this.mCustomQrMessage + "super.toString()=" + super.toString());
        return this.mCustomQrMessage + super.getQRString();
    }

    public Map<String, String> getQRMap(String code, String time, String ip, Context mContext) {
        Map<String, String> map = super.getQRMap(code, time, ip, mContext);
        String QRString = getQRString();
        if (!StringUtils.isEmpty((CharSequence) QRString)) {
            if (QRString.length() > LogRecordUtils.errUrlLength) {
                QRString = QRString.substring(0, LogRecordUtils.errUrlLength).replace(SearchCriteria.EQ, ": ") + "...";
            }
            map.put("playerinfo", QRString);
        }
        return map;
    }

    public String toString() {
        LogUtils.d(TAG, "toString, mCustomQrMessage = " + this.mCustomQrMessage + "super.toString()=" + super.toString());
        return this.mCustomQrMessage + super.toString();
    }
}

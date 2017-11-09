package com.gala.tvapi.vrs.result;

import com.gala.sdk.player.error.ErrorConstants;
import com.gala.tvapi.vrs.core.f;
import com.gala.tvapi.vrs.model.KeepAliveInterval;
import com.gala.video.api.ApiResult;
import com.gala.video.app.epg.ui.albumlist.constant.IAlbumConfig;

public class ApiResultKeepaliveInterval extends ApiResult {
    private boolean a = true;
    public String agenttype = "28";
    public KeepAliveInterval data;
    public String dataString;
    public String sign;

    public boolean checkSign() {
        if (this.sign == null || !f.a(this.sign, this.agenttype, this.dataString)) {
            return false;
        }
        return true;
    }

    public void setSendAliveFlag(boolean z) {
        this.a = false;
    }

    public boolean getSendAliveFlag() {
        return this.a;
    }

    public boolean isSuccessfull() {
        return this.code != null && (this.code.startsWith("N") || this.code.equals(IAlbumConfig.NET_ERROE_CODE) || this.code.equals(ErrorConstants.API_ERR_CODE_Q305));
    }
}

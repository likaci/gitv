package com.gala.tvapi.vrs.result;

import com.gala.sdk.player.error.ErrorConstants;
import com.gala.tvapi.vrs.core.C0376f;
import com.gala.tvapi.vrs.model.KeepAliveInterval;
import com.gala.video.api.ApiResult;
import com.gala.video.app.epg.ui.albumlist.constant.IAlbumConfig;

public class ApiResultKeepaliveInterval extends ApiResult {
    private boolean f1352a = true;
    public String agenttype = "28";
    public KeepAliveInterval data;
    public String dataString;
    public String sign;

    public boolean checkSign() {
        if (this.sign == null || !C0376f.m807a(this.sign, this.agenttype, this.dataString)) {
            return false;
        }
        return true;
    }

    public void setSendAliveFlag(boolean z) {
        this.f1352a = false;
    }

    public boolean getSendAliveFlag() {
        return this.f1352a;
    }

    public boolean isSuccessfull() {
        return this.code != null && (this.code.startsWith("N") || this.code.equals(IAlbumConfig.NET_ERROE_CODE) || this.code.equals(ErrorConstants.API_ERR_CODE_Q305));
    }
}

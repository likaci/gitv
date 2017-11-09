package com.gala.tvapi.vrs.a;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.vrs.result.ApiResultKeepaliveInterval;
import com.gala.video.api.ApiResult;
import com.mcto.ads.internal.net.PingbackConstants;
import org.cybergarage.upnp.std.av.server.object.SearchCriteria;

public final class a<T extends ApiResult> extends k<T> {
    public final T a(String str, Class<T> cls) {
        T apiResultKeepaliveInterval = new ApiResultKeepaliveInterval();
        JSONObject jSONObject = new JSONObject(true);
        jSONObject = JSON.parseObject(str, Feature.OrderedField);
        String string = jSONObject.getString(PingbackConstants.CODE);
        String string2 = jSONObject.getString("sign");
        apiResultKeepaliveInterval.code = string;
        apiResultKeepaliveInterval.sign = string2;
        apiResultKeepaliveInterval.dataString = jSONObject.getString("data");
        String a = a(this.b, "agenttype");
        if (com.gala.tvapi.b.a.a(a)) {
            a = TVApiBase.getTVApiProperty().getPlatform().getAgentType();
        }
        apiResultKeepaliveInterval.agenttype = a;
        com.gala.tvapi.log.a.a("CheckVipProcess", "agenttype=" + apiResultKeepaliveInterval.agenttype);
        return apiResultKeepaliveInterval;
    }

    private static String a(String str, String str2) {
        if (!(str == null || str.isEmpty())) {
            String[] split = str.split("&");
            if (split != null && split.length > 0) {
                for (String str3 : split) {
                    if (str3.contains(str2)) {
                        String[] split2 = str3.split(SearchCriteria.EQ);
                        if (split2 != null && split2.length == 2) {
                            return split2[1];
                        }
                    }
                }
            }
        }
        return "";
    }
}

package com.gala.tvapi.vrs.p031a;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.gala.tvapi.log.C0262a;
import com.gala.tvapi.p008b.C0214a;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.vrs.result.ApiResultKeepaliveInterval;
import com.gala.video.api.ApiResult;
import com.mcto.ads.internal.net.PingbackConstants;
import org.cybergarage.upnp.std.av.server.object.SearchCriteria;

public final class C0337a<T extends ApiResult> extends C0336k<T> {
    public final T mo850a(String str, Class<T> cls) {
        T apiResultKeepaliveInterval = new ApiResultKeepaliveInterval();
        JSONObject jSONObject = new JSONObject(true);
        jSONObject = JSON.parseObject(str, Feature.OrderedField);
        String string = jSONObject.getString(PingbackConstants.CODE);
        String string2 = jSONObject.getString("sign");
        apiResultKeepaliveInterval.code = string;
        apiResultKeepaliveInterval.sign = string2;
        apiResultKeepaliveInterval.dataString = jSONObject.getString("data");
        String a = C0337a.m763a(this.b, "agenttype");
        if (C0214a.m592a(a)) {
            a = TVApiBase.getTVApiProperty().getPlatform().getAgentType();
        }
        apiResultKeepaliveInterval.agenttype = a;
        C0262a.m629a("CheckVipProcess", "agenttype=" + apiResultKeepaliveInterval.agenttype);
        return apiResultKeepaliveInterval;
    }

    private static String m763a(String str, String str2) {
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

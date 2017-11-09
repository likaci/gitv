package com.gala.tvapi.vrs;

import com.gala.tvapi.b.a;
import com.gala.tvapi.vrs.a.k;
import com.gala.tvapi.vrs.a.l;
import com.gala.tvapi.vrs.core.IVrsServer;
import com.gala.tvapi.vrs.result.ApiResultAreaList;
import com.gala.tvapi.vrs.result.ApiResultLocation;
import com.gala.tvapi.vrs.result.ApiResultWeather;
import com.gala.video.app.epg.ui.setting.SettingConstants;

public class OSHelper extends BaseHelper {
    public static final IVrsServer<ApiResultAreaList> areaList = a.a(new BaseHelper.a(com.gala.tvapi.vrs.core.a.ay), new l(), ApiResultAreaList.class, "weatherArea", false, true);
    public static final IVrsServer<ApiResultLocation> location = a.a(new BaseHelper.a(com.gala.tvapi.vrs.core.a.az), new k(), ApiResultLocation.class, "weatherLocation", false, true);
    public static final IVrsServer<ApiResultWeather> weather = a.a(new BaseHelper.a(com.gala.tvapi.vrs.core.a.aA), new k(), ApiResultWeather.class, SettingConstants.WEATHER, false, true);
}

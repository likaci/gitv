package com.gala.tvapi.vrs;

import com.gala.tvapi.p008b.C0214a;
import com.gala.tvapi.vrs.BaseHelper.C0328a;
import com.gala.tvapi.vrs.core.C0365a;
import com.gala.tvapi.vrs.core.IVrsServer;
import com.gala.tvapi.vrs.p031a.C0336k;
import com.gala.tvapi.vrs.p031a.C0353l;
import com.gala.tvapi.vrs.result.ApiResultAreaList;
import com.gala.tvapi.vrs.result.ApiResultLocation;
import com.gala.tvapi.vrs.result.ApiResultWeather;
import com.gala.video.app.epg.ui.setting.SettingConstants;

public class OSHelper extends BaseHelper {
    public static final IVrsServer<ApiResultAreaList> areaList = C0214a.m581a(new C0328a(C0365a.ay), new C0353l(), ApiResultAreaList.class, "weatherArea", false, true);
    public static final IVrsServer<ApiResultLocation> location = C0214a.m581a(new C0328a(C0365a.az), new C0336k(), ApiResultLocation.class, "weatherLocation", false, true);
    public static final IVrsServer<ApiResultWeather> weather = C0214a.m581a(new C0328a(C0365a.aA), new C0336k(), ApiResultWeather.class, SettingConstants.WEATHER, false, true);
}

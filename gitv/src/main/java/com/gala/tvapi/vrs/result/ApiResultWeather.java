package com.gala.tvapi.vrs.result;

import com.gala.tvapi.vrs.model.WeatherResult;
import com.gala.video.api.ApiResult;

public class ApiResultWeather extends ApiResult {
    public String date = "";
    public String error = "";
    public WeatherResult results;
    public String status = "";
}

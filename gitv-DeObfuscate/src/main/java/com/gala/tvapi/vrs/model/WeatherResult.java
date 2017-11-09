package com.gala.tvapi.vrs.model;

import java.util.List;

public class WeatherResult extends Model {
    private static final long serialVersionUID = 1;
    public String currentCity = "";
    public List<WeatherTrip> index;
    public String pm25 = "";
    public List<WeatherData> weather_data;
}

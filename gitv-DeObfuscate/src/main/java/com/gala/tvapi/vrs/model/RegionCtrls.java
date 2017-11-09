package com.gala.tvapi.vrs.model;

public class RegionCtrls extends Model {
    private static final long serialVersionUID = 1;
    public String areaIds;
    public String cityIds;
    public String continentIds;
    public String countryIds;
    public String provinceIds;
    public int status;

    public String[] getProvinceIds() {
        if (this.provinceIds == null || this.provinceIds.isEmpty()) {
            return null;
        }
        return this.provinceIds.split(",");
    }

    public String[] getAreaIds() {
        if (this.areaIds == null || this.areaIds.isEmpty()) {
            return null;
        }
        return this.areaIds.split(",");
    }

    public String[] getCountryIds() {
        if (this.countryIds == null || this.countryIds.isEmpty()) {
            return null;
        }
        return this.countryIds.split(",");
    }

    public String[] getContinentIds() {
        if (this.continentIds == null || this.continentIds.isEmpty()) {
            return null;
        }
        return this.continentIds.split(",");
    }

    public String[] getCityIds() {
        if (this.cityIds == null || this.cityIds.isEmpty()) {
            return null;
        }
        return this.cityIds.split(",");
    }
}

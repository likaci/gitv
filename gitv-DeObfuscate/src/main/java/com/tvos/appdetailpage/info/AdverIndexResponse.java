package com.tvos.appdetailpage.info;

import java.util.ArrayList;

public class AdverIndexResponse {
    public String code;
    public ArrayList<AdverIndexInfo> data;
    public String msg;

    public static class AdverIndexInfo {
        public String appRating;
        public String appTitle;
        public String appType;
        public String appVersion;
        public String app_id;
        public String descriptionDetails;
        public long downloadCount;
        public String downloadUrl;
        public String img_url;
        public String logoUrl;
        public String packageName;
        public long packageSize;
        public String platform_id;
        public String publishTime;
        public ArrayList<String> res;
    }
}

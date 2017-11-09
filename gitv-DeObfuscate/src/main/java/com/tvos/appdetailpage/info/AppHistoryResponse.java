package com.tvos.appdetailpage.info;

import java.util.ArrayList;

public class AppHistoryResponse {
    public String code;
    public ArrayList<AppHistoryInfo> data;
    public int next;

    public static class AppHistoryInfo {
        public String app_ver_code;
        public String app_ver_name;
        public String change_log;
        public String publish_time;
    }
}

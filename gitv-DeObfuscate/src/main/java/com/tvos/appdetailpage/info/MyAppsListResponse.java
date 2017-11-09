package com.tvos.appdetailpage.info;

import java.util.ArrayList;

public class MyAppsListResponse {
    public String code;
    public Data data;
    public String msg;

    public static class Data {
        public ArrayList<MyAppInfo> list;
        public int realTotal;
        public int total;
    }
}

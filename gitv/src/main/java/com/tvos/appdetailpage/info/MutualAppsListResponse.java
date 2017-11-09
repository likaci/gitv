package com.tvos.appdetailpage.info;

import java.util.ArrayList;

public class MutualAppsListResponse {
    public String code;
    public Data data;
    public String msg;

    public static class Data {
        public ArrayList<String> list;
        public int total;
    }
}

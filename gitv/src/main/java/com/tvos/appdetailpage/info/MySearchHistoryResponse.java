package com.tvos.appdetailpage.info;

import java.util.ArrayList;

public class MySearchHistoryResponse {
    public String code;
    public Data data;
    public String msg;

    public static class Data {
        public ArrayList<SearchInfo> list;
        public int total;
    }

    public static class SearchInfo {
        public String createTime;
        public String deviceId;
        public String searchText;
        public long uid;
    }
}

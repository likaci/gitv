package com.tvos.appdetailpage.info;

import java.util.ArrayList;

public class RecommendResponse {
    public Attributes attributes;
    public String code;
    public ArrayList<RecommendApp> data;
    public String version;

    public static class Attributes {
        public String area;
        public String bucket;
        public String event_id;
    }
}

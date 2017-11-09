package com.tvos.appdetailpage.info;

import java.util.ArrayList;

public class RankHistoryResponse {
    public String code;
    public ArrayList<RankHistoryInfo> data;

    public static class RankHistoryInfo {
        public String app_ver_code;
        public String overall_rank;
        public String rank1_cnt;
        public String rank2_cnt;
        public String rank3_cnt;
        public String rank4_cnt;
        public String rank5_cnt;
    }
}

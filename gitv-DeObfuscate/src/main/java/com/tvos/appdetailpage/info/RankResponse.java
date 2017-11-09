package com.tvos.appdetailpage.info;

public class RankResponse {
    public String code;
    public RankStat data;
    public RankStat stat;

    public static class RankStat {
        public String overall_rank;
        public int rank1_cnt;
        public int rank2_cnt;
        public int rank3_cnt;
        public int rank4_cnt;
        public int rank5_cnt;
    }
}

package com.gala.video.app.epg.home.data.provider;

import java.util.ArrayList;
import java.util.List;

public class QChannelListDataBuiler {
    public static final int CHANNEL_7DAY = 100004;
    public static final int CHANNEL_CINEMA = 100003;
    public static final int CHANNEL_HOT = 10009;
    public static final int CHANNEL_RECENT_UPDATE = 100004;
    public static final int DAILYNEWS_CHANNEL = 10007;
    private static final int MAX_COUNT = 7;
    public static final int SUBJECT_REVIEW = 10008;
    private static final String TAG = "EPG/home/QChannelListDataBuiler";
    private static final List<String> mFixChannel = new ArrayList();

    static {
        mFixChannel.add(String.valueOf(100003));
        mFixChannel.add(String.valueOf(100004));
        mFixChannel.add(String.valueOf(10009));
        mFixChannel.add(String.valueOf(10008));
        mFixChannel.add(String.valueOf(10007));
    }
}

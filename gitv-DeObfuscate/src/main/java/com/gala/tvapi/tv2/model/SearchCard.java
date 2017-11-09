package com.gala.tvapi.tv2.model;

import com.gala.tvapi.p008b.C0214a;
import com.gala.tvapi.tools.DateLocalThread;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchCard extends Model {
    public static final int ANIME = 3;
    public static final int DEFAULT = 99;
    public static final int LIVE = 9;
    public static final int MOVIE_OFFLINE = 2;
    public static final int MOVIE_ONLINE = 1;
    public static final int PERSON = 8;
    public static final int PLAYLIST = 7;
    public static final int SERIES_OFFLINE = 5;
    public static final int SERIES_ONLINE = 4;
    public static final int SOURCE = 6;
    private static Date mDate = new Date();
    private static final long serialVersionUID = 1;
    public List<Director> actor;
    public int chnId;
    public String cnPubDate = "";
    public String director;
    public List<Director> directorList;
    public int isNew;
    public String len;
    public String mainActor;
    public String name;
    public List<Album> prevues;
    public List<Recom> recoms;
    public String shortName;
    public String sliveTime;
    public String strategy;
    public String tag;
    public String time;
    public int tvCount;
    public String tvName;
    public int tvsets;
    public int type = 99;
    public List<Video> videos;
    public String viewerShip;

    public List<String> getCardInfo() {
        switch (this.type) {
            case 1:
                return getMovieInfoOnline();
            case 2:
                return getMovieInfoOffline();
            case 3:
                return getAnimeInfo();
            case 4:
                return getSeriesInfoOnline();
            case 5:
                return getSeriesInfoOffline();
            case 6:
                return getSourceInfo();
            case 7:
                return getPlayListInfo();
            case 8:
                return getPersonInfo();
            case 9:
                return getLiveInfo();
            default:
                return null;
        }
    }

    private List<String> getMovieInfoOnline() {
        List<String> arrayList = new ArrayList();
        arrayList.add(checkInfo("", getName()));
        arrayList.add(checkInfo("上映：", dateToStr1(getDate(this.time))));
        arrayList.add(checkInfo("片长：", getPlayLength(this.len)));
        arrayList.add(checkInfo("导演：", this.director));
        arrayList.add(checkInfo("主演：", getMainActor()));
        arrayList.add(checkInfo("类型：", getTag()));
        return arrayList;
    }

    private List<String> getMovieInfoOffline() {
        List<String> arrayList = new ArrayList();
        arrayList.add(checkInfo("", getName()));
        arrayList.add(checkInfo("上映：", dateToStr1(getDate(this.cnPubDate))));
        try {
            arrayList.add(checkInfo("导演：", ((Director) this.directorList.get(0)).name));
        } catch (Exception e) {
            arrayList.add("");
        }
        arrayList.add(checkInfo("主演：", getOffLineActor()));
        return arrayList;
    }

    private List<String> getAnimeInfo() {
        List<String> arrayList = new ArrayList();
        arrayList.add(checkInfo("", getName()));
        arrayList.add(checkInfo("上映：", dateToStr1(getDate(this.time))));
        arrayList.add(checkInfo("片长：", getPlayLength(this.len)));
        arrayList.add(checkInfo("类型：", getTag()));
        return arrayList;
    }

    private List<String> getSeriesInfoOnline() {
        List<String> arrayList = new ArrayList();
        arrayList.add(checkInfo("", getName()));
        arrayList.add(getOrder());
        arrayList.add(checkInfo("", this.strategy));
        if (this.chnId == 2) {
            arrayList.add(checkInfo("主演：", getMainActor()));
        } else {
            arrayList.add("");
        }
        arrayList.add(checkInfo("类型：", getTag()));
        return arrayList;
    }

    private List<String> getSeriesInfoOffline() {
        List<String> arrayList = new ArrayList();
        arrayList.add(checkInfo("", getName()));
        arrayList.add(checkInfo("上映：", dateToStr1(getDate(this.cnPubDate))));
        arrayList.add(checkInfo("主演：", getOffLineActor()));
        return arrayList;
    }

    private List<String> getSourceInfo() {
        List<String> arrayList = new ArrayList();
        arrayList.add(checkInfo("", getName()));
        arrayList.add(checkInfo("", this.tvName));
        arrayList.add(checkInfo("更新至", dateToStr2(getDate(this.time))));
        arrayList.add(checkInfo("", this.strategy));
        return arrayList;
    }

    private List<String> getPlayListInfo() {
        List<String> arrayList = new ArrayList();
        arrayList.add(checkInfo("", getName()));
        if (this.videos == null || this.videos.isEmpty() || this.videos.size() <= 0) {
            arrayList.add("");
            arrayList.add("");
            arrayList.add("");
        } else {
            for (int i = 0; i < 3; i++) {
                if (i < this.videos.size()) {
                    arrayList.add(checkInfo("", ((Video) this.videos.get(i)).name));
                } else {
                    arrayList.add("");
                }
            }
        }
        return arrayList;
    }

    private List<String> getPersonInfo() {
        List<String> arrayList = new ArrayList();
        arrayList.add(checkInfo("", getName()));
        if (this.recoms == null || this.recoms.isEmpty() || this.recoms.size() <= 0) {
            arrayList.add("");
            arrayList.add("");
            arrayList.add("");
        } else {
            for (int i = 0; i < 3; i++) {
                if (i < this.recoms.size()) {
                    arrayList.add(checkInfo("", getRecomName((Recom) this.recoms.get(i))));
                } else {
                    arrayList.add("");
                }
            }
        }
        return arrayList;
    }

    private List<String> getLiveInfo() {
        List<String> arrayList = new ArrayList();
        arrayList.add(checkInfo("", this.name));
        arrayList.add(checkInfo("", this.shortName));
        return arrayList;
    }

    private String getOffLineActor() {
        String str = "";
        int i = 0;
        while (i < 3) {
            try {
                if (i >= this.actor.size()) {
                    return str;
                }
                String str2 = str + ((Director) this.actor.get(i)).name + " ";
                i++;
                str = str2;
            } catch (Exception e) {
                return "";
            }
        }
        return str;
    }

    private String checkInfo(String title, String info) {
        if (C0214a.m592a(info)) {
            return "";
        }
        return title + info;
    }

    private String getTag() {
        try {
            String[] split = this.tag.split(",");
            String str = "";
            int i = 0;
            while (i < 4 && i < split.length) {
                String str2 = str + split[i] + " ";
                i++;
                str = str2;
            }
            return str;
        } catch (Exception e) {
            return "";
        }
    }

    private String getMainActor() {
        try {
            String[] split = this.mainActor.split(",");
            String str = "";
            int i = 0;
            while (i < 3 && i < split.length) {
                String str2 = str + split[i] + " ";
                i++;
                str = str2;
            }
            return str;
        } catch (Exception e) {
            return "";
        }
    }

    private Date getDate(String str) {
        try {
            mDate = DateLocalThread.parseY(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mDate;
    }

    private String dateToStr1(Date date) {
        String str = "";
        if (date != null) {
            return DateLocalThread.formatY2(date);
        }
        return str;
    }

    private String dateToStr2(Date date) {
        String str = "";
        if (date != null) {
            return DateLocalThread.formatY1(date);
        }
        return str;
    }

    private String dateToStr3(Date date) {
        String str = "";
        if (date != null) {
            return DateLocalThread.formatM(date);
        }
        return str;
    }

    private String getPlayLength(String len) {
        int intValue = Integer.valueOf(len).intValue() / 60;
        if (intValue == 0) {
            intValue++;
        }
        return intValue + "分钟";
    }

    private String getOrder() {
        if (this.tvsets == this.tvCount || this.tvCount == 0) {
            if (this.tvsets != this.tvCount || this.tvsets == 0) {
                return "";
            }
            return this.tvCount + "集全";
        } else if (this.tvsets > this.tvCount) {
            return "更新至" + this.tvCount + "集（共" + this.tvsets + "集）";
        } else {
            return "更新至" + this.tvCount + "集";
        }
    }

    private String getRecomName(Recom r) {
        if (!C0214a.m592a(r.chnName)) {
            if (!C0214a.m592a(r.name)) {
                return r.chnName + " 《" + r.name + "》";
            }
        }
        return "";
    }

    public long getDaysLater() {
        if (C0214a.m592a(this.cnPubDate)) {
            return 0;
        }
        return ((((getDate(this.cnPubDate).getTime() - System.currentTimeMillis()) / 1000) / 60) / 60) / 24;
    }

    public String getName() {
        if (!C0214a.m592a(this.shortName)) {
            return this.shortName;
        }
        if (C0214a.m592a(this.name)) {
            return "";
        }
        return this.name;
    }
}

package com.gala.tvapi.tv2.model;

import android.annotation.SuppressLint;
import com.gala.tvapi.p008b.C0214a;
import com.gala.tvapi.type.ChannelLayoutType;
import com.gala.tvapi.type.ChannelType;
import com.gala.tvapi.type.FunType;
import com.gala.tvapi.type.PicSpecType;
import com.gala.tvapi.type.PropType;
import java.util.List;

public class Channel extends Model {
    public static final int CHANNEL_ALBUM_LIST_SHOW_TYPE_HORIAONTAL = 1;
    public static final int CHANNEL_ALBUM_LIST_SHOW_TYPE_HV_MIX = 3;
    public static final int CHANNEL_ALBUM_LIST_SHOW_TYPE_VERTICAL = 2;
    private static final long serialVersionUID = 1;
    public String HOT_TAG = "";
    public String NEW_TAG = "";
    public String back = "";
    public String focus = "";
    public String icon = "";
    public String id = "";
    public int layout = 0;
    public String name = "";
    public String pic2k = "";
    public String picUrl = "";
    public List<ChannelPool> prosVals;
    public String qipuId = "";
    public int recPlay = 0;
    public String recRes = "";
    public String recResGroupId;
    public String recTag = "";
    public int run = 0;
    public int seq = 0;
    public int simu = 0;
    public int spec = 0;
    public List<TwoLevelTag> tags;
    public int type = 0;

    public ChannelType getChannelType() {
        switch (this.type) {
            case 0:
                return ChannelType.REAL_CHANNEL;
            case 1:
                return ChannelType.TOPIC_CHANNEL;
            case 2:
                return ChannelType.VIRTUAL_CHANNEL;
            case 3:
                return ChannelType.FUNCTION_CHANNEL;
            default:
                return ChannelType.REAL_CHANNEL;
        }
    }

    public boolean isSimulcast() {
        return this.simu != 0;
    }

    public ChannelLayoutType getLayoutType() {
        switch (this.layout) {
            case 1:
                return ChannelLayoutType.HORIZONTAL;
            case 2:
                return ChannelLayoutType.VERTICAL;
            case 3:
                return ChannelLayoutType.INFORMATION;
            case 4:
                return ChannelLayoutType.MUSIC;
            case 5:
                return ChannelLayoutType.PLAY;
            default:
                return ChannelLayoutType.HORIZONTAL;
        }
    }

    public PicSpecType getPictureType() {
        switch (this.spec) {
            case 1:
                return PicSpecType.LANDSCAPE;
            case 2:
                return PicSpecType.PORTRAIT;
            case 3:
                return PicSpecType.MIXING;
            default:
                return PicSpecType.OTHER;
        }
    }

    public boolean hasRecommendList() {
        if (Integer.valueOf(this.id).intValue() != 1 && Integer.valueOf(this.id).intValue() != 2 && Integer.valueOf(this.id).intValue() != 4 && Integer.valueOf(this.id).intValue() != 5 && Integer.valueOf(this.id).intValue() != 15) {
            return false;
        }
        if (C0214a.m592a(this.focus)) {
            return false;
        }
        return true;
    }

    public boolean hasPlayList() {
        return this.recPlay != 0;
    }

    public boolean isRun() {
        return this.run != 0;
    }

    public boolean hasChannelLabels() {
        return !C0214a.m592a(this.recTag);
    }

    @SuppressLint({"DefaultLocale"})
    public List<ChannelPool> getChannelPoolsList() {
        if (this.prosVals != null && this.prosVals.size() > 0) {
            for (ChannelPool channelPool : this.prosVals) {
                if (channelPool.props != null && channelPool.props.size() > 0) {
                    for (Prop prop : channelPool.props) {
                        if (prop.type == PropType.FUNCTION.getValue() && prop.abProps != null && prop.abProps.size() > 0) {
                            for (AbProp abProp : prop.abProps) {
                                if (abProp.abKey.equals(FunType.HOTEST.getValue())) {
                                    if (this.HOT_TAG == null || this.HOT_TAG.equals("")) {
                                        setMutilTagValue();
                                    }
                                    abProp.tagId = this.HOT_TAG;
                                } else if (abProp.abKey.equals(FunType.NEWEST.getValue())) {
                                    if (this.NEW_TAG == null || this.NEW_TAG.equals("")) {
                                        setMutilTagValue();
                                    }
                                    abProp.tagId = this.NEW_TAG;
                                } else if (abProp.abKey.equals(FunType.MYMOVIE.getValue())) {
                                    abProp.tagId = this.id;
                                }
                            }
                        }
                    }
                }
            }
        }
        return this.prosVals;
    }

    private void setMutilTagValue() {
        String str = "";
        if (this.tags != null && this.tags.size() > 0) {
            for (TwoLevelTag twoLevelTag : this.tags) {
                if (twoLevelTag.tags != null && twoLevelTag.tags.size() > 0) {
                    List list = twoLevelTag.tags;
                    if (list != null && list.size() > 0) {
                        int i = 0;
                        while (i < list.size()) {
                            String str2;
                            if (((ThreeLevelTag) list.get(i)).f1016v.equals("11;sort")) {
                                this.HOT_TAG = str + "11;sort";
                                str2 = str;
                            } else if (((ThreeLevelTag) list.get(i)).f1016v.equals("4;sort")) {
                                this.NEW_TAG = str + "4;sort";
                                str2 = str;
                            } else if (i == 0) {
                                str2 = str + ((ThreeLevelTag) list.get(0)).f1016v + ",";
                            } else {
                                str2 = str;
                            }
                            i++;
                            str = str2;
                        }
                    }
                }
            }
        }
    }
}

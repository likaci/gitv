package com.gala.albumprovider.logic.source;

import android.content.Context;
import android.util.SparseArray;
import com.gala.albumprovider.model.LibString;
import com.gala.albumprovider.model.QChannel;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.albumprovider.model.Tag;
import com.gala.albumprovider.private.g;
import com.gala.albumprovider.util.DefaultMenus;
import java.util.List;

public class SourceTool {
    public static final String ALBUM_TYPE = "album";
    public static final String CARD_TAG = "-114";
    public static final String CHANNEL_ID_LIVE = "1000004";
    public static final String COLLECT_TAG = "-110";
    public static final String HISTORY_TAG = "-111";
    public static final String LABEL_CHANNEL_TAG = "-103";
    public static final String MULTI_CHANNEL_TAG = "-100";
    public static final String NO_TAG = "-1000";
    public static final String PEOPLE_TAG = "-105";
    public static final String PLAYLIST_TAG = "-107";
    public static final String PLAYLIST_TYPE = "collection";
    public static final String PLAY_CHANNEL_TAG = "-102";
    public static final String REC_CHANNEL_TAG = "-101";
    public static final String RESOURCE_TYPE = "resource";
    public static final String SEARCH_TAG = "-104";
    public static final String SUBSCRIBE_TAG = "-109";
    public static final String TOPIC_CHANNEL_TAG = "-108";
    public static final String TVTAG = "tvtag";
    public static final String VIRTUAL_CHANNEL_TAG = "-106";
    public static Context gContext = null;
    public static final String gHotestTagValue = "11;sort";
    public static final String gNewestTagValue = "4;sort";

    public static void setContent(Context ctx) {
        gContext = ctx;
    }

    public static Tag getAlbumTag(String tagId, List<Tag> tags) {
        if (tagId != null) {
            for (Tag tag : tags) {
                if (tag.getID().equals(tagId)) {
                    return tag;
                }
            }
        }
        return null;
    }

    public static void createTags(List<Tag> tags, boolean isAddAllTag) {
        if (isAddAllTag) {
            tags.add(new Tag(DefaultMenus.TagAll.id, DefaultMenus.TagAll.name));
        }
        SparseArray a = g.a().a();
        int i = 1;
        int size = a.size();
        int i2 = 0;
        while (i2 < size) {
            QChannel qChannel = (QChannel) a.valueAt(i2);
            if (i <= 13) {
                int i3;
                if (qChannel == null || qChannel.isTopic() || qChannel.isVirtual()) {
                    i3 = i;
                } else {
                    tags.add(new Tag(qChannel.id, qChannel.name));
                    i3 = i + 1;
                }
                i2++;
                i = i3;
            } else {
                return;
            }
        }
    }

    public static Tag getTag(String channelId, String tagId) {
        if (tagId == null || tagId.equals("")) {
            return new Tag(DefaultMenus.TagAll.id, DefaultMenus.TagAll.name);
        }
        return new Tag(tagId, null);
    }

    public static String getMultiTagsValue(String values) {
        if (values != null) {
            return values.substring(0, values.length());
        }
        return null;
    }

    public static Tag getDefaultTag() {
        return new Tag("0", LibString.DefaultTagName, NO_TAG);
    }

    public static QLayoutKind setLayoutKind(String tagId) {
        QChannel a = g.a().a(tagId);
        if (a == null) {
            return QLayoutKind.PORTRAIT;
        }
        if (a.id.equals("6")) {
            return QLayoutKind.PORTRAIT;
        }
        return a.getLayoutKind();
    }

    public static String getRecommendTagName() {
        return LibString.RecommendTagName;
    }

    public static String getHotTagName() {
        return LibString.HotTagName;
    }

    public static String getNewestTagName() {
        return LibString.NewestTagName;
    }

    public static String getChannelPlayListTagName() {
        return LibString.ChannelPlayListTagName;
    }
}

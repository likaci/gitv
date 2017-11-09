package com.gala.video.app.epg.ui.albumlist.utils;

import com.gala.albumprovider.logic.source.SourceTool;
import com.gala.albumprovider.model.Tag;
import com.gala.tvapi.vrs.model.TVTag;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import java.util.List;

public class TagUtils {
    public static int getIndex(String tagId, List<Tag> tags) {
        if (ListUtils.isEmpty((List) tags) || StringUtils.isEmpty((CharSequence) tagId)) {
            return 0;
        }
        int i = 0;
        for (Tag tag : tags) {
            if (tag.getID().equals(tagId)) {
                return i;
            }
            i++;
        }
        return 0;
    }

    public static int getRecommendTagIndex(List<Tag> tags) {
        if (ListUtils.isEmpty((List) tags)) {
            return -1;
        }
        int i = 0;
        for (Tag tag : tags) {
            if (SourceTool.REC_CHANNEL_TAG.equals(tag.getType())) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static String[] getIds(List<TVTag> tags) {
        int count = ListUtils.getCount((List) tags);
        if (count == 0) {
            return null;
        }
        String[] ids = new String[count];
        for (int i = 0; i < count; i++) {
            TVTag tvTag = (TVTag) tags.get(i);
            if (tvTag != null) {
                ids[i] = tvTag.value;
            }
        }
        return ids;
    }
}

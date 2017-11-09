package com.gala.albumprovider.logic.source.search;

import android.util.SparseArray;
import com.gala.albumprovider.AlbumProviderApi;
import com.gala.albumprovider.base.IAlbumSet;
import com.gala.albumprovider.logic.set.search.SearchAlbumSet;
import com.gala.albumprovider.logic.set.search.SearchPeopleSet;
import com.gala.albumprovider.logic.source.SourceTool;
import com.gala.albumprovider.model.LibString;
import com.gala.albumprovider.model.QChannel;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.albumprovider.model.Tag;
import com.gala.albumprovider.p001private.C0060i;
import com.gala.albumprovider.p001private.C0067g;

public class AlbumSearchSource extends C0060i {
    private String f241a = null;

    public AlbumSearchSource(String keyword) {
        this.f241a = keyword;
    }

    public IAlbumSet getSearchAlbumSet(Tag tag) {
        return new SearchAlbumSet(this.f241a, tag);
    }

    public IAlbumSet getSearchPeopleSet(String pid, Tag tag) {
        return new SearchPeopleSet(pid, tag.getID());
    }

    public String getChannelName() {
        return AlbumProviderApi.getLanguages().getSearchName();
    }

    public Tag getDefaultTag() {
        return new Tag("0", LibString.DefaultTagName, SourceTool.SEARCH_TAG, QLayoutKind.PORTRAIT);
    }

    public Tag getPeopleDefaultTag() {
        String str = "";
        SparseArray a = C0067g.m139a().m139a();
        int size = a.size();
        int i = 0;
        while (i < size) {
            String str2;
            QChannel qChannel = (QChannel) a.valueAt(i);
            if (qChannel == null || qChannel.isTopic() || qChannel.isVirtual()) {
                str2 = str;
            } else {
                str2 = str + qChannel.id + ",";
            }
            i++;
            str = str2;
        }
        return new Tag(str.substring(0, str.length()), LibString.DefaultTagName, SourceTool.PEOPLE_TAG, QLayoutKind.PORTRAIT);
    }
}

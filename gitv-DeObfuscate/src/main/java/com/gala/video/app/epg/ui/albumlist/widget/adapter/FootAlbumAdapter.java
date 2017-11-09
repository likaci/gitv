package com.gala.video.app.epg.ui.albumlist.widget.adapter;

import android.content.Context;
import com.gala.albumprovider.model.Tag;
import com.gala.video.app.epg.ui.albumlist.model.AlbumInfoModel;
import java.util.List;

public class FootAlbumAdapter extends LabelAlbumAdapter {
    public FootAlbumAdapter(Context context, List<Tag> list, AlbumInfoModel infoModel) {
        super(context, list, infoModel);
    }

    public int getItemViewType(int position) {
        if (((Tag) this.mDataList.get(position)).getLevel() == 1) {
            return 1;
        }
        return 0;
    }
}

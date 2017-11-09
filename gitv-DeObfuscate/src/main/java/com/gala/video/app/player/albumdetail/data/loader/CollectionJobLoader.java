package com.gala.video.app.player.albumdetail.data.loader;

import android.content.Context;
import com.gala.video.app.player.albumdetail.data.AlbumInfo;
import com.gala.video.app.player.albumdetail.data.job.CancelCollectJob;
import com.gala.video.app.player.albumdetail.data.job.UploadCollectJob;
import com.gala.video.app.player.albumdetail.data.loader.DetailAlbumLoader.LoadType;
import com.gala.video.app.player.albumdetail.data.loader.DetailAlbumLoader.MyVideoJobListener;

public class CollectionJobLoader extends DetailAlbumLoader {
    public CollectionJobLoader(Context context, AlbumInfo albumInfo) {
        super(context, albumInfo);
    }

    public void collectUpload() {
        submit(new UploadCollectJob(getInfo(), new MyVideoJobListener(this, 7)));
    }

    public void collectCancel() {
        submit(new CancelCollectJob(getInfo(), new MyVideoJobListener(this, 7)));
    }

    public void dataLoad(LoadType l) {
    }
}

package com.gala.video.app.player.albumdetail;

import android.view.ViewGroup.LayoutParams;
import com.gala.sdk.player.PlayParams;
import com.gala.sdk.player.ScreenMode;
import com.gala.sdk.player.data.IVideo;
import com.gala.tv.voice.service.AbsVoiceAction;
import com.gala.video.app.player.albumdetail.data.AlbumInfo;
import com.gala.video.app.player.albumdetail.data.AlbumVideoItem;
import com.gala.video.lib.share.ifimpl.multisubject.MultiSubjectVGridView;
import java.util.List;

public class DetailViewContract {

    public interface Presenter {
        AlbumInfo getAlbumInfo();

        AlbumVideoItem getAlbumVideoItem();

        void setView(View view);
    }

    public interface View {
        void erasePlayingIcon();

        LayoutParams getLayoutParams();

        List<AbsVoiceAction> getPlayerSupportedVoices(List<AbsVoiceAction> list);

        void notifyBasicInfoReady(AlbumInfo albumInfo);

        void notifyCouponReady();

        void notifyFavInfoReady(AlbumInfo albumInfo);

        void notifyPlayFinished();

        void notifyScreenModeSwitched(ScreenMode screenMode, boolean z);

        void notifyTvodReady();

        void notifyVIPInfoReady();

        void notifyVideoPlayFinished();

        void resetBasicInfo(IVideo iVideo, MultiSubjectVGridView multiSubjectVGridView);

        void setAlbumInfo(AlbumInfo albumInfo);

        void setSummaryFocus();

        void startTrailers(PlayParams playParams);
    }
}

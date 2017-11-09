package com.gala.video.app.epg.ui.albumlist.fragment.right.recommend;

import android.graphics.Bitmap;
import android.os.Message;
import com.gala.imageprovider.ImageProviderApi;
import com.gala.imageprovider.base.ImageRequest;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.albumlist.adapter.BaseGridAdapter;
import com.gala.video.app.epg.ui.albumlist.adapter.GridAdapter;
import com.gala.video.app.epg.ui.albumlist.constant.IAlbumConfig;
import com.gala.video.lib.framework.core.utils.BitmapUtils;
import com.gala.video.lib.share.common.configs.ViewConstant.AlbumViewType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import java.util.List;

public class ChannelRecommend1Fragment extends ChannelRecommendBaseFragment {
    public void handlerMessage2Right(Message msg) {
        super.handlerMessage2Right(msg);
        if (msg != null && msg.what == 51) {
            log(NOLOG ? null : "---handlerMessage2Right---refresh ChannelRecommend1Fragment");
            ImageProviderApi.getImageProvider().stopAllTasks();
            if (getInfoModel() != null) {
                getDataApi();
                loadData();
            }
        }
    }

    public void onImageSuccess(ChannelRecommendBaseFragment outer, ImageRequest request, Bitmap bitmap) {
        super.onImageSuccess(outer, request, bitmap);
    }

    private Bitmap createScaleBitmap(int width, int height, Bitmap bit) {
        Bitmap bitmap = null;
        if (bit != null) {
            try {
                bitmap = Bitmap.createBitmap(BitmapUtils.scale(bit, Math.max(((float) width) / ((float) bit.getWidth()), (float) (height / bit.getHeight()))), 0, 0, width, height);
                return bitmap;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }
        }
        return null;
    }

    protected int getBigViewWidth() {
        return getDimen(R.dimen.dimen_968dp);
    }

    protected int getBigViewHeight() {
        return getDimen(R.dimen.dimen_245dp);
    }

    protected int getLayoutResId() {
        return R.layout.epg_channel_recommed_page1;
    }

    protected void setBigViewData() {
        if (this.mBigViewData == null) {
            log(NOLOG ? null : "setBigViewData---mBigViewData is null ");
            return;
        }
        this.mBigView.setTitle(this.mBigViewData.getText(1));
        this.mBigView.setFocusScale(1.03f);
        loadBigView(getBigViewWidth());
    }

    protected void initCartoonViews() {
    }

    protected List<IData> getRealDataList(List<IData> list) {
        return list;
    }

    protected void setCartoonData() {
    }

    protected String getLogCatTag() {
        return IAlbumConfig.UNIQUE_CHANNEL_RECOMMEND1;
    }

    protected void showCache4CartoonView() {
    }

    protected BaseGridAdapter<IData> getAdapter() {
        return new GridAdapter(this.mContext, AlbumViewType.RECOMMEND_VERTICAL);
    }

    protected int getMaxDisplaySize() {
        return 5;
    }
}

package com.gala.video.app.epg.ui.albumlist.fragment.right.recommend;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.imageprovider.ImageProviderApi;
import com.gala.imageprovider.base.IImageCallback;
import com.gala.imageprovider.base.ImageRequest;
import com.gala.tvapi.type.ResourceType;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.albumlist.adapter.BaseGridAdapter;
import com.gala.video.app.epg.ui.albumlist.adapter.GridAdapter;
import com.gala.video.app.epg.ui.albumlist.constant.IAlbumConfig;
import com.gala.video.app.epg.ui.albumlist.data.type.AlbumData;
import com.gala.video.app.epg.ui.albumlist.model.CacheAlbum;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.app.epg.ui.albumlist.widget.PhotoGridParams;
import com.gala.video.app.epg.ui.albumlist.widget.PhotoGridView;
import com.gala.video.app.epg.ui.albumlist.widget.WidgetStatusListener;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.common.configs.ViewConstant.AlbumViewType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.lib.share.utils.ImageCacheUtil;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ChannelRecommend2Fragment extends ChannelRecommendBaseFragment {
    private List<IData> mCartoonDataList;
    private List<IData> mCartoonSrcDataList;
    private CartoonCircleAdapter mCurtoonAdapter;
    private WidgetStatusListener mPhotoViewListener = new WidgetStatusListener() {
        public void onLoseFocus(ViewGroup parent, View view, int position) {
        }

        public void onItemTouch(View arg0, MotionEvent arg1, int arg2) {
        }

        public void onItemSelectChange(View v, int position, boolean isSelected) {
            AnimationUtil.zoomAnimation(v, isSelected, 1.12f, 200, true);
            if (isSelected) {
                if (position < 3) {
                    ChannelRecommend2Fragment.this.setNextFocusUpId(v);
                }
                ChannelRecommend2Fragment.this.mCurrentFocusedView = v;
                ChannelRecommend2Fragment.this.setGlobalLastFocusView(v);
            }
        }

        public void onItemClick(ViewGroup viewGroup, View view, int position) {
            if (ChannelRecommend2Fragment.this.mCartoonDataList != null && position < ListUtils.getCount(ChannelRecommend2Fragment.this.mCartoonDataList)) {
                if (position < 3) {
                    ChannelRecommend2Fragment.this.mInfoModel.setRseat("1_" + (position + 2));
                } else {
                    ChannelRecommend2Fragment.this.mInfoModel.setRseat("2_" + (position - 2));
                }
                String buySource = ChannelRecommend2Fragment.this.mBuySourceSrc;
                if (IAlbumConfig.PROJECT_NAME_BASE_LINE.equals(ChannelRecommend2Fragment.this.mInfoModel.getProjectName()) && !TextUtils.isEmpty(buySource) && buySource.contains("rec")) {
                    ChannelRecommend2Fragment.this.setBuySource(buySource + "[" + ChannelRecommend2Fragment.this.mInfoModel.getRseat() + AlbumEnterFactory.SIGN_STR);
                } else if (IAlbumConfig.PROJECT_NAME_OPEN_API.equals(ChannelRecommend2Fragment.this.mInfoModel.getProjectName())) {
                    ChannelRecommend2Fragment.this.setBuySource("openAPI");
                }
                ((IData) ChannelRecommend2Fragment.this.mCartoonDataList.get(position)).click(ChannelRecommend2Fragment.this.mContext, ChannelRecommend2Fragment.this.mInfoModel);
            }
        }
    };

    private class CartoonCircleAdapter extends BaseAdapter {
        private static final int CARTOON_NUM = 6;
        private int pading = ResourceUtil.getPx(16);

        public CartoonCircleAdapter(Context context) {
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView image = new ImageView(ChannelRecommend2Fragment.this.mContext);
            image.setScaleType(ScaleType.FIT_XY);
            image.setFocusable(false);
            image.setImageDrawable(ImageCacheUtil.DEFAULT_CIRCLE_DRAWABLE);
            image.setPadding(this.pading, this.pading, this.pading, this.pading);
            image.setBackgroundResource(R.drawable.epg_cartoon_circle_bg);
            if (ListUtils.getCount(ChannelRecommend2Fragment.this.mCartoonDataList) > position) {
                ImageProviderApi.getImageProvider().loadImage(new ImageRequest(((IData) ChannelRecommend2Fragment.this.mCartoonDataList.get(position)).getImageUrl(1), image), new IImageCallbackImpl(ChannelRecommend2Fragment.this));
            }
            return image;
        }

        public int getCount() {
            return 6;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public void notifyDataSetChanged(List<IData> list) {
            ChannelRecommend2Fragment.this.mCartoonDataList = list;
            super.notifyDataSetChanged();
        }
    }

    private static class IImageCallbackImpl implements IImageCallback {
        WeakReference<ChannelRecommend2Fragment> mOuter;

        public IImageCallbackImpl(ChannelRecommend2Fragment outer) {
            this.mOuter = new WeakReference(outer);
        }

        public void onSuccess(ImageRequest imageRequest, Bitmap bmp) {
            if (((ChannelRecommend2Fragment) this.mOuter.get()) != null) {
                ImageView view = (ImageView) imageRequest.getCookie();
                if (view != null && bmp != null) {
                    view.setImageBitmap(bmp);
                }
            }
        }

        public void onFailure(ImageRequest arg0, Exception e) {
            ChannelRecommend2Fragment outer = (ChannelRecommend2Fragment) this.mOuter.get();
            if (outer != null) {
                outer.log(ChannelRecommend2Fragment.NOLOG ? null : "---ImageRequest---mCartoonList---onException--" + e);
            }
        }
    }

    public void handlerMessage2Right(Message msg) {
        super.handlerMessage2Right(msg);
        if (msg != null && msg.what == 51) {
            log(NOLOG ? null : "---handlerMessage2Right---refresh ChannelRecommend2Fragment");
            ImageProviderApi.getImageProvider().stopAllTasks();
            if (getInfoModel() != null) {
                getDataApi();
                loadData();
            }
        }
    }

    protected int getBigViewWidth() {
        return ResourceUtil.getPx(781);
    }

    protected int getBigViewHeight() {
        return ResourceUtil.getPx(381);
    }

    protected int getLayoutResId() {
        return R.layout.epg_channel_recommed_page2;
    }

    protected void initCartoonViews() {
        this.mPhotoView = (PhotoGridView) this.mMainView.findViewById(R.id.epg_recommend_small_item);
        this.mPhotoView.setNextRightFocusLeaveAvail(false);
        this.mPhotoView.setNextUpFocusLeaveAvail(true);
        this.mPhotoView.setParams(getPhotoViewParams());
        this.mPhotoView.setListener(this.mPhotoViewListener);
        clearDataList();
    }

    protected void setBigViewData() {
        if (this.mBigViewData == null) {
            log(NOLOG ? null : "---setBigViewData()---mBigViewInfo is null ");
            return;
        }
        this.mBigView.setNameText(this.mBigViewData.getText(1));
        this.mBigView.setTitle(this.mBigViewData.getText(2));
        this.mBigView.setFocusScale(1.05f);
        loadBigView(getBigViewWidth());
    }

    protected List<IData> getRealDataList(List<IData> list) {
        List<IData> list2 = null;
        try {
            this.mCartoonSrcDataList = new ArrayList(6);
            this.mCartoonSrcDataList.addAll(list.subList(0, 6));
            list2 = list.subList(6, ListUtils.getCount((List) list));
        } catch (Exception e) {
            log(NOLOG ? list2 : "---getRealDataList---list.size=" + ListUtils.getCount((List) list) + "---e=" + e);
        }
        return list2;
    }

    protected void setCartoonData() {
        String str = null;
        clearDataList();
        if (!ListUtils.isEmpty(this.mCartoonSrcDataList)) {
            log(NOLOG ? null : "---setCartoonData---data.size=" + ListUtils.getCount(this.mCartoonSrcDataList));
            int cartoonMaxCount = 0;
            for (int i = 0; i < ListUtils.getCount(this.mCartoonSrcDataList) && cartoonMaxCount < 6; i++) {
                if (!ResourceType.DIY.equals(((IData) this.mCartoonSrcDataList.get(i)).getResourceType())) {
                    this.mCartoonDataList.add(this.mCartoonSrcDataList.get(i));
                    cartoonMaxCount++;
                }
            }
            if (!NOLOG) {
                str = "---setCartoonData---convert.size=" + ListUtils.getCount(this.mCartoonDataList);
            }
            log(str);
            if (this.mCurtoonAdapter == null) {
                this.mCurtoonAdapter = new CartoonCircleAdapter(this.mContext);
                this.mPhotoView.setAdapter(this.mCurtoonAdapter);
            } else {
                this.mCurtoonAdapter.notifyDataSetChanged(this.mCartoonDataList);
            }
            this.mBigView.setNextFocusRightId(this.mPhotoView.getChildAt(0).getId());
            this.mPhotoView.getChildAt(this.mCurtoonAdapter.getCount() - 1).setNextFocusRightId(R.id.epg_recommend_gridview_item);
        }
    }

    private void clearDataList() {
        if (this.mCartoonDataList == null) {
            this.mCartoonDataList = new ArrayList(6);
        } else {
            this.mCartoonDataList.clear();
        }
    }

    protected void showCache4CartoonView() {
        clearDataList();
        for (int i = 0; i < 6; i++) {
            this.mCartoonDataList.add(new AlbumData(new CacheAlbum(), QLayoutKind.PORTRAIT, 0));
        }
        if (this.mCurtoonAdapter == null) {
            this.mCurtoonAdapter = new CartoonCircleAdapter(this.mContext);
            this.mPhotoView.setAdapter(this.mCurtoonAdapter);
            return;
        }
        this.mCurtoonAdapter.notifyDataSetChanged(this.mCartoonDataList);
    }

    private PhotoGridParams getPhotoViewParams() {
        int itemWidth = ResourceUtil.getPx(203);
        int itemPadding = getDimen(R.dimen.dimen_30dp);
        PhotoGridParams p = new PhotoGridParams();
        p.columnNum = 3;
        p.verticalSpace = ResourceUtil.getPx(6);
        p.horizontalSpace = ResourceUtil.getPx(21);
        p.contentHeight = itemWidth;
        p.contentWidth = itemWidth;
        p.scaleRate = 1.1f;
        return p;
    }

    protected String getLogCatTag() {
        return IAlbumConfig.UNIQUE_CHANNEL_RECOMMEND2;
    }

    protected BaseGridAdapter<IData> getAdapter() {
        return new GridAdapter(this.mContext, AlbumViewType.RECOMMEND_VERTICAL);
    }

    protected int getMaxDisplaySize() {
        return 5;
    }
}

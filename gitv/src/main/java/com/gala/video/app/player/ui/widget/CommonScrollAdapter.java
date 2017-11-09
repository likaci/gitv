package com.gala.video.app.player.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import com.gala.imageprovider.ImageProviderApi;
import com.gala.imageprovider.base.IImageCallback;
import com.gala.imageprovider.base.IImageProvider;
import com.gala.imageprovider.base.ImageRequest;
import com.gala.sdk.player.SourceType;
import com.gala.sdk.player.data.IVideo;
import com.gala.sdk.player.data.IVideo.VideoKind;
import com.gala.tvapi.type.ContentType;
import com.gala.video.albumlist4.widget.RecyclerView.Adapter;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.app.player.R;
import com.gala.video.app.player.data.VideoData;
import com.gala.video.app.player.ui.GalleryCornerHelper;
import com.gala.video.app.player.ui.overlay.UiHelper;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.configs.ViewConstant.AlbumViewType;
import com.gala.video.lib.share.common.widget.AlbumView;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.utils.ImageCacheUtil;
import com.gala.video.lib.share.utils.ResourceUtil;
import com.gala.video.lib.share.utils.TagKeyUtil;
import java.util.ArrayList;
import java.util.List;

public class CommonScrollAdapter extends Adapter<DetailViewHolder> {
    private static final String PINGFEN = "PINGFEN";
    private static final int TAG_KEY_INFO_DATA = TagKeyUtil.generateTagKey();
    private static final int TAG_KEY_SHOW_DEFAULT = TagKeyUtil.generateTagKey();
    private static final int TAG_KEY_SHOW_IMAGE = TagKeyUtil.generateTagKey();
    private final String TAG = "Player/ui/detail/CommonScrollAdapter";
    private AlbumViewType mAlbumViewType;
    private boolean mCanceledTask;
    private Context mContext;
    private List<VideoData> mDataList = new ArrayList();
    private boolean mDirectLoad = false;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private IImageProvider mImageProvider = ImageProviderApi.getImageProvider();
    private boolean mIsOtherType;
    private boolean mIsPort;

    public static class DetailViewHolder extends ViewHolder {
        public DetailViewHolder(View itemView) {
            super(itemView);
        }
    }

    public CommonScrollAdapter(Context context, boolean isPort, AlbumViewType type) {
        this.mContext = context;
        this.mIsPort = isPort;
        this.mAlbumViewType = type;
    }

    public void updateDataSet(List<VideoData> datas) {
        if (LogUtils.mIsDebug) {
            LogUtils.d("Player/ui/detail/CommonScrollAdapter", ">> updateDataSet, datas.size=" + datas.size());
        }
        this.mDataList.clear();
        this.mDataList.addAll(datas);
        notifyDataSetUpdate();
    }

    public void changeDataSet(List<VideoData> datas) {
        if (LogUtils.mIsDebug) {
            LogUtils.d("Player/ui/detail/CommonScrollAdapter", ">> changeDataSet, datas.size=" + datas.size());
        }
        this.mDataList.clear();
        this.mDataList.addAll(datas);
        notifyDataSetChanged();
    }

    private AlbumView createAlbumView() {
        if (LogUtils.mIsDebug) {
            LogUtils.d("Player/ui/detail/CommonScrollAdapter", ">> createAlbumView");
        }
        AlbumView albumView = new AlbumView(this.mContext.getApplicationContext(), this.mAlbumViewType);
        albumView.setTag(TAG_KEY_SHOW_DEFAULT, Boolean.valueOf(true));
        return albumView;
    }

    private boolean needRefreshAlbumView(VideoData newData, VideoData oldData) {
        boolean ret = true;
        if (newData.getData().getTvId().equals(oldData == null ? null : oldData.getData().getTvId())) {
            ret = false;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d("Player/ui/detail/CommonScrollAdapter", "<< needRefreshAlbumView, ret=" + ret);
        }
        return ret;
    }

    private void setAlbumViewContent(int position, AlbumView albumView) {
        if (LogUtils.mIsDebug) {
            LogUtils.d("Player/ui/detail/CommonScrollAdapter", "setAlbumViewContent(" + position + ")");
        }
        if (ListUtils.isEmpty(this.mDataList) || position >= getCount()) {
            LogUtils.e("Player/ui/detail/CommonScrollAdapter", "setAlbumViewContent(" + position + ") invalid position!", new Throwable());
            return;
        }
        VideoData newData = (VideoData) this.mDataList.get(position);
        VideoData lastData = (VideoData) albumView.getTag(TAG_KEY_INFO_DATA);
        albumView.setTag(TAG_KEY_INFO_DATA, newData);
        updatePlaying(albumView);
        if (needRefreshAlbumView(newData, lastData)) {
            albumView.releaseData();
            albumView.setDescLine1Left(getBottomString(albumView, true));
            if (PINGFEN.endsWith(getBottomString(albumView, false))) {
                albumView.setFilmScore(newData.getAlbum().score);
            } else {
                albumView.setDescLine1Right(getBottomString(albumView, false));
            }
            updateImage(albumView);
            updateBottomName(albumView);
        }
    }

    private void setSpecialViewContent(View view) {
        if (LogUtils.mIsDebug) {
            LogUtils.d("Player/ui/detail/CommonScrollAdapter", ">> setSpecialViewContent, view=" + view);
        }
    }

    private void updatePlaying(AlbumView albumView) {
        if (albumView != null) {
            VideoData info = (VideoData) albumView.getTag(TAG_KEY_INFO_DATA);
            if (info != null) {
                boolean playing = info.isPlaying();
                albumView.setPlaying(playing);
                albumView.setLeftBottomConner(info);
                albumView.setDescLine1Left(getBottomString(albumView, true));
                if (LogUtils.mIsDebug) {
                    LogUtils.d("Player/ui/detail/CommonScrollAdapter", "updatePlaying() mIsPort=" + this.mIsPort + ", data=" + info + ", playing=" + playing + ", view=" + albumView);
                }
            } else if (LogUtils.mIsDebug) {
                LogUtils.d("Player/ui/detail/CommonScrollAdapter", "updatePlaying，info is null.");
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.d("Player/ui/detail/CommonScrollAdapter", "updatePlaying，albumView is null.");
        }
    }

    private void updateImage(AlbumView albumView) {
        VideoData data = (VideoData) albumView.getTag(TAG_KEY_INFO_DATA);
        if (data == null) {
            if (LogUtils.mIsDebug) {
                LogUtils.d("Player/ui/detail/CommonScrollAdapter", "updateImage, data is null, reset to default.");
            }
            showDefaultBitmap(albumView);
            return;
        }
        CharSequence imageUrl = data.getImageUrl(2);
        if (StringUtils.isEmpty(imageUrl)) {
            if (LogUtils.mIsDebug) {
                LogUtils.d("Player/ui/detail/CommonScrollAdapter", "updateImage, url is null, reset to default.");
            }
            showDefaultBitmap(albumView);
            return;
        }
        loadBitmap(albumView, imageUrl);
    }

    private void setCorner(AlbumView albumView) {
        VideoData data = (VideoData) albumView.getTag(TAG_KEY_INFO_DATA);
        if (data != null) {
            albumView.setCorner(data);
        } else if (LogUtils.mIsDebug) {
            LogUtils.d("Player/ui/detail/CommonScrollAdapter", "updateImage, data is null.");
        }
    }

    public void clearData(AlbumView albumView) {
        albumView.setTag(TAG_KEY_INFO_DATA, null);
        albumView.setTag(TAG_KEY_SHOW_IMAGE, null);
    }

    private void loadBitmap(View convertView, String imageUrl) {
        boolean z = true;
        if (convertView == null) {
            if (LogUtils.mIsDebug) {
                LogUtils.d("Player/ui/detail/CommonScrollAdapter", "loadBitmap( return convertView == null !! imageUrl = " + imageUrl);
            }
        } else if (StringUtils.isEmpty((CharSequence) imageUrl)) {
            if (LogUtils.mIsDebug) {
                LogUtils.d("Player/ui/detail/CommonScrollAdapter", "loadBitmap( return imageUrl has null = " + imageUrl);
            }
            showDefaultBitmap(convertView);
        } else {
            if (!imageUrl.equals(convertView.getTag(TAG_KEY_SHOW_IMAGE))) {
                showDefaultBitmap(convertView);
                this.mDirectLoad = true;
            }
            convertView.setTag(TAG_KEY_SHOW_IMAGE, imageUrl);
            if (LogUtils.mIsDebug) {
                String str = "Player/ui/detail/CommonScrollAdapter";
                StringBuilder append = new StringBuilder().append("loadBitmap( mCanceledTask ").append(this.mCanceledTask).append(", middle ");
                if (isShowingDefault(convertView)) {
                    z = false;
                }
                LogUtils.d(str, append.append(z).append(",end ").append(this.mDirectLoad).toString());
            }
            if (!this.mCanceledTask && (isShowingDefault(convertView) || this.mDirectLoad)) {
                if (LogUtils.mIsDebug) {
                    LogUtils.d("Player/ui/detail/CommonScrollAdapter", "loadBitmap( imageUrl = " + imageUrl);
                }
                this.mImageProvider.loadImage(getImageRequest(new ImageRequest(imageUrl, convertView)), new IImageCallback() {
                    public void onSuccess(ImageRequest imageRequest, Bitmap bitmap) {
                        if (bitmap != null) {
                            Object cookie = imageRequest.getCookie();
                            if (!CommonScrollAdapter.this.mCanceledTask && cookie != null) {
                                CommonScrollAdapter.this.requestBitmapSucc(imageRequest.getUrl(), bitmap, cookie);
                            }
                        } else if (LogUtils.mIsDebug) {
                            LogUtils.d("Player/ui/detail/CommonScrollAdapter", "loadBitmap >> onSuccess-------  netBitmap = null !! ");
                        }
                    }

                    public void onFailure(ImageRequest imageRequest, Exception exception) {
                        if (LogUtils.mIsDebug) {
                            LogUtils.d("Player/ui/detail/CommonScrollAdapter", "loadBitmap >> onFailure ------- !! ");
                        }
                        Object cookie = imageRequest.getCookie();
                        if (!CommonScrollAdapter.this.mCanceledTask && cookie != null) {
                            CommonScrollAdapter.this.requestBitmapFail(imageRequest.getUrl(), cookie, exception);
                        }
                    }
                });
            } else if (LogUtils.mIsDebug) {
                LogUtils.d("Player/ui/detail/CommonScrollAdapter", "loadBitmap( return " + imageUrl);
            }
        }
    }

    private String getBottomString(AlbumView albumView, boolean isLeft) {
        if (albumView == null) {
            return "";
        }
        VideoData info = (VideoData) albumView.getTag(TAG_KEY_INFO_DATA);
        if (info == null) {
            return "";
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d("Player/ui/detail/CommonScrollAdapter", ">> updateLeftAndRightDesc video = " + info.getData());
        }
        IVideo video = info.getData();
        SourceType type = video.getSourceType();
        this.mIsOtherType = !GalleryCornerHelper.isVerticalType(video.getAlbum().chnId);
        VideoKind videoKind = video.getKind();
        if (LogUtils.mIsDebug) {
            LogUtils.d("Player/ui/detail/CommonScrollAdapter", video.getAlbumName() + ">> SourceType == " + type + "mIsOtherType" + this.mIsOtherType + "VideoKind" + videoKind + "mIsPort" + this.mIsPort + "isFlower" + video.isFlower());
        }
        if (type == SourceType.DAILY_NEWS) {
            if (isLeft) {
                if (!(!this.mIsOtherType || this.mIsPort || albumView.isPlaying())) {
                    return getOnlineTime(video);
                }
            } else if (this.mIsOtherType) {
                return info.getText(7);
            }
        }
        switch (videoKind) {
            case VIDEO_SINGLE:
                if (isLeft) {
                    if (!(!this.mIsOtherType || this.mIsPort || albumView.isPlaying())) {
                        return getOnlineTime(video);
                    }
                } else if (this.mIsOtherType) {
                    return info.getText(7);
                } else {
                    if (!this.mIsOtherType) {
                        return PINGFEN;
                    }
                }
                break;
            case ALBUM_EPISODE:
            case ALBUM_SOURCE:
                if (!isLeft) {
                    if (type == SourceType.BO_DAN) {
                        return GalleryCornerHelper.getSeriesDesc(video.getAlbum());
                    }
                    return info.getText(4);
                }
                break;
            case VIDEO_EPISODE:
                if (!isLeft) {
                    return info.getText(7);
                }
                break;
            case VIDEO_SOURCE:
                if (type == SourceType.BO_DAN) {
                    if (!isLeft) {
                        return info.getText(7);
                    }
                } else if (video.getAlbum().getContentType() != ContentType.FEATURE_FILM) {
                    if (LogUtils.mIsDebug) {
                        LogUtils.d("Player/ui/detail/CommonScrollAdapter", "albumView video " + video + ", playing " + albumView.isPlaying());
                    }
                    if (!isLeft) {
                        return info.getText(7);
                    }
                    if (!albumView.isPlaying()) {
                        return getOnlineTime(video);
                    }
                } else if (!isLeft) {
                    return formateIssueTime(info.getText(5));
                }
                break;
        }
        return "";
    }

    private String getOnlineTime(IVideo video) {
        if (video == null) {
            LogUtils.e("Player/ui/detail/CommonScrollAdapter", "getOnlineTime: video is null!");
            return "";
        }
        String time = video.getAlbum().getInitIssueTimeFormat();
        if (!LogUtils.mIsDebug) {
            return time;
        }
        LogUtils.d("Player/ui/detail/CommonScrollAdapter", "getOnlineTime() time" + time);
        return time;
    }

    private String formateIssueTime(String textdate) {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(this.mContext.getString(R.string.horizontal_date_di));
        ssb.append(ResourceUtil.getStr(R.string.horizontal_date_qi, textdate));
        return ssb.toString();
    }

    public void showDefaultBitmap(final View convertView) {
        this.mHandler.post(new Runnable() {
            public void run() {
                if (convertView != null && (convertView instanceof AlbumView)) {
                    AlbumView albumView = convertView;
                    albumView.setTag(CommonScrollAdapter.TAG_KEY_SHOW_DEFAULT, Boolean.valueOf(true));
                    albumView.releaseCorner();
                    if (CommonScrollAdapter.this.mAlbumViewType == AlbumViewType.PLAYER_HORIZONAL || CommonScrollAdapter.this.mAlbumViewType == AlbumViewType.EXITDIALOG_VERTICAL) {
                        albumView.setImageDrawable(ImageCacheUtil.DEFAULT_DRAWABLE_NO_SKIN);
                    } else {
                        albumView.setImageDrawable(ImageCacheUtil.DEFAULT_DRAWABLE);
                    }
                } else if (LogUtils.mIsDebug) {
                    LogUtils.d("Player/ui/detail/CommonScrollAdapter", "showDefaultBitmap---convertView is null");
                }
            }
        });
    }

    private boolean isShowingDefault(View convertView) {
        if (convertView != null && convertView.getTag(TAG_KEY_SHOW_DEFAULT) != null) {
            return ((Boolean) ((AlbumView) convertView).getTag(TAG_KEY_SHOW_DEFAULT)).booleanValue();
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d("Player/ui/detail/CommonScrollAdapter", "isShowingDefault--wrong-convertView =" + convertView);
        }
        return true;
    }

    private void requestBitmapSucc(String url, Bitmap netBitmap, Object cookie) {
        setAlbumDisplayData(url, netBitmap, cookie);
    }

    private void requestBitmapFail(String url, Object cookie, Exception exception) {
        setAlbumDisplayData(url, null, cookie);
    }

    private void setAlbumDisplayData(String url, final Bitmap netBitmap, Object cookie) {
        final AlbumView albumView = (AlbumView) cookie;
        if (LogUtils.mIsDebug) {
            LogUtils.d("Player/ui/detail/CommonScrollAdapter", "setAlbumDisplayData url " + url + ", netBitmap " + ", cookie " + cookie);
        }
        if (albumView != null) {
            String rightUrl = (String) albumView.getTag(TAG_KEY_SHOW_IMAGE);
            if (url == null || url.equals(rightUrl)) {
                this.mHandler.post(new Runnable() {
                    public void run() {
                        CommonScrollAdapter.this.setCorner(albumView);
                        if (netBitmap != null) {
                            albumView.setImageBitmap(netBitmap);
                            albumView.setTag(CommonScrollAdapter.TAG_KEY_SHOW_DEFAULT, Boolean.valueOf(false));
                        }
                    }
                });
            } else if (LogUtils.mIsDebug) {
                LogUtils.d("Player/ui/detail/CommonScrollAdapter", "--return---current.url=" + url + "---right.url=" + rightUrl);
            }
        }
    }

    private void updateBottomName(AlbumView albumView) {
        if (albumView != null) {
            VideoData info = (VideoData) albumView.getTag(TAG_KEY_INFO_DATA);
            if (info != null) {
                if (LogUtils.mIsDebug) {
                    LogUtils.d("Player/ui/detail/CommonScrollAdapter", ">> updateBottomName( video = " + info.getData());
                }
                String name = info.getText(3);
                if (LogUtils.mIsDebug) {
                    LogUtils.d("Player/ui/detail/CommonScrollAdapter", ">> updateBottomName( name = " + name + ")");
                }
                albumView.setTitle(name);
            }
        }
    }

    private ImageRequest getImageRequest(ImageRequest request) {
        return request;
    }

    public void onBindViewHolder(DetailViewHolder detailViewHolder, int i) {
        if (LogUtils.mIsDebug) {
            LogUtils.d("Player/ui/detail/CommonScrollAdapter", ">> onBindViewHolder, positioin=" + i);
        }
        View view = detailViewHolder.itemView;
        setItemParams(view);
        if (view instanceof AlbumView) {
            AlbumView albumView = detailViewHolder.itemView;
            if (ListUtils.isEmpty(this.mDataList)) {
                albumView.setFocusable(false);
                return;
            }
            showDefaultBitmap(albumView);
            albumView.releaseData();
            albumView.setPlaying(false);
            albumView.setFocusable(true);
            setAlbumViewContent(i, albumView);
            return;
        }
        view.setFocusable(true);
        setSpecialViewContent(view);
    }

    public DetailViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = null;
        LogRecordUtils.logd("Player/ui/detail/CommonScrollAdapter", "parseViewType, viewType=" + viewType);
        switch (viewType) {
            case 1:
                view = createAlbumView();
                break;
            case 2:
                view = createSpecialView(2);
                break;
            case 3:
                view = createSpecialView(3);
                break;
        }
        return new DetailViewHolder(view);
    }

    private View createSpecialView(int type) {
        return new SpecialCloudView(this.mContext, type);
    }

    private int parseViewType(int i) {
        int i2 = 1;
        IVideo video = ((VideoData) this.mDataList.get(i)).getData();
        CharSequence albumID = video.getAlbumId();
        CharSequence videoID = video.getTvId();
        if (StringUtils.isEmpty(albumID) || StringUtils.isEmpty(videoID)) {
            LogRecordUtils.logd("Player/ui/detail/CommonScrollAdapter", "parseViewType, invalid albumID=" + albumID + ", videoID=" + videoID + ", video=" + video);
            return 1;
        }
        if (Integer.parseInt(video.getAlbumId()) == -1 && Integer.parseInt(video.getTvId()) == -1) {
            i2 = 2;
        }
        if (Integer.parseInt(video.getAlbumId()) == -2 && Integer.parseInt(video.getTvId()) == -2) {
            i2 = 3;
        }
        return i2;
    }

    public int getCount() {
        if (!ListUtils.isEmpty(this.mDataList)) {
            return ListUtils.getCount(this.mDataList);
        }
        if (this.mAlbumViewType == AlbumViewType.DETAIL_VERTICAL) {
            return 8;
        }
        if (this.mAlbumViewType == AlbumViewType.DETAIL_HORIZONAL) {
            return 5;
        }
        if (this.mAlbumViewType == AlbumViewType.PLAYER_HORIZONAL) {
            return 6;
        }
        if (this.mAlbumViewType == AlbumViewType.EXITDIALOG_VERTICAL) {
            return 6;
        }
        return 0;
    }

    public void onCancelAllTasks() {
        this.mCanceledTask = true;
        if (LogUtils.mIsDebug) {
            LogUtils.d("Player/ui/detail/CommonScrollAdapter", "onCancelAllTasks");
        }
        this.mImageProvider.stopAllTasks();
    }

    public void onReloadTasks(List<View> list) {
        this.mCanceledTask = false;
        if (LogUtils.mIsDebug) {
            LogUtils.d("Player/ui/detail/CommonScrollAdapter", "onReloadTasks");
        }
        reloadBitmap(list);
    }

    private void reloadBitmap(List<View> list) {
        LogUtils.e("Player/ui/detail/CommonScrollAdapter", "reloadBitmap " + list);
        for (View view : list) {
            loadBitmap(view, (String) view.getTag(TAG_KEY_SHOW_IMAGE));
        }
    }

    public int getItemViewType(int position) {
        int ret;
        if (ListUtils.isEmpty(this.mDataList)) {
            ret = 1;
        } else {
            ret = parseViewType(position);
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d("Player/ui/detail/CommonScrollAdapter", "<< getItemViewType, position=" + position + ", ret=" + ret);
        }
        return ret;
    }

    private void setItemParams(View view) {
        int itemWidth;
        int itemHeight;
        Rect bgRect = UiHelper.getBgDrawablePaddings(ResourceUtil.getDrawable(R.drawable.player_item_rect_bg));
        if (LogUtils.mIsDebug) {
            LogUtils.d("Player/ui/detail/CommonScrollAdapter", ">> setItemParams, bgRect(top=" + bgRect.top + ", bottom=" + bgRect.bottom + ", left=" + bgRect.left + ", right=" + bgRect.right + ")");
        }
        LayoutParams LP = view.getLayoutParams();
        int imageWidth;
        int imageHeight;
        if (view instanceof AlbumView) {
            int titleHeight = ResourceUtil.getDimen(R.dimen.dimen_53dp);
            if (this.mAlbumViewType == AlbumViewType.DETAIL_VERTICAL) {
                imageWidth = ResourceUtil.getDimen(R.dimen.dimen_160dp);
                imageHeight = ResourceUtil.getDimen(R.dimen.dimen_221dp);
            } else if (this.mAlbumViewType == AlbumViewType.DETAIL_HORIZONAL) {
                imageWidth = ResourceUtil.getDimen(R.dimen.dimen_248dp);
                imageHeight = ResourceUtil.getDimen(R.dimen.dimen_139dp);
            } else if (this.mAlbumViewType == AlbumViewType.PLAYER_HORIZONAL) {
                imageWidth = ResourceUtil.getDimen(R.dimen.dimen_218dp);
                imageHeight = ResourceUtil.getDimen(R.dimen.dimen_123dp);
            } else if (this.mAlbumViewType == AlbumViewType.EXITDIALOG_VERTICAL) {
                imageWidth = ResourceUtil.getDimen(R.dimen.dimen_160dp);
                imageHeight = ResourceUtil.getDimen(R.dimen.dimen_221dp);
            } else {
                imageWidth = ResourceUtil.getDimen(R.dimen.dimen_0dp);
                imageHeight = ResourceUtil.getDimen(R.dimen.dimen_0dp);
            }
            itemWidth = (bgRect.right + imageWidth) + bgRect.left;
            itemHeight = ((imageHeight + titleHeight) + bgRect.top) + bgRect.bottom;
        } else if (view instanceof SpecialCloudView) {
            if (this.mAlbumViewType == AlbumViewType.EXITDIALOG_VERTICAL) {
                imageWidth = ResourceUtil.getDimen(R.dimen.dimen_160dp);
                imageHeight = ResourceUtil.getDimen(R.dimen.dimen_261dp);
            } else {
                imageWidth = ResourceUtil.getDimen(R.dimen.dimen_155dp);
                imageHeight = ResourceUtil.getDimen(R.dimen.dimen_253dp);
            }
            itemWidth = (bgRect.right + imageWidth) + bgRect.left;
            itemHeight = (bgRect.top + imageHeight) + bgRect.bottom;
        } else {
            itemWidth = 0;
            itemHeight = 0;
        }
        LP.width = itemWidth;
        LP.height = itemHeight;
        view.setLayoutParams(LP);
        if (LogUtils.mIsDebug) {
            LogUtils.d("Player/ui/detail/CommonScrollAdapter", "<< setItemParams, itemWidth=" + itemWidth + ", itemHeight=" + itemHeight + ", mAlbumViewType=" + this.mAlbumViewType);
        }
    }
}

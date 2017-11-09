package com.gala.video.app.epg.ui.albumlist.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import com.gala.imageprovider.ImageProviderApi;
import com.gala.imageprovider.base.IImageCallback;
import com.gala.imageprovider.base.IImageProvider;
import com.gala.imageprovider.base.ImageRequest;
import com.gala.video.albumlist4.widget.RecyclerView.Adapter;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.app.epg.R;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.widget.AlbumView;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import com.gala.video.lib.share.utils.ImageCacheUtil;
import com.gala.video.lib.share.utils.TagKeyUtil;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseGridAdapter<T> extends Adapter<ViewHolder> {
    protected static final int TAG_KEY_INFO_DATA = TagKeyUtil.generateTagKey();
    protected static final int TAG_KEY_SHOW_DEFAULT = TagKeyUtil.generateTagKey();
    protected static final int TAG_KEY_SHOW_IMAGE_UIL = TagKeyUtil.generateTagKey();
    public static final int VIEW_TYPE_DEFAULT = 18;
    public static final int VIEW_TYPE_LOADING = 17;
    protected String TAG = "EPG/album4/BaseGridAdapter";
    protected Context mContext;
    protected List<T> mDataList = new ArrayList();
    protected Handler mHandler = new Handler(Looper.getMainLooper());
    protected final IImageProvider mImageProvider = ImageProviderApi.getImageProvider();
    private boolean mIsCanceled = false;
    protected boolean mShowLoading = false;
    protected boolean mStopLoadtask = false;

    public static class AlbumViewHolder extends ViewHolder {
        public AlbumViewHolder(View view) {
            super(view);
        }
    }

    private static class IImageCallbackImpl implements IImageCallback {
        private String imageUrl;
        private WeakReference<BaseGridAdapter> mOuter;
        private long time1 = System.currentTimeMillis();

        public IImageCallbackImpl(BaseGridAdapter outer, String imageUrl) {
            this.mOuter = new WeakReference(outer);
            this.imageUrl = imageUrl;
        }

        public void onSuccess(ImageRequest imageRequest, Bitmap bitmap) {
            BaseGridAdapter outer = (BaseGridAdapter) this.mOuter.get();
            if (outer != null) {
                if (bitmap != null && imageRequest != null) {
                    Object cookie = imageRequest.getCookie();
                    if (!outer.mStopLoadtask && !outer.mIsCanceled && cookie != null) {
                        outer.requestBitmapSucc(imageRequest.getUrl(), bitmap, cookie);
                    }
                } else if (LogUtils.mIsDebug) {
                    LogUtils.d(outer.TAG, "loadBitmap >> onSuccess bitmap = null!");
                }
            }
        }

        public void onFailure(ImageRequest imageRequest, Exception exception) {
            BaseGridAdapter outer = (BaseGridAdapter) this.mOuter.get();
            if (outer != null) {
                if (LogUtils.mIsDebug) {
                    LogUtils.e(outer.TAG, "loadBitmap---fail !!! Load,url = " + this.imageUrl + ", consume(time3-time2) = " + (System.currentTimeMillis() - this.time1) + "ms");
                }
                if (imageRequest != null) {
                    Object cookie = imageRequest.getCookie();
                    if (!outer.mStopLoadtask && !outer.mIsCanceled && cookie != null) {
                        outer.requestBitmapFailed(imageRequest.getUrl(), cookie, exception);
                    }
                }
            }
        }
    }

    protected abstract int getDataCount();

    public abstract void initLayoutParamsParams(ViewHolder viewHolder, LayoutParams layoutParams);

    protected abstract void onBindItemViewHolder(ViewHolder viewHolder, int i, LayoutParams layoutParams);

    protected abstract View onCreateItemViewHolder(int i);

    public abstract void releaseData(View view);

    protected abstract void requestBitmapFailed(String str, Object obj, Exception exception);

    protected abstract void requestBitmapSucc(String str, Bitmap bitmap, Object obj);

    protected abstract void showDefaultBitmap(View view);

    public BaseGridAdapter(Context context) {
        this.mContext = context;
    }

    public List<T> getDataList() {
        return this.mDataList;
    }

    public T getData(int position) {
        return this.mDataList.get(position);
    }

    public void deleteData(int position) {
        this.mDataList.remove(position);
    }

    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 17) {
            view = LayoutInflater.from(this.mContext).inflate(R.layout.share_albumlist5_loading, parent, false);
        } else {
            view = onCreateItemViewHolder(viewType);
        }
        return new AlbumViewHolder(view);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        LayoutParams params = holder.itemView.getLayoutParams();
        initLayoutParamsParams(holder, params);
        if (holder.getItemViewType() == 17) {
            holder.itemView.setFocusable(true);
            params.width = -1;
            return;
        }
        onBindItemViewHolder(holder, position, params);
    }

    protected void loadBitmap(View convertView, String imageUrl) {
        if (this.mStopLoadtask || convertView == null || StringUtils.isEmpty((CharSequence) imageUrl)) {
            if (LogUtils.mIsDebug) {
                LogUtils.e("loadBitmap() -> convertView == null or mStopLoadTask = " + this.mStopLoadtask + ",convertView:" + convertView + ",imageUrl:" + imageUrl);
            }
        } else if (StringUtils.isEmpty((CharSequence) imageUrl)) {
            if (LogUtils.mIsDebug) {
                LogUtils.e(this.TAG, "loadBitmap() -> imageUrl is null");
            }
            showDefaultBitmap(convertView);
        } else {
            boolean directLoad = false;
            if (!imageUrl.equals(convertView.getTag(TAG_KEY_SHOW_IMAGE_UIL))) {
                showDefaultBitmap(convertView);
                directLoad = true;
            }
            convertView.setTag(TAG_KEY_SHOW_IMAGE_UIL, imageUrl);
            if (!this.mIsCanceled) {
                if (isShowingDefault(convertView) || directLoad) {
                    this.mImageProvider.loadImage(new ImageRequest(imageUrl, convertView), new IImageCallbackImpl(this, imageUrl));
                }
            }
        }
    }

    public void onCancelAllTasks() {
        this.mIsCanceled = true;
        this.mImageProvider.stopAllTasks();
    }

    public void onReloadTasks(View view) {
        this.mIsCanceled = false;
        if (view != null) {
            loadBitmap(view, (String) view.getTag(TAG_KEY_SHOW_IMAGE_UIL));
        }
    }

    public void onPause() {
        this.mStopLoadtask = true;
    }

    public void onResume() {
        this.mStopLoadtask = false;
        this.mIsCanceled = false;
    }

    public void resetList() {
        notifyDataSetChanged();
    }

    public void updateData(List<T> datas) {
        if (datas != null) {
            int size = datas.size();
            int oldDataSize = ListUtils.getCount(this.mDataList);
            this.mDataList.clear();
            this.mDataList.addAll(datas);
            if (oldDataSize <= 0 || size < oldDataSize) {
                if (LogUtils.mIsDebug) {
                    LogUtils.e(this.TAG, "updateData notifyDataSetChanged");
                }
                notifyDataSetChanged();
                return;
            }
            if (LogUtils.mIsDebug) {
                LogUtils.e(this.TAG, "updateData notifyDataSetUpdate");
            }
            notifyDataSetAdd();
        } else if (LogUtils.mIsDebug) {
            LogUtils.e(this.TAG, "updateData datas is null!");
        }
    }

    public void recycleBitmap(View view) {
        if (view != null) {
            showDefaultBitmap(view);
            this.mImageProvider.recycleBitmap((String) view.getTag(TAG_KEY_SHOW_IMAGE_UIL));
        } else if (LogUtils.mIsDebug) {
            LogUtils.e(this.TAG, "recycleBitmap view is null");
        }
    }

    public boolean isShowLoading() {
        return this.mShowLoading;
    }

    public void showLoading(boolean showLoading) {
        this.mShowLoading = showLoading;
    }

    public int getItemViewType(int position) {
        if (position == getCount() - 1 && this.mShowLoading) {
            return 17;
        }
        return getItemType(position);
    }

    public int getCount() {
        int count = getDataCount();
        if (this.mShowLoading) {
            return count + 1;
        }
        return count;
    }

    public boolean isFocusable(int position) {
        if (position == getCount() - 1 && this.mShowLoading) {
            return false;
        }
        return true;
    }

    public int getNumRows(int position) {
        if (position == getCount() - 1 && this.mShowLoading) {
            return 1;
        }
        return getItemNumRows(position);
    }

    protected int getItemNumRows(int position) {
        return 0;
    }

    public void hideLoading() {
        int position = getLastPosition();
        if (isShowLoading()) {
            notifyDataSetRemoved(position);
        }
        this.mShowLoading = false;
    }

    protected void setTitleText(AlbumView albumView) {
        if (albumView != null) {
            IData info = (IData) albumView.getTag(TAG_KEY_INFO_DATA);
            if (info != null) {
                albumView.setTitle(info.getText(3));
            }
        }
    }

    protected int getItemType(int position) {
        return 0;
    }

    protected boolean isShowingDefault(View convertView) {
        return false;
    }

    protected Drawable getDefaultDrawable() {
        return ImageCacheUtil.DEFAULT_DRAWABLE;
    }

    public int getDefalutCount() {
        return 0;
    }

    public int getScrollCount() {
        return 0;
    }
}

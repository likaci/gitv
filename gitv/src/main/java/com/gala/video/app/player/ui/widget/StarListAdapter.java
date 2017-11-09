package com.gala.video.app.player.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import com.gala.imageprovider.ImageProviderApi;
import com.gala.imageprovider.base.IImageCallback;
import com.gala.imageprovider.base.IImageProvider;
import com.gala.imageprovider.base.ImageRequest;
import com.gala.imageprovider.base.ImageRequest.ImageType;
import com.gala.imageprovider.base.ImageRequest.ScaleType;
import com.gala.sdk.player.data.IStarData;
import com.gala.video.albumlist4.widget.RecyclerView.Adapter;
import com.gala.video.app.player.R;
import com.gala.video.app.player.ui.widget.CommonScrollAdapter.DetailViewHolder;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.utils.ResourceUtil;
import com.gala.video.lib.share.utils.TagKeyUtil;
import java.util.ArrayList;
import java.util.List;

public class StarListAdapter extends Adapter<DetailViewHolder> {
    private static final int CIRCLE_SIZE = ResourceUtil.getDimen(R.dimen.dimen_153dp);
    private static final int MAX_TEXT_COUNT = 6;
    private static final String TAG = "StarListAdapter";
    private static final int TAG_KEY_INFO_DATA = TagKeyUtil.generateTagKey();
    private static final int TAG_KEY_SHOW_DEFAULT = TagKeyUtil.generateTagKey();
    private static final int TAG_KEY_SHOW_IMAGE = TagKeyUtil.generateTagKey();
    private boolean mCanceledTask;
    private Context mContext;
    private List<IStarData> mDataList = new ArrayList();
    private IImageProvider mImageProvider = ImageProviderApi.getImageProvider();
    private Handler mMainHandler = new Handler(Looper.getMainLooper());

    public StarListAdapter(Context context) {
        this.mContext = context;
    }

    public DetailViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> onCreateViewHolder");
        }
        return createCircleView();
    }

    private DetailViewHolder createCircleView() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> createCircleView");
        }
        return new DetailViewHolder(new CirclePersonView(this.mContext));
    }

    public void onBindViewHolder(DetailViewHolder detailViewHolder, int i) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> onBindViewHolder");
        }
        CirclePersonView view = detailViewHolder.itemView;
        if (ListUtils.isEmpty(this.mDataList)) {
            view.clearViewContent();
            view.setFocusable(false);
            return;
        }
        view.setFocusable(true);
        setViewContent(i, view);
    }

    private void setViewContent(int index, CirclePersonView view) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> setViewContent, index=" + index + ", view=" + view);
        }
        if (ListUtils.isEmpty(this.mDataList) || index >= getCount()) {
            showDefaultImage(view);
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "setViewContent, invalid index=" + index);
                return;
            }
            return;
        }
        IStarData dataItem = (IStarData) this.mDataList.get(index);
        updateMainImage(dataItem, view);
        updateTitle(dataItem, view);
    }

    private void updateMainImage(IStarData data, CirclePersonView view) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> updateMainImage");
        }
        loadImage(view, data.getStarCover());
    }

    private String getUrlWithSize(String url) {
        if (url == null || url.isEmpty()) {
            return url;
        }
        int index = url.lastIndexOf(".");
        return index >= 0 ? index : url;
    }

    private void loadImage(CirclePersonView view, String url) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> loadImage, view=" + view + ", url=" + url);
        }
        if (view == null) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "loadImage, view is null!");
            }
        } else if (StringUtils.isEmpty((CharSequence) url)) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "loadImage, invalid url=" + url);
            }
            showDefaultImage(view);
        } else {
            String sizedURL = getUrlWithSize(url);
            view.setTag(TAG_KEY_SHOW_IMAGE, sizedURL);
            if (!this.mCanceledTask) {
                ImageRequest imageRequest = createImageRequest(sizedURL, view);
                if (LogUtils.mIsDebug) {
                    LogUtils.d(TAG, "loadImage, sizedURL=" + sizedURL);
                }
                this.mImageProvider.loadImage(imageRequest, new IImageCallback() {
                    public void onSuccess(ImageRequest imageRequest, final Bitmap bitmap) {
                        if (bitmap != null) {
                            CirclePersonView cookie = imageRequest.getCookie();
                            if (cookie != null) {
                                final CirclePersonView view = cookie;
                                String url = imageRequest.getUrl();
                                String rightUrl = (String) view.getTag(StarListAdapter.TAG_KEY_SHOW_IMAGE);
                                if (url == null || url.equals(rightUrl)) {
                                    StarListAdapter.this.mMainHandler.post(new Runnable() {
                                        public void run() {
                                            view.setMainImage(bitmap);
                                            view.setTag(StarListAdapter.TAG_KEY_SHOW_DEFAULT, Boolean.valueOf(false));
                                        }
                                    });
                                } else if (LogUtils.mIsDebug) {
                                    LogUtils.d(StarListAdapter.TAG, "--return---current.url=" + url + "---right.url=" + rightUrl);
                                }
                            } else if (LogUtils.mIsDebug) {
                                LogUtils.d(StarListAdapter.TAG, "loadBitmap >> onSuccess-------  cookie = null !! ");
                            }
                        } else if (LogUtils.mIsDebug) {
                            LogUtils.d(StarListAdapter.TAG, "loadBitmap >> onSuccess-------  netBitmap = null !! ");
                        }
                    }

                    public void onFailure(ImageRequest imageRequest, Exception e) {
                        CirclePersonView cookie = imageRequest.getCookie();
                        if (cookie != null) {
                            final CirclePersonView view = cookie;
                            StarListAdapter.this.mMainHandler.post(new Runnable() {
                                public void run() {
                                    view.setDefaultImage();
                                    view.setTag(StarListAdapter.TAG_KEY_SHOW_DEFAULT, Boolean.valueOf(true));
                                }
                            });
                        } else if (LogUtils.mIsDebug) {
                            LogUtils.d(StarListAdapter.TAG, "loadBitmap >> onSuccess-------  cookie = null !! ");
                        }
                    }
                });
            } else if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "loadImage, mCanceledTask is true, return");
            }
        }
    }

    private ImageRequest createImageRequest(String url, View view) {
        ImageRequest request = new ImageRequest(url, view);
        request.setScaleType(ScaleType.CENTER_CROP);
        request.setTargetWidth(CIRCLE_SIZE);
        request.setTargetHeight(CIRCLE_SIZE);
        request.setImageType(ImageType.ROUND);
        request.setRadius((float) (CIRCLE_SIZE >> 1));
        return request;
    }

    public void showDefaultImage(CirclePersonView view) {
        view.setDefaultImage();
    }

    private void updateTitle(IStarData data, CirclePersonView view) {
        view.setTitleText(data.getStarName());
    }

    public int getCount() {
        if (ListUtils.isEmpty(this.mDataList)) {
            return 13;
        }
        return this.mDataList.size();
    }

    public void updateDataSet(List<IStarData> list) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> updateDataSet, list.size=" + list.size());
        }
        this.mDataList.clear();
        this.mDataList.addAll(list);
        notifyDataSetUpdate();
    }

    public void changeDataSet(List<IStarData> list) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> changeDataSet, list.size=" + list.size());
        }
        this.mDataList.clear();
        this.mDataList.addAll(list);
        notifyDataSetChanged();
    }

    public void onCancelAllTasks() {
        this.mCanceledTask = true;
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "onCancelAllTasks");
        }
        this.mImageProvider.stopAllTasks();
    }

    public void onReloadTasks(List<View> list) {
        this.mCanceledTask = false;
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "onReloadTasks");
        }
        reloadBitmap(list);
    }

    private void reloadBitmap(List<View> list) {
        LogUtils.e(TAG, "reloadBitmap " + list);
        for (View view : list) {
            loadImage((CirclePersonView) view, (String) view.getTag(TAG_KEY_SHOW_IMAGE));
        }
    }
}

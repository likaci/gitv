package com.tvos.appdetailpage.ui.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import com.tvos.appdetailpage.ui.AppStoreDetailActivity.OnItemClickedListener;
import com.tvos.appdetailpage.utils.CommonUtils;
import com.tvos.appdetailpage.utils.TagKeyUtil;
import java.util.List;

public class AppDetailThumbNailAdapter extends BaseImageViewVAdapter {
    public static int FIRST_ITEM = TagKeyUtil.generateTagKey();
    public static int Last_ITEM = TagKeyUtil.generateTagKey();
    private static final int TAG_POSITION = TagKeyUtil.generateTagKey();
    private ImageView floatImageView;
    private View imageViewContainer;
    private ImageView leftArrowImageView;
    private Drawable mDefautDrawable;
    protected Handler mHandler = new Handler();
    private LayoutInflater mLayoutInflater;
    private Drawable mLeftArrowEnableDrawable;
    private Drawable mLeftArrowUnableDrawable;
    private List<String> mListUrl;
    private OnItemClickedListener mOnItemClickedListener;
    private View mPopupWindowToken;
    private Drawable mRightArrowEnableDrawable;
    private Drawable mRightArrowUnableDrawable;
    private PopupWindow pop;
    private int position = 0;
    private ImageView rightArrowImageView;

    public class ThumbnailOnClickListener implements OnClickListener {
        public void onClick(View thumbnailView) {
            if (AppDetailThumbNailAdapter.this.mOnItemClickedListener != null) {
                AppDetailThumbNailAdapter.this.mOnItemClickedListener.onItemClicked(thumbnailView, ((Integer) thumbnailView.getTag(AppDetailThumbNailAdapter.TAG_POSITION)).intValue());
            }
            if (!CommonUtils.isListEmpty(AppDetailThumbNailAdapter.this.mListUrl)) {
                AppDetailThumbNailAdapter.this.position = ((Integer) thumbnailView.getTag(AppDetailThumbNailAdapter.TAG_POSITION)).intValue();
                if (AppDetailThumbNailAdapter.this.imageViewContainer == null) {
                    AppDetailThumbNailAdapter.this.imageViewContainer = AppDetailThumbNailAdapter.this.mLayoutInflater.inflate(AppDetailThumbNailAdapter.this.getResId("layout", "apps_appdetails_thumb_float"), null);
                    AppDetailThumbNailAdapter.this.floatImageView = (ImageView) AppDetailThumbNailAdapter.this.imageViewContainer.findViewById(AppDetailThumbNailAdapter.this.getResId("id", "apps_thumbnail_float"));
                    AppDetailThumbNailAdapter.this.leftArrowImageView = (ImageView) AppDetailThumbNailAdapter.this.imageViewContainer.findViewById(AppDetailThumbNailAdapter.this.getResId("id", "apps_thumbnail_leftarrow"));
                    AppDetailThumbNailAdapter.this.rightArrowImageView = (ImageView) AppDetailThumbNailAdapter.this.imageViewContainer.findViewById(AppDetailThumbNailAdapter.this.getResId("id", "apps_thumbnail_Rightarrow"));
                    AppDetailThumbNailAdapter.this.imageViewContainer.setVisibility(0);
                    AppDetailThumbNailAdapter.this.floatImageView.setImageDrawable(AppDetailThumbNailAdapter.this.mDefautDrawable);
                }
                updateArrows(AppDetailThumbNailAdapter.this.position);
                AppDetailThumbNailAdapter.this.loadBitmap(AppDetailThumbNailAdapter.this.floatImageView, (String) AppDetailThumbNailAdapter.this.mListUrl.get(AppDetailThumbNailAdapter.this.position), AppDetailThumbNailAdapter.this.position, AppDetailThumbNailAdapter.this.getResId("drawable", "apps_image_default_bg"));
                AppDetailThumbNailAdapter.this.imageViewContainer.setOnKeyListener(new OnKeyListener() {
                    public boolean onKey(View arg0, int keyCode, KeyEvent event) {
                        if (event.getAction() == 0) {
                            AppDetailThumbNailAdapter access$1;
                            switch (keyCode) {
                                case 21:
                                    if (AppDetailThumbNailAdapter.this.position > 0) {
                                        access$1 = AppDetailThumbNailAdapter.this;
                                        access$1.position = access$1.position - 1;
                                        ThumbnailOnClickListener.this.updateArrows(AppDetailThumbNailAdapter.this.position);
                                        AppDetailThumbNailAdapter.this.loadBitmap(AppDetailThumbNailAdapter.this.floatImageView, (String) AppDetailThumbNailAdapter.this.mListUrl.get(AppDetailThumbNailAdapter.this.position), AppDetailThumbNailAdapter.this.position, AppDetailThumbNailAdapter.this.getResId("drawable", "apps_image_default_bg"));
                                        break;
                                    }
                                    break;
                                case 22:
                                    if (AppDetailThumbNailAdapter.this.position < AppDetailThumbNailAdapter.this.mListUrl.size() - 1) {
                                        access$1 = AppDetailThumbNailAdapter.this;
                                        access$1.position = access$1.position + 1;
                                        ThumbnailOnClickListener.this.updateArrows(AppDetailThumbNailAdapter.this.position);
                                        AppDetailThumbNailAdapter.this.loadBitmap(AppDetailThumbNailAdapter.this.floatImageView, (String) AppDetailThumbNailAdapter.this.mListUrl.get(AppDetailThumbNailAdapter.this.position), AppDetailThumbNailAdapter.this.position, AppDetailThumbNailAdapter.this.getResId("drawable", "apps_image_default_bg"));
                                        break;
                                    }
                                    break;
                            }
                        }
                        return false;
                    }
                });
                AppDetailThumbNailAdapter.this.pop = new PopupWindow(AppDetailThumbNailAdapter.this.mContext);
                AppDetailThumbNailAdapter.this.pop.setContentView(AppDetailThumbNailAdapter.this.imageViewContainer);
                AppDetailThumbNailAdapter.this.pop.setWidth(-1);
                AppDetailThumbNailAdapter.this.pop.setHeight(-1);
                AppDetailThumbNailAdapter.this.pop.setBackgroundDrawable(new ColorDrawable(-16777216));
                AppDetailThumbNailAdapter.this.pop.setFocusable(true);
                Log.d(AppDetailThumbNailAdapter.this.TAG, "AppStoreDetailPage ThumbnailOnClickListener: before showAtLocation");
                AppDetailThumbNailAdapter.this.pop.showAtLocation(AppDetailThumbNailAdapter.this.mPopupWindowToken, 17, 0, 0);
                Log.d(AppDetailThumbNailAdapter.this.TAG, "AppStoreDetailPage ThumbnailOnClickListener: after showAtLocation");
            }
        }

        private void updateArrows(int position) {
            if (position == 0) {
                AppDetailThumbNailAdapter.this.leftArrowImageView.setImageDrawable(AppDetailThumbNailAdapter.this.mLeftArrowUnableDrawable);
            } else {
                AppDetailThumbNailAdapter.this.leftArrowImageView.setImageDrawable(AppDetailThumbNailAdapter.this.mLeftArrowEnableDrawable);
            }
            if (position == AppDetailThumbNailAdapter.this.mListUrl.size() - 1) {
                AppDetailThumbNailAdapter.this.rightArrowImageView.setImageDrawable(AppDetailThumbNailAdapter.this.mRightArrowUnableDrawable);
            } else {
                AppDetailThumbNailAdapter.this.rightArrowImageView.setImageDrawable(AppDetailThumbNailAdapter.this.mRightArrowEnableDrawable);
            }
        }
    }

    static class ViewHolder {
        ImageView thumbnailImageView;

        ViewHolder() {
        }
    }

    public AppDetailThumbNailAdapter(List<String> mListUrl, Context mContext, Drawable defaultDrawable, Drawable mLeftArrowUnableDrawable, Drawable mLeftArrowEnableDrawable, Drawable mRightArrowUnableDrawable, Drawable mRightArrowEnableDrawable, View popupWindowToken) {
        this.mListUrl = mListUrl;
        this.mContext = mContext;
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.mDefautDrawable = defaultDrawable;
        this.mLeftArrowUnableDrawable = mLeftArrowUnableDrawable;
        this.mLeftArrowEnableDrawable = mLeftArrowEnableDrawable;
        this.mRightArrowUnableDrawable = mRightArrowUnableDrawable;
        this.mRightArrowEnableDrawable = mRightArrowEnableDrawable;
        this.mPopupWindowToken = popupWindowToken;
    }

    public void release() {
        super.release();
        this.mContext = null;
        if (this.mListUrl != null) {
            this.mListUrl.clear();
            this.mListUrl = null;
        }
        this.mLayoutInflater = null;
        this.mDefautDrawable = null;
        this.mHandler = null;
        this.mPopupWindowToken = null;
        this.floatImageView = null;
        this.leftArrowImageView = null;
        this.rightArrowImageView = null;
        this.mLeftArrowEnableDrawable = null;
        this.mLeftArrowUnableDrawable = null;
        this.mRightArrowEnableDrawable = null;
        this.mRightArrowUnableDrawable = null;
        this.imageViewContainer = null;
        this.pop = null;
        this.mOnItemClickedListener = null;
    }

    public View getView(int position, ViewGroup parent) {
        ViewHolder holder;
        View convertView = parent.getChildAt(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = this.mLayoutInflater.inflate(getResId("layout", "apps_appdetail_image"), null);
            holder.thumbnailImageView = (ImageView) convertView.findViewById(getResId("id", "apps_appImage"));
            convertView.setTag(TAG_POSITION, Integer.valueOf(position));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        loadBitmap(holder.thumbnailImageView, (String) this.mListUrl.get(position), position, getResId("drawable", "apps_image_default_bg"));
        notifyDataSetChanged();
        convertView.setFocusable(true);
        convertView.setOnClickListener(new ThumbnailOnClickListener());
        if (position == this.mListUrl.size() - 1) {
            convertView.setNextFocusRightId(AppDetailRecommdAdapter.FIRST_ITEM);
        } else if (position == 0) {
            convertView.setId(FIRST_ITEM);
            convertView.setNextFocusLeftId(FIRST_ITEM);
        }
        return convertView;
    }

    public int getCount() {
        return this.mListUrl.size();
    }

    protected void requestBitmapSucc() {
    }

    protected void requestBitmapFailed() {
    }

    public void onScrollItemIn(int arg0, View arg1) {
    }

    public void onScrollItemOut(int arg0, View arg1) {
    }

    public void setOnItemClickedListener(OnItemClickedListener l) {
        this.mOnItemClickedListener = l;
    }
}

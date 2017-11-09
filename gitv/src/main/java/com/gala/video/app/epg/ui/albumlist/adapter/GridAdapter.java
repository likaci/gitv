package com.gala.video.app.epg.ui.albumlist.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.app.epg.R;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.configs.ViewConstant.AlbumViewType;
import com.gala.video.lib.share.common.widget.AlbumView;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import com.gala.video.lib.share.utils.ResourceUtil;

public class GridAdapter extends BaseGridAdapter<IData> {
    public static final int HEIGHT = ResourceUtil.getDimen(R.dimen.dimen_313dp);
    public static final int WIDTH = ResourceUtil.getDimen(R.dimen.dimen_196dp);
    private int mScrlllCount;
    protected AlbumViewType mType;

    public GridAdapter(Context context) {
        super(context);
        this.mScrlllCount = 0;
        this.TAG = "EPG/adapter/GridAdapter";
    }

    public GridAdapter(Context context, AlbumViewType type) {
        this(context);
        this.mType = type;
    }

    public int getDefalutCount() {
        if (this.mType == AlbumViewType.VERTICAL) {
            this.mScrlllCount = 12;
            return 24;
        } else if (this.mType == AlbumViewType.HORIZONTAL) {
            this.mScrlllCount = 8;
            return 12;
        } else if (this.mType == AlbumViewType.RECOMMEND_VERTICAL) {
            this.mScrlllCount = 6;
            return 6;
        } else if (this.mType != AlbumViewType.RECOMMEND_HORIZONTAL) {
            return 0;
        } else {
            this.mScrlllCount = 4;
            return 4;
        }
    }

    public int getScrollCount() {
        return this.mScrlllCount;
    }

    protected int getDataCount() {
        int count = ListUtils.getCount(this.mDataList);
        return count <= 0 ? getDefalutCount() : count;
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        LayoutParams params = holder.itemView.getLayoutParams();
        initLayoutParamsParams(holder, params);
        if (holder.getItemViewType() == 17) {
            holder.itemView.setFocusable(true);
            params.width = -1;
            params.height = ResourceUtil.getDimen(R.dimen.dimen_275dp);
            return;
        }
        onBindItemViewHolder(holder, position, params);
    }

    protected void onBindItemViewHolder(ViewHolder holder, int position, LayoutParams params) {
        initLayoutParamsParams(holder, params);
        AlbumView itemView = holder.itemView;
        if (ListUtils.isEmpty(this.mDataList)) {
            itemView.setFocusable(false);
            return;
        }
        itemView.setFocusable(true);
        setViewContent(position, itemView);
    }

    public void initLayoutParamsParams(ViewHolder holder, LayoutParams params) {
        params.width = WIDTH;
        params.height = HEIGHT;
    }

    protected View onCreateItemViewHolder(int viewType) {
        AlbumView itemView = new AlbumView(this.mContext.getApplicationContext(), AlbumViewType.VERTICAL);
        itemView.setTag(TAG_KEY_SHOW_DEFAULT, Boolean.valueOf(true));
        itemView.setImageDrawable(getDefaultDrawable());
        return itemView;
    }

    private void setViewContent(int position, AlbumView albumView) {
        if (!ListUtils.isEmpty(this.mDataList) && position < getCount()) {
            IData info = (IData) this.mDataList.get(position);
            String imageUrlByPos = getImageUrlByPos(position);
            albumView.setTag(TAG_KEY_INFO_DATA, info);
            albumView.releaseData();
            if (info != null) {
                setTitleText(albumView);
            }
            if (TextUtils.isEmpty(imageUrlByPos)) {
                setDescAndCorner(albumView);
            } else {
                loadBitmap(albumView, imageUrlByPos);
            }
        }
    }

    protected void setDescAndCorner(AlbumView albumView) {
        if (albumView != null) {
            IData info = (IData) albumView.getTag(TAG_KEY_INFO_DATA);
            if (info != null && info.getAlbum() != null) {
                albumView.setFilmScore(info.getText(9));
                albumView.setDescLine1Left(info.getText(10));
                albumView.setDescLine1Right(info.getText(11));
                albumView.setCorner(info);
            }
        }
    }

    private String getImageUrlByPos(int position) {
        if (!ListUtils.isLegal(this.mDataList, position)) {
            return null;
        }
        IData albumInfo = (IData) this.mDataList.get(position);
        return albumInfo == null ? "" : albumInfo.getImageUrl(2);
    }

    protected void requestBitmapSucc(String url, Bitmap netBitmap, Object cookie) {
        setAlbumDisplayData(url, netBitmap, cookie);
    }

    protected void requestBitmapFailed(String url, Object cookie, Exception exception) {
        setAlbumDisplayData(url, null, cookie);
    }

    private void setAlbumDisplayData(String url, final Bitmap netBitmap, Object cookie) {
        if (cookie != null) {
            final AlbumView albumView = (AlbumView) cookie;
            IData info = (IData) albumView.getTag(TAG_KEY_INFO_DATA);
            if (info != null) {
                String rightUrl = info.getImageUrl(2);
                if (url == null || url.equals(rightUrl)) {
                    this.mHandler.post(new Runnable() {
                        public void run() {
                            GridAdapter.this.setDescAndCorner(albumView);
                            if (netBitmap != null) {
                                albumView.setImageBitmap(netBitmap);
                                albumView.setTag(BaseGridAdapter.TAG_KEY_SHOW_DEFAULT, Boolean.valueOf(false));
                            }
                        }
                    });
                } else if (LogUtils.mIsDebug) {
                    LogUtils.e("--return---current.url=" + url + "---right.url=" + rightUrl);
                }
            } else if (LogUtils.mIsDebug) {
                LogUtils.e("info is null");
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.e("cookie is null");
        }
    }

    protected void showDefaultBitmap(View convertView) {
        if (convertView != null && (convertView instanceof AlbumView)) {
            AlbumView albumView = (AlbumView) convertView;
            albumView.setTag(TAG_KEY_SHOW_DEFAULT, Boolean.valueOf(true));
            albumView.setImageDrawable(getDefaultDrawable());
        }
    }

    public void releaseData(View convertView) {
        if (convertView != null && (convertView instanceof AlbumView)) {
            ((AlbumView) convertView).releaseData();
        }
    }

    public void resetList() {
        this.mDataList.clear();
        super.resetList();
    }

    protected boolean isShowingDefault(View convertView) {
        if (convertView == null || convertView.getTag(TAG_KEY_SHOW_DEFAULT) == null) {
            return true;
        }
        return ((Boolean) convertView.getTag(TAG_KEY_SHOW_DEFAULT)).booleanValue();
    }
}

package com.gala.video.app.epg.ui.star.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.gala.albumprovider.model.Tag;
import com.gala.video.albumlist4.widget.HorizontalGridView;
import com.gala.video.albumlist4.widget.RecyclerView.OnFocusLostListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemClickListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemFocusChangedListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemRecycledListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnScrollListener;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.albumlist.adapter.GridAdapter;
import com.gala.video.app.epg.ui.star.adapter.StarsAdapter;
import com.gala.video.app.epg.ui.star.model.StarsInfoModel;
import com.gala.video.app.epg.ui.star.utils.StarsPingbackUtil;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.List;

public class StarHorizontalGridView extends HorizontalGridView {
    private static final int LEFTPADDING = getDimen(R.dimen.dimen_16dp);
    private final Handler mBaseHandler;
    private Context mContext;
    private GridAdapter mHorizonAdapter;
    public List<IData> mHorizonLists;
    private StarsInfoModel mInfoModel;
    private OnFocusLostListener mOnFocusLostListener;
    private OnItemClickListener mOnItemClickListener;
    private OnItemFocusChangedListener mOnItemFocusChangedListener;
    private OnItemRecycledListener mOnItemRecycledListener;
    private OnScrollListener mOnScrollListener;
    private OnStarItemFocusChangedListener mOnStarItemFocusChangedListener;
    private int mRowIndex;
    private int mScaleDuration;
    private final Runnable mScrollStopRunnable;
    private StarsAdapter mStarsAdapter;
    private Tag mTag;

    public interface OnStarItemFocusChangedListener {
        void onItemFocusChanged(ViewGroup viewGroup, ViewHolder viewHolder, boolean z);
    }

    public StarHorizontalGridView(Context context) {
        super(context);
        this.mBaseHandler = new Handler(Looper.getMainLooper());
        this.mOnItemFocusChangedListener = new OnItemFocusChangedListener() {
            public void onItemFocusChanged(ViewGroup parent, ViewHolder holder, boolean hasFocus) {
                parent.invalidate();
                holder.itemView.bringToFront();
                AnimationUtil.zoomAnimation(holder.itemView, hasFocus, 1.1f, StarHorizontalGridView.this.mScaleDuration, true);
                if (StarHorizontalGridView.this.mScaleDuration == 0) {
                    StarHorizontalGridView.this.mScaleDuration = 200;
                    if (StarHorizontalGridView.this.mOnStarItemFocusChangedListener != null) {
                        StarHorizontalGridView.this.mOnStarItemFocusChangedListener.onItemFocusChanged(parent, holder, hasFocus);
                    }
                }
            }
        };
        this.mOnItemClickListener = new OnItemClickListener() {
            public void onItemClick(ViewGroup parent, ViewHolder holder) {
                if (!ListUtils.isEmpty(StarHorizontalGridView.this.mHorizonLists)) {
                    IData data = (IData) StarHorizontalGridView.this.mHorizonLists.get(holder.getLayoutPosition());
                    if (data != null && StarHorizontalGridView.this.mInfoModel != null) {
                        data.click(StarHorizontalGridView.this.mContext, StarHorizontalGridView.this.mInfoModel);
                        StarsPingbackUtil.sendPageItemClick(data, StarHorizontalGridView.this.getTagName(), StarHorizontalGridView.this.mRowIndex, holder.getLayoutPosition() + 1, StarHorizontalGridView.this.mInfoModel.getBuySource(), StarHorizontalGridView.this.mInfoModel.getE());
                    }
                }
            }
        };
        this.mOnFocusLostListener = new OnFocusLostListener() {
            public void onFocusLost(ViewGroup parent, ViewHolder holder) {
                if (StarHorizontalGridView.this.mRowIndex == 1) {
                    StarHorizontalGridView.this.invalidate();
                }
                if (StarHorizontalGridView.this.mStarsAdapter != null) {
                    StarHorizontalGridView.this.mStarsAdapter.setLastLoseFocusPosition(StarHorizontalGridView.this.mRowIndex, holder.getLayoutPosition());
                }
            }
        };
        this.mOnScrollListener = new OnScrollListener() {
            public void onScrollStart() {
                StarHorizontalGridView.this.mBaseHandler.removeCallbacks(StarHorizontalGridView.this.mScrollStopRunnable);
                if (StarHorizontalGridView.this.mHorizonAdapter != null) {
                    StarHorizontalGridView.this.mHorizonAdapter.onCancelAllTasks();
                }
            }

            public void onScrollStop() {
                StarHorizontalGridView.this.mBaseHandler.removeCallbacks(StarHorizontalGridView.this.mScrollStopRunnable);
                StarHorizontalGridView.this.mBaseHandler.postDelayed(StarHorizontalGridView.this.mScrollStopRunnable, 100);
            }

            public void onScroll(ViewParent parent, int firstVisibleItem, int lastVisibleItem, int totalItemCount) {
            }

            public void onScrollBefore(int nextPos) {
            }
        };
        this.mScrollStopRunnable = new Runnable() {
            public void run() {
                int first = StarHorizontalGridView.this.getFirstAttachedPosition();
                int last = StarHorizontalGridView.this.getLastAttachedPosition();
                for (int i = first; i <= last; i++) {
                    StarHorizontalGridView.this.reLoadTask(StarHorizontalGridView.this.getViewByPosition(i));
                }
            }
        };
        this.mOnItemRecycledListener = new OnItemRecycledListener() {
            public void onItemRecycled(ViewGroup parent, ViewHolder holder) {
                StarHorizontalGridView.this.recycleBitmap(holder.itemView);
                StarHorizontalGridView.this.releaseData(holder.itemView);
            }
        };
        this.mContext = context;
        setLabelColor(ResourceUtil.getColor(R.color.album_list_default_text_color));
        setLabelPadding(getDimen(R.dimen.dimen_30dp), 0, getDimen(R.dimen.dimen_12dp), 0);
        setLabelSize(ResourceUtil.getDimensionPixelSize(R.dimen.dimen_18dp));
        setClipToPadding(false);
        setScrollRoteScale(0.8f, 1.0f, 2.5f);
        setPadding(LEFTPADDING, getDimen(R.dimen.dimen_20dp), LEFTPADDING, 0);
        setVerticalMargin(getDimen(R.dimen.dimen_4dp));
        setHorizontalMargin(getDimen(R.dimen.dimen_011dp));
        setFocusMode(1);
        setFocusLeaveForbidden(83);
        setOnFocusLostListener(this.mOnFocusLostListener);
        setOnItemClickListener(this.mOnItemClickListener);
        setOnScrollListener(this.mOnScrollListener);
        setOnItemRecycledListener(this.mOnItemRecycledListener);
        setOnItemFocusChangedListener(this.mOnItemFocusChangedListener);
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mOnFocusLostListener = null;
        this.mOnItemClickListener = null;
        this.mOnScrollListener = null;
        this.mOnItemRecycledListener = null;
        this.mOnItemFocusChangedListener = null;
        this.mOnStarItemFocusChangedListener = null;
    }

    public StarHorizontalGridView(Context context, StarsAdapter adapter) {
        this(context);
        this.mStarsAdapter = adapter;
        this.mInfoModel = this.mStarsAdapter.getInfoModel();
    }

    public StarHorizontalGridView(Context context, StarsAdapter adapter, GridAdapter gridAdapter) {
        this(context, adapter);
        this.mHorizonAdapter = gridAdapter;
        setAdapter(this.mHorizonAdapter);
    }

    private String getTagName() {
        return this.mTag != null ? this.mTag.getName() : "";
    }

    public boolean isViewVisible(int i) {
        if (i < 0 || i > getLastPosition()) {
            return false;
        }
        boolean leftIn;
        View view = getViewByPosition(i);
        int left = view.getLeft() - getScrollX();
        int right = view.getRight() - getScrollX();
        if (left < 0 || left >= ResourceUtil.getScreenWidth()) {
            leftIn = false;
        } else {
            leftIn = true;
        }
        boolean rightIn;
        if (right <= 0 || right > ResourceUtil.getScreenWidth()) {
            rightIn = false;
        } else {
            rightIn = true;
        }
        if (leftIn || rightIn) {
            return true;
        }
        return false;
    }

    public void init(int rowPosition, int horizontalPosition, int width, int height, List<IData> data, Tag tag) {
        this.mRowIndex = rowPosition;
        setContentWidth(width);
        setContentHeight(height);
        setFocusPosition(horizontalPosition);
        this.mTag = tag;
        this.mHorizonLists = data;
        setLabel(getTagName());
        this.mHorizonAdapter.updateData(this.mHorizonLists);
    }

    private static int getDimen(int id) {
        return ResourceUtil.getDimen(id);
    }

    public int getLine() {
        return this.mRowIndex + 1;
    }

    public void setOnStarHorizontalFocusChangedListener(OnStarItemFocusChangedListener l) {
        this.mOnStarItemFocusChangedListener = l;
    }

    private void recycleBitmap(View view) {
        if (this.mHorizonAdapter != null) {
            this.mHorizonAdapter.recycleBitmap(view);
        }
    }

    private void releaseData(View view) {
        if (this.mHorizonAdapter != null) {
            this.mHorizonAdapter.releaseData(view);
        }
    }

    private void reLoadTask(View view) {
        if (this.mHorizonAdapter != null) {
            this.mHorizonAdapter.onReloadTasks(view);
        }
    }

    public void recycle() {
        int first = getFirstAttachedPosition();
        int last = getLastAttachedPosition();
        for (int i = first; i <= last; i++) {
            recycleBitmap(getViewByPosition(i));
        }
    }

    public void reLoadBitmap() {
        int first = getFirstAttachedPosition();
        int last = getLastAttachedPosition();
        int i = first;
        while (i <= last) {
            View view = getViewByPosition(i);
            if (view != null && isViewVisible(i)) {
                reLoadTask(view);
            }
            i++;
        }
    }
}

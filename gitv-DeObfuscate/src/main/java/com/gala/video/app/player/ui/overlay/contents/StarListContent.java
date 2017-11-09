package com.gala.video.app.player.ui.overlay.contents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.gala.sdk.player.data.IStarData;
import com.gala.video.albumlist4.widget.HorizontalGridView;
import com.gala.video.albumlist4.widget.LayoutManager.FocusPlace;
import com.gala.video.albumlist4.widget.LayoutManager.Orientation;
import com.gala.video.albumlist4.widget.RecyclerView;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemClickListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemFocusChangedListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemRecycledListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnScrollListener;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.app.player.C1291R;
import com.gala.video.app.player.ui.overlay.contents.IContent.IItemListener;
import com.gala.video.app.player.ui.widget.CirclePersonView;
import com.gala.video.app.player.ui.widget.StarListAdapter;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.ArrayList;
import java.util.List;

public class StarListContent implements IContent<List<IStarData>, IStarData> {
    private static final String TAG = "StarListContent";
    private View mContentView;
    private Context mContext;
    private List<IStarData> mDataList = new ArrayList();
    private HorizontalGridView mHorizontalGridView;
    private boolean mIsShown;
    private OnItemFocusChangedListener mItemFocusChangedListener = new C14992();
    private IItemListener<IStarData> mItemListener;
    private OnItemRecycledListener mItemRecycledListener = new C15003();
    private OnItemClickListener mItemSelectedListener = new C14981();
    private OnScrollListener mScrollListener = new C15014();
    private String mTitle;
    private TextView mTitleTextView;

    class C14981 implements OnItemClickListener {
        C14981() {
        }

        public void onItemClick(ViewGroup viewGroup, ViewHolder viewHolder) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(StarListContent.TAG, "onItemSelected ");
            }
            int index = viewHolder.getLayoutPosition();
            if (index < StarListContent.this.mDataList.size() && index >= 0) {
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(StarListContent.TAG, "onItemSelected, focused index=" + index);
                }
                if (!ListUtils.isEmpty(StarListContent.this.mDataList)) {
                    IStarData clickedStar = (IStarData) StarListContent.this.mDataList.get(index);
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1568d(StarListContent.TAG, "onItemSelected clicked item=" + clickedStar);
                    }
                    if (clickedStar == null) {
                        if (LogUtils.mIsDebug) {
                            LogUtils.m1572e(StarListContent.TAG, "onItemSelected: pos=" + index + ", null video!! backtrace=", new Throwable().fillInStackTrace());
                        }
                    } else if (StarListContent.this.mItemListener != null) {
                        StarListContent.this.mItemListener.onItemClicked(clickedStar, index);
                    }
                } else if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(StarListContent.TAG, "onItemSelected, mDataList is empty!");
                }
            } else if (LogUtils.mIsDebug) {
                LogUtils.m1568d(StarListContent.TAG, "onItemSelected, invalid index=" + index);
            }
        }
    }

    class C14992 implements OnItemFocusChangedListener {
        C14992() {
        }

        public void onItemFocusChanged(ViewGroup viewGroup, ViewHolder viewHolder, boolean hasFocus) {
            View v = viewHolder.itemView;
            int index = ((RecyclerView) viewGroup).getFocusPosition();
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(StarListContent.TAG, "onItemFocusChanged, index=" + index + ", hasFocus=" + hasFocus);
            }
            if (!ListUtils.isEmpty(StarListContent.this.mDataList) && index < StarListContent.this.mDataList.size() && index >= 0) {
                if (hasFocus) {
                    v.bringToFront();
                    v.getParent().requestLayout();
                    AnimationUtil.zoomInAnimation(v, 1.1f);
                    return;
                }
                AnimationUtil.zoomOutAnimation(v, 1.0f);
            }
        }
    }

    class C15003 implements OnItemRecycledListener {
        C15003() {
        }

        public void onItemRecycled(ViewGroup viewGroup, ViewHolder viewHolder) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(StarListContent.TAG, ">> onItemRecycled");
            }
            View v = viewHolder.itemView;
            int index = viewHolder.getLayoutPosition();
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(StarListContent.TAG, "onItemRecycled, index=" + index + ", v=" + v);
            }
            ((StarListAdapter) StarListContent.this.mHorizontalGridView.getAdapter()).showDefaultImage((CirclePersonView) v);
        }
    }

    class C15014 extends OnScrollListener {
        C15014() {
        }

        public void onScrollStart() {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(StarListContent.TAG, ">> onScrollStart");
            }
        }

        public void onScrollStop() {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(StarListContent.TAG, ">> onScrollStop");
            }
        }

        public void onScroll(ViewParent arg0, int arg1, int arg2, int arg3) {
        }

        public void onScrollBefore(int arg0) {
        }
    }

    public StarListContent(Context context, String title) {
        this.mContext = context;
        if (title == null) {
            title = "";
        }
        this.mTitle = title;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public View getView() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> getView");
        }
        if (this.mContentView == null) {
            initViews();
        }
        return this.mContentView;
    }

    private void initViews() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> initViews");
        }
        initContentView();
        initGalleryPager();
    }

    private void initContentView() {
        this.mContentView = LayoutInflater.from(this.mContext).inflate(C1291R.layout.player_star_list_content, null);
        this.mTitleTextView = (TextView) this.mContentView.findViewById(C1291R.id.title_text_view);
        this.mTitleTextView.setText(this.mTitle);
        setContentLayoutParams();
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "initContentView() inflate: result=" + this.mContentView);
        }
    }

    private void initGalleryPager() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> initGalleryPager ");
        }
        this.mHorizontalGridView = (HorizontalGridView) this.mContentView.findViewById(C1291R.id.horizontalgirdview);
        setupHorizontalGridView();
        setItemParams();
        if (this.mHorizontalGridView.getAdapter() == null) {
            initAdapter();
        }
    }

    private void setupHorizontalGridView() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> setupHorizontalGridView");
        }
        setLayoutProperties();
        setFocusForbidden();
        setupListeners();
    }

    private void setLayoutProperties() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> setLayoutProperties");
        }
        this.mHorizontalGridView.setOrientation(Orientation.HORIZONTAL);
        this.mHorizontalGridView.setNumRows(1);
        this.mHorizontalGridView.setFocusPlace(FocusPlace.FOCUS_CENTER);
        this.mHorizontalGridView.setFocusMode(1);
        this.mHorizontalGridView.setScrollRoteScale(1.0f, 1.0f, 2.0f);
        this.mHorizontalGridView.setHorizontalMargin(ResourceUtil.getDimen(C1291R.dimen.dimen_36dp));
        this.mHorizontalGridView.setPadding(0, 0, ResourceUtil.getDimen(C1291R.dimen.dimen_36dp), 0);
        if (ListUtils.isEmpty(this.mDataList)) {
            this.mHorizontalGridView.setFocusable(false);
        }
        this.mHorizontalGridView.setQuickFocusLeaveForbidden(false);
    }

    private void setFocusForbidden() {
        this.mHorizontalGridView.setFocusLeaveForbidden(83);
    }

    private void setupListeners() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> setupListeners");
        }
        this.mHorizontalGridView.setOnItemClickListener(this.mItemSelectedListener);
        this.mHorizontalGridView.setOnItemFocusChangedListener(this.mItemFocusChangedListener);
        this.mHorizontalGridView.setOnScrollListener(this.mScrollListener);
        this.mHorizontalGridView.setOnItemRecycledListener(this.mItemRecycledListener);
    }

    private void setItemParams() {
        this.mHorizontalGridView.setContentWidth(ResourceUtil.getDimen(C1291R.dimen.dimen_160dp));
        this.mHorizontalGridView.setContentHeight(ResourceUtil.getDimen(C1291R.dimen.dimen_223dp));
    }

    private void setContentLayoutParams() {
        this.mContentView.setLayoutParams(new LayoutParams(-1, ResourceUtil.getDimen(C1291R.dimen.dimen_290dp)));
    }

    private void initAdapter() {
        StarListAdapter adapter = new StarListAdapter(this.mContext);
        this.mHorizontalGridView.setAdapter(adapter);
        adapter.changeDataSet(this.mDataList);
    }

    public View getFocusableView() {
        return this.mHorizontalGridView;
    }

    public List<IStarData> getContentData() {
        return this.mDataList;
    }

    public void setData(List<IStarData> data) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> setData");
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "setData, data.size=" + data.size());
        }
        this.mDataList.clear();
        this.mDataList.addAll(data);
        if (this.mHorizontalGridView != null) {
            this.mHorizontalGridView.setFocusable(true);
            this.mHorizontalGridView.setFocusPosition(0, true);
            ((StarListAdapter) this.mHorizontalGridView.getAdapter()).changeDataSet(this.mDataList);
        }
    }

    public void setSelection(IStarData item) {
    }

    public void show() {
        if (!this.mIsShown) {
            this.mIsShown = true;
            if (this.mContentView == null) {
                initViews();
            }
            this.mContentView.setVisibility(0);
        }
    }

    public void setItemListener(IItemListener<IStarData> listener) {
        this.mItemListener = listener;
    }

    public void hide() {
        if (this.mIsShown) {
            this.mIsShown = false;
            this.mContentView.setVisibility(8);
        }
    }
}

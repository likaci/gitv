package com.gala.video.app.epg.ui.imsg.mvpl.wrapper;

import android.os.Handler;
import android.os.Looper;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import com.gala.video.albumlist4.widget.LayoutManager.FocusPlace;
import com.gala.video.albumlist4.widget.RecyclerView;
import com.gala.video.albumlist4.widget.RecyclerView.ItemDecoration;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemClickListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemFocusChangedListener;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.albumlist4.widget.VerticalGridView;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.imsg.adapter.MsgAdapter;
import com.gala.video.app.epg.utils.ActivityUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.List;

public class ContentViewWrapper {
    private static final int DEFAULT_DURATION = 200;
    private static final float DEFAULT_SCALE = 1.01f;
    private static final String TAG = "EPG/ContentPresenter";
    private VerticalGridView mGridView;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private MsgAdapter mMsgAdapter;
    private OnFocusChangeListener mOnFocusChangeListener;
    OnItemFocusChangedListener mOnMsgItemFocusChangeListener = new OnItemFocusChangedListener() {
        public void onItemFocusChanged(ViewGroup viewGroup, ViewHolder viewHolder, boolean hasFocus) {
            if (viewHolder != null && viewHolder.itemView != null) {
                if (ContentViewWrapper.this.mOnFocusChangeListener != null) {
                    ContentViewWrapper.this.mOnFocusChangeListener.onFocusChange(viewGroup, hasFocus);
                }
                AnimationUtil.zoomAnimation(viewHolder.itemView, hasFocus ? ContentViewWrapper.DEFAULT_SCALE : 1.0f, 200);
            }
        }
    };

    public ContentViewWrapper(VerticalGridView gridView) {
        this.mGridView = (VerticalGridView) ActivityUtils.checkNotNull(gridView);
        this.mMsgAdapter = new MsgAdapter(ResourceUtil.getContext(), this.mGridView);
        initRightView();
        this.mGridView.setAdapter(this.mMsgAdapter);
    }

    private void initRightView() {
        this.mGridView.setNumRows(1);
        this.mGridView.setFocusMode(1);
        this.mGridView.setScrollRoteScale(0.8f, 1.0f, 2.5f);
        this.mGridView.setPadding(0, 0, getDimen(R.dimen.dimen_13dp), getDimen(R.dimen.dimen_5dp));
        this.mGridView.setContentWidth(getDimen(R.dimen.dimen_1061dp));
        this.mGridView.setContentHeight(getDimen(R.dimen.dimen_94dp));
        this.mGridView.setShakeForbidden(115);
        this.mGridView.setQuickFocusLeaveForbidden(true);
        this.mGridView.setItemDecoration(new ItemDecoration() {
            public int getItemOffsets(int itemPosition, RecyclerView parent) {
                return ContentViewWrapper.this.getDimen(R.dimen.dimen_012dp);
            }
        });
        this.mGridView.setOnItemFocusChangedListener(this.mOnMsgItemFocusChangeListener);
        this.mGridView.setWillNotDraw(false);
        this.mGridView.setVisibility(0);
        this.mGridView.setFocusPlace(FocusPlace.FOCUS_CENTER);
        this.mGridView.setScrollBarDrawable(R.drawable.epg_thumb);
        this.mGridView.setVerticalScrollBarEnabled(true);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mGridView.setOnItemClickListener(listener);
    }

    private int getDimen(int id) {
        return ResourceUtil.getDimen(id);
    }

    public void showMsgs(final List<IMsgContent> list) {
        if (LogUtils.mIsDebug) {
            LogUtils.e(TAG, "showMsgs list:" + ListUtils.getCount((List) list));
        }
        if (this.mHandler != null) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    ContentViewWrapper.this.mMsgAdapter.updateList(list);
                    ContentViewWrapper.this.setScollBarScrollRange(list);
                }
            });
        }
    }

    private void setScollBarScrollRange(List<IMsgContent> list) {
        int count = ListUtils.getCount((List) list);
        this.mGridView.setScrollRange((getDimen(R.dimen.dimen_94dp) * count) - ((count - 1) * getDimen(R.dimen.dimen_12dp)));
    }

    public void updateAllMsgsUI() {
        this.mMsgAdapter.updateAllMsgsUI();
    }

    public void updateMsgUI(ViewHolder viewHolder) {
        this.mMsgAdapter.updateMsgUI(viewHolder);
    }

    public boolean hasMsgData() {
        return this.mGridView != null && this.mGridView.getLastPosition() >= 0;
    }

    public void onDestroy() {
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages(null);
            this.mHandler = null;
        }
    }

    public void setOnFocusChangeListener(OnFocusChangeListener listener) {
        this.mOnFocusChangeListener = listener;
    }

    public int getMsgCount() {
        return this.mMsgAdapter.getCount();
    }

    public void setFocusPosition(int position) {
        this.mGridView.setFocusPosition(position);
    }
}

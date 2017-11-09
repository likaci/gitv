package com.gala.video.app.epg.ui.imsg.mvpl.wrapper;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import com.gala.albumprovider.model.Tag;
import com.gala.video.albumlist4.widget.LayoutManager.FocusPlace;
import com.gala.video.albumlist4.widget.ListView;
import com.gala.video.albumlist4.widget.ListView.ItemDivider;
import com.gala.video.albumlist4.widget.RecyclerView;
import com.gala.video.albumlist4.widget.RecyclerView.ItemDecoration;
import com.gala.video.albumlist4.widget.RecyclerView.OnFocusLostListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemClickListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemFocusChangedListener;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.imsg.adapter.MsgLabelAdapter;
import com.gala.video.app.epg.utils.KeyEventUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.List;
import org.xbill.DNS.WKSRecord.Service;

public class LabelViewWrapper {
    private static final float DEFAULT_SCALE = 1.1f;
    private static final long DELAY_MILLIS = 350;
    private static final int REFRESH_MSG_WHAT_INDEX = 65793;
    private static final String TAG = "EPG/LabelPresenter";
    private int DEFAULT_DURATION = 0;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            LabelViewWrapper.this.mLastLabelItemPos = msg.arg1;
            LabelViewWrapper.this.mListAdapter.resetUIState();
            if (LabelViewWrapper.this.mOnLabelSwitchListener != null) {
                LabelViewWrapper.this.mOnLabelSwitchListener.onLabelSwitched(LabelViewWrapper.this.mLastLabelItemPos);
            }
        }
    };
    private boolean mIsImmediatelyRefreshData = false;
    private int mLastLabelItemPos = 0;
    private MsgLabelAdapter mListAdapter;
    private ListView mListView;
    private OnFocusChangeListener mOnFocusChangeListener;
    OnFocusLostListener mOnLabelFocusLostListener = new OnFocusLostListener() {
        public void onFocusLost(ViewGroup arg0, ViewHolder arg1) {
            int first = LabelViewWrapper.this.mListView.getFirstAttachedPosition();
            int last = LabelViewWrapper.this.mListView.getLastAttachedPosition();
            LabelViewWrapper.this.mListView.setFocusPosition(LabelViewWrapper.this.mLastLabelItemPos, true);
            if (LabelViewWrapper.this.mLastLabelItemPos < first || LabelViewWrapper.this.mLastLabelItemPos > last) {
                LabelViewWrapper.this.mListAdapter.notifyDataSetChanged();
            }
        }
    };
    OnItemClickListener mOnLabelItemClickListener = new OnItemClickListener() {
        public void onItemClick(ViewGroup viewGroup, ViewHolder viewHolder) {
            if (isDataAlreadyLoaded(viewHolder)) {
                KeyEventUtils.simulateKeyEvent(22);
                return;
            }
            LabelViewWrapper.this.mIsImmediatelyRefreshData = true;
            LabelViewWrapper.this.refreshData(viewHolder.getLayoutPosition());
        }

        protected boolean isDataAlreadyLoaded(ViewHolder viewHolder) {
            return LabelViewWrapper.this.mLastLabelItemPos == viewHolder.getLayoutPosition();
        }
    };
    OnItemFocusChangedListener mOnLabelItemFocusChangeListener = new OnItemFocusChangedListener() {
        public void onItemFocusChanged(ViewGroup viewGroup, ViewHolder viewHolder, boolean hasFocus) {
            if (viewHolder != null && viewHolder.itemView != null && LabelViewWrapper.this.mHandler != null) {
                AnimationUtil.zoomLeftAnimation(viewHolder.itemView, hasFocus, 1.1f, LabelViewWrapper.this.DEFAULT_DURATION);
                LabelViewWrapper.this.DEFAULT_DURATION = 200;
                int position = viewHolder.getLayoutPosition();
                if (LabelViewWrapper.this.mOnFocusChangeListener != null) {
                    LabelViewWrapper.this.mOnFocusChangeListener.onFocusChange(viewGroup, hasFocus);
                }
                if (!hasFocus) {
                    LabelViewWrapper.this.mListAdapter.setRemainedPosition(LabelViewWrapper.this.mLastLabelItemPos);
                    LabelViewWrapper.this.mHandler.removeMessages(LabelViewWrapper.REFRESH_MSG_WHAT_INDEX);
                } else if (LabelViewWrapper.this.mListAdapter.getRemainedPosition() == position) {
                    LabelViewWrapper.this.mListAdapter.resetUIState();
                } else {
                    LabelViewWrapper.this.refreshData(position);
                }
            }
        }
    };
    private OnLabelSwitchListener mOnLabelSwitchListener;

    public interface OnLabelSwitchListener {
        void onLabelSwitched(int i);
    }

    public LabelViewWrapper(ListView listView) {
        this.mListView = listView;
        initLeftView();
        this.mListAdapter = new MsgLabelAdapter(ResourceUtil.getContext(), this.mListView);
        this.mListView.setAdapter(this.mListAdapter);
    }

    private void initLeftView() {
        this.mListView.setFocusPlace(FocusPlace.FOCUS_CENTER);
        this.mListView.setFocusMode(1);
        this.mListView.setScrollRoteScale(1.0f, 1.0f, 2.0f);
        this.mListView.setOnItemFocusChangedListener(this.mOnLabelItemFocusChangeListener);
        this.mListView.setOnItemClickListener(this.mOnLabelItemClickListener);
        this.mListView.setOnItemFocusChangedListener(this.mOnLabelItemFocusChangeListener);
        this.mListView.setOnFocusLostListener(this.mOnLabelFocusLostListener);
        this.mListView.setFocusLeaveForbidden(Service.CISCO_FNA);
        this.mListView.setItemDivider(new ItemDivider() {
            public Drawable getItemDivider(int position, RecyclerView arg1) {
                return position == 0 ? null : ResourceUtil.getDrawable(R.drawable.epg_album_label_line);
            }
        });
        this.mListView.setItemDecoration(new ItemDecoration() {
            public int getItemOffsets(int position, RecyclerView parent) {
                return ResourceUtil.getDimen(R.dimen.dimen_1dp);
            }
        });
        this.mListView.setDividerWidth(ResourceUtil.getDimen(R.dimen.dimen_133dp));
        this.mListView.setBackgroundWidth(ResourceUtil.getDimen(R.dimen.dimen_183dp));
        this.mListView.setShakeForbidden(83);
        this.mListView.setPadding(0, ResourceUtil.getDimen(R.dimen.dimen_46dp), 0, 0);
    }

    private void refreshData(int position) {
        if (this.mHandler != null) {
            if (this.mLastLabelItemPos == position) {
                this.mHandler.removeMessages(REFRESH_MSG_WHAT_INDEX);
                return;
            }
            Message msg = this.mHandler.obtainMessage(REFRESH_MSG_WHAT_INDEX);
            msg.arg1 = position;
            this.mHandler.sendMessageDelayed(msg, this.mIsImmediatelyRefreshData ? 0 : DELAY_MILLIS);
            this.mIsImmediatelyRefreshData = false;
        }
    }

    public void setOnLabelSwitchListener(OnLabelSwitchListener listener) {
        this.mOnLabelSwitchListener = listener;
    }

    public void onDestroy() {
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages(null);
            this.mHandler = null;
        }
    }

    public void showLabels(List<Tag> list) {
        if (LogUtils.mIsDebug) {
            LogUtils.e(TAG, "showLabels ,list:" + ListUtils.getCount((List) list));
        }
        this.mListView.setFocusPosition(0);
        this.mListView.requestFocus();
        this.mListAdapter.updateList(list);
    }

    public int getLastLabelItemPos() {
        return this.mLastLabelItemPos;
    }

    public void requestFocus(int position) {
        this.mListView.setFocusPosition(position);
        this.mListView.requestFocus();
    }

    public void setFocusLeaveForbidden(int direction) {
        this.mListView.setFocusLeaveForbidden(direction);
    }

    public void setOnFocusChangeListener(OnFocusChangeListener listener) {
        this.mOnFocusChangeListener = listener;
    }
}

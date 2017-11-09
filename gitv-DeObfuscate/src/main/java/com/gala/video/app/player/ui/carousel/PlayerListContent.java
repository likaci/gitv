package com.gala.video.app.player.ui.carousel;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import com.alibaba.fastjson.asm.Opcodes;
import com.gala.video.albumlist4.widget.LayoutManager.FocusPlace;
import com.gala.video.albumlist4.widget.ListView;
import com.gala.video.albumlist4.widget.RecyclerView.Adapter;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemClickListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemFocusChangedListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemRecycledListener;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.app.player.C1291R;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.widget.ProgressBarNewItem;

public class PlayerListContent extends FrameLayout implements OnItemRecycledListener, OnItemClickListener, OnItemFocusChangedListener {
    private static String TAG_S = "PlayerListContent";
    private String TAG = (TAG_S + hashCode());
    private Context mContext;
    private PlayerListListener mListener;
    private ListView mListview;
    private ProgressBarNewItem mProgress;
    private TextView mTxt;

    protected enum ProgrammeViewMode {
        MODE_NONE,
        MODE_PROGRAMME,
        MODE_PROGRAMME_NODATA,
        MODE_LOADING
    }

    public PlayerListContent(Context context) {
        super(context);
        this.mContext = context;
    }

    public PlayerListContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public PlayerListContent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    public void initView() {
        Log.e(this.TAG, "initView()");
        this.mListview = new ListView(this.mContext);
        addView(this.mListview, new LayoutParams(-2, -1));
        this.mListview.setVerticalScrollBarEnabled(false);
        this.mListview.setOverScrollMode(2);
        this.mListview.setVisibility(8);
        this.mListview.setFocusPlace(FocusPlace.FOCUS_CENTER);
        this.mListview.setFocusMode(1);
        this.mListview.setScrollRoteScale(1.0f, 1.0f, 2.0f);
        this.mListview.setFocusLeaveForbidden(Opcodes.IF_ICMPGT);
        this.mListview.setOnItemFocusChangedListener(this);
        this.mListview.setOnItemRecycledListener(this);
        this.mListview.setOnItemClickListener(this);
        this.mTxt = new TextView(this.mContext);
        LayoutParams txtparams = new LayoutParams(-2, -2);
        txtparams.gravity = 17;
        this.mTxt.setTextSize(0, (float) this.mContext.getResources().getDimensionPixelSize(C1291R.dimen.dimen_27dp));
        this.mTxt.setTextColor(Color.parseColor("#FFFFB400"));
        this.mTxt.setVisibility(8);
        addView(this.mTxt, txtparams);
        this.mProgress = new ProgressBarNewItem(this.mContext);
        LayoutParams barparams = new LayoutParams(this.mContext.getResources().getDimensionPixelSize(C1291R.dimen.dimen_40dp), this.mContext.getResources().getDimensionPixelSize(C1291R.dimen.dimen_40dp));
        barparams.gravity = 17;
        this.mProgress.setVisibility(8);
        addView(this.mProgress, barparams);
        setVisibility(8);
    }

    public void setListParams(int contentwidth, int contentheight, int topMargin, int height) {
        if (this.mListview != null) {
            this.mListview.setContentHeight(contentheight);
            this.mListview.setContentWidth(contentwidth);
            LayoutParams params = (LayoutParams) this.mListview.getLayoutParams();
            params.height = height;
            params.gravity = 16;
            this.mListview.setLayoutParams(params);
        }
    }

    public void setBackgroud(int back) {
        setBackgroud(back);
    }

    public void setAdapter(Adapter adapter) {
        this.mListview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void showList(boolean needFocus) {
        Log.e(this.TAG, "showList()");
        showMode(ProgrammeViewMode.MODE_PROGRAMME, needFocus);
    }

    public void showLoading() {
        showMode(ProgrammeViewMode.MODE_LOADING, false);
    }

    public void showError(String str) {
        if (this.mTxt != null) {
            this.mTxt.setText(str);
            showMode(ProgrammeViewMode.MODE_PROGRAMME_NODATA, false);
        }
    }

    public int getFocusPosition() {
        if (this.mListview != null) {
            return this.mListview.getFocusPosition();
        }
        return -1;
    }

    public void setFocusPosition(int position) {
        Log.e(this.TAG, "setFocusPostition() position=" + position);
        if (this.mListview != null) {
            this.mListview.setFocusPosition(position);
        }
    }

    public boolean isFcoused() {
        boolean selected = false;
        if (this.mListview != null) {
            selected = this.mListview.hasFocus();
        }
        Log.e(this.TAG, "isFcoused() " + selected);
        return selected;
    }

    private void showMode(ProgrammeViewMode mode, boolean needFocus) {
        Log.e(this.TAG, "showMode() mode=" + mode);
        setVisibility(0);
        switch (mode) {
            case MODE_PROGRAMME_NODATA:
                this.mListview.setVisibility(8);
                this.mProgress.setVisibility(8);
                this.mTxt.setVisibility(0);
                return;
            case MODE_PROGRAMME:
                this.mListview.setVisibility(0);
                this.mTxt.setVisibility(8);
                this.mProgress.setVisibility(8);
                return;
            case MODE_LOADING:
                this.mListview.setVisibility(8);
                this.mTxt.setVisibility(8);
                this.mProgress.setVisibility(0);
                return;
            default:
                return;
        }
    }

    public void setListListener(PlayerListListener listener) {
        this.mListener = listener;
    }

    public void onItemFocusChanged(ViewGroup arg0, ViewHolder holder, boolean isSelected) {
        if (this.mListener != null) {
            this.mListener.onItemFocusChanged(holder, isSelected, null, -1);
        }
    }

    public void onItemClick(ViewGroup arg0, ViewHolder holder) {
        if (this.mListener != null) {
            this.mListener.onItemClick(holder, -1);
        }
    }

    public void onItemRecycled(ViewGroup arg0, ViewHolder holder) {
        if (this.mListener != null) {
            this.mListener.onItemRecycled(holder);
        }
    }

    public void hide() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "hide()");
        }
        if (isShown() && this.mListview != null) {
            setVisibility(8);
            this.mListview.setVisibility(8);
            this.mProgress.setVisibility(8);
            this.mTxt.setVisibility(8);
        }
    }

    public boolean isListShow() {
        if (this.mListview != null) {
            return this.mListview.isShown();
        }
        return false;
    }
}

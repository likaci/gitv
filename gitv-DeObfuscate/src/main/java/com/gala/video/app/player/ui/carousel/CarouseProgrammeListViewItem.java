package com.gala.video.app.player.ui.carousel;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gala.video.app.player.C1291R;
import com.gala.video.app.player.ui.widget.views.MarqueeTextView;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;

public class CarouseProgrammeListViewItem extends RelativeLayout {
    private static final int IS_LIVE = 1;
    protected String TAG = "CarouseProgrammeListViewItem";
    private Context mContext;
    private long mEndTime = 0;
    private boolean mIsLive = false;
    private int mItemNormalBgResId;
    private MarqueeTextView mProgrammeName;
    private TextView mProgrammeTime;

    public CarouseProgrammeListViewItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        initViews();
    }

    public CarouseProgrammeListViewItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initViews();
    }

    public CarouseProgrammeListViewItem(Context context) {
        super(context);
        this.mContext = context;
        initViews();
    }

    private void initUI() {
        this.mItemNormalBgResId = C1291R.drawable.player_carousel_btn_transparent;
    }

    private void initViews() {
        this.TAG = "CarouseProgramListViewItem@" + Integer.toHexString(hashCode());
        initUI();
        LayoutInflater.from(this.mContext).inflate(C1291R.layout.player_carousel_programme_listview, this);
        this.mProgrammeName = (MarqueeTextView) findViewById(C1291R.id.channel_programme_name);
        this.mProgrammeTime = (TextView) findViewById(C1291R.id.channel_programme_time);
        setBackgroundResource(this.mItemNormalBgResId);
    }

    public void setProgrammeName(String name) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "setProgrammeName = " + name);
        }
        this.mProgrammeName.setViewBound(this.mContext.getResources().getDimensionPixelSize(C1291R.dimen.dimen_237dp), this.mContext.getResources().getDimensionPixelSize(C1291R.dimen.dimen_28dp));
        this.mProgrammeName.setText(name);
    }

    public MarqueeTextView getProgramName() {
        return this.mProgrammeName;
    }

    public TextView getProgramTime() {
        return this.mProgrammeTime;
    }

    public void setProgrammeTime(String time) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "setProgrammeTime = " + time);
        }
        this.mProgrammeTime.setText(time);
    }

    private void showLiveStyle() {
        Log.d(this.TAG, "showLiveStyle() live=" + this.mIsLive + ", mEndTime=" + this.mEndTime);
        if (!this.mIsLive) {
            return;
        }
        if (DeviceUtils.getServerTimeMillis() < this.mEndTime || this.mEndTime <= 0) {
            this.mProgrammeName.setTextColor(Color.parseColor("#b2b2b2"));
            this.mProgrammeTime.setTextColor(Color.parseColor("#777777"));
            return;
        }
        this.mProgrammeName.setTextColor(Color.parseColor("#80f1f1f1"));
        this.mProgrammeTime.setTextColor(Color.parseColor("#80f1f1f1"));
    }

    public void clearItem() {
        Log.d(this.TAG, "clearItem()");
        this.mProgrammeName.setText(null);
        this.mProgrammeTime.setText(null);
    }

    public void setIsLive(int live) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "setIsLive(" + live + ")");
        }
        if (live == 1) {
            this.mIsLive = true;
        } else {
            this.mIsLive = false;
        }
        showLiveStyle();
    }

    public void setEndTime(String end) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "setEndTime(" + end + ")");
        }
        if (StringUtils.isEmpty((CharSequence) end)) {
            this.mEndTime = 0;
        } else {
            this.mEndTime = Long.parseLong(end);
        }
    }
}

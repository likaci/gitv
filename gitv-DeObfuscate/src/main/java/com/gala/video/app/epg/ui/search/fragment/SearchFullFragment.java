package com.gala.video.app.epg.ui.search.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.ui.albumlist.widget.PhotoGridParams;
import com.gala.video.app.epg.ui.albumlist.widget.PhotoGridView;
import com.gala.video.app.epg.ui.albumlist.widget.WidgetStatusListener;
import com.gala.video.app.epg.ui.search.SearchEnterUtils;
import com.gala.video.app.epg.ui.search.adapter.FullKeyBoardAdapter;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.AnimationUtil;
import java.util.ArrayList;
import java.util.List;

public class SearchFullFragment extends SearchBaseFragment {
    private PhotoGridView mFullKeyboard;
    private WidgetStatusListener mFullKeyboardListener = new C09882();
    private FullKeyBoardAdapter mKeyboardAdapter;
    private List<String> mKeyboardNumList;
    private View mMainView;

    class C09871 implements Runnable {
        C09871() {
        }

        public void run() {
            SearchFullFragment.this.mFullKeyboard.setAdapter(SearchFullFragment.this.mKeyboardAdapter);
            if (SearchBaseFragment.mIsRequireFocus) {
                SearchFullFragment.this.requestDefaultFocus();
            }
        }
    }

    class C09882 implements WidgetStatusListener {
        C09882() {
        }

        public void onLoseFocus(ViewGroup parent, View view, int position) {
        }

        public void onItemTouch(View view, MotionEvent arg1, int position) {
        }

        public void onItemSelectChange(View view, int position, boolean focus) {
            TextView textView = (TextView) view;
            if (focus) {
                if (Project.getInstance().getBuild().isLitchi()) {
                    textView.setTextColor(SearchFullFragment.this.getColor(C0508R.color.gala_write));
                } else {
                    textView.setTextColor(SearchFullFragment.this.getColor(C0508R.color.gala_write));
                }
                AnimationUtil.scaleAnimation(view, 1.0f, 1.1f, 200);
                return;
            }
            textView.setTextColor(SearchFullFragment.this.getColor(C0508R.color.keyboard_num));
            if (position <= 25) {
                textView.setTextColor(SearchFullFragment.this.getColor(C0508R.color.keyboard_letter));
            }
            AnimationUtil.scaleAnimation(view, 1.1f, 1.0f, 200);
        }

        public void onItemClick(ViewGroup viewGroup, View view, int position) {
            if (SearchFullFragment.this.mContext != null) {
                SearchFullFragment.this.sendClickPingback();
                if (!Project.getInstance().getBuild().isOpenTestPerformance()) {
                    AnimationUtil.clickScaleAnimation(view);
                    if (!SearchEnterUtils.checkNetWork(SearchFullFragment.this.mContext)) {
                        return;
                    }
                }
                if (SearchFullFragment.this.mSearchEvent != null && SearchFullFragment.this.mSearchEvent.onInputText(((TextView) view).getText().toString())) {
                    SearchFullFragment.this.mSearchEvent.onKeyBoardTextChanged();
                }
            }
        }
    }

    class C09893 implements Runnable {
        C09893() {
        }

        public void run() {
            SearchFullFragment.this.mFullKeyboard.getViewByPos(14).requestFocus();
            SearchFullFragment.this.mKeyboardAdapter.changeTextColor(SearchFullFragment.this.mFullKeyboard.getViewByPos(14));
        }
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (this.mSearchEvent != null) {
            this.mSearchEvent.onAttachActivity(this);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mMainView = inflater.inflate(C0508R.layout.epg_fragment_full_keyboard, null);
        init();
        return this.mMainView;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() != 0) {
            return super.dispatchKeyEvent(event);
        }
        LogUtils.m1568d("test", "test>>>>>QSearchFullKeyboardFragment --- dispatchKeyEvent" + System.currentTimeMillis());
        int keyCode = event.getKeyCode();
        if (this.mSearchEvent != null && (keyCode == 21 || keyCode == 22 || keyCode == 19 || keyCode == 20 || keyCode == 23)) {
            this.mSearchEvent.onInputAdd();
        }
        if (keyCode >= 7 && keyCode <= 16 && this.mSearchEvent != null) {
            if (this.mSearchEvent.onInputText((keyCode - 7) + "")) {
                this.mSearchEvent.onKeyBoardTextChanged();
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private void init() {
        this.mFullKeyboard = (PhotoGridView) this.mMainView.findViewById(C0508R.id.epg_full_keyboard_gridview);
        initKeyboard();
        this.mFullKeyboard.setListener(this.mFullKeyboardListener);
    }

    public int getPageLocationType() {
        return 0;
    }

    private void initKeyboard() {
        if (this.mContext != null) {
            String[] keyNum = this.mContext.getResources().getStringArray(C0508R.array.full_keyboard);
            this.mKeyboardNumList = new ArrayList();
            for (String value : keyNum) {
                this.mKeyboardNumList.add(value);
            }
            this.mFullKeyboard.setNextLeftFocusLeaveAvail(false);
            this.mFullKeyboard.setNextRightFocusLeaveAvail(true);
            this.mFullKeyboard.setNextUpFocusLeaveAvail(true);
            this.mFullKeyboard.setNextDownFocusLeaveAvail(true);
            setKeyboardParams();
            if (this.mContext != null) {
                this.mKeyboardAdapter = new FullKeyBoardAdapter(this.mContext, this.mKeyboardNumList);
                this.mFullKeyboard.post(new C09871());
            }
        }
    }

    private void setKeyboardParams() {
        this.mFullKeyboard.setParams(getFullKeyboardParams());
    }

    private PhotoGridParams getFullKeyboardParams() {
        PhotoGridParams p = new PhotoGridParams();
        p.columnNum = 6;
        p.verticalSpace = getDimen(C0508R.dimen.dimen_8dp);
        p.horizontalSpace = getDimen(C0508R.dimen.dimen_13dp);
        p.contentHeight = getDimen(C0508R.dimen.dimen_53dp);
        p.contentWidth = getDimen(C0508R.dimen.dimen_53dp);
        p.scaleRate = 1.1f;
        return p;
    }

    private void sendClickPingback() {
        PingBackParams params = new PingBackParams();
        params.add(Keys.f2035T, "20").add("rseat", "fullkeyboard").add("rpage", "srch_keyboard").add("block", "fullkeyboard").add("rt", "i");
        PingBack.getInstance().postPingBackToLongYuan(params.build());
    }

    public void requestDefaultFocus() {
        if (this.mFullKeyboard != null && this.mFullKeyboard.getViewByPos(14) != null) {
            this.mFullKeyboard.getViewByPos(14).post(new C09893());
        }
    }
}

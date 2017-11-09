package com.gala.video.app.epg.ui.search.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.search.IKeyboardListener;
import com.gala.video.app.epg.ui.search.SearchEnterUtils;
import com.gala.video.app.epg.ui.search.adapter.T9KeyboardAdapter;
import com.gala.video.app.epg.ui.search.widget.T9Keyboard;
import com.gala.video.app.epg.ui.search.widget.T9Keyboard.OnT9FocusListener;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.utils.AnimationUtil;

public class SearchT9Fragment extends SearchBaseFragment {
    private IKeyboardListener mKeyboardListener = new IKeyboardListener() {
        public void onTextChange(String text) {
            LogUtils.i("T9", ">>>>>text ------" + text);
            if (SearchEnterUtils.checkNetWork(SearchT9Fragment.this.mContext)) {
                SearchT9Fragment.this.sendClickPingback();
                if (SearchT9Fragment.this.mSearchEvent != null && SearchT9Fragment.this.mSearchEvent.onInputText(text)) {
                    SearchT9Fragment.this.mSearchEvent.onKeyBoardTextChanged();
                }
            }
        }
    };
    private View mMainView;
    private OnT9FocusListener mT9FocusListener = new OnT9FocusListener() {
        public void onFocus(View view, int pos, boolean hasFocus) {
            AnimationUtil.zoomAnimation(view, hasFocus, 1.1f, 200);
            T9KeyboardAdapter t9KeyboardAdapter = SearchT9Fragment.this.mT9Keyboard.getT9KeyboardAdapter();
            if (t9KeyboardAdapter != null) {
                t9KeyboardAdapter.changeTextColor(view, pos, hasFocus);
            }
        }
    };
    private T9Keyboard mT9Keyboard;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (this.mSearchEvent != null) {
            this.mSearchEvent.onAttachActivity(this);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mMainView = inflater.inflate(R.layout.epg_fragment_t9_keyboard, null);
        init();
        return this.mMainView;
    }

    private void init() {
        this.mT9Keyboard = (T9Keyboard) this.mMainView.findViewById(R.id.epg_keyboard_t9);
        initKeyboard();
    }

    private void initKeyboard() {
        this.mT9Keyboard.setKeyPanelParams(getDimen(R.dimen.dimen_86dp), getDimen(R.dimen.dimen_83dp), getDimen(R.dimen.dimen_28dp), getDimen(R.dimen.dimen_46dp));
        this.mT9Keyboard.setLayerParams(getDimen(R.dimen.dimen_53dp), getDimen(R.dimen.dimen_53dp));
        this.mT9Keyboard.setOnFocusListener(this.mT9FocusListener);
        this.mT9Keyboard.setKeyboardListener(this.mKeyboardListener);
        this.mT9Keyboard.generate();
        if (mIsRequireFocus) {
            requestDefaultFocus();
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() != 0) {
            return super.dispatchKeyEvent(event);
        }
        int keyCode = event.getKeyCode();
        if (this.mSearchEvent != null && (keyCode == 21 || keyCode == 22 || keyCode == 19 || keyCode == 20 || keyCode == 23)) {
            this.mSearchEvent.onInputAdd();
        }
        if (keyCode >= 7 && keyCode <= 16 && this.mSearchEvent != null) {
            if (this.mSearchEvent.onInputText((keyCode - 7) + "")) {
                this.mSearchEvent.onKeyBoardTextChanged();
            }
        }
        if (keyCode == 4) {
            return this.mT9Keyboard.onKeyDown(event.getKeyCode(), event);
        }
        if (keyCode == 19) {
            this.mT9Keyboard.onKeyDown(event.getKeyCode(), event);
            return super.dispatchKeyEvent(event);
        } else if (keyCode == 20) {
            this.mT9Keyboard.onKeyDown(event.getKeyCode(), event);
            return super.dispatchKeyEvent(event);
        } else if (keyCode == 21) {
            this.mT9Keyboard.onKeyDown(event.getKeyCode(), event);
            return super.dispatchKeyEvent(event);
        } else if (keyCode != 22) {
            return super.dispatchKeyEvent(event);
        } else {
            this.mT9Keyboard.onKeyDown(event.getKeyCode(), event);
            return super.dispatchKeyEvent(event);
        }
    }

    public int getPageLocationType() {
        return 0;
    }

    public void requestDefaultFocus() {
        this.mT9Keyboard.requestDefaultFocus();
    }

    private void sendClickPingback() {
        PingBackParams params = new PingBackParams();
        params.add(Keys.T, "20").add("rseat", "t9keyboard").add("rpage", "srch_keyboard").add("block", "t9keyboard").add("rt", "i");
        PingBack.getInstance().postPingBackToLongYuan(params.build());
    }
}

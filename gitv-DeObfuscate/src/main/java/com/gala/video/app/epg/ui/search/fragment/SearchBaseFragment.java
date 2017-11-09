package com.gala.video.app.epg.ui.search.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import com.gala.multiscreen.dmr.model.MSMessage.KeyKind;
import com.gala.multiscreen.dmr.model.MSMessage.RequestKind;
import com.gala.video.app.epg.QBaseFragment;
import com.gala.video.app.epg.ui.search.ISearchEvent;
import com.gala.video.app.epg.ui.search.db.SearchHistoryDao;
import java.util.List;

public abstract class SearchBaseFragment extends QBaseFragment {
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    protected static final String TAG_KEY_WORDS = "KEY_WORDS";
    protected static List<String> mHotHordsCacheList;
    public static boolean mIsRequireFocus = true;
    protected int START_REQUESTCODE = 1;
    protected Handler mBaseHandler = new Handler(Looper.getMainLooper());
    protected Context mContext;
    protected SearchHistoryDao mHistoryDao;
    protected ISearchEvent mSearchEvent;

    protected void runOnUiThread(Runnable action) {
        this.mBaseHandler.post(action);
    }

    protected void onActionFlingEvent(KeyKind kind) {
    }

    protected String onActionNotifyEvent(RequestKind kind, String message) {
        return null;
    }

    protected void onNewIntent(Intent intent) {
    }

    protected void changeTabValid(int pageType) {
        switch (pageType) {
            case 0:
                setTabFocusble(true);
                return;
            case 1:
                setTabFocusble(false);
                return;
            default:
                return;
        }
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mContext = activity;
            this.mSearchEvent = (ISearchEvent) activity;
            this.mHistoryDao = new SearchHistoryDao(activity);
        } catch (Exception e) {
            throw new IllegalStateException("your activity must implements ISearchEvent  !");
        }
    }

    public void onDetach() {
        this.mSearchEvent = null;
        super.onDetach();
    }

    public void onKeyBoardTextChanged(String keyWords) {
        SearchSuggestFragment fragment = new SearchSuggestFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TAG_KEY_WORDS, keyWords);
        fragment.setArguments(bundle);
        if (this.mSearchEvent != null) {
            this.mSearchEvent.onSwitchFragment(fragment);
        }
    }

    protected void showLoading() {
        if (this.mSearchEvent != null) {
            this.mSearchEvent.showLoading();
        }
    }

    protected void hideLoading() {
        if (this.mSearchEvent != null) {
            this.mSearchEvent.hideLoading();
        }
    }

    protected void startSearch(int clickType, String key, String qpid, int mode, int requestCode) {
        if (this.mSearchEvent != null) {
            this.mSearchEvent.onStartSearch(clickType, key, qpid, mode, requestCode);
        }
    }

    protected void setTabFocusble(boolean isFocusble) {
        if (this.mSearchEvent != null) {
            this.mSearchEvent.onChangeTabFocusable(isFocusble);
        }
    }

    public void requestDefaultFocus() {
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return false;
    }

    public int getPageLocationType() {
        return 1;
    }

    protected int getDimen(int id) {
        if (this.mContext != null) {
            return (int) this.mContext.getResources().getDimension(id);
        }
        return 0;
    }

    protected int getColor(int id) {
        if (this.mContext != null) {
            return this.mContext.getResources().getColor(id);
        }
        return 0;
    }

    protected String getStr(int id) {
        if (this.mContext != null) {
            return this.mContext.getResources().getString(id);
        }
        return "";
    }

    protected Drawable getDrawable(int id) {
        if (this.mContext != null) {
            return this.mContext.getResources().getDrawable(id);
        }
        return null;
    }
}

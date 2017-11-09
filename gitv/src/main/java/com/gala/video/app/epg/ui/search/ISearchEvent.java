package com.gala.video.app.epg.ui.search;

import android.view.View;
import com.gala.video.app.epg.ui.search.fragment.SearchBaseFragment;

public interface ISearchEvent {
    void hideLoading();

    boolean isSuggestDisplay();

    void onAttachActivity(SearchBaseFragment searchBaseFragment);

    void onChangeClearViewFocus(View view);

    void onChangeSuggestDisplay(boolean z);

    void onChangeTabFocusable(boolean z);

    String onGetSearchText();

    void onInputAdd();

    boolean onInputText(String str);

    void onKeyBoardTextChanged();

    void onRequestRightDefaultFocus();

    void onStartSearch(int i, String str, String str2, int i2, int i3);

    void onSuggestClickPingback(String str, String str2, int i, String str3, String str4, boolean z);

    void onSwitchFragment(SearchBaseFragment searchBaseFragment);

    void showLoading();
}

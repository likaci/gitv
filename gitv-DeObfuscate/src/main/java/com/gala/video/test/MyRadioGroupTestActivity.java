package com.gala.video.test;

import android.app.Activity;
import android.os.Bundle;
import com.gala.video.widget.MyRadioGroup;
import com.gala.video.widget.test.R;
import java.util.ArrayList;
import java.util.List;

public class MyRadioGroupTestActivity extends Activity {
    private MyRadioGroup mRadioGroup;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_radio_group_test);
        initViews();
        initData();
    }

    private void initData() {
        List<String> items = new ArrayList();
        items.add("Add");
        items.add("Delete");
        this.mRadioGroup.setDataSource(items, 0);
    }

    private void initViews() {
        this.mRadioGroup = (MyRadioGroup) findViewById(R.id.radiogroup1);
        setupMyRadioGroupCommon(this.mRadioGroup);
    }

    private void setupMyRadioGroupCommon(MyRadioGroup radioGroup) {
        radioGroup.setTextSize(getDimensionPixelSize(R.dimen.dimen_27sp));
        radioGroup.setTextColors(getColor(R.color.radio_group_txt_color_default), getColor(R.color.radio_group_txt_color_selected), getColor(R.color.radio_group_txt_color_focused), getColor(R.color.radio_group_txt_color_disabled));
        radioGroup.setItemBackground(R.drawable.episode_item_bg2);
        radioGroup.setDimens(new int[]{getDimensionPixelSize(R.dimen.dimen_147dp), getDimensionPixelSize(R.dimen.dimen_61dp)});
        radioGroup.setZoomEnabled(true);
        radioGroup.setChildAutoAlignTop(false);
        radioGroup.setContentSpacing(0);
        radioGroup.setDividerPadding(getResources().getDimensionPixelSize(R.dimen.player_definition_widget_divider_padding));
        radioGroup.setShowDivider(((0 | 1) | 2) | 4);
        radioGroup.setDividerDrawable(R.drawable.radio_item_divider);
    }

    private int getDimensionPixelSize(int resId) {
        return getResources().getDimensionPixelSize(resId);
    }

    private int getColor(int resId) {
        return getResources().getColor(resId);
    }
}

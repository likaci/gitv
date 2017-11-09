package com.gala.video.test;

import android.app.Activity;
import android.os.Bundle;
import com.gala.video.widget.episode.DimensParamBuilder;
import com.gala.video.widget.episode.EpisodeListView;
import com.gala.video.widget.episode.ItemStyleParamBuilder;
import com.gala.video.widget.episode.ParentLayoutMode;
import com.gala.video.widget.test.R;

public class EpisodeTestActivity extends Activity {
    private EpisodeListView mEpListMain;
    private EpisodeListView mEpListMirror;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episodetest);
        initViews();
        initData();
    }

    private void initViews() {
        this.mEpListMain = (EpisodeListView) findViewById(R.id.episode_main);
        this.mEpListMain.setItemBackgroundResource(R.drawable.episode_item_bg2);
        DimensParamBuilder dimensBuilder = new DimensParamBuilder();
        dimensBuilder.setChildTextSizeResId(R.dimen.dimen_33sp).setChildWidth(getResources().getDimensionPixelSize(R.dimen.dimen_111dp)).setChildHeight(getResources().getDimensionPixelSize(R.dimen.dimen_57dp)).setItemSpacing(0).setParentHeight(getResources().getDimensionPixelSize(R.dimen.dimen_30dp)).setParentLayoutMode(ParentLayoutMode.SINGLE_CHILD_WIDTH).setParentTextSizeResId(R.dimen.dimen_20sp);
        this.mEpListMain.setDimens(dimensBuilder);
        ItemStyleParamBuilder styleBuilder = new ItemStyleParamBuilder();
        styleBuilder.setTextNormalColor(-1).setTextFocusedColor(-1).setTextSelectedColor(-65536).setParentTextNormalColor(-16711681);
        this.mEpListMain.setItemTextStyle(styleBuilder);
        this.mEpListMain.setZoomEnabled(false);
        this.mEpListMain.setAutoFocusSelection(true);
        this.mEpListMirror = (EpisodeListView) findViewById(R.id.episode_mirror);
        this.mEpListMirror.setItemBackgroundResource(R.drawable.episode_item_bg2);
        this.mEpListMirror.setDimens(new int[]{R.dimen.dimen_33sp, getResources().getDimensionPixelSize(R.dimen.dimen_111dp), getResources().getDimensionPixelSize(R.dimen.dimen_57dp)}, 0);
        this.mEpListMirror.setItemTextStyle(-1, -7681775, -1);
        this.mEpListMirror.setZoomEnabled(false);
        this.mEpListMirror.setAutoFocusSelection(true);
    }

    private void initData() {
        this.mEpListMain.setDataSource(14, 1);
        this.mEpListMirror.setDataSource(14, 1);
    }
}

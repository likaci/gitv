package com.gala.video.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.gala.video.widget.test.R;

public class MainActivity extends Activity implements OnClickListener {
    private Button mBtnEpisodeTest;
    private Button mBtnGridViewPagerTest;
    private Button mBtnMyRadioGroupTest;
    private Button mBtnNetworkManagerTest;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        this.mBtnEpisodeTest = (Button) findViewById(R.id.btn_episodetest);
        this.mBtnEpisodeTest.setOnClickListener(this);
        this.mBtnNetworkManagerTest = (Button) findViewById(R.id.btn_networkmanagertest);
        this.mBtnNetworkManagerTest.setOnClickListener(this);
        this.mBtnGridViewPagerTest = (Button) findViewById(R.id.btn_gridviewpagertest);
        this.mBtnGridViewPagerTest.setOnClickListener(this);
        this.mBtnMyRadioGroupTest = (Button) findViewById(R.id.btn_myradiogrouptest);
        this.mBtnMyRadioGroupTest.setOnClickListener(this);
    }

    public void onClick(View view) {
        throw new Error("Unresolved compilation problems: \n\tcase expressions must be constant expressions\n\tcase expressions must be constant expressions\n\tcase expressions must be constant expressions\n");
    }
}

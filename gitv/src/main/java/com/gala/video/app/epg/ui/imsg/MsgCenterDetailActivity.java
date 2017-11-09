package com.gala.video.app.epg.ui.imsg;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.imsg.mvpd.MsgDetailFragment;
import com.gala.video.app.epg.ui.imsg.mvpd.MsgDetailPresenter;
import com.gala.video.app.epg.utils.ActivityUtils;
import com.gala.video.lib.share.common.activity.QMultiScreenActivity;

public class MsgCenterDetailActivity extends QMultiScreenActivity {
    MsgDetailFragment mFragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.epg_activity_msg_detail);
        this.mFragment = (MsgDetailFragment) getFragmentManager().findFragmentById(R.id.epg_layout_msg_detail);
        if (this.mFragment == null) {
            this.mFragment = MsgDetailFragment.newInstance();
        }
        MsgDetailPresenter msgDetailPresenter = new MsgDetailPresenter(this.mFragment);
        ActivityUtils.replaceFragment(getFragmentManager(), this.mFragment, R.id.epg_layout_msg_detail);
    }

    public View getBackgroundContainer() {
        return findViewById(R.id.epg_layout_msg_detail);
    }

    public boolean handleKeyEvent(KeyEvent event) {
        if ((event.getKeyCode() == 4 || event.getKeyCode() == 111) && event.getAction() == 0 && this.mFragment != null) {
            this.mFragment.sendClickPingBack(false);
        }
        return super.handleKeyEvent(event);
    }
}

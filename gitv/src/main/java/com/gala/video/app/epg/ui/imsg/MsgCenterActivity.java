package com.gala.video.app.epg.ui.imsg;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import com.gala.imageprovider.ImageProviderApi;
import com.gala.video.app.epg.ui.imsg.fetch.TasksRepository;
import com.gala.video.app.epg.ui.imsg.mvpl.MsgFragment;
import com.gala.video.app.epg.ui.imsg.mvpl.MsgPresenter;
import com.gala.video.app.epg.utils.ActivityUtils;
import com.gala.video.lib.share.common.activity.QMultiScreenActivity;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import java.util.ArrayList;
import java.util.Iterator;

public class MsgCenterActivity extends QMultiScreenActivity {
    private boolean mIsFromOutside = false;
    private ArrayList<IMsgCenterKeyEventListener> mKeyEventListeners = new ArrayList(1);
    private MsgPresenter mMsgPresenter;
    private View mRootView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stopImageProvider();
        this.mIsFromOutside = getIntent().getBooleanExtra("isFromOutside", false);
        MsgFragment fragment = new MsgFragment();
        this.mMsgPresenter = new MsgPresenter(new TasksRepository(), fragment);
        ActivityUtils.replaceFragment(getFragmentManager(), fragment, 16908290);
    }

    private void stopImageProvider() {
        ImageProviderApi.getImageProvider().stopAllTasks();
    }

    public boolean handleKeyEvent(KeyEvent event) {
        if (event.getAction() != 0) {
            return super.handleKeyEvent(event);
        }
        int keyCode = event.getKeyCode();
        boolean result = false;
        Iterator it = this.mKeyEventListeners.iterator();
        while (it.hasNext()) {
            IMsgCenterKeyEventListener listener = (IMsgCenterKeyEventListener) it.next();
            if (listener != null) {
                result |= listener.onKeyEvent(keyCode);
            }
        }
        if (result) {
            return result;
        }
        if (keyCode == 4 || keyCode == 111) {
            handleExitEvent();
            finish();
        }
        return super.handleKeyEvent(event);
    }

    private View getRootView() {
        if (this.mRootView == null) {
            this.mRootView = getWindow().getDecorView().findViewById(16908290);
        }
        return this.mRootView;
    }

    protected View getBackgroundContainer() {
        return getRootView();
    }

    public void registerKeyEventListener(IMsgCenterKeyEventListener listener) {
        this.mKeyEventListeners.add(listener);
    }

    public void unRegisterKeyEventListener(IMsgCenterKeyEventListener listener) {
        this.mKeyEventListeners.remove(listener);
    }

    private void handleExitEvent() {
        if (this.mIsFromOutside) {
            startHomePage();
        }
        stopImageProvider();
    }

    private void startHomePage() {
        CreateInterfaceTools.createEpgEntry().startHomeActivity(this, true);
    }
}

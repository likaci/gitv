package com.gala.video.app.epg.home.data.tool;

import android.util.Log;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.video.lib.share.ifimpl.ucenter.account.impl.LoginCallbackRecorder;
import com.gala.video.lib.share.ifimpl.ucenter.account.impl.LoginCallbackRecorder.LoginCallbackRecorderListener;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus.MyObserver;

public class GroupDetailABTestListener {
    private static final String TAG = "GroupDetailABTestListener";
    private static GroupDetailABTestListener mInstance = null;
    private LoginCallbackRecorderListener mListener;
    private MyObserver mObserver;

    public void setListener() {
        Log.d(TAG, "setListener");
        GetInterfaceTools.getDataBus().registerSubscriber(IDataBus.DYNAMIC_REQUEST_FINISHED_EVENT, this.mObserver);
    }

    private GroupDetailABTestListener() {
        this.mListener = null;
        this.mObserver = null;
        this.mListener = new LoginCallbackRecorderListener() {
            public void onLogout(String uid) {
                TVApiBase.getTVApiProperty().setPassportId("0");
                Log.d(GroupDetailABTestListener.TAG, "onLogout,passportid = " + TVApiBase.getTVApiProperty().getPassportId());
            }

            public void onLogin(String uid) {
                TVApiBase.getTVApiProperty().setPassportId(uid);
                Log.d(GroupDetailABTestListener.TAG, "onLogin,passportid = " + TVApiBase.getTVApiProperty().getPassportId());
            }
        };
        this.mObserver = new MyObserver() {
            public void update(String event) {
                boolean a = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel().getIsCardSort();
                Log.d(GroupDetailABTestListener.TAG, "event = " + event + ", isCardSort = " + GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel().getIsCardSort());
                if (GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel().getIsCardSort()) {
                    TVApiBase.getTVApiProperty().setPassportId(GetInterfaceTools.getIGalaAccountManager().getUID());
                    Log.d(GroupDetailABTestListener.TAG, "true passportid = " + TVApiBase.getTVApiProperty().getPassportId());
                    LoginCallbackRecorder.get().addListener(GroupDetailABTestListener.this.mListener);
                    return;
                }
                TVApiBase.getTVApiProperty().setPassportId(null);
                Log.d(GroupDetailABTestListener.TAG, "false passportid = " + TVApiBase.getTVApiProperty().getPassportId());
                LoginCallbackRecorder.get().removeListener(GroupDetailABTestListener.this.mListener);
            }
        };
    }

    public static GroupDetailABTestListener get() {
        if (mInstance == null) {
            mInstance = new GroupDetailABTestListener();
        }
        return mInstance;
    }
}

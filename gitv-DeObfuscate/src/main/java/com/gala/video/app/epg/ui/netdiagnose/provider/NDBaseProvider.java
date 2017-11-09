package com.gala.video.app.epg.ui.netdiagnose.provider;

import android.content.Context;
import android.util.Log;
import com.gala.sdk.player.BitStream;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.coreservice.netdiagnose.INDUploadCallback;
import com.gala.video.lib.framework.coreservice.netdiagnose.INDWrapperOperate;
import com.gala.video.lib.framework.coreservice.netdiagnose.INetDiagnoseController;
import com.gala.video.lib.framework.coreservice.netdiagnose.model.NetDiagnoseInfo;
import com.gala.video.lib.share.ifimpl.netdiagnose.NetDiagnoseController;
import com.gala.video.lib.share.ifimpl.netdiagnose.model.CDNNetDiagnoseInfo;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.IGalaAccountManager;
import com.gala.video.lib.share.system.preference.setting.SettingPlayPreference;
import com.gala.video.lib.share.utils.DataUtils;
import java.util.ArrayList;
import java.util.List;

public abstract class NDBaseProvider {
    public static final String ENDSTRING = "\n-------end--------\r\n";
    private static final String TAG = "NDBaseProvider";
    protected Context mContext;
    protected INetDiagnoseController mController;
    private INetDiagnoseResultListener mINetDiagnoseResultLinstener;
    protected NetDiagnoseInfo mNetDiagnoseInfo;
    protected int mPlayerType = 0;
    protected NetDiagnoseInfo mResult;
    protected INDUploadCallback mUploadcallback = new C09381();
    private List<INDWrapperOperate> mWrapperList = null;

    public interface INetDiagnoseResultListener {
        void onReslut(String str);
    }

    class C09381 implements INDUploadCallback {
        C09381() {
        }

        public void uploadNetDiagnoseDone() {
            Log.i(NDBaseProvider.TAG, "onFinish mUploadcallback uploadNetDiagnoseDone");
            NDBaseProvider.this.mController.stopCheck();
        }
    }

    public abstract void initWrapperList();

    public abstract void uploadResultInfo(StringBuilder stringBuilder);

    public NDBaseProvider(Context context) {
        this.mContext = context;
    }

    public void setPlayerType(int playerType) {
        this.mPlayerType = playerType;
    }

    public void setReslut(NetDiagnoseInfo result) {
        this.mResult = result;
    }

    public void startCheckEx() {
        if (this.mController == null) {
            initInfo();
            this.mController = new NetDiagnoseController(this.mContext, this.mNetDiagnoseInfo);
        }
        initDefaultWrapperList();
        this.mController.startCheckEx(this.mWrapperList);
    }

    public void addWrapperJob(INDWrapperOperate wrapperOperate) {
        this.mWrapperList.add(wrapperOperate);
    }

    private void initDefaultWrapperList() {
        if (ListUtils.isEmpty(this.mWrapperList)) {
            this.mWrapperList = new ArrayList();
        }
        this.mWrapperList.clear();
        initWrapperList();
    }

    private void initInfo() {
        if (this.mNetDiagnoseInfo == null) {
            IGalaAccountManager manager = GetInterfaceTools.getIGalaAccountManager();
            BitStream bitStream = new BitStream();
            bitStream.setDefinition(SettingPlayPreference.getNetDoctorBitStreamDefinition(this.mContext));
            bitStream.setAudioType(SettingPlayPreference.getNetDoctorAudioType(this.mContext));
            bitStream.setCodecType(SettingPlayPreference.getNetDoctorCodecType(this.mContext));
            bitStream.setDynamicRangeType(SettingPlayPreference.getNetDoctorDRType(this.mContext));
            this.mNetDiagnoseInfo = new CDNNetDiagnoseInfo(manager.getAuthCookie(), manager.getUID(), manager.getUserType(), 0, BitStream.getBid(bitStream), DataUtils.createReverForNetDoctor(bitStream));
        }
        Log.i(TAG, "initInfo: " + this.mNetDiagnoseInfo);
    }

    public void uploadResult() {
        if (this.mResult == null) {
            LogUtils.m1571e(TAG, "uploadResult mResult is null!");
            return;
        }
        StringBuilder info = new StringBuilder();
        uploadResultInfo(info);
        LogUtils.m1574i(TAG, "uploadResult : " + info.toString());
        if (this.mINetDiagnoseResultLinstener != null) {
            this.mINetDiagnoseResultLinstener.onReslut(info.toString());
        }
    }

    public void setNetDiagnoseResultListener(INetDiagnoseResultListener listener) {
        this.mINetDiagnoseResultLinstener = listener;
    }
}

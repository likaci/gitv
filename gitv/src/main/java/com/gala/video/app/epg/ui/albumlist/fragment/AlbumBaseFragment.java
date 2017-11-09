package com.gala.video.app.epg.ui.albumlist.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gala.albumprovider.model.Tag;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.QBaseFragment;
import com.gala.video.app.epg.ui.albumlist.common.NetworkPrompt;
import com.gala.video.app.epg.ui.albumlist.common.NetworkPrompt.INetworkStateListener;
import com.gala.video.app.epg.ui.albumlist.data.BaseDataApi;
import com.gala.video.app.epg.ui.albumlist.enums.IAlbumEnum.AlbumFragmentLocation;
import com.gala.video.app.epg.ui.albumlist.event.IAlbumBaseEvent;
import com.gala.video.app.epg.ui.albumlist.model.AlbumInfoModel;
import com.gala.video.app.epg.ui.albumlist.multimenu.MultiMenuPanel;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumInfoFactory;
import com.gala.video.app.epg.ui.albumlist.utils.DebugUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.albumlist.ErrorKind;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.GlobalQRFeedbackPanel;
import com.gala.video.lib.share.utils.ResourceUtil;

public abstract class AlbumBaseFragment extends QBaseFragment implements IAlbumBaseEvent {
    protected static boolean NOLOG = (!DebugUtils.ALBUM4_NEEDLOG);
    protected String LOG_TAG = "EPG/album4/AlbumBaseFragment";
    protected Bundle mArguments;
    protected final Handler mBaseHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            AlbumBaseFragment.this.handlerMessage(msg);
        }
    };
    protected Context mContext;
    protected BaseDataApi mDataApi;
    protected IAlbumBaseEvent mIAlbumBaseEvent;
    protected AlbumInfoModel mInfoModel;
    protected int mLayoutResId;
    protected View mMainView;
    private NetworkPrompt mNetworkStatePrompt;
    protected String mTopCountExpandTxt = "";
    protected String mTopMenuDesTxt;
    protected String mTopTagDesTxt;
    protected String mTopTagNameTxt;

    private class NetworkListener implements INetworkStateListener {
        private NetworkListener() {
        }

        public void onConnected(boolean isChanged) {
            if (isChanged) {
                AlbumBaseFragment.this.log(AlbumBaseFragment.NOLOG ? null : "---NetworkListener----isChanged=true");
                AlbumBaseFragment.this.onNetChanged();
            }
            AlbumBaseFragment.this.setNetworkState(isChanged);
        }
    }

    public abstract AlbumFragmentLocation getLocationType();

    protected abstract String getLogCatTag();

    protected abstract void onNetChanged();

    protected void runOnUiThread(Runnable action) {
        this.mBaseHandler.post(action);
    }

    protected void handlerMessage(Message msg) {
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.LOG_TAG = "EPG/album4/" + getLogCatTag();
        this.mIAlbumBaseEvent = (IAlbumBaseEvent) activity;
        this.mContext = activity;
        log(NOLOG ? null : "---onAttach--");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        log(NOLOG ? null : "----onCreateView------");
        getDataApi();
        getInfoModel();
        this.mArguments = getArguments();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void onPause() {
        log(NOLOG ? null : "---onPause");
        if (this.mNetworkStatePrompt != null) {
            this.mNetworkStatePrompt.unregisterNetworkListener();
        }
        super.onPause();
    }

    public void onResume() {
        log(NOLOG ? null : "---onResume");
        super.onResume();
        if (this.mNetworkStatePrompt == null) {
            this.mNetworkStatePrompt = new NetworkPrompt(this.mContext);
        }
        this.mNetworkStatePrompt.registerNetworkListener(new NetworkListener());
    }

    public void onDestroy() {
        log(NOLOG ? null : "---onDestroy");
        super.onDestroy();
        this.mIAlbumBaseEvent = null;
        this.mDataApi = null;
        this.mBaseHandler.removeCallbacksAndMessages(null);
    }

    public void replaceFragment(AlbumBaseFragment f) {
        if (this.mIAlbumBaseEvent != null) {
            this.mIAlbumBaseEvent.replaceFragment(f);
        }
    }

    public void addFragment(AlbumBaseFragment f) {
        if (this.mIAlbumBaseEvent != null) {
            this.mIAlbumBaseEvent.addFragment(f);
        }
    }

    public void removeFragment(AlbumBaseFragment f) {
        if (this.mIAlbumBaseEvent != null) {
            this.mIAlbumBaseEvent.removeFragment(f);
        }
    }

    public AlbumBaseFragment getFragmentAddingRight(int pos) {
        if (this.mIAlbumBaseEvent != null) {
            return this.mIAlbumBaseEvent.getFragmentAddingRight(pos);
        }
        return null;
    }

    public AlbumBaseFragment getFragmentAddingLeft(int pos) {
        if (this.mIAlbumBaseEvent != null) {
            return this.mIAlbumBaseEvent.getFragmentAddingLeft(pos);
        }
        return null;
    }

    public void showProgress() {
        if (this.mIAlbumBaseEvent != null) {
            if (AlbumInfoFactory.needShowLoadingView(this.mInfoModel.getPageType())) {
                this.mIAlbumBaseEvent.showProgress();
            }
            setLoadingData(true);
        }
    }

    public void showProgressWithoutDelay() {
        if (this.mIAlbumBaseEvent != null) {
            this.mIAlbumBaseEvent.showProgressWithoutDelay();
            setLoadingData(true);
        }
    }

    public Bitmap showNoResultPanel(ErrorKind kind, ApiException e) {
        if (this.mIAlbumBaseEvent != null) {
            setLoadingData(false);
            return this.mIAlbumBaseEvent.showNoResultPanel(kind, e);
        }
        log(NOLOG ? null : "---showNoResultPanel---callback error!!! ");
        return null;
    }

    public GlobalQRFeedbackPanel getNoResultPanel() {
        if (this.mIAlbumBaseEvent != null) {
            return this.mIAlbumBaseEvent.getNoResultPanel();
        }
        return null;
    }

    public void showHasResultPanel() {
        if (this.mIAlbumBaseEvent != null) {
            this.mIAlbumBaseEvent.showHasResultPanel();
            setLoadingData(false);
        }
    }

    public View getMenuView() {
        if (this.mIAlbumBaseEvent != null) {
            return this.mIAlbumBaseEvent.getMenuView();
        }
        return null;
    }

    protected View createMenu() {
        if (this.mDataApi == null) {
            String str;
            if (NOLOG) {
                str = null;
            } else {
                str = "---create MultiMenu mDataApi == null, return";
            }
            log(str);
            return null;
        } else if (!ListUtils.isEmpty(this.mDataApi.getMultiTags())) {
            return new MultiMenuPanel(this.mContext);
        } else {
            log(NOLOG ? null : "---create MultiMenu wrong---multiTags.size=0");
            return null;
        }
    }

    public synchronized void setMenu2Activity() {
        View menuView = getMenuView();
        if (this.mIAlbumBaseEvent != null && menuView == null) {
            log(NOLOG ? null : "---setMenu2Activity---");
            setMenuView(createMenu());
        }
    }

    public void setMenuView(View view) {
        if (this.mIAlbumBaseEvent != null) {
            this.mIAlbumBaseEvent.setMenuView(view);
        }
    }

    public void hideMenu() {
        if (this.mIAlbumBaseEvent != null) {
            this.mIAlbumBaseEvent.hideMenu();
        }
    }

    public void showMenu() {
        if (this.mIAlbumBaseEvent != null) {
            this.mIAlbumBaseEvent.showMenu();
        }
    }

    public void setTopTagLayoutVisible(int visible) {
        if (this.mIAlbumBaseEvent != null) {
            this.mIAlbumBaseEvent.setTopTagLayoutVisible(visible);
        }
    }

    public void setTopTagTxt(String tagDesTxt, String tagNameTxt, String tagCountTxt) {
        if (this.mIAlbumBaseEvent != null) {
            this.mIAlbumBaseEvent.setTopTagTxt(tagDesTxt, tagNameTxt, tagCountTxt);
        }
    }

    public void setTopChannelNameTxtVisible(int visible) {
        if (this.mIAlbumBaseEvent != null) {
            this.mIAlbumBaseEvent.setTopChannelNameTxtVisible(visible);
        }
    }

    public void setTopChannelNameTxt(String name) {
        if (this.mIAlbumBaseEvent != null) {
            this.mIAlbumBaseEvent.setTopChannelNameTxt(name);
        }
    }

    public void setTopMenuLayoutVisible(int visible) {
        if (this.mIAlbumBaseEvent != null) {
            this.mIAlbumBaseEvent.setTopMenuLayoutVisible(visible);
        }
    }

    public void setTopMenuDesTxt(String menuDesTxt) {
        if (this.mIAlbumBaseEvent != null) {
            this.mIAlbumBaseEvent.setTopMenuDesTxt(menuDesTxt);
        }
    }

    public void setGlobalLastFocusView(View focusView) {
        if (this.mIAlbumBaseEvent != null) {
            this.mIAlbumBaseEvent.setGlobalLastFocusView(focusView);
        }
    }

    public void setNextFocusUpId(View v) {
        if (this.mIAlbumBaseEvent != null) {
            this.mIAlbumBaseEvent.setNextFocusUpId(v);
        }
    }

    public void setFeedbackPanelFocus(View v) {
        if (this.mIAlbumBaseEvent != null) {
            this.mIAlbumBaseEvent.setFeedbackPanelFocus(v);
        }
    }

    public View getGlobalLastFocusView() {
        if (this.mIAlbumBaseEvent != null) {
            return this.mIAlbumBaseEvent.getGlobalLastFocusView();
        }
        return null;
    }

    public BaseDataApi getDataApi() {
        if (this.mIAlbumBaseEvent == null) {
            return null;
        }
        this.mDataApi = this.mIAlbumBaseEvent.getDataApi();
        return this.mDataApi;
    }

    public void setDataApi(BaseDataApi baseAlbumListApi) {
        if (this.mIAlbumBaseEvent != null) {
            this.mDataApi = baseAlbumListApi;
            this.mIAlbumBaseEvent.setDataApi(this.mDataApi);
        }
    }

    public void resetDataApi(Tag tag) {
        if (this.mIAlbumBaseEvent != null) {
            this.mIAlbumBaseEvent.resetDataApi(tag);
            getDataApi();
        }
    }

    public AlbumInfoModel getInfoModel() {
        if (this.mIAlbumBaseEvent == null) {
            return null;
        }
        this.mInfoModel = this.mIAlbumBaseEvent.getInfoModel();
        return this.mInfoModel;
    }

    public void setInfoModel(AlbumInfoModel infoModel) {
        if (this.mIAlbumBaseEvent != null) {
            this.mInfoModel = infoModel;
            this.mIAlbumBaseEvent.setInfoModel(infoModel);
        }
    }

    protected void setLeftFragmentHasData(boolean leftHasData) {
        if (this.mInfoModel != null) {
            this.mInfoModel.setLeftFragmentHasData(leftHasData);
            setInfoModel(this.mInfoModel);
        }
    }

    protected boolean isLeftFragmentHasData() {
        if (getInfoModel() != null) {
            return this.mInfoModel.isLeftFragmentHasData();
        }
        return false;
    }

    protected void setRightFragmentHasData(boolean rightHasData) {
        if (this.mInfoModel != null) {
            this.mInfoModel.setRightFragmentHasData(rightHasData);
            setInfoModel(this.mInfoModel);
        }
    }

    protected boolean isRightFragmentHasData() {
        if (getInfoModel() != null) {
            return this.mInfoModel.isRightFragmentHasData();
        }
        return false;
    }

    protected void setLoadingData(boolean isloading) {
        if (this.mInfoModel != null) {
            this.mInfoModel.setLoadingData(isloading);
            setInfoModel(this.mInfoModel);
        }
    }

    protected boolean isLoadingData() {
        if (getInfoModel() != null) {
            return this.mInfoModel.isLoadingData();
        }
        return false;
    }

    protected void setShowingCacheData(boolean isShowing) {
        if (this.mInfoModel != null) {
            this.mInfoModel.setShowingCacheData(isShowing);
            setInfoModel(this.mInfoModel);
        }
    }

    protected boolean isShowingCacheData() {
        if (getInfoModel() != null) {
            return this.mInfoModel.isShowingCacheData();
        }
        return false;
    }

    protected void setChannelFromInfo(String from) {
        if (this.mInfoModel != null) {
            this.mInfoModel.setFrom(from);
            setInfoModel(this.mInfoModel);
        }
    }

    protected void setChannelNameInfo(String channelName) {
        if (this.mInfoModel != null) {
            this.mInfoModel.setChannelName(channelName);
            setInfoModel(this.mInfoModel);
        }
    }

    protected void setChannelIdInfo(int channelId) {
        if (this.mInfoModel != null) {
            this.mInfoModel.setChannelId(channelId);
            setInfoModel(this.mInfoModel);
        }
    }

    protected void setTagId(String tagId) {
        if (this.mInfoModel != null) {
            this.mInfoModel.setDataTagId(tagId);
            setInfoModel(this.mInfoModel);
        }
    }

    protected void setTagName(String tagName) {
        if (this.mInfoModel != null) {
            this.mInfoModel.setDataTagName(tagName);
            setInfoModel(this.mInfoModel);
        }
    }

    protected void setTagType(String tagType) {
        if (this.mInfoModel != null) {
            this.mInfoModel.setDataTagType(tagType);
            setInfoModel(this.mInfoModel);
        }
    }

    protected void setTagResourceType(String tagResourceType) {
        if (this.mInfoModel != null) {
            this.mInfoModel.setDataTagResourceType(tagResourceType);
            setInfoModel(this.mInfoModel);
        }
    }

    protected void setBuySource(String buySource) {
        if (this.mInfoModel != null) {
            this.mInfoModel.setBuySource(buySource);
            setInfoModel(this.mInfoModel);
        }
    }

    protected void setMultiMenuDataInfo(boolean isMultiMenuData) {
        if (this.mInfoModel != null) {
            this.mInfoModel.setMultiHasData(isMultiMenuData);
            setInfoModel(this.mInfoModel);
        }
    }

    public boolean onPressBack() {
        return false;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return false;
    }

    protected void log(String str) {
        if (str != null) {
            if (this.mInfoModel != null) {
                Log.e(this.LOG_TAG, this.mInfoModel.getChannelName() + "/qfragment/" + this.mInfoModel.getDataTagName() + "//---" + str);
            } else {
                Log.e(this.LOG_TAG, "qfragment//---" + str);
            }
        }
    }

    protected void logRecord(String str) {
        if (str != null) {
        }
    }

    protected String getStr(int resId) {
        return ResourceUtil.getStr(resId);
    }

    protected String getStr(int resId, Object... formatArgs) {
        return ResourceUtil.getStr(resId, formatArgs);
    }

    protected int getDimen(int dimen) {
        return ResourceUtil.getDimen(dimen);
    }

    protected int getColor(int resId) {
        return ResourceUtil.getColor(resId);
    }

    protected Drawable getDrawable(int resId) {
        return ResourceUtil.getDrawable(resId);
    }

    protected void setNetworkState(boolean state) {
    }

    protected void sendRquestPingback(boolean fromScrollEnd) {
    }
}

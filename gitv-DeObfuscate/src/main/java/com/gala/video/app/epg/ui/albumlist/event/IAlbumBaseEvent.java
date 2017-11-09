package com.gala.video.app.epg.ui.albumlist.event;

import android.graphics.Bitmap;
import android.os.Message;
import android.view.View;
import com.gala.albumprovider.model.Tag;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.ui.albumlist.data.BaseDataApi;
import com.gala.video.app.epg.ui.albumlist.fragment.AlbumBaseFragment;
import com.gala.video.app.epg.ui.albumlist.model.AlbumInfoModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.albumlist.ErrorKind;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.GlobalQRFeedbackPanel;

public interface IAlbumBaseEvent {
    void addFragment(AlbumBaseFragment albumBaseFragment);

    BaseDataApi getDataApi();

    AlbumBaseFragment getFragmentAddingLeft(int i);

    AlbumBaseFragment getFragmentAddingRight(int i);

    View getGlobalLastFocusView();

    AlbumInfoModel getInfoModel();

    View getMenuView();

    GlobalQRFeedbackPanel getNoResultPanel();

    void handlerMessage2Left(Message message);

    void handlerMessage2Right(Message message);

    void hideMenu();

    void removeFragment(AlbumBaseFragment albumBaseFragment);

    void replaceFragment(AlbumBaseFragment albumBaseFragment);

    void requestLeftPanelFocus();

    void resetDataApi(Tag tag);

    void setDataApi(BaseDataApi baseDataApi);

    void setFeedbackPanelFocus(View view);

    void setGlobalLastFocusView(View view);

    void setInfoModel(AlbumInfoModel albumInfoModel);

    void setMenuView(View view);

    void setNextFocusUpId(View view);

    void setTopChannelNameTxt(String str);

    void setTopChannelNameTxtVisible(int i);

    void setTopMenuDesTxt(String str);

    void setTopMenuLayoutVisible(int i);

    void setTopTagLayoutVisible(int i);

    void setTopTagTxt(String str, String str2, String str3);

    void showHasResultPanel();

    void showMenu();

    Bitmap showNoResultPanel(ErrorKind errorKind, ApiException apiException);

    void showProgress();

    void showProgressWithoutDelay();
}

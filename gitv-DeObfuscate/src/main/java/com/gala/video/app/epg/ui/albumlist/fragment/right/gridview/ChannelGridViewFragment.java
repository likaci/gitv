package com.gala.video.app.epg.ui.albumlist.fragment.right.gridview;

import android.graphics.Bitmap;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.ui.albumlist.adapter.BaseGridAdapter;
import com.gala.video.app.epg.ui.albumlist.adapter.ChannelHorizontalAdapter;
import com.gala.video.app.epg.ui.albumlist.adapter.GridAdapter;
import com.gala.video.app.epg.ui.albumlist.constant.IAlbumConfig;
import com.gala.video.app.epg.ui.albumlist.data.BaseDataApi;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.configs.ViewConstant.AlbumViewType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.albumlist.ErrorKind;
import java.util.List;

public class ChannelGridViewFragment extends ChannelGridBaseFragment {
    protected void initView() {
        resetTopInfoAfterChangeTag();
        super.initView();
    }

    protected void resetTopInfoAfterChangeTag() {
        String str = null;
        this.mTopCountExpandTxt = IAlbumConfig.STR_TOP_COUNT_BU;
        this.mTopTagDesTxt = null;
        if (this.mInfoModel.isNoLeftFragment()) {
            this.mTopTagNameTxt = null;
            this.mTopMenuDesTxt = null;
            return;
        }
        this.mTopTagNameTxt = getInfoModel().getDataTagName();
        if (!ListUtils.isEmpty(this.mDataApi.getMultiTags())) {
            str = IAlbumConfig.STR_FILTER;
        }
        this.mTopMenuDesTxt = str;
    }

    public void showHasResultPanel() {
        super.showHasResultPanel();
        if (!StringUtils.equals(this.mTopMenuDesTxt, IAlbumConfig.STR_FILTER)) {
            setTopMenuLayoutVisible(4);
        }
    }

    public Bitmap showNoResultPanel(ErrorKind kind, ApiException e) {
        super.showNoResultPanel(kind, e);
        if (StringUtils.equals(this.mTopMenuDesTxt, IAlbumConfig.STR_FILTER)) {
            setTopMenuLayoutVisible(0);
        }
        return null;
    }

    protected BaseDataApi getNewDataApi() {
        return null;
    }

    protected BaseGridAdapter<IData> getVerticalGridAdapter() {
        if (this.mDataApi == null || !QLayoutKind.LANDSCAPE.equals(this.mDataApi.getLayoutKind())) {
            this.mGridAnimationScale = 1.093f;
            return new GridAdapter(this.mContext, AlbumViewType.VERTICAL);
        }
        this.mGridAnimationScale = 1.09f;
        return new ChannelHorizontalAdapter(this.mContext, AlbumViewType.HORIZONTAL);
    }

    protected void onFetchDataSucceed(List<IData> list) {
        String str = null;
        if (isRemoving()) {
            if (!NOLOG) {
                str = "---onFetchDataSucceed is Detached ---";
            }
            log(str);
        } else if (ListUtils.isEmpty((List) list)) {
            showNoResultPanel(ListUtils.isEmpty(this.mDataApi.getMultiTags()) ? ErrorKind.NO_RESULT_AND_NO_MENU : ErrorKind.NO_RESULT, null);
        } else {
            onDownloadCompleted(list);
        }
    }

    protected void onFetchDataFailure(ApiException e, String code) {
        if (ListUtils.isEmpty(this.mConvertList)) {
            showNoResultPanel(ErrorKind.NET_ERROR, e);
        } else {
            showHasResultPanel();
        }
    }

    protected String getLogCatTag() {
        return IAlbumConfig.UNIQUE_CHANNEL_GRIDVIEW;
    }
}

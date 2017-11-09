package com.gala.video.app.epg.ui.albumlist.fragment.right.cardview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.home.data.tool.TabModelManager;
import com.gala.video.app.epg.ui.albumlist.constant.IAlbumConfig;
import com.gala.video.app.epg.ui.albumlist.desktop.searchresult.SearchResultAlbumProvider;
import com.gala.video.app.epg.ui.albumlist.model.AlbumInfoModel.SearchInfoModel;
import com.gala.video.app.epg.ui.albumlist.utils.QAPingback;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.widget.GlobalDialog;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.system.preference.AppPreference;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.cybergarage.upnp.control.Control;

@SuppressLint({"DefaultLocale"})
public class ChannelSearchResultCardFragment extends ChannelCardBaseFragment {
    private static final int ADD_FAILURE = 0;
    private static final int ADD_SUCCESS = 1;
    private static final boolean HASSHOWED = true;
    private static final int RSEAT_ADD = 0;
    private static final int RSEAT_BACK = 2;
    private static final int RSEAT_RETURN = 1;
    private boolean hasShowedDialog = false;
    private ArrayList<Integer> mFlagNum = new ArrayList();
    private SearchResultAlbumProvider mSearchProvider = SearchResultAlbumProvider.getInstance();
    private Map<Integer, Long> mTimeMap;

    @SuppressLint({"UseSparseArrays"})
    protected void resetTopInfoAfterChangeTag() {
        super.resetTopInfoAfterChangeTag();
        String keyword = this.mInfoModel.getSearchModel().getKeyWord();
        if (!StringUtils.isEmpty((CharSequence) keyword)) {
            if (!StringUtils.isEmpty((CharSequence) keyword) && keyword.length() > 15) {
                keyword = keyword.substring(0, 15) + "...";
            }
            this.mTopTagDesTxt = String.format(" \"%s\"", new Object[]{keyword.toUpperCase()});
        }
        if (this.mTimeMap == null) {
            this.mTimeMap = new HashMap();
        }
        this.mTimeMap.clear();
    }

    protected void onFetchDataSucceed(List<IData> list) {
        super.onFetchDataSucceed(list);
    }

    protected void onFetchDataFailure(ApiException e, String code) {
        super.onFetchDataFailure(e, code);
        sendRquestPingback(false);
    }

    @SuppressLint({"UseSparseArrays"})
    protected void sendRquestPingback(boolean fromScrollEnd) {
        long timeToken = System.currentTimeMillis() - this.mStartLoadingTime;
        if (fromScrollEnd) {
            int firstRow;
            int firstPosition = this.mGridView.getFirstAttachedPosition();
            int size1 = ListUtils.getCount(this.mCardConvertList);
            if (firstPosition < size1) {
                firstRow = firstPosition / 2;
            } else {
                firstRow = ((size1 + 1) / 2) + (((firstPosition - size1) + 3) / 4);
            }
            timeToken = getVisibleTimeToken(firstRow);
        } else {
            if (this.mTimeMap == null) {
                this.mTimeMap = new HashMap();
            }
            int count = this.listCount;
            if (!this.mTimeMap.containsKey(Integer.valueOf(count))) {
                this.mTimeMap.put(Integer.valueOf(count), Long.valueOf(timeToken));
            }
        }
        List list = getVisibleList(fromScrollEnd);
        SearchInfoModel searchModel = this.mInfoModel.getSearchModel();
        LogUtils.e(this.LOG_TAG, "sendRquestPingback ---- time = " + timeToken + ", count = " + ListUtils.getCount(list));
        try {
            QAPingback.searchRequestPingback(this.mContext, list, timeToken, this.mDataApi.getCurPage(), 0, searchModel.getClickType(), searchModel.getKeyWord());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private long getVisibleTimeToken(int firstRow) {
        int firstPos;
        int size1 = ListUtils.getCount(this.mCardConvertList);
        int rowCount1 = ((size1 + 2) - 1) / 2;
        if (firstRow <= rowCount1) {
            firstPos = firstRow * 2;
        } else {
            firstPos = size1 + ((firstRow - rowCount1) * 4);
        }
        Set<Integer> keySet = this.mTimeMap.keySet();
        if (keySet == null || keySet.size() <= 0) {
            return 0;
        }
        int posKey = -1;
        int offset = 10000;
        for (Integer key : keySet) {
            int off = key.intValue() - firstPos;
            if (off >= 0 && off < offset) {
                offset = off;
                posKey = key.intValue();
            }
            if (posKey < 0) {
                posKey = key.intValue();
            }
        }
        return ((Long) this.mTimeMap.get(Integer.valueOf(posKey))).longValue();
    }

    private List<Album> getVisibleList(boolean fromScrollEnd) {
        this.firstVisble = this.mGridView.getFirstAttachedPosition();
        this.lastVisble = this.mGridView.getLastAttachedPosition();
        List<Album> list = new ArrayList();
        for (int i = this.firstVisble; i <= this.lastVisble; i++) {
            View view = this.mGridView.getViewByPosition(i);
            if (view != null) {
                int topDifference = view.getTop() - this.mGridView.getScrollY();
                int bottomDifference = view.getBottom() - this.mGridView.getScrollY();
                int top = ResourceUtil.getDimen(R.dimen.dimen_32dp);
                int viewHeight = this.mGridView.getHeight();
                IData data = this.mMultiGridAdapter.getIData(i);
                if (data != null) {
                    Album album = data.getAlbum();
                    if ((top < topDifference && topDifference < viewHeight) || (top < bottomDifference && bottomDifference < viewHeight)) {
                        list.add(album);
                    }
                }
            }
        }
        return list;
    }

    protected String getLogCatTag() {
        return IAlbumConfig.UNIQUE_CHANNEL_SEARCH_RESULT_CARD;
    }

    protected void reloadBitmap() {
    }

    protected void recyleBitmap() {
    }

    public void onResume() {
        super.onResume();
        if (canAddTab() && Project.getInstance().getBuild().isSupportDesktopManage() && !getShowed(ResourceUtil.getContext(), this.mChannelId) && this.mSearchProvider.isShowDialog(this.mChannelId) && !this.hasShowedDialog) {
            showAddTabDialog();
            saveShowed(ResourceUtil.getContext(), this.mChannelId);
        }
    }

    protected void onGridItemClick(int position) {
        super.onGridItemClick(position);
        locationCount();
    }

    private void showAddTabDialog() {
        String nameDialog = getResources().getString(R.string.add_tab_dialog_content);
        String nameToastSuccess = getResources().getString(R.string.add_tab_toast_content_success);
        String nameToastFailure = getResources().getString(R.string.add_tab_toast_content_failure);
        String showDialog = String.format(nameDialog, new Object[]{this.mChannelName});
        final String showToastSuccess = String.format(nameToastSuccess, new Object[]{this.mChannelName});
        final String showToastFailure = String.format(nameToastFailure, new Object[]{this.mChannelName});
        final GlobalDialog addTabDialog = Project.getInstance().getControl().getGlobalDialog(this.mContext);
        addTabDialog.setParams(showDialog, ResourceUtil.getStr(R.string.add_tab_dialog_ok), new OnClickListener() {
            public void onClick(View v) {
                addTabDialog.dismiss();
                if (TabModelManager.addChannel(ChannelSearchResultCardFragment.this.mChannelId) == 0) {
                    QToast.makeTextAndShow(ChannelSearchResultCardFragment.this.mContext, showToastSuccess, 3000);
                    ChannelSearchResultCardFragment.this.sendAddPingback(1, ChannelSearchResultCardFragment.this.mChannelName);
                } else {
                    QToast.makeTextAndShow(ChannelSearchResultCardFragment.this.mContext, showToastFailure, 3000);
                    ChannelSearchResultCardFragment.this.sendAddPingback(0, ChannelSearchResultCardFragment.this.mChannelName);
                }
                ChannelSearchResultCardFragment.this.sendClickPingback(0, ChannelSearchResultCardFragment.this.mChannelId, ChannelSearchResultCardFragment.this.mChannelName);
            }
        }, ResourceUtil.getStr(R.string.add_tab_dialog_cancel), new OnClickListener() {
            public void onClick(View v) {
                ChannelSearchResultCardFragment.this.sendClickPingback(1, ChannelSearchResultCardFragment.this.mChannelId, ChannelSearchResultCardFragment.this.mChannelName);
                addTabDialog.dismiss();
            }
        });
        sendShowPingback(this.mChannelId, this.mChannelName);
        addTabDialog.show();
        addTabDialog.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == 4 && event.getAction() == 0) {
                    ChannelSearchResultCardFragment.this.sendClickPingback(2, ChannelSearchResultCardFragment.this.mChannelId, ChannelSearchResultCardFragment.this.mChannelName);
                }
                return false;
            }
        });
        this.hasShowedDialog = true;
    }

    private boolean canAddTab() {
        if (TabModelManager.canAddChannel(this.mChannelId) == 0) {
            return true;
        }
        return false;
    }

    private void saveShowed(Context ctx, int id) {
        String idString = String.valueOf(id);
        new AppPreference(ctx, idString).save(idString, true);
    }

    private boolean getShowed(Context ctx, int id) {
        String idString = String.valueOf(id);
        if (new AppPreference(ctx, idString).getBoolean(idString, false)) {
            return true;
        }
        return false;
    }

    private void locationCount() {
        if (!this.mFlagNum.contains(Integer.valueOf(this.mChannelId))) {
            this.mFlagNum.add(Integer.valueOf(this.mChannelId));
            if (Project.getInstance().getBuild().isSupportDesktopManage() && !getShowed(ResourceUtil.getContext(), this.mChannelId)) {
                this.mSearchProvider.insert(this.mChannelId);
            }
        }
    }

    private void sendClickPingback(int type, int albumId, String albumName) {
        String rpage = "搜索结果";
        String block = "add_tab_guide";
        String rt = "i";
        String rseat = null;
        switch (type) {
            case 0:
                rseat = "add";
                break;
            case 1:
                rseat = Control.RETURN;
                break;
            case 2:
                rseat = "back";
                break;
        }
        QAPingback.sendMyPageClick("tab_" + albumName, block, rt, rseat, rpage, null, String.valueOf(albumId), null, null, null, null, null, "", "", "", "", "", null, "");
    }

    private void sendShowPingback(int albumId, String albumName) {
        QAPingback.sendMyPageShow(String.valueOf(albumId), "搜索结果", "tab_" + albumName, "", null, "", "add_tab_guide", "", "", null, null, null, null, null, "");
    }

    private void sendAddPingback(int add, String albumName) {
        String st = String.valueOf(add);
        String tab = "tab_" + albumName;
        PingBackParams params = new PingBackParams();
        params.add("rpage", "搜索结果").add("st", st).add("tab", tab).add("a", "addtab").add(Keys.T, "5");
        PingBack.getInstance().postPingBackToLongYuan(params.build());
    }
}

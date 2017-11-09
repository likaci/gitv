package com.gala.video.lib.share.uikit.item;

import android.text.TextUtils;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.PingbackPage;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.data.RecordPingbackData;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.history.HistoryInfo;
import com.gala.video.lib.share.pingback.ClickPingbackUtils;
import com.gala.video.lib.share.pingback.PingBackCollectionFieldUtils;
import com.gala.video.lib.share.pingback.PingbackUtils;
import com.gala.video.lib.share.uikit.action.model.RecordActionModel;
import com.gala.video.lib.share.uikit.contract.RecordItemContract.Presenter;
import com.gala.video.lib.share.uikit.view.widget.record.LongRecordView.LongHistoryItemModel;
import com.gala.video.lib.share.uikit.view.widget.record.RecordItemView;
import java.util.List;

public class RecordItem extends Item implements Presenter {
    private static final String LOG_TAG = "RecordItem";
    private static final int PLAY_FINISHED = -2;
    private List<HistoryInfo> mHistoryInfosList;
    private int mPos;
    private RecordPingbackData mRecordPingbackData;
    private RecordItemView mView;

    public void updateUI(RecordItemView recordItemView) {
        this.mView = recordItemView;
        int longRecordDataCount = fetchLongRecordData();
        LogUtils.i(LOG_TAG, "updateUI longRecordDataCount = ", Integer.valueOf(longRecordDataCount));
        setViewType(longRecordDataCount);
        setLongHistoryData(longRecordDataCount);
    }

    public void onClick(int viewType, int clickType) {
        Album album = null;
        switch (clickType) {
            case 10:
                album = ((HistoryInfo) this.mHistoryInfosList.get(0)).getAlbum();
                break;
            case 11:
                album = ((HistoryInfo) this.mHistoryInfosList.get(1)).getAlbum();
                break;
            case 12:
                album = null;
                break;
        }
        ((RecordActionModel) this.mItemInfoModel.getActionModel()).setHistoryInfoAlbum(album);
        ((RecordActionModel) this.mItemInfoModel.getActionModel()).setSearchRecordType(clickType);
        this.mItemInfoModel.getActionModel().onItemClick(this.mView.getContext());
        int line = ClickPingbackUtils.getLine(getParent().getParent(), getParent(), this);
        if (this.mRecordPingbackData == null) {
            this.mRecordPingbackData = new RecordPingbackData();
            this.mRecordPingbackData.setCardId(getParent().getModel().mCardId);
            this.mRecordPingbackData.setPos(this.mPos);
            this.mRecordPingbackData.setCardLine("" + (getLine() + 1));
            this.mRecordPingbackData.setAllline("" + getParent().getAllLine());
            this.mRecordPingbackData.setLine(String.valueOf(line));
        }
        if (PingbackUtils.getPingbackPage(this.mView.getContext()) == PingbackPage.HomePage) {
            PingBackCollectionFieldUtils.setCardIndex(line + "");
            PingBackCollectionFieldUtils.setItemIndex(this.mPos + "");
            PingBackCollectionFieldUtils.setIncomeSrc(PingBackCollectionFieldUtils.getTabName() + "_" + PingBackCollectionFieldUtils.getTabIndex() + "_c_" + line + "_item_" + this.mPos);
            LogUtils.d(LOG_TAG, "incomesrc = " + PingBackCollectionFieldUtils.getIncomeSrc());
        }
        this.mRecordPingbackData.setSearchRecordType(clickType);
        this.mRecordPingbackData.setHissrch(getHissrch(viewType, clickType));
        ClickPingbackUtils.recordClickForPingbackPost(this.mView.getContext(), this.mRecordPingbackData);
    }

    private String getHissrch(int viewType, int clickType) {
        switch (viewType) {
            case 1:
                return "记录";
            case 2:
                switch (clickType) {
                    case 10:
                        return "记录1-1";
                    case 12:
                        return "记录1-记录";
                    default:
                        return null;
                }
            case 3:
                switch (clickType) {
                    case 10:
                        return "记录2-1";
                    case 11:
                        return "记录2-2";
                    case 12:
                        return "记录2-记录";
                    default:
                        return null;
                }
            default:
                return null;
        }
    }

    public void setPos(int pos) {
        this.mPos = pos;
    }

    public synchronized int fetchLongRecordData() {
        this.mHistoryInfosList = GetInterfaceTools.getIHistoryCacheManager().getLatestLongVideoHistory(2);
        LogUtils.i(LOG_TAG, "fetchLongRecordData count = ", Integer.valueOf(ListUtils.getCount(this.mHistoryInfosList)));
        return ListUtils.getCount(this.mHistoryInfosList);
    }

    protected void setViewType(int longRecordDataCount) {
        int viewType = 1;
        if (longRecordDataCount == 0) {
            viewType = 1;
        } else if (longRecordDataCount == 1) {
            viewType = 2;
        } else if (longRecordDataCount >= 2) {
            viewType = 3;
        }
        this.mView.setViewType(viewType, this.mItemInfoModel);
    }

    private void setLongHistoryData(int longRecordDataCount) {
        if (this.mView != null) {
            if (longRecordDataCount == 1) {
                this.mView.setFirstLongHistoryContent(createLongHistoryContentModel(((HistoryInfo) this.mHistoryInfosList.get(0)).getAlbum()));
            } else if (longRecordDataCount >= 2) {
                this.mView.setFirstLongHistoryContent(createLongHistoryContentModel(((HistoryInfo) this.mHistoryInfosList.get(0)).getAlbum()));
                this.mView.setSecondLongHistoryContent(createLongHistoryContentModel(((HistoryInfo) this.mHistoryInfosList.get(1)).getAlbum()));
            }
        }
    }

    protected LongHistoryItemModel createLongHistoryContentModel(Album album) {
        if (album == null) {
            return null;
        }
        LongHistoryItemModel longHistoryItemModel = new LongHistoryItemModel();
        String titleString = album.name;
        LogUtils.i(LOG_TAG, "album name = " + titleString);
        if (TextUtils.isEmpty(titleString)) {
            longHistoryItemModel.mTitle = "";
        } else {
            longHistoryItemModel.mTitle = "《" + titleString + "》";
        }
        int currentTime = album.playTime;
        String totalTime = album.len;
        LogUtils.i(LOG_TAG, "currentTime= " + currentTime + " ;totalTime  = " + totalTime);
        int totalTimeInt = StringUtils.parse(totalTime, 1);
        int percent = 0;
        if (currentTime == -2) {
            percent = 100;
        } else if (totalTimeInt > 0) {
            percent = Math.max(Math.min((currentTime * 100) / totalTimeInt, 100), 0);
        } else {
            LogUtils.e(LOG_TAG, "illegal argument total time = " + totalTimeInt);
        }
        longHistoryItemModel.mPercent = percent;
        String descString = "";
        boolean isSeries = album.isSeries();
        LogUtils.i(LOG_TAG, "album>isSeries = " + isSeries);
        if (isSeries) {
            boolean isSourceType = album.isSourceType();
            LogUtils.i(LOG_TAG, "album>isSourceType = " + isSourceType);
            if (isSourceType) {
                LogUtils.i(LOG_TAG, "album.time = ", album.time);
                if (!StringUtils.isEmpty(album.time) && album.time.length() == 8) {
                    descString = "观看至第" + (((descString + album.time.substring(0, 4) + "-") + album.time.substring(4, 6) + "-") + album.time.substring(6, 8)) + "期";
                }
                if (StringUtils.isEmpty((CharSequence) descString)) {
                    descString = album.tvName;
                }
            } else {
                descString = "观看至第" + album.order + "集";
            }
        } else {
            LogUtils.i(LOG_TAG, "albumisSeries.false..");
            if (currentTime == totalTimeInt || currentTime == -2) {
                descString = "已看完";
            } else if (currentTime < 60) {
                descString = "观看不足1分钟";
            } else {
                descString = "观看至" + (currentTime / 60) + "分钟";
            }
        }
        LogUtils.i(LOG_TAG, "album>descString = " + descString);
        longHistoryItemModel.mDesc = descString;
        return longHistoryItemModel;
    }
}

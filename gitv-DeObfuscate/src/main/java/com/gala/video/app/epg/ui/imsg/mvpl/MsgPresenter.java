package com.gala.video.app.epg.ui.imsg.mvpl;

import com.gala.albumprovider.model.Tag;
import com.gala.video.app.epg.ui.imsg.fetch.IMsgCallback;
import com.gala.video.app.epg.ui.imsg.fetch.TasksRepository;
import com.gala.video.app.epg.ui.imsg.mvpl.MsgContract.Presenter;
import com.gala.video.app.epg.ui.imsg.mvpl.MsgContract.View;
import com.gala.video.app.epg.ui.imsg.utils.MsgPingbackSender;
import com.gala.video.app.epg.utils.ActivityUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.ifimpl.imsg.utils.IMsgUtils.IMsgType;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent;
import java.util.List;

public class MsgPresenter implements Presenter {
    protected static final String TAG = "EPG/StarsPresenter";
    protected List<Tag> mLabelList;
    protected List<IMsgContent> mMsgList;
    private final View mMsgView;
    private boolean mNeedSendPageShow = true;
    private TasksRepository mTasksRepository;
    private Runnable mUpdateUnreadMsgCountRunnable = new C09112();

    class C09112 implements Runnable {
        C09112() {
        }

        public void run() {
            MsgPresenter.this.mMsgView.updateUnreadMsgCount(GetInterfaceTools.getMsgCenter().getUnreadIMsgListCount());
        }
    }

    public MsgPresenter(TasksRepository tasksRepository, View statisticsView) {
        this.mTasksRepository = (TasksRepository) ActivityUtils.checkNotNull(tasksRepository, "msgDataSource cannot be null");
        this.mMsgView = (View) ActivityUtils.checkNotNull(statisticsView, "View cannot be null!");
        this.mMsgView.setPresenter(this);
    }

    public void start(int labelIndex) {
        startTask(labelIndex);
    }

    private void startTask(int labelIndex) {
        if (this.mLabelList == null) {
            getLabels();
        }
        onLabelSwitch(labelIndex);
    }

    private void getLabels() {
        this.mLabelList = this.mTasksRepository.getLabels();
        this.mMsgView.showLabels(this.mLabelList);
    }

    private void fetchLabelContent(final int labelIndex) {
        LogUtils.m1573e(TAG, "fetchLabelContent --- index = ", Integer.valueOf(labelIndex));
        int type = getTagType(labelIndex);
        final long startTime = System.currentTimeMillis();
        this.mTasksRepository.getMsgList(type, new IMsgCallback() {
            public void onSuccess(List<IMsgContent> list) {
                MsgPresenter.this.mMsgList = list;
                MsgPresenter.this.mMsgView.showMsgContentsAndMenuDesc(list);
                LogUtils.m1576i(MsgPresenter.TAG, "fetchLabelContent --- consumeTime = ", Long.valueOf(System.currentTimeMillis() - startTime));
                if (MsgPresenter.this.mNeedSendPageShow) {
                    MsgPingbackSender.sendMsgsShowPingback(labelIndex, consumeTime);
                }
                MsgPresenter.this.mNeedSendPageShow = true;
            }

            public void onFail() {
                MsgPresenter.this.mMsgList = null;
                MsgPresenter.this.mMsgView.showMsgContentsAndMenuDesc(null);
                MsgPresenter.this.mNeedSendPageShow = true;
            }
        });
    }

    private int getTagType(int index) {
        if (ListUtils.isEmpty(this.mLabelList)) {
            return 0;
        }
        return IMsgType.getTypeType(index);
    }

    public void onMsgClick(int labelIndex, int position) {
        if (ListUtils.isLegal(this.mMsgList, position)) {
            IMsgContent msgContent = (IMsgContent) this.mMsgList.get(position);
            if (msgContent != null) {
                updateUnreadMsgCount();
                MsgPingbackSender.sendMsgClickPingback(msgContent, labelIndex, position);
                GetInterfaceTools.getMsgCenter().updateIsReadFlag(msgContent);
                this.mMsgView.jumpToPage(msgContent);
            }
        }
    }

    private void updateTopTagName(int pos) {
        this.mMsgView.updateTopTagName(getTagName(pos));
    }

    private void updateUnreadMsgCount() {
        ThreadUtils.execute(this.mUpdateUnreadMsgCountRunnable);
    }

    public void onStop() {
        this.mNeedSendPageShow = false;
    }

    public void onLabelSwitch(int newPosition) {
        fetchLabelContent(newPosition);
        updateTopTagName(newPosition);
        updateUnreadMsgCount();
    }

    private String getTagName(int position) {
        if (!ListUtils.isLegal(this.mLabelList, position)) {
            return "";
        }
        Tag tag = (Tag) this.mLabelList.get(position);
        if (tag == null) {
            return "";
        }
        return tag.getName();
    }

    public void onMenuViewClick(int labelIndex) {
        GetInterfaceTools.getMsgCenter().updateIsReadFlag(getTagType(labelIndex));
        updateUnreadMsgCount();
    }
}

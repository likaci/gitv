package com.gala.video.app.epg.ui.imsg.fetch;

import com.gala.albumprovider.model.Tag;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent;
import java.util.List;

public class TasksRepository implements IMsgDataSource {
    private IMsgDataSource mMsgDataSource = new MsgDataSource();

    public void getMsgList(final int type, final IMsgCallback callback) {
        ThreadUtils.execute(new Runnable() {

            class C08981 implements IMsgCallback {
                C08981() {
                }

                public void onSuccess(List<IMsgContent> list) {
                    callback.onSuccess(list);
                }

                public void onFail() {
                    callback.onFail();
                }
            }

            public void run() {
                TasksRepository.this.mMsgDataSource.getMsgList(type, new C08981());
            }
        });
    }

    public List<Tag> getLabels() {
        return this.mMsgDataSource.getLabels();
    }
}

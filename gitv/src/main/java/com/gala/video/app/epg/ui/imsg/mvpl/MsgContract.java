package com.gala.video.app.epg.ui.imsg.mvpl;

import com.gala.albumprovider.model.Tag;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent;
import java.util.List;

public interface MsgContract {

    public interface MsgAdapter {
        void updateAllMsgsUI();

        void updateMsgUI(ViewHolder viewHolder);
    }

    public interface Presenter {
        void onLabelSwitch(int i);

        void onMenuViewClick(int i);

        void onMsgClick(int i, int i2);

        void onStop();

        void start(int i);
    }

    public interface View {
        void jumpToPage(IMsgContent iMsgContent);

        void setPresenter(Presenter presenter);

        void showLabels(List<Tag> list);

        void showMsgContentsAndMenuDesc(List<IMsgContent> list);

        void updateTopTagName(String str);

        void updateUnreadMsgCount(int i);
    }
}

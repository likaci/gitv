package com.gala.video.app.epg.ui.imsg.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.gala.video.albumlist4.widget.RecyclerView.Adapter;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.albumlist4.widget.VerticalGridView;
import com.gala.video.app.epg.ui.imsg.widget.MsgCenterView;
import com.gala.video.app.epg.ui.imsg.widget.MsgCenterView.MessageCenterModel;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent;
import java.util.ArrayList;
import java.util.List;

public class MsgAdapter extends Adapter<MyViewHolder> implements com.gala.video.app.epg.ui.imsg.mvpl.MsgContract.MsgAdapter {
    private List<IMsgContent> mContentList;
    private Context mContext;

    static class MyViewHolder extends ViewHolder {
        public MyViewHolder(View view) {
            super(view);
        }
    }

    public boolean isFocusable(int position) {
        return true;
    }

    public MsgAdapter(Context context) {
        this.mContentList = new ArrayList();
        this.mContext = context;
    }

    public MsgAdapter(Context context, VerticalGridView gridView) {
        this(context);
    }

    public void setList(List<IMsgContent> list) {
        this.mContentList.clear();
        if (!ListUtils.isEmpty((List) list)) {
            this.mContentList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void updateList(List<IMsgContent> list) {
        this.mContentList.clear();
        if (!ListUtils.isEmpty((List) list)) {
            this.mContentList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public List<IMsgContent> getList() {
        return this.mContentList;
    }

    public int getCount() {
        return ListUtils.getCount(this.mContentList);
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (holder != null && holder.itemView != null) {
            IMsgContent msgContent = (IMsgContent) this.mContentList.get(position);
            MsgCenterView view = holder.itemView;
            MessageCenterModel model = new MessageCenterModel();
            model.mIsReaded = msgContent.isRead;
            model.mTitle = msgContent.msg_title;
            model.mTime = msgContent.showTime;
            view.setData(model);
        }
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(new MsgCenterView(this.mContext));
    }

    public int getItemViewType(int position) {
        return 0;
    }

    public void updateMsgUI(ViewHolder viewHolder) {
        if (viewHolder != null && viewHolder.itemView != null) {
            int position = viewHolder.getLayoutPosition();
            if (ListUtils.isLegal(this.mContentList, position)) {
                IMsgContent msgContent = (IMsgContent) this.mContentList.get(position);
                if (!msgContent.isRead) {
                    MsgCenterView view = viewHolder.itemView;
                    msgContent.isRead = true;
                    view.updateUIState(true);
                }
            }
        }
    }

    public void updateAllMsgsUI() {
        if (this.mContentList != null) {
            boolean isNeedUpdate = false;
            for (IMsgContent msgContent : this.mContentList) {
                if (!(msgContent == null || msgContent.isRead)) {
                    msgContent.isRead = true;
                    isNeedUpdate = true;
                }
            }
            if (isNeedUpdate) {
                notifyDataSetUpdate();
            }
        }
    }
}

package com.gala.albumprovider.model;

import com.gala.tvapi.tv2.model.Channel;
import java.util.ArrayList;
import java.util.List;

public class QChannel extends Channel {
    public boolean isTopic() {
        return this.type == 1;
    }

    public boolean isVirtual() {
        return this.type == 2;
    }

    public boolean isSimulcast() {
        return this.simu == 1;
    }

    public boolean isFunction() {
        return this.type == 3;
    }

    public boolean isChannel() {
        return this.type == 0;
    }

    public boolean isAggregation() {
        return this.id.equals("6");
    }

    public boolean isMultiMenu() {
        return this.tags != null && this.tags.size() > 0;
    }

    public QLayoutKind getLayoutKind() {
        QLayoutKind qLayoutKind = QLayoutKind.PORTRAIT;
        switch (this.spec) {
            case 1:
                return QLayoutKind.LANDSCAPE;
            case 2:
                return QLayoutKind.PORTRAIT;
            default:
                return qLayoutKind;
        }
    }

    public static List<QChannel> createQChannelList(List<Channel> channels) {
        List<QChannel> arrayList = new ArrayList();
        for (Channel loadData : channels) {
            arrayList.add(loadData(loadData));
        }
        return arrayList;
    }

    public static QChannel loadData(Channel channel) {
        QChannel qChannel = new QChannel();
        qChannel.back = channel.back;
        qChannel.id = channel.id;
        qChannel.name = channel.name;
        qChannel.picUrl = channel.picUrl;
        qChannel.spec = channel.spec;
        qChannel.type = channel.type;
        qChannel.simu = channel.simu;
        qChannel.tags = channel.tags;
        qChannel.recTag = channel.recTag;
        qChannel.focus = channel.focus;
        qChannel.recRes = channel.recRes;
        qChannel.recPlay = channel.recPlay;
        qChannel.run = channel.run;
        qChannel.qipuId = channel.qipuId;
        return qChannel;
    }
}

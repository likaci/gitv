package com.gala.albumprovider.private;

import android.util.SparseArray;
import com.gala.albumprovider.model.QChannel;
import com.gala.albumprovider.util.ParseUtils;
import com.gala.tvapi.tv2.model.Channel;
import java.util.List;

public class g {
    private static g a = new g();
    private SparseArray<QChannel> f55a = new SparseArray();

    private g() {
    }

    public static g a() {
        return a;
    }

    public synchronized void a(List<Channel> list) {
        if (list != null) {
            this.f55a.clear();
            for (Channel channel : list) {
                QChannel loadData = QChannel.loadData(channel);
                int str2Int = ParseUtils.str2Int(channel.id);
                if (((QChannel) this.f55a.get(str2Int)) == null) {
                    this.f55a.put(str2Int, loadData);
                }
            }
        }
    }

    public QChannel a(String str) {
        return (QChannel) this.f55a.get(ParseUtils.str2Int(str));
    }

    public SparseArray<QChannel> m17a() {
        return this.f55a;
    }
}

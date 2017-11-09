package com.gala.albumprovider.p001private;

import android.util.SparseArray;
import com.gala.albumprovider.model.QChannel;
import com.gala.albumprovider.util.ParseUtils;
import com.gala.tvapi.tv2.model.Channel;
import java.util.List;

public class C0067g {
    private static C0067g f290a = new C0067g();
    private SparseArray<QChannel> f291a = new SparseArray();

    private C0067g() {
    }

    public static C0067g m139a() {
        return f290a;
    }

    public synchronized void m142a(List<Channel> list) {
        if (list != null) {
            this.f291a.clear();
            for (Channel channel : list) {
                QChannel loadData = QChannel.loadData(channel);
                int str2Int = ParseUtils.str2Int(channel.id);
                if (((QChannel) this.f291a.get(str2Int)) == null) {
                    this.f291a.put(str2Int, loadData);
                }
            }
        }
    }

    public QChannel m141a(String str) {
        return (QChannel) this.f291a.get(ParseUtils.str2Int(str));
    }

    public SparseArray<QChannel> m140a() {
        return this.f291a;
    }
}

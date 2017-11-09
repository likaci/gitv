package com.gala.video.lib.share.common.base;

import com.gala.albumprovider.model.QLayoutKind;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import java.util.ArrayList;
import java.util.List;

public abstract class DataMakeupFactory<T> {
    protected abstract IData dataMakeup(Object obj, QLayoutKind qLayoutKind, int i, Object obj2);

    public List dataListMakeup(List<T> list, QLayoutKind layout, int locationPage, Object model) {
        List<IData> newList = new ArrayList();
        if (ListUtils.isEmpty((List) list)) {
            return newList;
        }
        if (list.get(0) instanceof IData) {
            return list;
        }
        int count = ListUtils.getCount((List) list);
        for (int i = 0; i < count; i++) {
            IData data = dataMakeup(list.get(i), layout, locationPage, model);
            data.setIndexOfCurPage(i + 1);
            newList.add(data);
        }
        return newList;
    }
}

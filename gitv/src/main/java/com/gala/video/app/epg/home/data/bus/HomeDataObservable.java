package com.gala.video.app.epg.home.data.bus;

import com.gala.video.app.epg.home.data.hdata.HomeDataType;
import com.gala.video.app.epg.home.data.provider.AppsProvider;
import com.gala.video.app.epg.home.data.provider.CarouselChannelProvider;
import com.gala.video.app.epg.home.data.provider.ChannelProvider;
import com.gala.video.app.epg.home.data.provider.DailyNewsProvider;
import com.gala.video.app.epg.home.data.provider.HomeMenuProvider;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.HomeModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.WidgetChangeStatus;
import com.gala.video.lib.share.uikit.data.data.Model.DeviceCheckModel;
import com.gala.video.lib.share.utils.Precondition;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class HomeDataObservable {
    private static final HomeDataObservable sHomeDataObservable = new HomeDataObservable();
    private Hashtable<Integer, List<IHomeDataObserver>> mHomeDataObserverList = new Hashtable();

    private HomeDataObservable() {
    }

    public static HomeDataObservable getInstance() {
        return sHomeDataObservable;
    }

    public void addObserver(int type, IHomeDataObserver observer) {
        if (this.mHomeDataObserverList.containsKey(Integer.valueOf(type))) {
            ((List) this.mHomeDataObserverList.get(Integer.valueOf(type))).add(observer);
        } else {
            List<IHomeDataObserver> list = new CopyOnWriteArrayList();
            list.add(observer);
            this.mHomeDataObserverList.put(Integer.valueOf(type), list);
        }
        checkShouldPost(type, observer);
    }

    private void checkShouldPost(int type, IHomeDataObserver observer) {
        if (type == HomeDataType.DAILY_INFO.ordinal() && DailyNewsProvider.getInstance().isCached()) {
            observer.update(WidgetChangeStatus.DataChange, null);
        } else if (type == HomeDataType.APP_OPERATOR.ordinal() && !Precondition.isEmpty(AppsProvider.getInstance().getAppsList())) {
            observer.update(WidgetChangeStatus.DataChange, null);
        } else if (type == HomeDataType.APP_STORE.ordinal() && !StringUtils.isEmpty(AppsProvider.getInstance().getDownloadUrl())) {
            observer.update(WidgetChangeStatus.DataChange, null);
        } else if (type == HomeDataType.CHANNEL.ordinal() && !Precondition.isEmpty(ChannelProvider.getInstance().getChannelList())) {
            observer.update(WidgetChangeStatus.DataChange, null);
        } else if (type == HomeDataType.CAROUSEL_CHANNEL.ordinal() && !Precondition.isEmpty(CarouselChannelProvider.getInstance().getChannelList())) {
            observer.update(WidgetChangeStatus.DataChange, null);
        } else if (type == HomeDataType.DEVICE_REGISTER.ordinal()) {
            if (DeviceCheckModel.getInstance().isDevCheckPass()) {
                observer.update(WidgetChangeStatus.DataChange, null);
            } else {
                observer.update(WidgetChangeStatus.NoData, null);
            }
        } else if (type == HomeDataType.HOME_MENU.ordinal() && !Precondition.isEmpty(HomeMenuProvider.getInstance().getDataList())) {
            observer.update(WidgetChangeStatus.DataChange, null);
        }
    }

    public void deleteObserver(int type, IHomeDataObserver observer) {
        if (this.mHomeDataObserverList.containsKey(Integer.valueOf(type)) && ((List) this.mHomeDataObserverList.get(Integer.valueOf(type))).contains(observer)) {
            ((List) this.mHomeDataObserverList.get(Integer.valueOf(type))).remove(observer);
        }
    }

    public void post(HomeDataType hType, WidgetChangeStatus status, HomeModel data) {
        List list = null;
        if (this.mHomeDataObserverList.containsKey(Integer.valueOf(hType.ordinal()))) {
            list = (List) this.mHomeDataObserverList.get(Integer.valueOf(hType.ordinal()));
        }
        if (!Precondition.isEmpty((List) list)) {
            for (IHomeDataObserver observer : list) {
                if (observer != null) {
                    observer.update(status, data);
                }
            }
        }
    }

    public void clear() {
        this.mHomeDataObserverList.clear();
    }
}

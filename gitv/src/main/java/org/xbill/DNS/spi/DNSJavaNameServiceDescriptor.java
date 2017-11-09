package org.xbill.DNS.spi;

import com.gala.video.app.epg.ui.setting.interactor.impl.SettingAboutInteractorImpl;
import java.lang.reflect.Proxy;
import sun.net.spi.nameservice.NameService;
import sun.net.spi.nameservice.NameServiceDescriptor;

public class DNSJavaNameServiceDescriptor implements NameServiceDescriptor {
    static Class class$sun$net$spi$nameservice$NameService;
    private static NameService nameService;

    static {
        Class class$;
        if (class$sun$net$spi$nameservice$NameService == null) {
            class$ = class$("sun.net.spi.nameservice.NameService");
            class$sun$net$spi$nameservice$NameService = class$;
        } else {
            class$ = class$sun$net$spi$nameservice$NameService;
        }
        ClassLoader loader = class$.getClassLoader();
        Class[] clsArr = new Class[1];
        if (class$sun$net$spi$nameservice$NameService == null) {
            class$ = class$("sun.net.spi.nameservice.NameService");
            class$sun$net$spi$nameservice$NameService = class$;
        } else {
            class$ = class$sun$net$spi$nameservice$NameService;
        }
        clsArr[0] = class$;
        nameService = (NameService) Proxy.newProxyInstance(loader, clsArr, new DNSJavaNameService());
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    public NameService createNameService() {
        return nameService;
    }

    public String getType() {
        return SettingAboutInteractorImpl.DNS;
    }

    public String getProviderName() {
        return "dnsjava";
    }
}

package org.cybergarage.upnp.event;

import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.lib.share.ifmanager.bussnessIF.openplay.IOpenApiCommandHolder;
import java.net.URL;
import org.cybergarage.soap.SOAP;
import org.cybergarage.upnp.Service;
import org.cybergarage.upnp.StateVariable;
import org.cybergarage.util.Debug;
import org.cybergarage.util.ThreadCore;
import org.cybergarage.xml.Node;

public class Subscriber extends ThreadCore {
    private String SID = null;
    private String deliveryHost = "";
    private String deliveryPath = "";
    private int deliveryPort = 0;
    private String deliveryURL = "";
    private String ifAddr = "";
    private SubscriberList mList = null;
    private Service mService = null;
    private boolean mTvguo = false;
    private String mValue = "";
    private long notifyCount = 0;
    private long subscriptionTime = 0;
    private long timeOut = 0;

    public Subscriber() {
        renew();
    }

    public String getSID() {
        return this.SID;
    }

    public void setSID(String sid) {
        this.SID = sid;
    }

    public void setInterfaceAddress(String addr) {
        this.ifAddr = addr;
    }

    public String getInterfaceAddress() {
        return this.ifAddr;
    }

    public String getDeliveryURL() {
        return this.deliveryURL;
    }

    public void setDeliveryURL(String deliveryURL) {
        this.deliveryURL = deliveryURL;
        try {
            URL url = new URL(deliveryURL);
            this.deliveryHost = url.getHost();
            this.deliveryPath = url.getPath();
            this.deliveryPort = url.getPort();
        } catch (Exception e) {
        }
    }

    public String getDeliveryHost() {
        return this.deliveryHost;
    }

    public String getDeliveryPath() {
        return this.deliveryPath;
    }

    public int getDeliveryPort() {
        return this.deliveryPort;
    }

    public long getTimeOut() {
        return this.timeOut;
    }

    public void setTimeOut(long value) {
        this.timeOut = value;
    }

    public boolean isExpired() {
        long currTime = System.currentTimeMillis();
        if (this.timeOut != -1 && getSubscriptionTime() + (getTimeOut() * 1000) < currTime) {
            return true;
        }
        return false;
    }

    public long getSubscriptionTime() {
        return this.subscriptionTime;
    }

    public void setSubscriptionTime(long time) {
        this.subscriptionTime = time;
    }

    public long getNotifyCount() {
        return this.notifyCount;
    }

    public void setNotifyCount(int cnt) {
        this.notifyCount = (long) cnt;
    }

    public void incrementNotifyCount() {
        if (this.notifyCount == IOpenApiCommandHolder.OAA_NO_LIMIT) {
            this.notifyCount = 1;
        } else {
            this.notifyCount++;
        }
    }

    public void renew() {
        setSubscriptionTime(System.currentTimeMillis());
        setNotifyCount(0);
    }

    public void initThreadParams(Node serviceNode, boolean tvguo) {
        this.mTvguo = tvguo;
        this.mService = new Service(serviceNode);
        this.mList = this.mTvguo ? this.mService.getSubscriberList_tvguo() : this.mService.getSubscriberList_dlna();
    }

    public void run() {
        Debug.message("[Subscriber] start...[" + this.deliveryURL + AlbumEnterFactory.SIGN_STR);
        int retryCount = 0;
        NotifyRequest notifyReq = new NotifyRequest();
        while (isRunnable()) {
            synchronized (this.mList) {
                String tmpValue = "";
                StateVariable var = Service.getStateVar(this.mTvguo);
                if (var != null) {
                    if (this.mTvguo) {
                        tmpValue = var.getValue_tvguo();
                    } else {
                        try {
                            tmpValue = var.getValue_dlna();
                        } catch (InterruptedException e) {
                            Debug.message("[Subscriber] Notify thread interruptted...[" + this.deliveryURL + AlbumEnterFactory.SIGN_STR);
                        }
                    }
                }
                if (this.mValue.equals(tmpValue) || retryCount >= 10) {
                    Debug.message("[Subscriber] Notify thread sleep...[" + this.deliveryURL + AlbumEnterFactory.SIGN_STR);
                    this.mList.wait();
                    retryCount = 0;
                    Debug.message("[Subscriber] Notify thread wake up...[" + this.deliveryURL + AlbumEnterFactory.SIGN_STR);
                }
            }
            if (this.mService.getSubscriber(this.SID, this.mTvguo) == null) {
                Debug.message("[Subscriber] Notify thread expired...[" + this.deliveryURL + AlbumEnterFactory.SIGN_STR);
                break;
            }
            StateVariable stateVar = Service.getStateVar(this.mTvguo);
            if (stateVar == null) {
                Debug.message("[Subscriber] mad world continue...[" + this.deliveryURL + AlbumEnterFactory.SIGN_STR);
            } else {
                String name = stateVar.getName();
                String value = this.mTvguo ? stateVar.getValue_tvguo() : stateVar.getValue_dlna();
                Debug.message("[Subscriber] Notify thread notify [" + this.deliveryURL + "][" + name + SOAP.DELIM + value + AlbumEnterFactory.SIGN_STR);
                notifyReq.setRequest(this, name, value);
                if (notifyReq.post(this.deliveryHost, this.deliveryPort, true).isSuccessful()) {
                    Debug.message("[Subscriber] Notify thread notify success [" + this.deliveryURL + "][" + name + SOAP.DELIM + value + AlbumEnterFactory.SIGN_STR);
                    this.mValue = value;
                    incrementNotifyCount();
                } else {
                    retryCount++;
                    Debug.message("[Subscriber] Notify thread notify failure [" + this.deliveryURL + "][" + name + SOAP.DELIM + value + "] retryCount=" + retryCount);
                    if (retryCount >= 10) {
                        Debug.message("[Subscriber] Notify thread notify failure [Give up!!!] [" + this.deliveryURL + "][" + name + SOAP.DELIM + value + "] retryCount=" + retryCount);
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e2) {
                        Debug.message("[Subscriber] Notify thread interrupted 2...[" + this.deliveryURL + AlbumEnterFactory.SIGN_STR);
                    }
                }
            }
        }
        notifyReq.closeHostSocket();
        Debug.message("[Subscriber] Notify thread exit...[" + this.deliveryURL + AlbumEnterFactory.SIGN_STR);
    }
}

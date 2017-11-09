package org.cybergarage.upnp;

import com.gala.android.dlna.sdk.controlpoint.TVGuoDescription;
import com.gala.android.dlna.sdk.stddmrcontroller.Util;
import com.gala.android.sdk.dlna.keeper.DmcInforKeeper;
import com.gala.android.sdk.dlna.keeper.DmrInfor;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import org.cybergarage.http.HTTP;
import org.cybergarage.soap.SOAP;
import org.cybergarage.upnp.control.ActionListener;
import org.cybergarage.upnp.control.QueryListener;
import org.cybergarage.upnp.device.InvalidDescriptionException;
import org.cybergarage.upnp.device.NTS;
import org.cybergarage.upnp.device.ST;
import org.cybergarage.upnp.event.NotifyRequest;
import org.cybergarage.upnp.event.Subscriber;
import org.cybergarage.upnp.event.SubscriberList;
import org.cybergarage.upnp.ssdp.SSDPNotifyRequest;
import org.cybergarage.upnp.ssdp.SSDPNotifySocket;
import org.cybergarage.upnp.ssdp.SSDPPacket;
import org.cybergarage.upnp.std.av.server.object.DIDLLite;
import org.cybergarage.upnp.xml.ServiceData;
import org.cybergarage.util.Debug;
import org.cybergarage.util.Mutex;
import org.cybergarage.util.StringUtil;
import org.cybergarage.xml.Node;
import org.cybergarage.xml.ParserException;

public class Service {
    private static final String CONTROL_URL = "controlURL";
    public static final String ELEM_NAME = "service";
    private static final String EVENT_SUB_URL = "eventSubURL";
    public static final String MAJOR = "major";
    public static final String MAJOR_VALUE = "1";
    public static final String MINOR = "minor";
    public static final String MINOR_VALUE = "0";
    private static final String SCPDURL = "SCPDURL";
    public static final String SCPD_AVTRANSPORT = "urn:schemas-upnp-org:service:AVTransport:1";
    public static final String SCPD_RENDERING_CONTROL = "urn:schemas-upnp-org:service:RenderingControl:1";
    public static final String SCPD_ROOTNODE = "scpd";
    public static final String SCPD_ROOTNODE_NS = "urn:schemas-upnp-org:service-1-0";
    private static final String SERVICE_ID = "serviceId";
    private static final String SERVICE_TYPE = "serviceType";
    public static final String SERVICE_TYPE_AVTRANSPORT = "AVTransport";
    public static final String SERVICE_TYPE_RENDERING_CONTROL = "RenderingControl";
    public static final String SPEC_VERSION = "specVersion";
    public static StateVariable mStateVar_external;
    public static StateVariable mStateVar_internal;
    private int NOTIFY_RETRY_NUM;
    private String descriptionXmlContent;
    private Device mDevice;
    private String mServiceNt;
    private String mServiceUsn;
    private Mutex mutex;
    private Node serviceNode;
    private Object userData;

    public Node getServiceNode() {
        return this.serviceNode;
    }

    public Service() {
        this(new Node(ELEM_NAME));
        Node sp = new Node("specVersion");
        Node M = new Node("major");
        M.setValue("1");
        sp.addNode(M);
        Node m = new Node("minor");
        m.setValue("0");
        sp.addNode(m);
        Node scpd = new Node(SCPD_ROOTNODE);
        scpd.addAttribute(DIDLLite.XMLNS, SCPD_ROOTNODE_NS);
        scpd.addNode(sp);
        getServiceData().setSCPDNode(scpd);
    }

    public Service(Node node) {
        this.NOTIFY_RETRY_NUM = 4;
        this.descriptionXmlContent = "";
        this.mutex = new Mutex();
        this.userData = null;
        this.serviceNode = node;
    }

    public String getDescriptionXmlContent() {
        return this.descriptionXmlContent;
    }

    public void setDescriptionXmlContent(String descriptionXmlContent) {
        this.descriptionXmlContent = descriptionXmlContent;
    }

    public void setActionListener(ActionListener listener) {
        ActionList actionList = getActionList();
        int nActions = actionList.size();
        for (int n = 0; n < nActions; n++) {
            actionList.getAction(n).setActionListener(listener);
        }
    }

    public void lock() {
        this.mutex.lock();
    }

    public void unlock() {
        this.mutex.unlock();
    }

    public static boolean isServiceNode(Node node) {
        return ELEM_NAME.equals(node.getName());
    }

    private Node getDeviceNode() {
        Node node = getServiceNode().getParentNode();
        if (node == null) {
            return null;
        }
        return node.getParentNode();
    }

    private Node getRootNode() {
        return getServiceNode().getRootNode();
    }

    public Device getDevice() {
        return new Device(getRootNode(), getDeviceNode());
    }

    public Device getRootDevice() {
        return getDevice().getRootDevice();
    }

    public void setServiceType(String value) {
        getServiceNode().setNode(SERVICE_TYPE, value);
    }

    public String getServiceType() {
        return getServiceNode().getNodeValue(SERVICE_TYPE);
    }

    public void setServiceID(String value) {
        getServiceNode().setNode(SERVICE_ID, value);
    }

    public String getServiceID() {
        return getServiceNode().getNodeValue(SERVICE_ID);
    }

    private boolean isURL(String referenceUrl, String url) {
        if (referenceUrl == null || url == null) {
            return false;
        }
        if (url.equals(referenceUrl) || url.equals(HTTP.toRelativeURL(referenceUrl, false))) {
            return true;
        }
        return false;
    }

    public void setSCPDURL(String value) {
        getServiceNode().setNode(SCPDURL, value);
    }

    public String getSCPDURL() {
        return getServiceNode().getNodeValue(SCPDURL);
    }

    public boolean isSCPDURL(String url) {
        return isURL(getSCPDURL(), url);
    }

    public void setControlURL(String value) {
        getServiceNode().setNode(CONTROL_URL, value);
    }

    public String getControlURL() {
        return getServiceNode().getNodeValue(CONTROL_URL);
    }

    public boolean isControlURL(String url) {
        return isURL(getControlURL(), url);
    }

    public void setEventSubURL(String value) {
        getServiceNode().setNode(EVENT_SUB_URL, value);
    }

    public String getEventSubURL() {
        return getServiceNode().getNodeValue(EVENT_SUB_URL);
    }

    public boolean isEventSubURL(String url) {
        return isURL(getEventSubURL(), url);
    }

    public boolean loadSCPD(String scpdStr) throws InvalidDescriptionException {
        try {
            Node scpdNode = UPnP.getXMLParser().parse(scpdStr);
            if (scpdNode == null) {
                return false;
            }
            getServiceData().setSCPDNode(scpdNode);
            scpdNode.addAttribute(DIDLLite.XMLNS, SCPD_ROOTNODE_NS);
            return true;
        } catch (Exception e) {
            throw new InvalidDescriptionException(e);
        }
    }

    public boolean loadSCPD(File file) throws ParserException {
        Node scpdNode = UPnP.getXMLParser().parse(file);
        if (scpdNode == null) {
            return false;
        }
        getServiceData().setSCPDNode(scpdNode);
        return true;
    }

    public boolean loadSCPD(InputStream input) throws ParserException {
        Node scpdNode = UPnP.getXMLParser().parse(input);
        if (scpdNode == null) {
            return false;
        }
        getServiceData().setSCPDNode(scpdNode);
        return true;
    }

    public void setDescriptionURL(String value) {
        getServiceData().setDescriptionURL(value);
    }

    public String getDescriptionURL() {
        return getServiceData().getDescriptionURL();
    }

    private Node getSCPDNode(URL scpdUrl) throws ParserException {
        return UPnP.getXMLParser().parse(scpdUrl);
    }

    private Node getSCPDNode(File scpdFile) throws ParserException {
        return UPnP.getXMLParser().parse(scpdFile);
    }

    private Node getSCPDNode(String scpdDocument) throws ParserException {
        return UPnP.getXMLParser().parse(scpdDocument);
    }

    private Node getSCPDNode() {
        ServiceData data = getServiceData();
        Node scpdNode = data.getSCPDNode();
        if (scpdNode != null) {
            return scpdNode;
        }
        Device rootDev = getRootDevice();
        if (rootDev == null) {
            return null;
        }
        String scpdURLStr = getSCPDURL();
        if (rootDev.isGalaServer()) {
            DmrInfor dmrInfor = DmcInforKeeper.getInstance().getDmrInfor(rootDev.getUDN());
            if (dmrInfor != null) {
                if (dmrInfor.getServerMap().containsKey(scpdURLStr)) {
                    try {
                        scpdNode = getSCPDNode((String) dmrInfor.getServerMap().get(scpdURLStr));
                    } catch (ParserException e) {
                        e.printStackTrace();
                    }
                    if (scpdNode != null) {
                        data.setSCPDNode(scpdNode);
                        return scpdNode;
                    }
                }
                try {
                    scpdNode = getSCPDNode(TVGuoDescription.TVGuoSCPD);
                } catch (ParserException e2) {
                    e2.printStackTrace();
                }
                if (scpdNode != null) {
                    data.setSCPDNode(scpdNode);
                    return scpdNode;
                }
            }
        }
        String rootDevPath = rootDev.getDescriptionFilePath();
        if (rootDevPath != null) {
            File f = new File(rootDevPath.concat(scpdURLStr));
            if (f.exists()) {
                try {
                    scpdNode = getSCPDNode(f);
                } catch (ParserException e22) {
                    e22.printStackTrace();
                }
                if (scpdNode != null) {
                    data.setSCPDNode(scpdNode);
                    saveServiceToCache(rootDev, scpdURLStr, scpdNode);
                    return scpdNode;
                }
            }
        }
        try {
            scpdNode = getSCPDNode(new URL(rootDev.getAbsoluteURL(scpdURLStr)));
            if (scpdNode != null) {
                data.setSCPDNode(scpdNode);
                saveServiceToCache(rootDev, scpdURLStr, scpdNode);
                return scpdNode;
            }
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        try {
            scpdNode = getSCPDNode(new File(rootDev.getDescriptionFilePath() + HTTP.toRelativeURL(scpdURLStr)));
            saveServiceToCache(rootDev, scpdURLStr, scpdNode);
            return scpdNode;
        } catch (Exception e32) {
            Debug.warning(e32);
            return null;
        }
    }

    private void saveServiceToCache(Device rootDev, String scpdURLStr, Node scpdNode) {
        if (rootDev.getSSDPPacket() != null && rootDev.getSSDPPacket().isGalaServer()) {
            DmrInfor dmrInfor = DmcInforKeeper.getInstance().getDmrInfor(rootDev.getUDN());
            if (dmrInfor != null && !dmrInfor.getServerMap().containsKey(scpdURLStr)) {
                dmrInfor.getServerMap().put(scpdURLStr, scpdNode.toString());
                DmcInforKeeper.getInstance().SaveDmrInfor(dmrInfor);
            }
        }
    }

    public byte[] getSCPDData() {
        if (getDescriptionXmlContent() == "") {
            Node scpdNode = getSCPDNode();
            if (scpdNode == null) {
                return new byte[0];
            }
            setDescriptionXmlContent(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new String())).append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").toString())).append("\n").toString())).append(scpdNode.toString()).toString());
        }
        return getDescriptionXmlContent().getBytes();
    }

    public ActionList getActionList() {
        ActionList actionList = new ActionList();
        Node scdpNode = getSCPDNode();
        if (scdpNode != null) {
            Node actionListNode = scdpNode.getNode(ActionList.ELEM_NAME);
            if (actionListNode != null) {
                int nNode = actionListNode.getNNodes();
                for (int n = 0; n < nNode; n++) {
                    Node node = actionListNode.getNode(n);
                    if (Action.isActionNode(node)) {
                        actionList.add(new Action(this.serviceNode, node));
                    }
                }
            }
        }
        return actionList;
    }

    public Action getAction(String actionName) {
        if (actionName == null || actionName == "") {
            return null;
        }
        ActionList actionList = getActionList();
        int nActions = actionList.size();
        for (int n = 0; n < nActions; n++) {
            Action action = actionList.getAction(n);
            String name = action.getName();
            if (name != null && name.equals(actionName)) {
                return action;
            }
        }
        return null;
    }

    public void addAction(Action a) {
        if (a != null) {
            Iterator i = a.getArgumentList().iterator();
            while (i.hasNext()) {
                ((Argument) i.next()).setService(this);
            }
            Node scdpNode = getSCPDNode();
            Node actionListNode = scdpNode.getNode(ActionList.ELEM_NAME);
            if (actionListNode == null) {
                actionListNode = new Node(ActionList.ELEM_NAME);
                scdpNode.addNode(actionListNode);
            }
            actionListNode.addNode(a.getActionNode());
        }
    }

    public ServiceStateTable getServiceStateTable() {
        ServiceStateTable stateTable = new ServiceStateTable();
        Node stateTableNode = getSCPDNode().getNode(ServiceStateTable.ELEM_NAME);
        if (stateTableNode != null) {
            Node serviceNode = getServiceNode();
            int nNode = stateTableNode.getNNodes();
            for (int n = 0; n < nNode; n++) {
                Node node = stateTableNode.getNode(n);
                if (StateVariable.isStateVariableNode(node)) {
                    stateTable.add(new StateVariable(serviceNode, node));
                }
            }
        }
        return stateTable;
    }

    public StateVariable getStateVariable(String name) {
        if (name == null || name == "") {
            return null;
        }
        ServiceStateTable stateTable = getServiceStateTable();
        int tableSize = stateTable.size();
        for (int n = 0; n < tableSize; n++) {
            StateVariable var = stateTable.getStateVariable(n);
            String varName = var.getName();
            if (varName != null && varName.equals(name)) {
                return var;
            }
        }
        return null;
    }

    public boolean hasStateVariable(String name) {
        return getStateVariable(name) != null;
    }

    public boolean isService(String name) {
        if (name == null) {
            return false;
        }
        if (name.endsWith(getServiceType())) {
            return true;
        }
        if (name.endsWith(getServiceID())) {
            return true;
        }
        return false;
    }

    private ServiceData getServiceData() {
        Node node = getServiceNode();
        ServiceData userData = (ServiceData) node.getUserData();
        if (userData != null) {
            return userData;
        }
        userData = new ServiceData();
        node.setUserData(userData);
        userData.setNode(node);
        return userData;
    }

    private String getNotifyServiceTypeNT() {
        return getServiceType();
    }

    private String getNotifyServiceTypeUSN() {
        return new StringBuilder(String.valueOf(getDevice().getUDN())).append("::").append(getServiceType()).toString();
    }

    public void announce(String bindAddr, int bindssdPort) {
        String devLocation = getRootDevice().getLocationURL(bindAddr);
        String serviceNT = getNotifyServiceTypeNT();
        String serviceUSN = getNotifyServiceTypeUSN();
        Device dev = getDevice();
        SSDPNotifyRequest ssdpReq = SSDPNotifyRequest.getInstance();
        ssdpReq.setServer(UPnP.getServerName());
        ssdpReq.setLeaseTime(dev.getLeaseTime());
        ssdpReq.setLocation(devLocation);
        ssdpReq.setNTS(NTS.ALIVE);
        ssdpReq.setNT(serviceNT);
        ssdpReq.setUSN(serviceUSN);
        ssdpReq.setConnect(true);
        SSDPNotifySocket ssdpSock = SSDPNotifySocket.getInstance(bindAddr);
        Device.notifyWait();
        ssdpSock.post(ssdpReq, bindAddr, bindssdPort);
    }

    public void byebye(String bindAddr) {
        String devNT = getNotifyServiceTypeNT();
        String devUSN = getNotifyServiceTypeUSN();
        SSDPNotifyRequest ssdpReq = SSDPNotifyRequest.getInstance();
        ssdpReq.setNTS(NTS.BYEBYE);
        ssdpReq.setNT(devNT);
        ssdpReq.setUSN(devUSN);
        SSDPNotifySocket ssdpSock = SSDPNotifySocket.getInstance(bindAddr);
        Device.notifyWait();
        ssdpSock.post(ssdpReq, null, -1);
    }

    public boolean serviceSearchResponse(SSDPPacket ssdpPacket) {
        if (ssdpPacket == null) {
            Debug.message("serviceSearchResponse() ssdppacket is null");
            return false;
        }
        byte[] message = ssdpPacket.getData();
        if (this.mDevice == null) {
            this.mDevice = getDevice();
        }
        if (this.mServiceNt == null) {
            this.mServiceNt = getNotifyServiceTypeNT();
        }
        if (this.mServiceUsn == null) {
            this.mServiceUsn = getNotifyServiceTypeUSN();
        }
        if (Util.hasCode(message, ST.ALL_DEVICE_BYTE)) {
            this.mDevice.postSearchResponse(ssdpPacket, this.mServiceNt, this.mServiceUsn);
        } else if (Util.hasCode(message, ST.URN_SERVICE_BYTE)) {
            String serviceType = getServiceType();
            byte[] ssdpST = Util.copyFromByte(ssdpPacket.getData(), HTTP.ST_BYTES, HTTP.CRLF.getBytes());
            if (ssdpST != null && ssdpST.equals(serviceType)) {
                this.mDevice.postSearchResponse(ssdpPacket, serviceType, this.mServiceUsn);
            }
        }
        return true;
    }

    public void setQueryListener(QueryListener queryListener) {
        ServiceStateTable stateTable = getServiceStateTable();
        int tableSize = stateTable.size();
        for (int n = 0; n < tableSize; n++) {
            stateTable.getStateVariable(n).setQueryListener(queryListener);
        }
    }

    public SubscriberList getSubscriberList_dlna() {
        return getServiceData().getSubscriberList_dlna();
    }

    public SubscriberList getSubscriberList_tvguo() {
        return getServiceData().getSubscriberList_tvguo();
    }

    public synchronized void addSubscriber(Subscriber sub, boolean tvguo) {
        SubscriberList subList;
        Debug.message("sub: " + getServiceID() + " addSubscriber for " + (tvguo ? "TVGupApp" : Util.FUNCTION_TAG_DLNA) + ": " + sub.getSID() + "  " + sub.getDeliveryURL());
        if (tvguo) {
            subList = getSubscriberList_tvguo();
        } else {
            subList = getSubscriberList_dlna();
        }
        synchronized (subList) {
            int subListCnt = subList.size();
            for (int n = 0; n < subListCnt; n++) {
                Subscriber curSub = subList.getSubscriber(n);
                if (curSub != null && sub.getDeliveryHost().equals(curSub.getDeliveryHost()) && sub.getDeliveryPort() == curSub.getDeliveryPort()) {
                    Debug.message("sub: [New SID]: " + sub.getSID() + " [Replace SID]: " + curSub.getSID());
                    curSub.stop();
                    subList.remove(n);
                }
            }
            Debug.message("sub: [New SID] " + sub.getSID());
            subList.add(sub);
        }
        sub.initThreadParams(getServiceNode(), tvguo);
        sub.start("NotifySubscriberThread[" + sub.getDeliveryHost() + SOAP.DELIM + sub.getDeliveryPort() + AlbumEnterFactory.SIGN_STR);
    }

    public synchronized void removeSubscriber(Subscriber sub, boolean tvguo) {
        SubscriberList subList;
        if (tvguo) {
            Debug.message("sub: removeSubscriber DLNA" + sub.getSID());
            subList = getSubscriberList_tvguo();
        } else {
            Debug.message("sub: removeSubscriber TVGuoApp" + sub.getSID());
            subList = getSubscriberList_dlna();
        }
        synchronized (subList) {
            subList.remove(sub);
        }
        sub.stop();
    }

    public Subscriber getSubscriber(String name, boolean external) {
        SubscriberList subList;
        if (external) {
            subList = getSubscriberList_tvguo();
        } else {
            subList = getSubscriberList_dlna();
        }
        int subListCnt = subList.size();
        for (int n = 0; n < subListCnt; n++) {
            Subscriber sub = subList.getSubscriber(n);
            if (sub != null) {
                String sid = sub.getSID();
                if (sid != null && sid.equals(name)) {
                    return sub;
                }
            }
        }
        return null;
    }

    private boolean notify(Subscriber sub, StateVariable stateVar, boolean external) {
        String value;
        String varName = stateVar.getName();
        if (external) {
            value = stateVar.getValue_tvguo();
        } else {
            value = stateVar.getValue_dlna();
        }
        String host = sub.getDeliveryHost();
        int port = sub.getDeliveryPort();
        NotifyRequest notifyReq = new NotifyRequest();
        notifyReq.setRequest(sub, varName, value);
        int retry = 0;
        while (!notifyReq.post(host, port).isSuccessful()) {
            retry++;
            if (retry >= this.NOTIFY_RETRY_NUM) {
                Debug.message("sub: notify failure [" + host + SOAP.DELIM + port + "][" + varName + SOAP.DELIM + value + "][Give Up!]");
                return false;
            }
            Debug.message("sub: notify failure [" + host + SOAP.DELIM + port + "][" + varName + SOAP.DELIM + value + "][Retry:" + retry + AlbumEnterFactory.SIGN_STR);
        }
        Debug.message("sub: notify success [" + host + SOAP.DELIM + port + "][" + varName + SOAP.DELIM + value + AlbumEnterFactory.SIGN_STR);
        sub.incrementNotifyCount();
        return true;
    }

    public static StateVariable getStateVar(boolean external) {
        if (external) {
            return mStateVar_external;
        }
        return mStateVar_internal;
    }

    public static void setStateVar(StateVariable var, boolean tvguo) {
        if (tvguo) {
            mStateVar_external = new StateVariable(var.getServiceNode(), var.getStateVariableNode());
        } else {
            mStateVar_internal = new StateVariable(var.getServiceNode(), var.getStateVariableNode());
        }
    }

    public void notifySubscribers(StateVariable var, boolean tvguo) {
        if (tvguo) {
            synchronized (getSubscriberList_tvguo()) {
                Debug.message("sub: wake up TVGuoApp NotifySubsriberThreads");
                setStateVar(var, tvguo);
                getSubscriberList_tvguo().notifyAll();
            }
            return;
        }
        synchronized (getSubscriberList_dlna()) {
            Debug.message("sub: wake up DLNA NotifySubsriberThreads");
            setStateVar(var, tvguo);
            getSubscriberList_dlna().notifyAll();
        }
    }

    public synchronized void stopNotifyThreads() {
        int n;
        Debug.message("sub: stop NotifySubsriberThreads " + getServiceID());
        SubscriberList subList = getSubscriberList_dlna();
        int subListCnt = subList.size();
        Debug.message("sub: stop TVGuoApp NotifySubsriberThreads Count=" + subListCnt);
        for (n = 0; n < subListCnt; n++) {
            Subscriber curSub = subList.getSubscriber(n);
            if (curSub != null) {
                curSub.stop();
            }
        }
        subList.clear();
        subList = getSubscriberList_tvguo();
        subListCnt = subList.size();
        Debug.message("sub: stop DLNA NotifySubsriberThreads Count=" + subListCnt);
        for (n = 0; n < subListCnt; n++) {
            curSub = subList.getSubscriber(n);
            if (curSub != null) {
                curSub.stop();
            }
        }
        subList.clear();
    }

    public void notify(StateVariable stateVar, boolean external) {
        SubscriberList subList;
        String tmp = external ? "TVGupApp" : Util.FUNCTION_TAG_DLNA;
        Debug.message("sub: notify " + tmp + " [" + stateVar.getName() + "][" + stateVar.getValue_dlna() + AlbumEnterFactory.SIGN_STR);
        if (external) {
            Debug.message("sub: notify external");
            subList = getSubscriberList_tvguo();
        } else {
            subList = getSubscriberList_dlna();
        }
        int subListCnt = subList.size();
        if (subListCnt == 0) {
            Debug.message("sub: " + tmp + " list empty...");
            return;
        }
        int n;
        Subscriber[] subs = new Subscriber[subListCnt];
        for (n = 0; n < subListCnt; n++) {
            subs[n] = subList.getSubscriber(n);
        }
        for (n = 0; n < subListCnt; n++) {
            Subscriber sub = subs[n];
            if (sub != null && sub.isExpired()) {
                Debug.message("sub: removesubscriber..." + sub.getDeliveryURL());
                removeSubscriber(sub, external);
            }
        }
        notifySubscribers(stateVar, external);
    }

    public void notifyAllStateVariables(boolean external) {
        ServiceStateTable stateTable = getServiceStateTable();
        int tableSize = stateTable.size();
        for (int n = 0; n < tableSize; n++) {
            StateVariable var = stateTable.getStateVariable(n);
            if (var.isSendEvents()) {
                notify(var, external);
            }
        }
    }

    public String getSID() {
        return getServiceData().getSID();
    }

    public void setSID(String id) {
        getServiceData().setSID(id);
    }

    public void clearSID() {
        setSID("");
        setTimeout(0);
    }

    public boolean hasSID() {
        return StringUtil.hasData(getSID());
    }

    public boolean isSubscribed() {
        return hasSID();
    }

    public long getTimeout() {
        return getServiceData().getTimeout();
    }

    public void setTimeout(long value) {
        getServiceData().setTimeout(value);
    }

    public void addStateVariable(StateVariable var) {
        Node stateTableNode = getSCPDNode().getNode(ServiceStateTable.ELEM_NAME);
        if (stateTableNode == null) {
            stateTableNode = new Node(ServiceStateTable.ELEM_NAME);
            getSCPDNode().addNode(stateTableNode);
        }
        var.setServiceNode(getServiceNode());
        stateTableNode.addNode(var.getStateVariableNode());
    }

    public void setUserData(Object data) {
        this.userData = data;
    }

    public Object getUserData() {
        return this.userData;
    }
}

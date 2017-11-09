package com.gala.android.dlna.sdk.mediarenderer.service;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import com.gala.android.dlna.sdk.mediarenderer.DlnaMediaModel;
import com.gala.android.dlna.sdk.mediarenderer.MediaRenderer;
import com.gala.android.dlna.sdk.mediarenderer.StandardDLNAListener;
import com.gala.android.dlna.sdk.mediarenderer.service.infor.AVTransportConstStr;
import com.gala.android.dlna.sdk.mediarenderer.service.infor.AVTransportInfo;
import com.gala.android.dlna.sdk.mediarenderer.service.infor.AVTransportInfoList;
import com.gala.video.webview.utils.WebSDKConstants;
import java.io.ByteArrayInputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.Service;
import org.cybergarage.upnp.ServiceInterface;
import org.cybergarage.upnp.ServiceStateTable;
import org.cybergarage.upnp.StateVariable;
import org.cybergarage.upnp.UPnPStatus;
import org.cybergarage.upnp.control.ActionListener;
import org.cybergarage.upnp.control.QueryListener;
import org.cybergarage.upnp.device.InvalidDescriptionException;
import org.cybergarage.upnp.std.av.server.UPnP;
import org.cybergarage.upnp.std.av.server.object.SearchCriteria;
import org.cybergarage.util.Debug;
import org.cybergarage.util.Mutex;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class AVTransport extends Service implements ActionListener, QueryListener, ServiceInterface {
    private static final int GENA_PAUSED_PLAYBACK = 5;
    private static final int GENA_PLAY = 0;
    private static final int GENA_PLAYING_STATE_CHANGE = 3;
    private static final int GENA_PLAY_STATE = 2;
    private static final int GENA_STATE_UPDATE_DURATION = 4;
    private static final int GENA_STATE_UPDATE_DURATION_0 = 1;
    private static String absCount = "2147483647";
    private static String absTime = "00:00:00";
    private static String currentTransportActions = "Stop,Pause,Seek";
    private static AVTransport instance = null;
    private static StateVariable lastChangenotifyStateVariable = null;
    private static String relCount = "2147483647";
    private static String relTime = "00:00:00";
    private static String track = "1";
    private static String trackDuration = "00:00:00";
    private static String trackMetadata = "NOT_IMPLEMENTED";
    private static String trackUri = "";
    private static String transportPlaySpeed = "1";
    private static String transportState = "NO_MEDIA_PRESENT";
    private static String transportStatus = "OK";
    private AVTransportInfoList avTransInfoList;
    private HandlerThread handlerThread = new HandlerThread("GENA");
    private boolean isKugouMusic = false;
    private Handler mHandler = null;
    private MediaRenderer mediaRenderer;
    private Mutex mutex = new Mutex();

    public void setPlayingState(String playingstate) {
        transportState = playingstate;
        if (playingstate.equals("PLAYING")) {
            this.mHandler.sendEmptyMessage(4);
        } else if (playingstate.equals("PAUSED_PLAYBACK")) {
            this.mHandler.sendEmptyMessage(5);
        } else {
            this.mHandler.sendEmptyMessage(3);
        }
    }

    public static AVTransport getInstance(MediaRenderer render) {
        if (instance == null) {
            instance = new AVTransport(render);
        }
        return instance;
    }

    public static AVTransport getInstance() {
        return instance;
    }

    public void setTrackDuration(String duration) {
        trackDuration = duration;
        this.mHandler.sendEmptyMessage(4);
    }

    public void setPosition(String position) {
        relTime = position;
        absTime = position;
    }

    public AVTransport(MediaRenderer render) {
        setMediaRenderer(render);
        this.avTransInfoList = new AVTransportInfoList();
        initService();
        setActionListener(this);
        this.handlerThread.setPriority(10);
        this.handlerThread.start();
        this.mHandler = new Handler(this.handlerThread.getLooper()) {
            public void handleMessage(Message msg) {
                AVTransport.this.SendGENAEvent(msg.what);
            }
        };
    }

    protected void finalize() {
        this.handlerThread.quit();
    }

    public void setMediaRenderer(MediaRenderer render) {
        this.mediaRenderer = render;
    }

    public MediaRenderer getMediaRenderer() {
        return this.mediaRenderer;
    }

    public void lock() {
        this.mutex.lock();
    }

    public void unlock() {
        this.mutex.unlock();
    }

    public AVTransportInfoList getAvTransInfoList() {
        return this.avTransInfoList;
    }

    public void setCurrentAvTransInfo(AVTransportInfo avTransInfo) {
        AVTransportInfoList avTransInfoList = getAvTransInfoList();
        synchronized (avTransInfoList) {
            if (1 <= avTransInfoList.size()) {
                avTransInfoList.remove(0);
            }
            avTransInfoList.insertElementAt(avTransInfo, 0);
        }
    }

    public AVTransportInfo getCurrentAvTransInfo() {
        synchronized (this.avTransInfoList) {
            if (this.avTransInfoList.size() < 1) {
                return null;
            }
            AVTransportInfo avTransInfo = this.avTransInfoList.getAVTransportInfo(0);
            return avTransInfo;
        }
    }

    public void setNextAvTransInfo(AVTransportInfo avTransInfo) {
        synchronized (this.avTransInfoList) {
            if (2 <= this.avTransInfoList.size()) {
                this.avTransInfoList.remove(0);
            }
            this.avTransInfoList.insertElementAt(avTransInfo, 1);
        }
    }

    public AVTransportInfo getNextAvTransInfo() {
        synchronized (this.avTransInfoList) {
            if (this.avTransInfoList.size() < 2) {
                return null;
            }
            AVTransportInfo avTransInfo = this.avTransInfoList.getAVTransportInfo(1);
            return avTransInfo;
        }
    }

    public boolean actionControlReceived(Action action) {
        String actionName = action.getName();
        if (actionName == null) {
            return false;
        }
        StandardDLNAListener standardDLNAListener;
        int instanceID;
        boolean isActionSuccess = false;
        MediaRenderer dmr = getMediaRenderer();
        if (actionName.equals(AVTransportConstStr.SETAVTRANSPORTURI)) {
            Debug.message("Process Avt actionControlReceived() action: " + actionName);
            updateSetAVTransportURIRelaVariable(action);
            isActionSuccess = true;
            if (dmr != null) {
                standardDLNAListener = dmr.getStandardDLNAListener();
                if (standardDLNAListener != null) {
                    DlnaMediaModel metaData = createFromMetaData(getCurrentAvTransInfo().getURIMetaData(), getCurrentAvTransInfo().getURI());
                    standardDLNAListener.SetAVTransportURI(getCurrentAvTransInfo().getInstanceID(), getCurrentAvTransInfo().getURI(), metaData);
                }
            }
        }
        if (actionName.equals(AVTransportConstStr.SETNEXTAVTRANSPORTURI)) {
            Debug.message("Process Avt actionControlReceived() action: " + actionName);
            updateSetNextAVTransportURIRelaVariable(action);
            isActionSuccess = true;
            if (dmr != null) {
                standardDLNAListener = dmr.getStandardDLNAListener();
                if (standardDLNAListener != null) {
                    metaData = createFromMetaData(getNextAvTransInfo().getURIMetaData(), getNextAvTransInfo().getURI());
                    standardDLNAListener.SetNextAVTransportURI(getNextAvTransInfo().getInstanceID(), getNextAvTransInfo().getURI(), metaData);
                }
            }
        }
        if (actionName.equals(AVTransportConstStr.SETPLAYMODE)) {
            Debug.message("Process Avt actionControlReceived() action: " + actionName);
            instanceID = action.getArgument("InstanceID").getIntegerValue();
            String newPlayMode = action.getArgument(AVTransportConstStr.NEWPLAYMODE).getValue();
            updateSetPlayModeRelaVariable(instanceID, newPlayMode);
            isActionSuccess = true;
            if (dmr != null) {
                standardDLNAListener = dmr.getStandardDLNAListener();
                if (standardDLNAListener != null) {
                    standardDLNAListener.SetPlayMode(instanceID, newPlayMode);
                }
            }
        }
        if (actionName.equals(AVTransportConstStr.GETMEDIAINFO)) {
            Debug.message("Process Avt actionControlReceived() action: " + actionName);
            instanceID = action.getArgument("InstanceID").getIntegerValue();
            synchronized (this.avTransInfoList) {
                int avTransInfoCnt = this.avTransInfoList.size();
                for (int n = 0; n < avTransInfoCnt; n++) {
                    AVTransportInfo avTransInfo = this.avTransInfoList.getAVTransportInfo(n);
                    if (avTransInfo != null && avTransInfo.getInstanceID() == instanceID) {
                        if (trackDuration.equals("00:00:00") && this.isKugouMusic) {
                            Debug.message("actionControl kugou trackDuration");
                            trackDuration = "";
                        }
                        action.getArgument(AVTransportConstStr.NRTRACKS).setValue("1");
                        action.getArgument(AVTransportConstStr.MEDIADURATION).setValue(trackDuration);
                        action.getArgument(AVTransportConstStr.CURRENTURI).setValue(trackUri);
                        action.getArgument(AVTransportConstStr.CURRENTURIMETADATA).setValue(avTransInfo.getURIMetaData());
                        action.getArgument(AVTransportConstStr.PLAYMEDIUM).setValue("NONE");
                        action.getArgument(AVTransportConstStr.RECORDMEDIUM).setValue("NOT_IMPLEMENTED");
                        action.getArgument(AVTransportConstStr.WRITESTATUS).setValue("NOT_IMPLEMENTED");
                    }
                }
                isActionSuccess = true;
            }
        }
        if (actionName.equals(AVTransportConstStr.PLAY)) {
            Debug.message("Process Avt actionControlReceived() action: " + actionName);
            if (canPlay(transportState)) {
                instanceID = action.getArgument("InstanceID").getIntegerValue();
                String speed = action.getArgument(AVTransportConstStr.SPEED).getValue();
                updatePlayRelaVariable(instanceID, speed);
                if (!(transportState.equals("PAUSED_PLAYBACK") || transportState.equals("PLAYING"))) {
                    trackDuration = "00:00:00";
                    this.mHandler.sendEmptyMessage(0);
                    this.mHandler.sendEmptyMessage(1);
                }
                isActionSuccess = true;
                if (dmr != null) {
                    standardDLNAListener = dmr.getStandardDLNAListener();
                    if (standardDLNAListener != null) {
                        standardDLNAListener.Play(instanceID, speed);
                    }
                }
            } else {
                isActionSuccess = false;
                action.setStatus(UPnPStatus.TRANSATION_NOT_AVAILABLE);
            }
        }
        if (actionName.equals(AVTransportConstStr.NEXT)) {
            Debug.message("Process Avt actionControlReceived() action: " + actionName);
            instanceID = action.getArgument("InstanceID").getIntegerValue();
            isActionSuccess = true;
            if (dmr != null) {
                standardDLNAListener = dmr.getStandardDLNAListener();
                if (standardDLNAListener != null) {
                    standardDLNAListener.Next(instanceID);
                }
            }
        }
        if (actionName.equals(AVTransportConstStr.PREVIOUS)) {
            Debug.message("Process Avt actionControlReceived() action: " + actionName);
            instanceID = action.getArgument("InstanceID").getIntegerValue();
            isActionSuccess = true;
            if (dmr != null) {
                standardDLNAListener = dmr.getStandardDLNAListener();
                if (standardDLNAListener != null) {
                    standardDLNAListener.Previous(instanceID);
                }
            }
        }
        if (actionName.equals(AVTransportConstStr.STOP)) {
            Debug.message("Process Avt actionControlReceived() action: " + actionName);
            if (canStop(transportState)) {
                instanceID = action.getArgument("InstanceID").getIntegerValue();
                getStateVariable(AVTransportConstStr.TRANSPORTSTATE).setValue("STOPPED", false);
                isActionSuccess = true;
                if (dmr != null) {
                    standardDLNAListener = dmr.getStandardDLNAListener();
                    if (standardDLNAListener != null) {
                        standardDLNAListener.Stop(instanceID);
                    }
                }
            } else {
                isActionSuccess = true;
            }
            trackDuration = "00:00:00";
            relTime = "00:00:00";
            absTime = "00:00:00";
        }
        if (actionName.equals(AVTransportConstStr.PAUSE)) {
            Debug.message("Process Avt actionControlReceived() action: " + actionName);
            if (canPause(transportState)) {
                instanceID = action.getArgument("InstanceID").getIntegerValue();
                getStateVariable(AVTransportConstStr.TRANSPORTSTATE).setValue("PAUSED_PLAYBACK", false);
                isActionSuccess = true;
                if (dmr != null) {
                    standardDLNAListener = dmr.getStandardDLNAListener();
                    if (standardDLNAListener != null) {
                        standardDLNAListener.Pause(instanceID);
                    }
                }
            } else {
                isActionSuccess = true;
            }
        }
        if (actionName.equals(AVTransportConstStr.SEEK)) {
            Debug.message("Process Avt actionControlReceived() action: " + actionName);
            if (canSeek(transportState)) {
                instanceID = action.getArgument("InstanceID").getIntegerValue();
                String unit = action.getArgument(AVTransportConstStr.UNIT).getValue();
                String target = action.getArgument(AVTransportConstStr.TARGET).getValue();
                Debug.message("Process Avt seek unit: " + unit + " target: " + target);
                isActionSuccess = true;
                if (dmr != null) {
                    standardDLNAListener = dmr.getStandardDLNAListener();
                    if (standardDLNAListener != null) {
                        standardDLNAListener.Seek(instanceID, 0, new StringBuilder(String.valueOf(unit)).append(SearchCriteria.EQ).append(target).toString());
                    }
                }
            } else {
                isActionSuccess = false;
                action.setStatus(UPnPStatus.TRANSATION_NOT_AVAILABLE);
            }
        }
        if (actionName.equals(AVTransportConstStr.GETPOSITIONINFO)) {
            Debug.message("Process Avt actionControlReceived() action: " + actionName);
            instanceID = action.getArgumentIntegerValue("InstanceID");
            action.setArgumentValue(AVTransportConstStr.TRACK, track);
            action.setArgumentValue(AVTransportConstStr.TRACKDURATION, trackDuration);
            action.setArgumentValue(AVTransportConstStr.TRACKMETADATA, trackMetadata);
            action.setArgumentValue(AVTransportConstStr.TRACKURI, trackUri);
            action.setArgumentValue(AVTransportConstStr.RELTIME, relTime);
            action.setArgumentValue(AVTransportConstStr.ABSTIME, absTime);
            action.setArgumentValue(AVTransportConstStr.RELCOUNT, relCount);
            action.setArgumentValue(AVTransportConstStr.ABSCOUNT, absCount);
            isActionSuccess = true;
        }
        if (actionName.equals(AVTransportConstStr.GETTRANSPORTINFO)) {
            Debug.message("Process Avt actionControlReceived() action: " + actionName);
            instanceID = action.getArgumentIntegerValue("InstanceID");
            action.setArgumentValue(AVTransportConstStr.CURRENTTRANSPORTSTATE, transportState);
            action.setArgumentValue(AVTransportConstStr.CURRENTTRANSPORTSTATUS, transportStatus);
            action.setArgumentValue(AVTransportConstStr.CURRENTSPEED, transportPlaySpeed);
            isActionSuccess = true;
        }
        if (actionName.equals(AVTransportConstStr.GETDEVICECAPABILITIES)) {
            Debug.message("Process Avt actionControlReceived() action: " + actionName);
            instanceID = action.getArgumentIntegerValue("InstanceID");
            action.setArgumentValue(AVTransportConstStr.PLAYMEDIA, getStateVariable(AVTransportConstStr.POSSIBLEPLAYBACKSTORAGEMEDIA).getValue_dlna());
            action.setArgumentValue(AVTransportConstStr.RECMEDIA, "NOT_IMPLEMENTED");
            action.setArgumentValue(AVTransportConstStr.RECQUALITYMODES, "NOT_IMPLEMENTED");
            isActionSuccess = true;
        }
        if (actionName.equals(AVTransportConstStr.RECORD)) {
            Debug.message("Process Avt actionControlReceived() action: " + actionName);
            instanceID = action.getArgumentIntegerValue("InstanceID");
            isActionSuccess = true;
        }
        if (actionName.equals(AVTransportConstStr.GETTRANSPORTSETTINGS)) {
            Debug.message("Process Avt actionControlReceived() action: " + actionName);
            instanceID = action.getArgumentIntegerValue("InstanceID");
            isActionSuccess = true;
        }
        if (actionName.equals(AVTransportConstStr.SETRECORDQUALITYMODE)) {
            Debug.message("Process Avt actionControlReceived() action: " + actionName);
            instanceID = action.getArgumentIntegerValue("InstanceID");
            isActionSuccess = true;
        }
        if (actionName.equals(AVTransportConstStr.GETCURRENTTRANSPORTACTIONS)) {
            Debug.message("Process Avt actionControlReceived() action: " + actionName);
            instanceID = action.getArgumentIntegerValue("InstanceID");
            action.setArgumentValue(AVTransportConstStr.ACTIONS, currentTransportActions);
            isActionSuccess = true;
        }
        if (dmr == null) {
            return isActionSuccess;
        }
        ActionListener listener = dmr.getActionListener();
        if (listener == null) {
            return isActionSuccess;
        }
        listener.actionControlReceived(action);
        return isActionSuccess;
    }

    private DlnaMediaModel createFromMetaData(String uriMetaData, String url) {
        DlnaMediaModel mediainfo = new DlnaMediaModel();
        DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
        if (uriMetaData.contains("&") && !uriMetaData.contains("&amp;")) {
            uriMetaData = uriMetaData.replace("&", "&amp;");
        }
        try {
            Document doc = dfactory.newDocumentBuilder().parse(new ByteArrayInputStream(uriMetaData.getBytes("UTF-8")));
            mediainfo.setUrl(url);
            mediainfo.setTitle(getElementValue(doc, "dc:title"));
            mediainfo.setArtist(getElementValue(doc, "upnp:artist"));
            mediainfo.setAlbum(getElementValue(doc, "upnp:album"));
            mediainfo.setAlbumUri(getElementValue(doc, UPnP.ALBUMART_URI));
            mediainfo.setObjectClass(getElementValue(doc, "upnp:class"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mediainfo;
    }

    private static String getElementValue(Document doc, String element) {
        NodeList containers = doc.getElementsByTagName(element);
        for (int j = 0; j < containers.getLength(); j++) {
            NodeList childNodes = containers.item(j).getChildNodes();
            if (childNodes.getLength() != 0) {
                return childNodes.item(0).getNodeValue();
            }
        }
        return "";
    }

    private void updateSetAVTransportURIRelaVariable(Action action) {
        AVTransportInfo avTransInfo = new AVTransportInfo();
        avTransInfo.setInstanceID(action.getArgument("InstanceID").getIntegerValue());
        String currentUri = action.getArgument(AVTransportConstStr.CURRENTURI).getValue();
        if (currentUri.contains("pubnet.sandai.net")) {
            String uri = "s" + currentUri;
            trackUri = currentUri;
            avTransInfo.setURI(uri);
        } else {
            avTransInfo.setURI(action.getArgument(AVTransportConstStr.CURRENTURI).getValue());
        }
        String metadataUri = action.getArgument(AVTransportConstStr.CURRENTURIMETADATA).getValue();
        if (metadataUri.equals("") || metadataUri.equals("0")) {
            Debug.message("MetadataUri xunlei");
            avTransInfo.setURIMetaData("<DIDL-Lite xmlns=\"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\"xmlns:upnp=\"urn:schemas-upnp-org:metadata-1-0/upnp/\"xmlns:dlna=\"urn:schemas-dlna-org:metadata-1-0/\"><item id=\"1.flv\" parentID=\"-1\" restricted=\"1\"><dc:title>DLNA</dc:title><upnp:class>object.item.videoItem.movie</upnp:class></item></DIDL-Lite>");
        } else {
            avTransInfo.setURIMetaData(action.getArgument(AVTransportConstStr.CURRENTURIMETADATA).getValue());
        }
        this.isKugouMusic = false;
        if (metadataUri.contains("kugou")) {
            this.isKugouMusic = true;
        }
        setCurrentAvTransInfo(avTransInfo);
        getStateVariable(AVTransportConstStr.AVTRANSPORTURI).setValue(avTransInfo.getURI(), false);
        getStateVariable(AVTransportConstStr.AVTRANSPORTURIMETADATA).setValue(avTransInfo.getURIMetaData(), false);
        getStateVariable(AVTransportConstStr.CURRENTTRACK).setValue(0);
        getStateVariable(AVTransportConstStr.CURRENTTRACKDURATION).setValue("NOT_IMPLEMENTED", false);
        getStateVariable(AVTransportConstStr.CURRENTTRACKMETADATA).setValue(avTransInfo.getURIMetaData(), false);
        getStateVariable(AVTransportConstStr.CURRENTTRACKURI).setValue(avTransInfo.getURI(), false);
        if (!currentUri.contains("pubnet.sandai.net")) {
            trackUri = avTransInfo.getURI();
        }
    }

    private void updateSetNextAVTransportURIRelaVariable(Action action) {
        AVTransportInfo avTransInfo = new AVTransportInfo();
        avTransInfo.setInstanceID(action.getArgument("InstanceID").getIntegerValue());
        avTransInfo.setURI(action.getArgument(AVTransportConstStr.NEXTURI).getValue());
        avTransInfo.setURIMetaData(action.getArgument(AVTransportConstStr.NEXTURIMETADATA).getValue());
        setNextAvTransInfo(avTransInfo);
        getStateVariable(AVTransportConstStr.NEXTAVTRANSPORTURI).setValue(avTransInfo.getURI(), false);
        getStateVariable(AVTransportConstStr.NEXTAVTRANSPORTURIMETADATA).setValue(avTransInfo.getURIMetaData(), false);
    }

    private void updateSetPlayModeRelaVariable(int instanceId, String playMode) {
        getStateVariable(AVTransportConstStr.CURRENTPLAYMODE).setValue(playMode, false);
    }

    private void updatePlayRelaVariable(int instanceId, String speed) {
        getStateVariable(AVTransportConstStr.TRANSPORTPLAYSPEED).setValue(speed, false);
        getStateVariable(AVTransportConstStr.TRANSPORTSTATE).setValue("PLAYING", false);
        transportPlaySpeed = speed;
    }

    private boolean isContainer(String upnpClass) {
        return upnpClass.contains("object.container");
    }

    private boolean canPlay(String transportState) {
        Debug.message("Process Avt canPausPlay() state: " + transportState);
        if ("PLAYING".equals(transportState) || "STOPPED".equals(transportState) || "PAUSED_PLAYBACK".equals(transportState) || "NO_MEDIA_PRESENT".equals(transportState)) {
            return true;
        }
        return false;
    }

    private boolean canPause(String transportState) {
        Debug.message("Process Avt canPause() state: " + transportState);
        if ("PLAYING".equals(transportState)) {
            return true;
        }
        return false;
    }

    private boolean canStop(String transportState) {
        Debug.message("Process Avt canStop() state: " + transportState);
        if ("PAUSED_PLAYBACK".equals(transportState) || "PLAYING".equals(transportState)) {
            return true;
        }
        return false;
    }

    private boolean canSeek(String transportState) {
        Debug.message("Process Avt canSeek() state: " + transportState);
        return true;
    }

    public boolean queryControlReceived(StateVariable stateVar) {
        return false;
    }

    private void SendGENAEvent(int action) {
        String lastChangeExpected;
        Debug.message("SendGENAEvent: " + action);
        ServiceStateTable stateTable = getServiceStateTable();
        if (action == 0) {
            String resourceURI = trackUri;
            lastChangeExpected = "<Event xmlns=\"urn:schemas-upnp-org:metadata-1-0/AVT/\"><InstanceID val=\"0\"><AVTransportURI val=\"" + resourceURI + "\"/>" + "<CurrentTrackURI val=\"" + resourceURI + "\"/>" + "<TransportState val=\"STOPPED\"/>" + "<CurrentTransportActions val=\"Play\"/>" + "</InstanceID>" + "</Event>";
        } else if (action == 4) {
            lastChangeExpected = "<Event xmlns=\"urn:schemas-upnp-org:metadata-1-0/AVT/\"><InstanceID val=\"0\"><TransportState val=\"PLAYING\"/><CurrentTransportActions val=\"Stop,Pause,Seek\"/><CurrentTrackDuration val=\"" + trackDuration + "\"/>" + "<CurrentMediaDuration val=\"" + trackDuration + "\"/>" + "</InstanceID>" + "</Event>";
        } else if (action == 1) {
            lastChangeExpected = "<Event xmlns=\"urn:schemas-upnp-org:metadata-1-0/AVT/\"><InstanceID val=\"0\"><CurrentTrackDuration val=\"00:00:00\"/><CurrentMediaDuration val=\"00:00:00\"/></InstanceID></Event>";
        } else if (action == 2) {
            lastChangeExpected = "<Event xmlns=\"urn:schemas-upnp-org:metadata-1-0/AVT/\"><InstanceID val=\"0\"><TransportState val=\"PLAYING\"/><CurrentTransportActions val=\"Stop,Pause,Seek\"/></InstanceID></Event>";
        } else if (action == 3) {
            lastChangeExpected = "<Event xmlns=\"urn:schemas-upnp-org:metadata-1-0/AVT/\"><InstanceID val=\"0\"><TransportState val=\"" + transportState + "\"/>" + "<CurrentTransportActions val=\"Stop,Pause,Seek,Play\"/>" + "</InstanceID>" + "</Event>";
        } else if (action == 5) {
            lastChangeExpected = "<Event xmlns=\"urn:schemas-upnp-org:metadata-1-0/AVT/\"><InstanceID val=\"0\"><TransportState val=\"" + transportState + "\"/>" + "<CurrentTransportActions val=\"Play,Stop\"/>" + "</InstanceID>" + "</Event>";
        } else {
            return;
        }
        if (lastChangenotifyStateVariable == null) {
            int tableSize = stateTable.size();
            for (int n = 0; n < tableSize; n++) {
                StateVariable var = stateTable.getStateVariable(n);
                if (var.getStateVariableNode().getNodeValue(WebSDKConstants.PARAM_KEY_PL_NAME).compareTo("LastChange") == 0) {
                    lastChangenotifyStateVariable = var;
                    var.setValue(lastChangeExpected, false);
                    return;
                }
            }
            return;
        }
        lastChangenotifyStateVariable.setValue(lastChangeExpected, false);
    }

    public void initService() {
        setServiceType("urn:schemas-upnp-org:service:AVTransport:1");
        setServiceID(AVTransportConstStr.SERVICE_ID);
        setControlURL(AVTransportConstStr.CONTROL_URL);
        setSCPDURL(AVTransportConstStr.SCPDURL);
        setEventSubURL(AVTransportConstStr.EVENTSUB_URL);
        try {
            loadSCPD(AVTransportConstStr.SCPD);
        } catch (InvalidDescriptionException e) {
            Debug.message(e.toString());
        }
    }
}

package org.cybergarage.upnp;

import com.gala.android.dlna.sdk.stddmrcontroller.enums.RESULT_DESCRIPTION;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import org.cybergarage.http.HTTP;
import org.cybergarage.http.HTTPHeader;
import org.cybergarage.http.HTTPStatus;
import org.cybergarage.upnp.control.ActionListener;
import org.cybergarage.upnp.control.ActionRequest;
import org.cybergarage.upnp.control.ActionResponse;
import org.cybergarage.upnp.control.ControlResponse;
import org.cybergarage.upnp.xml.ActionData;
import org.cybergarage.util.Debug;
import org.cybergarage.util.Mutex;
import org.cybergarage.xml.Node;

public class Action {
    public static final String ELEM_NAME = "action";
    private static final String NAME = "name";
    private static final long TIMER_INTERVAL = 500;
    private final long MAX_ADJUST_TIME_COUNT = 100;
    private final long MIN_ADJUST_TIMNE_COUNT = 20;
    private Node actionNode;
    private ActionRequest actionRequest = null;
    private long adjustingCount = 0;
    private long differenceTime = 0;
    public Boolean isKeepAlive = null;
    private boolean isTimeoutTriggered = false;
    private RESULT_DESCRIPTION mPostActionFailedReson = null;
    private Timer mTimer = null;
    private long mTimerBeginTime;
    private TimerTask mTimerTask = null;
    private Mutex mutex = new Mutex();
    private Node serviceNode;
    private UPnPStatus upnpStatus = new UPnPStatus();
    private Object userData = null;

    class C21941 extends TimerTask {
        C21941() {
        }

        public void run() {
            long elapsedTime = System.currentTimeMillis() - Action.this.mTimerBeginTime;
            if (elapsedTime > 0) {
                if (elapsedTime < 2000 || elapsedTime >= NetworkMonitor.SUPER_BAD_RESPONSE_TIME) {
                    if (elapsedTime % NetworkMonitor.SUPER_BAD_RESPONSE_TIME < 50 || elapsedTime % NetworkMonitor.SUPER_BAD_RESPONSE_TIME > 3950) {
                        NetworkMonitor.getInstance().notifyResponseTime(NetworkMonitor.SUPER_BAD_RESPONSE_TIME);
                        Action.this.isTimeoutTriggered = true;
                    }
                } else if (!Action.this.isTimeoutTriggered) {
                    NetworkMonitor.getInstance().notifyResponseTime(2000);
                    Action.this.isTimeoutTriggered = true;
                }
            }
        }
    }

    private Node getServiceNode() {
        return this.serviceNode;
    }

    public Service getService() {
        return new Service(getServiceNode());
    }

    void setService(Service s) {
        this.serviceNode = s.getServiceNode();
        Iterator i = getArgumentList().iterator();
        while (i.hasNext()) {
            ((Argument) i.next()).setService(s);
        }
    }

    public Node getActionNode() {
        return this.actionNode;
    }

    public boolean isKeepAlive() {
        return this.isKeepAlive.booleanValue();
    }

    public void setKeepAlive(boolean keepAlive) {
        this.isKeepAlive = Boolean.valueOf(keepAlive);
    }

    public Action(Node serviceNode) {
        this.serviceNode = serviceNode;
        this.actionNode = new Node("action");
    }

    public Action(Node serviceNode, Node actionNode) {
        this.serviceNode = serviceNode;
        this.actionNode = actionNode;
    }

    public Action(Action action) {
        this.serviceNode = action.getServiceNode();
        this.actionNode = action.getActionNode();
    }

    public void lock() {
        this.mutex.lock();
    }

    public void unlock() {
        this.mutex.unlock();
    }

    public static boolean isActionNode(Node node) {
        return "action".equals(node.getName());
    }

    public void setName(String value) {
        getActionNode().setNode("name", value);
    }

    public String getName() {
        return getActionNode().getNodeValue("name");
    }

    public ArgumentList getArgumentList() {
        ArgumentList argumentList = new ArgumentList();
        Node argumentListNode = getActionNode().getNode(ArgumentList.ELEM_NAME);
        if (argumentListNode != null) {
            int nodeCnt = argumentListNode.getNNodes();
            for (int n = 0; n < nodeCnt; n++) {
                Node node = argumentListNode.getNode(n);
                if (Argument.isArgumentNode(node)) {
                    argumentList.add(new Argument(getServiceNode(), node));
                }
            }
        }
        return argumentList;
    }

    public void setArgumentList(ArgumentList al) {
        Node argumentListNode = getActionNode().getNode(ArgumentList.ELEM_NAME);
        if (argumentListNode == null) {
            argumentListNode = new Node(ArgumentList.ELEM_NAME);
            getActionNode().addNode(argumentListNode);
        } else {
            argumentListNode.removeAllNodes();
        }
        Iterator i = al.iterator();
        while (i.hasNext()) {
            Argument a = (Argument) i.next();
            a.setService(getService());
            argumentListNode.addNode(a.getArgumentNode());
        }
    }

    public ArgumentList getInputArgumentList() {
        ArgumentList allArgList = getArgumentList();
        int allArgCnt = allArgList.size();
        ArgumentList argList = new ArgumentList();
        for (int n = 0; n < allArgCnt; n++) {
            Argument arg = allArgList.getArgument(n);
            if (arg.isInDirection()) {
                argList.add(arg);
            }
        }
        return argList;
    }

    public ArgumentList getOutputArgumentList() {
        ArgumentList allArgList = getArgumentList();
        int allArgCnt = allArgList.size();
        ArgumentList argList = new ArgumentList();
        for (int n = 0; n < allArgCnt; n++) {
            Argument arg = allArgList.getArgument(n);
            if (arg.isOutDirection()) {
                argList.add(arg);
            }
        }
        return argList;
    }

    public Argument getArgument(String name) {
        ArgumentList argList = getArgumentList();
        int nArgs = argList.size();
        for (int n = 0; n < nArgs; n++) {
            Argument arg = argList.getArgument(n);
            String argName = arg.getName();
            if (argName != null && name.equals(argName)) {
                return arg;
            }
        }
        return null;
    }

    public void setArgumentValues(ArgumentList argList) {
        getArgumentList().set(argList);
    }

    public void setInArgumentValues(ArgumentList argList) {
        getArgumentList().setReqArgs(argList);
    }

    public void setOutArgumentValues(ArgumentList argList) {
        getArgumentList().setResArgs(argList);
    }

    public void setArgumentValue(String name, String value) {
        Argument arg = getArgument(name);
        if (arg != null) {
            arg.setValue(value);
        }
    }

    public void setArgumentValue(String name, int value) {
        setArgumentValue(name, Integer.toString(value));
    }

    private void clearOutputAgumentValues() {
        ArgumentList allArgList = getArgumentList();
        int allArgCnt = allArgList.size();
        for (int n = 0; n < allArgCnt; n++) {
            Argument arg = allArgList.getArgument(n);
            if (arg.isOutDirection()) {
                arg.setValue("");
            }
        }
    }

    public String getArgumentValue(String name) {
        Argument arg = getArgument(name);
        if (arg == null) {
            return "";
        }
        return arg.getValue();
    }

    public int getArgumentIntegerValue(String name) {
        Argument arg = getArgument(name);
        if (arg == null) {
            return 0;
        }
        return arg.getIntegerValue();
    }

    private ActionData getActionData() {
        Node node = getActionNode();
        ActionData userData = (ActionData) node.getUserData();
        if (userData != null) {
            return userData;
        }
        userData = new ActionData();
        node.setUserData(userData);
        userData.setNode(node);
        return userData;
    }

    public ActionListener getActionListener() {
        return getActionData().getActionListener();
    }

    public void setActionListener(ActionListener listener) {
        getActionData().setActionListener(listener);
    }

    public boolean performActionListener(ActionRequest actionReq) {
        ActionListener listener = getActionListener();
        if (listener == null) {
            return false;
        }
        String replyValue;
        ActionResponse actionRes = new ActionResponse();
        setStatus(UPnPStatus.INVALID_ACTION);
        clearOutputAgumentValues();
        long dmrTimes = System.currentTimeMillis();
        HTTPHeader replyHeader = actionReq.getHeader(HTTP.REPLY);
        HTTPHeader maxDelayHeader = actionReq.getHeader(HTTP.MAXDELAYTIME);
        if (maxDelayHeader != null) {
            HTTPHeader dmcTimeHeader = actionReq.getHeader(HTTP.DMCTIME);
            HTTPHeader diffTimeHeader = actionReq.getHeader(HTTP.DIFFTIME);
            if (!(dmcTimeHeader == null || diffTimeHeader == null)) {
                String dmcTimeValue = dmcTimeHeader.getValue();
                String diffTimeValue = diffTimeHeader.getValue();
                String maxDelayTimeValue = maxDelayHeader.getValue();
                if (!(dmcTimeValue == null || diffTimeValue == null || maxDelayTimeValue == null)) {
                    long dmcTimes = Long.parseLong(dmcTimeValue);
                    long diffTimes = Long.parseLong(diffTimeValue);
                    long maxDelayTimes = Long.parseLong(maxDelayTimeValue);
                    Debug.message("dmc:" + dmcTimes);
                    Debug.message("diffTimes:" + diffTimes);
                    Debug.message("maxDelayTimes:" + maxDelayTimes);
                    long absDelayTime = Math.abs((dmrTimes - dmcTimes) - diffTimes);
                    if (absDelayTime > maxDelayTimes) {
                        Debug.message("delay times is:" + absDelayTime);
                        replyValue = "";
                        if (replyHeader != null) {
                            replyValue = replyHeader.getValue();
                        }
                        if (replyValue.compareTo("1") == 0) {
                            Debug.message("give up message to DMR!but reply to dmc!");
                            actionRes.setResponse(this);
                            return actionReq.post(actionRes);
                        }
                        Debug.message("give up the message!!");
                        return true;
                    }
                    Debug.message("delay times is:" + absDelayTime);
                }
            }
        }
        if (listener.actionControlReceived(this)) {
            if (replyHeader != null) {
                replyValue = replyHeader.getValue();
                if (replyValue != null && replyValue.compareTo("0") == 0) {
                    Debug.message("DMR不需要回复消息");
                    return true;
                }
            }
            actionRes.setResponse(this);
        } else {
            UPnPStatus upnpStatus = getStatus();
            actionRes.setFaultResponse(upnpStatus.getCode(), upnpStatus.getDescription());
        }
        if (Debug.isOn()) {
            actionRes.print();
        }
        return actionReq.post(actionRes);
    }

    private ControlResponse getControlResponse() {
        return getActionData().getControlResponse();
    }

    private void setControlResponse(ControlResponse res) {
        getActionData().setControlResponse(res);
    }

    public UPnPStatus getControlStatus() {
        return getControlResponse().getUPnPError();
    }

    private void setRealTimeStrategy(ActionRequest ctrlReq) {
        if (ControlPoint.isOpenRealTime && this.adjustingCount >= 20 && ctrlReq != null) {
            ctrlReq.setHeader(HTTP.MAXDELAYTIME, ControlPoint.maxDelayTime);
            ctrlReq.setHeader(HTTP.DMCTIME, System.currentTimeMillis());
            ctrlReq.setHeader(HTTP.DIFFTIME, this.differenceTime);
        }
    }

    public RESULT_DESCRIPTION getPostActionFailedReason() {
        return this.mPostActionFailedReson;
    }

    public void setPostFailedReason(RESULT_DESCRIPTION reason) {
        this.mPostActionFailedReson = reason;
    }

    public void setPostFailedReasonByErrorCode(int errorCode) {
        switch (errorCode) {
            case 100:
                this.mPostActionFailedReson = RESULT_DESCRIPTION.FAIL_POST_ACTION_BY_ERROR_100;
                return;
            case 400:
                this.mPostActionFailedReson = RESULT_DESCRIPTION.FAIL_POST_ACTION_BY_ERROR_400;
                return;
            case 404:
                this.mPostActionFailedReson = RESULT_DESCRIPTION.FAIL_POST_ACTION_BY_ERROR_404;
                return;
            case 412:
                this.mPostActionFailedReson = RESULT_DESCRIPTION.FAIL_POST_ACTION_BY_ERROR_412;
                return;
            case HTTPStatus.INVALID_RANGE /*416*/:
                this.mPostActionFailedReson = RESULT_DESCRIPTION.FAIL_POST_ACTION_BY_ERROR_416;
                return;
            case 500:
                this.mPostActionFailedReson = RESULT_DESCRIPTION.FAIL_POST_ACTION_BY_ERROR_500;
                return;
            default:
                this.mPostActionFailedReson = RESULT_DESCRIPTION.FAIL_POST_ACTION_BY_ERROR_UNKOWN;
                return;
        }
    }

    public boolean postControlAction() {
        setPostFailedReason(RESULT_DESCRIPTION.FAIL_POST_ACTION_BY_ERROR_UNKOWN);
        Debug.message("do postControlAction()");
        if (this.isKeepAlive == null) {
            this.isKeepAlive = Boolean.valueOf(false);
        }
        ArgumentList actionArgList = getArgumentList();
        ArgumentList actionInputArgList = getInputArgumentList();
        ActionRequest ctrlReq = getActionRequest();
        ctrlReq.setHeader(HTTP.REPLY, "1");
        ctrlReq.setRequest(this, actionInputArgList);
        if (Debug.isOn()) {
            ctrlReq.print();
        }
        setRealTimeStrategy(ctrlReq);
        long beginsendTime = System.currentTimeMillis();
        resetTimer();
        ActionResponse ctrlRes = ctrlReq.post(true, this.isKeepAlive.booleanValue());
        if (Debug.isOn()) {
            ctrlRes.print();
        }
        setControlResponse(ctrlRes);
        setStatus(ctrlRes.getStatusCode());
        long receiveTime = System.currentTimeMillis();
        long responeTime = receiveTime - beginsendTime;
        if (!this.isTimeoutTriggered) {
            NetworkMonitor.getInstance().notifyResponseTime(responeTime);
        }
        stopTimer();
        Debug.message("responseTime:" + responeTime);
        if (ctrlRes.isSuccessful()) {
            HTTPHeader dmrTimeHeader = ctrlRes.getHeader(HTTP.DMRTIME);
            if (dmrTimeHeader != null && responeTime < 1000) {
                String dmrTimeStr = dmrTimeHeader.getValue();
                if (dmrTimeStr != null) {
                    long dmrTime = Long.parseLong(dmrTimeStr);
                    long sendTime = (long) (0.5d * ((double) responeTime));
                    long tmptime = (dmrTime - beginsendTime) - sendTime;
                    long tmptime1 = (receiveTime - dmrTime) - sendTime;
                    Debug.message("dmrTime:" + dmrTime);
                    Debug.message("sendTime:" + sendTime);
                    Debug.message("tmpTime:" + tmptime);
                    if (Math.abs(Math.abs(tmptime) - Math.abs(tmptime1)) < 100) {
                        if (this.differenceTime == 0) {
                            this.adjustingCount = 1;
                            this.differenceTime = tmptime;
                        } else if (this.adjustingCount < 100) {
                            if (this.differenceTime > tmptime) {
                                this.differenceTime = tmptime;
                            }
                            this.adjustingCount++;
                        }
                        Debug.message("DMC diff DMR Time is:" + this.differenceTime);
                    }
                }
            }
            try {
                actionArgList.setResArgs(ctrlRes.getResponse());
                return true;
            } catch (IllegalArgumentException e) {
                setPostFailedReason(RESULT_DESCRIPTION.FAIL_BAD_RESOPNSE_PARAMETER);
                setStatus(402, "Action succesfully delivered but invalid arguments returned.");
                return false;
            }
        }
        setPostFailedReasonByErrorCode(ctrlRes.getStatusCode());
        return false;
    }

    private void stopTimer() {
        if (this.mTimer != null) {
            if (this.mTimerTask != null) {
                this.mTimerTask.cancel();
                this.mTimerTask = null;
            }
            this.mTimer.cancel();
            this.mTimer = null;
        }
    }

    private void resetTimer() {
        stopTimer();
        this.isTimeoutTriggered = false;
        this.mTimer = new Timer();
        this.mTimerTask = new C21941();
        this.mTimerBeginTime = System.currentTimeMillis();
        this.mTimer.schedule(this.mTimerTask, TIMER_INTERVAL, TIMER_INTERVAL);
    }

    public ActionRequest getActionRequest() {
        if (this.actionRequest == null) {
            this.actionRequest = new ActionRequest();
            this.actionRequest.setVersion("1.1");
        }
        return this.actionRequest;
    }

    public boolean postControlActionNoReply() {
        if (this.isKeepAlive == null) {
            this.isKeepAlive = Boolean.valueOf(false);
        }
        ArgumentList actionInputArgList = getInputArgumentList();
        ActionRequest ctrlReq = getActionRequest();
        ctrlReq.setHeader(HTTP.REPLY, "0");
        ctrlReq.setRequest(this, actionInputArgList);
        if (Debug.isOn()) {
            ctrlReq.print();
        }
        setRealTimeStrategy(ctrlReq);
        if (ctrlReq.post(false, this.isKeepAlive.booleanValue()) == null) {
            return false;
        }
        return true;
    }

    public void print() {
        System.out.println("Action : " + getName());
        ArgumentList argList = getArgumentList();
        int nArgs = argList.size();
        for (int n = 0; n < nArgs; n++) {
            Argument arg = argList.getArgument(n);
            String name = arg.getName();
            System.out.println(" [" + n + "] = " + arg.getDirection() + ", " + name + ", " + arg.getValue());
        }
    }

    public void setStatus(int code, String descr) {
        this.upnpStatus.setCode(code);
        this.upnpStatus.setDescription(descr);
    }

    public void setStatus(int code) {
        setStatus(code, UPnPStatus.code2String(code));
    }

    public UPnPStatus getStatus() {
        return this.upnpStatus;
    }

    public void setUserData(Object data) {
        this.userData = data;
    }

    public Object getUserData() {
        return this.userData;
    }
}

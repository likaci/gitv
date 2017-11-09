package org.cybergarage.upnp.control;

import org.cybergarage.http.HTTPRequest;
import org.cybergarage.soap.SOAP;
import org.cybergarage.soap.SOAPResponse;
import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.Argument;
import org.cybergarage.upnp.ArgumentList;
import org.cybergarage.upnp.Service;
import org.cybergarage.util.Debug;
import org.cybergarage.util.Mutex;
import org.cybergarage.xml.Node;

public class ActionRequest extends ControlRequest {
    private Mutex mutex = new Mutex();

    public ActionRequest(HTTPRequest httpReq) {
        set(httpReq);
    }

    public Node getActionNode() {
        Node bodyNode = getBodyNode();
        if (bodyNode != null && bodyNode.hasNodes()) {
            return bodyNode.getNode(0);
        }
        return null;
    }

    public void lock() {
        this.mutex.lock();
    }

    public void unlock() {
        this.mutex.unlock();
    }

    public String getActionName() {
        Node node = getActionNode();
        if (node == null) {
            return "";
        }
        String name = node.getName();
        if (name == null) {
            return "";
        }
        int idx = name.indexOf(SOAP.DELIM) + 1;
        if (idx < 0) {
            return "";
        }
        return name.substring(idx, name.length());
    }

    public ArgumentList getArgumentList() {
        Node actNode = getActionNode();
        if (actNode == null) {
            return null;
        }
        int nArgNodes = actNode.getNNodes();
        ArgumentList argList = new ArgumentList();
        for (int n = 0; n < nArgNodes; n++) {
            Argument arg = new Argument();
            Node argNode = actNode.getNode(n);
            arg.setName(argNode.getName());
            arg.setValue(argNode.getValue());
            argList.add(arg);
        }
        return argList;
    }

    public void setRequest(Action action, ArgumentList argList) {
        Service service = action.getService();
        setRequestHost(service);
        setEnvelopeNode(SOAP.createEnvelopeBodyNode());
        Node envNode = getEnvelopeNode();
        getBodyNode().addNode(createContentNode(service, action, argList));
        setContent(envNode);
        String serviceType = service.getServiceType();
        setSOAPAction("\"" + serviceType + "#" + action.getName() + "\"");
    }

    private Node createContentNode(Service service, Action action, ArgumentList argList) {
        String actionName = action.getName();
        String serviceType = service.getServiceType();
        Node actionNode = new Node();
        actionNode.setName("u", actionName);
        actionNode.setNameSpace("u", serviceType);
        int argListCnt = argList.size();
        for (int n = 0; n < argListCnt; n++) {
            Argument arg = argList.getArgument(n);
            Node argNode = new Node();
            argNode.setName(arg.getName());
            argNode.setValue(arg.getValue());
            actionNode.addNode(argNode);
        }
        return actionNode;
    }

    public ActionResponse post(boolean isNeedReply, boolean isKeepAlive) {
        if (isNeedReply) {
            SOAPResponse soapRes = postMessage(getRequestHost(), getRequestPort(), isNeedReply, isKeepAlive);
            Debug.message("++++++++postMessage need reply Host =" + getRequestHost() + "; Port =" + getRequestPort() + "; isNeedReply =" + isNeedReply);
            return new ActionResponse(soapRes);
        } else if (postMessage(getRequestHost(), getRequestPort(), isNeedReply, isKeepAlive) == null) {
            return null;
        } else {
            Debug.message("++++++++postMessage no need reply host =" + getRequestHost() + "Port" + getRequestPort());
            return new ActionResponse();
        }
    }
}

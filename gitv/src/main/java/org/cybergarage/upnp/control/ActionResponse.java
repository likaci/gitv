package org.cybergarage.upnp.control;

import org.cybergarage.http.HTTP;
import org.cybergarage.soap.SOAP;
import org.cybergarage.soap.SOAPResponse;
import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.Argument;
import org.cybergarage.upnp.ArgumentList;
import org.cybergarage.upnp.Service;
import org.cybergarage.xml.Node;

public class ActionResponse extends ControlResponse {
    public ActionResponse() {
        setHeader(HTTP.EXT, "");
    }

    public ActionResponse(SOAPResponse soapRes) {
        super(soapRes);
        setHeader(HTTP.EXT, "");
    }

    public void setResponse(Action action) {
        setStatusCode(200);
        getBodyNode().addNode(createResponseNode(action));
        setContent(getEnvelopeNode());
    }

    private Node createResponseNode(Action action) {
        Node actionNameResNode = new Node("u:" + action.getName() + SOAP.RESPONSE);
        Service service = action.getService();
        if (service != null) {
            actionNameResNode.setAttribute("xmlns:u", service.getServiceType());
        }
        ArgumentList argList = action.getArgumentList();
        int nArgs = argList.size();
        for (int n = 0; n < nArgs; n++) {
            Argument arg = argList.getArgument(n);
            if (arg.isOutDirection()) {
                Node argNode = new Node();
                argNode.setName(arg.getName());
                argNode.setValue(arg.getValue());
                actionNameResNode.addNode(argNode);
            }
        }
        return actionNameResNode;
    }

    private Node getActionResponseNode() {
        Node bodyNode = getBodyNode();
        if (bodyNode == null || !bodyNode.hasNodes()) {
            return null;
        }
        return bodyNode.getNode(0);
    }

    public ArgumentList getResponse() {
        ArgumentList argList = new ArgumentList();
        Node resNode = getActionResponseNode();
        if (resNode != null) {
            int nArgs = resNode.getNNodes();
            for (int n = 0; n < nArgs; n++) {
                Node node = resNode.getNode(n);
                argList.add(new Argument(node.getName(), node.getValue()));
            }
        }
        return argList;
    }
}

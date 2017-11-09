package org.cybergarage.upnp;

import java.util.Iterator;
import org.cybergarage.upnp.control.QueryListener;
import org.cybergarage.upnp.control.QueryRequest;
import org.cybergarage.upnp.control.QueryResponse;
import org.cybergarage.upnp.xml.NodeData;
import org.cybergarage.upnp.xml.StateVariableData;
import org.cybergarage.util.Debug;
import org.cybergarage.xml.Node;

public class StateVariable extends NodeData {
    private static final String DATATYPE = "dataType";
    private static final String DEFAULT_VALUE = "defaultValue";
    public static final String ELEM_NAME = "stateVariable";
    private static final String NAME = "name";
    private static final String SENDEVENTS = "sendEvents";
    private static final String SENDEVENTS_NO = "no";
    private static final String SENDEVENTS_YES = "yes";
    private Node serviceNode;
    private Node stateVariableNode;
    private UPnPStatus upnpStatus;
    private Object userData;

    public Node getServiceNode() {
        return this.serviceNode;
    }

    void setServiceNode(Node n) {
        this.serviceNode = n;
    }

    public Service getService() {
        Node serviceNode = getServiceNode();
        if (serviceNode == null) {
            return null;
        }
        return new Service(serviceNode);
    }

    public Node getStateVariableNode() {
        return this.stateVariableNode;
    }

    public StateVariable() {
        this.upnpStatus = new UPnPStatus();
        this.userData = null;
        this.serviceNode = null;
        this.stateVariableNode = new Node(ELEM_NAME);
    }

    public StateVariable(Node serviceNode, Node stateVarNode) {
        this.upnpStatus = new UPnPStatus();
        this.userData = null;
        this.serviceNode = serviceNode;
        this.stateVariableNode = stateVarNode;
    }

    public static boolean isStateVariableNode(Node node) {
        return ELEM_NAME.equals(node.getName());
    }

    public void setName(String value) {
        getStateVariableNode().setNode("name", value);
    }

    public String getName() {
        return getStateVariableNode().getNodeValue("name");
    }

    public void setDataType(String value) {
        getStateVariableNode().setNode(DATATYPE, value);
    }

    public String getDataType() {
        return getStateVariableNode().getNodeValue(DATATYPE);
    }

    public void setSendEvents(boolean state) {
        getStateVariableNode().setAttribute(SENDEVENTS, state ? SENDEVENTS_YES : SENDEVENTS_NO);
    }

    public boolean isSendEvents() {
        String state = getStateVariableNode().getAttributeValue(SENDEVENTS);
        if (state != null && state.equalsIgnoreCase(SENDEVENTS_YES)) {
            return true;
        }
        return false;
    }

    public void set(StateVariable stateVar, boolean external) {
        setName(stateVar.getName());
        setValue(stateVar.getValue_dlna(), external);
        setDataType(stateVar.getDataType());
        setSendEvents(stateVar.isSendEvents());
    }

    public StateVariableData getStateVariableData() {
        Node node = getStateVariableNode();
        StateVariableData userData = (StateVariableData) node.getUserData();
        if (userData != null) {
            return userData;
        }
        userData = new StateVariableData();
        node.setUserData(userData);
        userData.setNode(node);
        return userData;
    }

    public void setValue(String value, boolean external) {
        String currValue;
        if (external) {
            currValue = getStateVariableData().getValue_ext();
        } else {
            currValue = getStateVariableData().getValue();
        }
        if (value != null) {
            if (external) {
                getStateVariableData().setValue_ext(value);
            } else {
                getStateVariableData().setValue(value);
            }
            Service service = getService();
            if (service != null && isSendEvents()) {
                service.notify(this, external);
            }
        }
    }

    public void setValue(int value) {
        setValue(Integer.toString(value), false);
    }

    public void setValue(long value) {
        setValue(Long.toString(value), false);
    }

    public String getValue_dlna() {
        return getStateVariableData().getValue();
    }

    public void setValue_ext(long value) {
        setValue(Long.toString(value), true);
    }

    public String getValue_tvguo() {
        return getStateVariableData().getValue_ext();
    }

    public AllowedValueList getAllowedValueList() {
        AllowedValueList valueList = new AllowedValueList();
        Node valueListNode = getStateVariableNode().getNode(AllowedValueList.ELEM_NAME);
        if (valueListNode == null) {
            return null;
        }
        int nNode = valueListNode.getNNodes();
        for (int n = 0; n < nNode; n++) {
            Node node = valueListNode.getNode(n);
            if (AllowedValue.isAllowedValueNode(node)) {
                valueList.add(new AllowedValue(node));
            }
        }
        return valueList;
    }

    public void setAllowedValueList(AllowedValueList avl) {
        getStateVariableNode().removeNode(AllowedValueList.ELEM_NAME);
        getStateVariableNode().removeNode(AllowedValueRange.ELEM_NAME);
        Node n = new Node(AllowedValueList.ELEM_NAME);
        Iterator i = avl.iterator();
        while (i.hasNext()) {
            n.addNode(((AllowedValue) i.next()).getAllowedValueNode());
        }
        getStateVariableNode().addNode(n);
    }

    public boolean hasAllowedValueList() {
        return getAllowedValueList() != null;
    }

    public AllowedValueRange getAllowedValueRange() {
        Node valueRangeNode = getStateVariableNode().getNode(AllowedValueRange.ELEM_NAME);
        if (valueRangeNode == null) {
            return null;
        }
        return new AllowedValueRange(valueRangeNode);
    }

    public void setAllowedValueRange(AllowedValueRange avr) {
        getStateVariableNode().removeNode(AllowedValueList.ELEM_NAME);
        getStateVariableNode().removeNode(AllowedValueRange.ELEM_NAME);
        getStateVariableNode().addNode(avr.getAllowedValueRangeNode());
    }

    public boolean hasAllowedValueRange() {
        return getAllowedValueRange() != null;
    }

    public QueryListener getQueryListener() {
        return getStateVariableData().getQueryListener();
    }

    public void setQueryListener(QueryListener listener) {
        getStateVariableData().setQueryListener(listener);
    }

    public boolean performQueryListener(QueryRequest queryReq, boolean external) {
        QueryListener listener = getQueryListener();
        if (listener == null) {
            return false;
        }
        QueryResponse queryRes = new QueryResponse();
        StateVariable retVar = new StateVariable();
        retVar.set(this, external);
        retVar.setValue("", false);
        retVar.setStatus(404);
        if (listener.queryControlReceived(retVar)) {
            queryRes.setResponse(retVar);
        } else {
            UPnPStatus upnpStatus = retVar.getStatus();
            queryRes.setFaultResponse(upnpStatus.getCode(), upnpStatus.getDescription());
        }
        queryReq.post(queryRes);
        return true;
    }

    public QueryResponse getQueryResponse() {
        return getStateVariableData().getQueryResponse();
    }

    private void setQueryResponse(QueryResponse res) {
        getStateVariableData().setQueryResponse(res);
    }

    public UPnPStatus getQueryStatus() {
        return getQueryResponse().getUPnPError();
    }

    public boolean postQuerylAction(boolean isKeepAlive) {
        QueryRequest queryReq = new QueryRequest();
        queryReq.setRequest(this);
        if (Debug.isOn()) {
            queryReq.print();
        }
        QueryResponse queryRes = queryReq.post(isKeepAlive);
        if (Debug.isOn()) {
            queryRes.print();
        }
        setQueryResponse(queryRes);
        if (queryRes.isSuccessful()) {
            setValue(queryRes.getReturnValue(), false);
            return true;
        }
        setValue(queryRes.getReturnValue(), false);
        return false;
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

    public String getDefaultValue() {
        return getStateVariableNode().getNodeValue(DEFAULT_VALUE);
    }

    public void setDefaultValue(String value) {
        getStateVariableNode().setNode(DEFAULT_VALUE, value);
    }

    public void setUserData(Object data) {
        this.userData = data;
    }

    public Object getUserData() {
        return this.userData;
    }
}

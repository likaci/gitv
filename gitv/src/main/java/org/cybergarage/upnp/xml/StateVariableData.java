package org.cybergarage.upnp.xml;

import org.cybergarage.upnp.control.QueryListener;
import org.cybergarage.upnp.control.QueryResponse;

public class StateVariableData extends NodeData {
    private QueryListener queryListener = null;
    private QueryResponse queryRes = null;
    private String value = "";
    private String value_ext = "";

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue_ext() {
        return this.value_ext;
    }

    public void setValue_ext(String value) {
        this.value_ext = value;
    }

    public QueryListener getQueryListener() {
        return this.queryListener;
    }

    public void setQueryListener(QueryListener queryListener) {
        this.queryListener = queryListener;
    }

    public QueryResponse getQueryResponse() {
        return this.queryRes;
    }

    public void setQueryResponse(QueryResponse res) {
        this.queryRes = res;
    }
}

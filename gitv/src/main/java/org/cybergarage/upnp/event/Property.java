package org.cybergarage.upnp.event;

public class Property {
    private String name = "";
    private String value = "";

    public String getName() {
        return this.name;
    }

    public void setName(String val) {
        if (val == null) {
            val = "";
        }
        this.name = val;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String val) {
        if (val == null) {
            val = "";
        }
        this.value = val;
    }
}

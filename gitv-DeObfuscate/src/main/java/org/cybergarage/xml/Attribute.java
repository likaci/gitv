package org.cybergarage.xml;

public class Attribute {
    private String name = new String();
    private String value = new String();

    public Attribute(String name, String value) {
        setName(name);
        setValue(value);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}

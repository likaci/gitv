package retrofit.client;

public final class Header {
    private final String name;
    private final String value;

    public Header(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Header header = (Header) o;
        if (this.name == null ? header.name != null : !this.name.equals(header.name)) {
            return false;
        }
        if (this.value != null) {
            if (this.value.equals(header.value)) {
                return true;
            }
        } else if (header.value == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result;
        int i = 0;
        if (this.name != null) {
            result = this.name.hashCode();
        } else {
            result = 0;
        }
        int i2 = result * 31;
        if (this.value != null) {
            i = this.value.hashCode();
        }
        return i2 + i;
    }

    public String toString() {
        return (this.name != null ? this.name : "") + ": " + (this.value != null ? this.value : "");
    }
}

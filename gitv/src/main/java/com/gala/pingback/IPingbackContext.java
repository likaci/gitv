package com.gala.pingback;

public interface IPingbackContext {
    PingbackItem getItem(String str);

    void setItem(String str, PingbackItem pingbackItem);

    void setPingbackValueProvider(IPingbackValueProvider iPingbackValueProvider);
}

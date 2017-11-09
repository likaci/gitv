package com.gala.pingback;

public interface IPingback {
    IPingback addItem(PingbackItem pingbackItem);

    void post();
}

package com.push.mqttv3.internal.wire;

import com.push.mqttv3.MqttException;
import java.io.IOException;

public class MqttPingResp extends MqttAck {
    public MqttPingResp(byte info, byte[] variableHeader) throws IOException {
        super((byte) 13);
    }

    protected byte[] getVariableHeader() throws MqttException {
        return new byte[0];
    }

    public boolean isMessageIdRequired() {
        return false;
    }
}

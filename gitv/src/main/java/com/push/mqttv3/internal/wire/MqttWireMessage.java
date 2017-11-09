package com.push.mqttv3.internal.wire;

import com.push.mqttv3.MqttException;
import com.push.mqttv3.MqttPersistable;
import com.push.mqttv3.internal.ExceptionHelper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.xbill.DNS.WKSRecord.Service;

public abstract class MqttWireMessage {
    public static final byte MESSAGE_TYPE_CONNACK = (byte) 2;
    public static final byte MESSAGE_TYPE_CONNECT = (byte) 1;
    public static final byte MESSAGE_TYPE_DISCONNECT = (byte) 14;
    public static final byte MESSAGE_TYPE_PINGREQ = (byte) 12;
    public static final byte MESSAGE_TYPE_PINGRESP = (byte) 13;
    public static final byte MESSAGE_TYPE_PUBACK = (byte) 4;
    public static final byte MESSAGE_TYPE_PUBCOMP = (byte) 7;
    public static final byte MESSAGE_TYPE_PUBLISH = (byte) 3;
    public static final byte MESSAGE_TYPE_PUBREC = (byte) 5;
    public static final byte MESSAGE_TYPE_PUBREL = (byte) 6;
    public static final byte MESSAGE_TYPE_SUBACK = (byte) 9;
    public static final byte MESSAGE_TYPE_SUBSCRIBE = (byte) 8;
    public static final byte MESSAGE_TYPE_UNSUBACK = (byte) 11;
    public static final byte MESSAGE_TYPE_UNSUBSCRIBE = (byte) 10;
    protected static final String STRING_ENCODING = "UTF-8";
    protected boolean duplicate = false;
    private byte[] encodedHeader = null;
    protected long msgId;
    private byte type;

    protected abstract byte getMessageInfo();

    protected abstract byte[] getVariableHeader() throws MqttException;

    public MqttWireMessage(byte type) {
        this.type = type;
        this.msgId = 0;
    }

    public byte[] getPayload() throws MqttException {
        return new byte[0];
    }

    public byte getType() {
        return this.type;
    }

    public long getMessageId() {
        return this.msgId;
    }

    public void setMessageId(long msgId) {
        this.msgId = msgId;
    }

    public byte[] getHeader() throws MqttException {
        if (this.encodedHeader == null) {
            try {
                int first = ((getType() & 15) << 4) ^ (getMessageInfo() & 15);
                byte[] varHeader = getVariableHeader();
                int remLen = varHeader.length + getPayload().length;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(baos);
                dos.writeByte(first);
                dos.write(encodeMBI((long) remLen));
                dos.write(varHeader);
                dos.flush();
                this.encodedHeader = baos.toByteArray();
            } catch (Throwable ioe) {
                throw new MqttException(ioe);
            }
        }
        return this.encodedHeader;
    }

    public boolean isMessageIdRequired() {
        return true;
    }

    public static MqttWireMessage createWireMessage(MqttPersistable data) throws MqttException {
        byte[] payload = data.getPayloadBytes();
        if (payload == null) {
            payload = new byte[0];
        }
        return createWireMessage(new MultiByteArrayInputStream(data.getHeaderBytes(), data.getHeaderOffset(), data.getHeaderLength(), payload, data.getPayloadOffset(), data.getPayloadLength()));
    }

    public static MqttWireMessage createWireMessage(byte[] bytes) throws MqttException {
        return createWireMessage(new ByteArrayInputStream(bytes));
    }

    private static MqttWireMessage createWireMessage(InputStream inputStream) throws MqttException {
        try {
            CountingInputStream counter = new CountingInputStream(inputStream);
            DataInputStream in = new DataInputStream(counter);
            int first = in.readUnsignedByte();
            byte type = (byte) (first >> 4);
            byte info = (byte) (first & 15);
            long remainder = (((long) counter.getCounter()) + readMBI(in).getValue()) - ((long) counter.getCounter());
            byte[] data = new byte[0];
            if (remainder > 0) {
                data = new byte[((int) remainder)];
                in.readFully(data, 0, data.length);
            }
            if (type == (byte) 3) {
                return new MqttPublish(info, data);
            }
            if (type == (byte) 4) {
                return new MqttPubAck(info, data);
            }
            if (type == (byte) 7) {
                return new MqttPubComp(info, data);
            }
            if (type == (byte) 2) {
                return new MqttConnack(info, data);
            }
            if (type == (byte) 13) {
                return new MqttPingResp(info, data);
            }
            if (type == (byte) 9) {
                return new MqttSuback(info, data);
            }
            if (type == (byte) 11) {
                return new MqttUnsubAck(info, data);
            }
            if (type == (byte) 6) {
                return new MqttPubRel(info, data);
            }
            if (type == (byte) 5) {
                return new MqttPubRec(info, data);
            }
            throw ExceptionHelper.createMqttException(6);
        } catch (Throwable io) {
            throw new MqttException(io);
        }
    }

    protected static byte[] encodeMBI(long number) {
        int numBytes = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        do {
            byte digit = (byte) ((int) (number % 128));
            number /= 128;
            if (number > 0) {
                digit = (byte) (digit | 128);
            }
            bos.write(digit);
            numBytes++;
            if (number <= 0) {
                break;
            }
        } while (numBytes < 4);
        return bos.toByteArray();
    }

    protected static MultiByteInteger readMBI(DataInputStream in) throws IOException {
        long msgLength = 0;
        int multiplier = 1;
        int count = 0;
        byte digit;
        do {
            digit = in.readByte();
            count++;
            msgLength += (long) ((digit & Service.LOCUS_CON) * multiplier);
            multiplier *= 128;
        } while ((digit & 128) != 0);
        return new MultiByteInteger(msgLength, count);
    }

    protected byte[] encodeMessageId() throws MqttException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeLong(this.msgId);
            dos.flush();
            return baos.toByteArray();
        } catch (Throwable ex) {
            throw new MqttException(ex);
        }
    }

    public boolean isRetryable() {
        return false;
    }
}

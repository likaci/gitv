package org.cybergarage.http;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Calendar;
import org.cybergarage.util.Debug;

public class HTTPSocket {
    private InputStream sockIn = null;
    private OutputStream sockOut = null;
    private Socket socket = null;

    public HTTPSocket(Socket socket) {
        setSocket(socket);
        open();
    }

    public HTTPSocket(HTTPSocket socket) {
        if (socket != null) {
            setSocket(socket.getSocket());
            setInputStream(socket.getInputStream());
            setOutputStream(socket.getOutputStream());
        }
    }

    public void finalize() {
        close();
    }

    private void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public String getLocalAddress() {
        if (getSocket() != null) {
            return getSocket().getLocalAddress().getHostAddress();
        }
        return "";
    }

    public int getLocalPort() {
        if (getSocket() != null) {
            return getSocket().getLocalPort();
        }
        return -1;
    }

    private void setInputStream(InputStream in) {
        this.sockIn = in;
    }

    public InputStream getInputStream() {
        return this.sockIn;
    }

    private void setOutputStream(OutputStream out) {
        this.sockOut = out;
    }

    private OutputStream getOutputStream() {
        return this.sockOut;
    }

    public boolean open() {
        Socket sock = getSocket();
        if (sock == null) {
            return false;
        }
        try {
            this.sockIn = sock.getInputStream();
            this.sockOut = sock.getOutputStream();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean close() {
        try {
            if (this.sockIn != null) {
                this.sockIn.close();
            }
            if (this.sockOut != null) {
                this.sockOut.close();
            }
            if (getSocket() != null) {
                getSocket().close();
            }
            return true;
        } catch (Exception e) {
            Debug.warning(e);
            return false;
        }
    }

    public static byte[] append(byte[] org, byte[] to) {
        byte[] newByte = new byte[(org.length + to.length)];
        System.arraycopy(org, 0, newByte, 0, org.length);
        System.arraycopy(to, 0, newByte, org.length, to.length);
        return newByte;
    }

    public static byte[] copyOfRange(byte[] original, int from, int length) {
        byte[] copy = new byte[length];
        System.arraycopy(original, from, copy, 0, length);
        return copy;
    }

    private boolean post(HTTPResponse httpRes, byte[] content, long contentOffset, long contentLength, boolean isOnlyHeader) {
        httpRes.setDate(Calendar.getInstance());
        OutputStream out = getOutputStream();
        try {
            httpRes.setContentLength(contentLength);
            byte[] resp = append(httpRes.getHeader().getBytes(), HTTP.CRLF.getBytes());
            if (isOnlyHeader) {
                out.write(resp);
                out.flush();
                return true;
            }
            boolean isChunkedResponse = httpRes.isChunked();
            if (isChunkedResponse) {
                resp = append(resp, (Long.toHexString(contentLength) + HTTP.CRLF).getBytes());
            }
            resp = append(resp, copyOfRange(content, (int) contentOffset, (int) contentLength));
            if (isChunkedResponse) {
                resp = append(resp, "\r\n0\r\n".getBytes());
            }
            out.write(resp);
            out.flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean post(HTTPResponse httpRes, InputStream in, long contentOffset, long contentLength, boolean isOnlyHeader) {
        httpRes.setDate(Calendar.getInstance());
        OutputStream out = getOutputStream();
        try {
            httpRes.setContentLength(contentLength);
            out.write(httpRes.getHeader().getBytes());
            out.write(HTTP.CRLF.getBytes());
            if (isOnlyHeader) {
                out.flush();
                return true;
            }
            long readSize;
            boolean isChunkedResponse = httpRes.isChunked();
            if (0 < contentOffset) {
                in.skip(contentOffset);
            }
            int chunkSize = HTTP.getChunkSize();
            byte[] readBuf = new byte[chunkSize];
            long readCnt = 0;
            if (((long) chunkSize) < contentLength) {
                readSize = (long) chunkSize;
            } else {
                readSize = contentLength;
            }
            int readLen = in.read(readBuf, 0, (int) readSize);
            while (readLen > 0 && readCnt < contentLength) {
                if (isChunkedResponse) {
                    out.write(Long.toHexString((long) readLen).getBytes());
                    out.write(HTTP.CRLF.getBytes());
                }
                out.write(readBuf, 0, readLen);
                if (isChunkedResponse) {
                    out.write(HTTP.CRLF.getBytes());
                }
                readCnt += (long) readLen;
                readLen = in.read(readBuf, 0, (int) (((long) chunkSize) < contentLength - readCnt ? (long) chunkSize : contentLength - readCnt));
            }
            if (isChunkedResponse) {
                out.write("0".getBytes());
                out.write(HTTP.CRLF.getBytes());
            }
            out.flush();
            return true;
        } catch (Exception e) {
            Debug.warning(e);
            return false;
        }
    }

    public boolean post(HTTPResponse httpRes, long contentOffset, long contentLength, boolean isOnlyHeader) {
        if (httpRes.hasContentInputStream()) {
            return post(httpRes, httpRes.getContentInputStream(), contentOffset, contentLength, isOnlyHeader);
        }
        return post(httpRes, httpRes.getContent(), contentOffset, contentLength, isOnlyHeader);
    }
}

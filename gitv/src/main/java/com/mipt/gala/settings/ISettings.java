package com.mipt.gala.settings;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ISettings extends IInterface {

    public static abstract class Stub extends Binder implements ISettings {
        private static final String DESCRIPTOR = "com.mipt.gala.settings.ISettings";
        static final int TRANSACTION_getAllDeviceName = 10;
        static final int TRANSACTION_getAllDreamTme = 5;
        static final int TRANSACTION_getAllOutputDisplay = 18;
        static final int TRANSACTION_getAllOutputLogic = 19;
        static final int TRANSACTION_getAllScreenSaveTime = 3;
        static final int TRANSACTION_getAudioOutputEntries = 11;
        static final int TRANSACTION_getCurrAudioOutputMode = 14;
        static final int TRANSACTION_getCurrDRCMode = 15;
        static final int TRANSACTION_getCurrDeviceName = 8;
        static final int TRANSACTION_getCurrDreamTime = 4;
        static final int TRANSACTION_getCurrOutput = 17;
        static final int TRANSACTION_getCurrScreenSaveTime = 2;
        static final int TRANSACTION_getDRCEntries = 13;
        static final int TRANSACTION_getInfo = 22;
        static final int TRANSACTION_goToPositionSetting = 21;
        static final int TRANSACTION_restoreFactory = 1;
        static final int TRANSACTION_setAudioOutputMode = 12;
        static final int TRANSACTION_setDRCMode = 16;
        static final int TRANSACTION_setDeviceName = 9;
        static final int TRANSACTION_setDreamTime = 7;
        static final int TRANSACTION_setOutputDisplay = 20;
        static final int TRANSACTION_setScreenSaverTime = 6;

        private static class Proxy implements ISettings {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public void restoreFactory() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getCurrScreenSaveTime() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getAllScreenSaveTime() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getCurrDreamTime() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getAllDreamTme() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setScreenSaverTime(String val) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(val);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setDreamTime(String val) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(val);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getCurrDeviceName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setDeviceName(String val) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(val);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getAllDeviceName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getAudioOutputEntries() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAudioOutputMode(String val) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(val);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getDRCEntries() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getCurrAudioOutputMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getCurrDRCMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setDRCMode(String val) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(val);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getCurrOutput() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getAllOutputDisplay() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getAllOutputLogic() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setOutputDisplay(String val) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(val);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void goToPositionSetting() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISettings asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ISettings)) {
                return new Proxy(obj);
            }
            return (ISettings) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String _result;
            String[] _result2;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    restoreFactory();
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getCurrScreenSaveTime();
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getAllScreenSaveTime();
                    reply.writeNoException();
                    reply.writeStringArray(_result2);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getCurrDreamTime();
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getAllDreamTme();
                    reply.writeNoException();
                    reply.writeStringArray(_result2);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    setScreenSaverTime(data.readString());
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    setDreamTime(data.readString());
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getCurrDeviceName();
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    setDeviceName(data.readString());
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getAllDeviceName();
                    reply.writeNoException();
                    reply.writeStringArray(_result2);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getAudioOutputEntries();
                    reply.writeNoException();
                    reply.writeStringArray(_result2);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    setAudioOutputMode(data.readString());
                    reply.writeNoException();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getDRCEntries();
                    reply.writeNoException();
                    reply.writeStringArray(_result2);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getCurrAudioOutputMode();
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getCurrDRCMode();
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    setDRCMode(data.readString());
                    reply.writeNoException();
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getCurrOutput();
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getAllOutputDisplay();
                    reply.writeNoException();
                    reply.writeStringArray(_result2);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getAllOutputLogic();
                    reply.writeNoException();
                    reply.writeStringArray(_result2);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    setOutputDisplay(data.readString());
                    reply.writeNoException();
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    goToPositionSetting();
                    reply.writeNoException();
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getInfo();
                    reply.writeNoException();
                    reply.writeStringArray(_result2);
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    String[] getAllDeviceName() throws RemoteException;

    String[] getAllDreamTme() throws RemoteException;

    String[] getAllOutputDisplay() throws RemoteException;

    String[] getAllOutputLogic() throws RemoteException;

    String[] getAllScreenSaveTime() throws RemoteException;

    String[] getAudioOutputEntries() throws RemoteException;

    String getCurrAudioOutputMode() throws RemoteException;

    String getCurrDRCMode() throws RemoteException;

    String getCurrDeviceName() throws RemoteException;

    String getCurrDreamTime() throws RemoteException;

    String getCurrOutput() throws RemoteException;

    String getCurrScreenSaveTime() throws RemoteException;

    String[] getDRCEntries() throws RemoteException;

    String[] getInfo() throws RemoteException;

    void goToPositionSetting() throws RemoteException;

    void restoreFactory() throws RemoteException;

    void setAudioOutputMode(String str) throws RemoteException;

    void setDRCMode(String str) throws RemoteException;

    void setDeviceName(String str) throws RemoteException;

    void setDreamTime(String str) throws RemoteException;

    void setOutputDisplay(String str) throws RemoteException;

    void setScreenSaverTime(String str) throws RemoteException;
}

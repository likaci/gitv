package com.gala.sdk.plugin.server.storage;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.gala.sdk.plugin.HostPluginInfo;
import com.gala.sdk.plugin.server.download.DownloadInfo;
import java.util.List;

public interface IStorageServer extends IInterface {

    public static abstract class Stub extends Binder implements IStorageServer {
        private static final String DESCRIPTOR = "com.gala.sdk.plugin.server.storage.IStorageServer";
        static final int TRANSACTION_copyPluginFromAssets = 5;
        static final int TRANSACTION_copySoLibToHost = 4;
        static final int TRANSACTION_download = 3;
        static final int TRANSACTION_loadPluginInfos = 1;
        static final int TRANSACTION_removePluginFiles = 6;
        static final int TRANSACTION_savePluginInfos = 2;

        private static class Proxy implements IStorageServer {
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

            public List<PluginInfo> loadPluginInfos(HostPluginInfo hPluginInfo, boolean needRemoveDumy) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (hPluginInfo != null) {
                        _data.writeInt(1);
                        hPluginInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!needRemoveDumy) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    List<PluginInfo> _result = _reply.createTypedArrayList(PluginInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void savePluginInfos(HostPluginInfo hPluginInfo, List<PluginInfo> infos) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (hPluginInfo != null) {
                        _data.writeInt(1);
                        hPluginInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeTypedList(infos);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean download(DownloadInfo info, StorageException e) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    if (_reply.readInt() != 0) {
                        e.readFromParcel(_reply);
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean copySoLibToHost(PluginInfo info, StorageException e) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    if (_reply.readInt() != 0) {
                        e.readFromParcel(_reply);
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean copyPluginFromAssets(String assetsPath, String targetPath, StorageException e) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(assetsPath);
                    _data.writeString(targetPath);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    if (_reply.readInt() != 0) {
                        e.readFromParcel(_reply);
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removePluginFiles(HostPluginInfo hPluginInfo, PluginInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (hPluginInfo != null) {
                        _data.writeInt(1);
                        hPluginInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IStorageServer asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IStorageServer)) {
                return new Proxy(obj);
            }
            return (IStorageServer) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            HostPluginInfo _arg0;
            StorageException _arg1;
            boolean _result;
            int i;
            switch (code) {
                case 1:
                    boolean _arg12;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (HostPluginInfo) HostPluginInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg12 = true;
                    } else {
                        _arg12 = false;
                    }
                    List<PluginInfo> _result2 = loadPluginInfos(_arg0, _arg12);
                    reply.writeNoException();
                    reply.writeTypedList(_result2);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (HostPluginInfo) HostPluginInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    savePluginInfos(_arg0, data.createTypedArrayList(PluginInfo.CREATOR));
                    reply.writeNoException();
                    return true;
                case 3:
                    DownloadInfo _arg02;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = (DownloadInfo) DownloadInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    _arg1 = new StorageException();
                    _result = download(_arg02, _arg1);
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    reply.writeInt(i);
                    if (_arg1 != null) {
                        reply.writeInt(1);
                        _arg1.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 4:
                    PluginInfo _arg03;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = (PluginInfo) PluginInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    _arg1 = new StorageException();
                    _result = copySoLibToHost(_arg03, _arg1);
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    reply.writeInt(i);
                    if (_arg1 != null) {
                        reply.writeInt(1);
                        _arg1.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg04 = data.readString();
                    String _arg13 = data.readString();
                    StorageException _arg2 = new StorageException();
                    _result = copyPluginFromAssets(_arg04, _arg13, _arg2);
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    reply.writeInt(i);
                    if (_arg2 != null) {
                        reply.writeInt(1);
                        _arg2.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 6:
                    PluginInfo _arg14;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (HostPluginInfo) HostPluginInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg14 = (PluginInfo) PluginInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg14 = null;
                    }
                    removePluginFiles(_arg0, _arg14);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    boolean copyPluginFromAssets(String str, String str2, StorageException storageException) throws RemoteException;

    boolean copySoLibToHost(PluginInfo pluginInfo, StorageException storageException) throws RemoteException;

    boolean download(DownloadInfo downloadInfo, StorageException storageException) throws RemoteException;

    List<PluginInfo> loadPluginInfos(HostPluginInfo hostPluginInfo, boolean z) throws RemoteException;

    void removePluginFiles(HostPluginInfo hostPluginInfo, PluginInfo pluginInfo) throws RemoteException;

    void savePluginInfos(HostPluginInfo hostPluginInfo, List<PluginInfo> list) throws RemoteException;
}

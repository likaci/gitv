package com.qiyi.tv.client;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IQiyiService extends IInterface {

    public static abstract class Stub extends Binder implements IQiyiService {

        static class Proxy implements IQiyiService {
            private IBinder f2056a;

            Proxy(IBinder remote) {
                this.f2056a = remote;
            }

            public IBinder asBinder() {
                return this.f2056a;
            }

            public String getInterfaceDescriptor() {
                return "com.qiyi.tv.client.IQiyiService";
            }

            public Bundle invoke(Bundle params) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    Bundle bundle;
                    obtain.writeInterfaceToken("com.qiyi.tv.client.IQiyiService");
                    if (params != null) {
                        obtain.writeInt(1);
                        params.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.f2056a.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(obtain2);
                    } else {
                        bundle = null;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return bundle;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, "com.qiyi.tv.client.IQiyiService");
        }

        public static IQiyiService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface queryLocalInterface = obj.queryLocalInterface("com.qiyi.tv.client.IQiyiService");
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IQiyiService)) {
                return new Proxy(obj);
            }
            return (IQiyiService) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    Bundle bundle;
                    data.enforceInterface("com.qiyi.tv.client.IQiyiService");
                    if (data.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    } else {
                        bundle = null;
                    }
                    bundle = invoke(bundle);
                    reply.writeNoException();
                    if (bundle != null) {
                        reply.writeInt(1);
                        bundle.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 1598968902:
                    reply.writeString("com.qiyi.tv.client.IQiyiService");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    Bundle invoke(Bundle bundle) throws RemoteException;
}

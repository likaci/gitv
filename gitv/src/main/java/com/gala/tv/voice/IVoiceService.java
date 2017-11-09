package com.gala.tv.voice;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IVoiceService extends IInterface {

    public static abstract class Stub extends Binder implements IVoiceService {

        private static class Proxy implements IVoiceService {
            private IBinder a;

            Proxy(IBinder iBinder) {
                this.a = iBinder;
            }

            public IBinder asBinder() {
                return this.a;
            }

            public String getInterfaceDescriptor() {
                return "com.gala.tv.voice.IVoiceService";
            }

            public Bundle invoke(Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    Bundle bundle2;
                    obtain.writeInterfaceToken("com.gala.tv.voice.IVoiceService");
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.a.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        bundle2 = (Bundle) Bundle.CREATOR.createFromParcel(obtain2);
                    } else {
                        bundle2 = null;
                    }
                    if (obtain2.readInt() != 0) {
                        bundle.readFromParcel(obtain2);
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return bundle2;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void registerCallback(IVoiceEventCallback iVoiceEventCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.gala.tv.voice.IVoiceService");
                    obtain.writeStrongBinder(iVoiceEventCallback != null ? iVoiceEventCallback.asBinder() : null);
                    this.a.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unregisterCallback(IVoiceEventCallback iVoiceEventCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.gala.tv.voice.IVoiceService");
                    obtain.writeStrongBinder(iVoiceEventCallback != null ? iVoiceEventCallback.asBinder() : null);
                    this.a.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, "com.gala.tv.voice.IVoiceService");
        }

        public static IVoiceService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.gala.tv.voice.IVoiceService");
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IVoiceService)) {
                return new Proxy(iBinder);
            }
            return (IVoiceService) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            switch (i) {
                case 1:
                    Bundle bundle;
                    parcel.enforceInterface("com.gala.tv.voice.IVoiceService");
                    if (parcel.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                    } else {
                        bundle = null;
                    }
                    Bundle invoke = invoke(bundle);
                    parcel2.writeNoException();
                    if (invoke != null) {
                        parcel2.writeInt(1);
                        invoke.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    if (bundle != null) {
                        parcel2.writeInt(1);
                        bundle.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    return true;
                case 2:
                    parcel.enforceInterface("com.gala.tv.voice.IVoiceService");
                    registerCallback(com.gala.tv.voice.IVoiceEventCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 3:
                    parcel.enforceInterface("com.gala.tv.voice.IVoiceService");
                    unregisterCallback(com.gala.tv.voice.IVoiceEventCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 1598968902:
                    parcel2.writeString("com.gala.tv.voice.IVoiceService");
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    Bundle invoke(Bundle bundle) throws RemoteException;

    void registerCallback(IVoiceEventCallback iVoiceEventCallback) throws RemoteException;

    void unregisterCallback(IVoiceEventCallback iVoiceEventCallback) throws RemoteException;
}

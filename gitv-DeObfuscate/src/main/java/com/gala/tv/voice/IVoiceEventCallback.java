package com.gala.tv.voice;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IVoiceEventCallback extends IInterface {

    public static abstract class Stub extends Binder implements IVoiceEventCallback {

        private static class Proxy implements IVoiceEventCallback {
            private IBinder f784a;

            Proxy(IBinder iBinder) {
                this.f784a = iBinder;
            }

            public IBinder asBinder() {
                return this.f784a;
            }

            public String getInterfaceDescriptor() {
                return "com.gala.tv.voice.IVoiceEventCallback";
            }

            public boolean dispatchVoice(VoiceEvent voiceEvent) throws RemoteException {
                boolean z = true;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.gala.tv.voice.IVoiceEventCallback");
                    if (voiceEvent != null) {
                        obtain.writeInt(1);
                        voiceEvent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.f784a.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() == 0) {
                        z = false;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Bundle getSupportedVoices() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    Bundle bundle;
                    obtain.writeInterfaceToken("com.gala.tv.voice.IVoiceEventCallback");
                    this.f784a.transact(2, obtain, obtain2, 0);
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
            attachInterface(this, "com.gala.tv.voice.IVoiceEventCallback");
        }

        public static IVoiceEventCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.gala.tv.voice.IVoiceEventCallback");
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IVoiceEventCallback)) {
                return new Proxy(iBinder);
            }
            return (IVoiceEventCallback) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            switch (i) {
                case 1:
                    VoiceEvent voiceEvent;
                    int i3;
                    parcel.enforceInterface("com.gala.tv.voice.IVoiceEventCallback");
                    if (parcel.readInt() != 0) {
                        voiceEvent = (VoiceEvent) VoiceEvent.CREATOR.createFromParcel(parcel);
                    } else {
                        voiceEvent = null;
                    }
                    boolean dispatchVoice = dispatchVoice(voiceEvent);
                    parcel2.writeNoException();
                    if (dispatchVoice) {
                        i3 = 1;
                    } else {
                        i3 = 0;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case 2:
                    parcel.enforceInterface("com.gala.tv.voice.IVoiceEventCallback");
                    Bundle supportedVoices = getSupportedVoices();
                    parcel2.writeNoException();
                    if (supportedVoices != null) {
                        parcel2.writeInt(1);
                        supportedVoices.writeToParcel(parcel2, 1);
                        return true;
                    }
                    parcel2.writeInt(0);
                    return true;
                case 1598968902:
                    parcel2.writeString("com.gala.tv.voice.IVoiceEventCallback");
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    boolean dispatchVoice(VoiceEvent voiceEvent) throws RemoteException;

    Bundle getSupportedVoices() throws RemoteException;
}

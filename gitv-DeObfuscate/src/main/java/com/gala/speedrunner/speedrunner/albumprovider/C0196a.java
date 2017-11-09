package com.gala.speedrunner.speedrunner.albumprovider;

import com.gala.speedrunner.speedrunner.IFailureCallback;
import com.gala.tvapi.tv2.model.ResId;
import com.gala.tvapi.tv2.result.ApiResultDeviceCheck;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;

public final class C0196a {
    private String f783a;

    public interface C0189a extends IFailureCallback {
        void mo817a(C0196a c0196a);
    }

    static class C01951 implements IApiCallback<ApiResultDeviceCheck> {
        private /* synthetic */ C0189a f782a;

        C01951(C0189a c0189a) {
            this.f782a = c0189a;
        }

        public final /* synthetic */ void onSuccess(Object obj) {
            ApiResultDeviceCheck apiResultDeviceCheck = (ApiResultDeviceCheck) obj;
            if (apiResultDeviceCheck == null || apiResultDeviceCheck.data == null || apiResultDeviceCheck.data.resIds == null) {
                this.f782a.onFailure(new Exception("device check failed"));
            } else if (apiResultDeviceCheck.data.resIds.size() > 0) {
                this.f782a.mo817a(new C0196a(((ResId) apiResultDeviceCheck.data.resIds.get(0)).id));
            } else {
                this.f782a.mo817a(new C0196a(""));
            }
        }

        public final void onException(ApiException arg0) {
            this.f782a.onFailure(new Exception(arg0.getMessage()));
        }
    }

    private C0196a(String str) {
        this.f783a = str;
    }

    public final String m511a() {
        return this.f783a;
    }
}

package com.gala.speedrunner.speedrunner.albumprovider;

import com.gala.speedrunner.speedrunner.IFailureCallback;
import com.gala.tvapi.tv2.model.ResId;
import com.gala.tvapi.tv2.result.ApiResultDeviceCheck;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;

public final class a {
    private String a;

    public interface a extends IFailureCallback {
        void a(a aVar);
    }

    static class AnonymousClass1 implements IApiCallback<ApiResultDeviceCheck> {
        private /* synthetic */ a a;

        AnonymousClass1(a aVar) {
            this.a = aVar;
        }

        public final /* synthetic */ void onSuccess(Object obj) {
            ApiResultDeviceCheck apiResultDeviceCheck = (ApiResultDeviceCheck) obj;
            if (apiResultDeviceCheck == null || apiResultDeviceCheck.data == null || apiResultDeviceCheck.data.resIds == null) {
                this.a.onFailure(new Exception("device check failed"));
            } else if (apiResultDeviceCheck.data.resIds.size() > 0) {
                this.a.a(new a(((ResId) apiResultDeviceCheck.data.resIds.get(0)).id));
            } else {
                this.a.a(new a(""));
            }
        }

        public final void onException(ApiException arg0) {
            this.a.onFailure(new Exception(arg0.getMessage()));
        }
    }

    private a(String str) {
        this.a = str;
    }

    public final String a() {
        return this.a;
    }
}

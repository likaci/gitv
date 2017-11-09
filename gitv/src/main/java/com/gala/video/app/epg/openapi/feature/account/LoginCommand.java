package com.gala.video.app.epg.openapi.feature.account;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.PassportTVHelper;
import com.gala.tvapi.vrs.model.User;
import com.gala.tvapi.vrs.result.ApiResultUserInfo;
import com.gala.video.api.ApiException;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.project.Project;
import com.qiyi.tv.client.feature.account.UserInfo;
import com.qiyi.tv.client.impl.Params.OperationType;

public class LoginCommand extends ServerCommand<Void> {

    private class MyListener implements IVrsCallback<ApiResultUserInfo> {
        private int mCode;
        private User mUser;

        private MyListener() {
        }

        public int getCode() {
            return this.mCode;
        }

        public User getUser() {
            return this.mUser;
        }

        public void onException(ApiException exception) {
            this.mCode = 1;
            this.mUser = new User();
        }

        public void onSuccess(ApiResultUserInfo result) {
            this.mCode = 0;
            this.mUser = result.getUser();
        }
    }

    public LoginCommand(Context context) {
        super(context, 10002, OperationType.OP_LOGIN, 30000);
    }

    public Bundle onProcess(Bundle params) {
        UserInfo userInfo = ServerParamsHelper.parseLoginUserInfo(params);
        boolean fromThirdUser = ServerParamsHelper.fromThirdUser(params);
        CharSequence authCookie = userInfo.getAuthCookie();
        CharSequence name = userInfo.getName();
        String nickName = userInfo.getNickName();
        CharSequence uid = userInfo.getUid();
        int code = 1;
        if (fromThirdUser) {
            MyListener listener = new MyListener();
            PassportTVHelper.thirdPartyLogin.callSync(listener, uid, userInfo.getToken(), userInfo.getTokenSecret(), String.valueOf(userInfo.getExpire()), nickName, userInfo.getRefreshToken(), String.valueOf(userInfo.getGender()), userInfo.getIconUrl(), Project.getInstance().getBuild().getVrsUUID());
            code = listener.getCode();
            User user = listener.getUser();
            setVipUser(getContext(), user);
            authCookie = user.authcookie;
            name = user.uname;
            if (code == 0) {
                setVipUser(getContext(), user);
            }
        }
        if (TextUtils.isEmpty(name)) {
            name = nickName;
        }
        if (!(StringUtils.isEmpty(authCookie) || StringUtils.isEmpty(name) || StringUtils.isEmpty(uid))) {
            code = 0;
        }
        increaseAccessCount();
        return OpenApiResultCreater.createResultBundle(code);
    }

    private void setVipUser(Context context, User info) {
        GetInterfaceTools.getIGalaAccountManager().saveVipInfo(info);
    }
}

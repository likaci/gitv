package com.gala.video.lib.share.ifimpl.netdiagnose.model;

import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.type.UserType;
import com.gala.video.lib.framework.coreservice.netdiagnose.model.NetDiagnoseInfo;
import com.gala.video.lib.share.utils.DataUtils;

public class CDNNetDiagnoseInfo extends NetDiagnoseInfo {
    private static final String TAG = "NetDiag/CDNNetDiagnoseInfo";
    private static final long serialVersionUID = -7989665766164565643L;
    private Album mAlbum;
    private UserType mUserType;

    public CDNNetDiagnoseInfo(String userCookie, String userId, UserType userType, int startTime, int bid, String rever) {
        super(userCookie, userId, startTime, bid, rever);
        this.mUserType = userType;
        this.mAlbum = null;
    }

    public CDNNetDiagnoseInfo(String userCookie, String userId, UserType userType, int startTime) {
        this(null, userCookie, userId, userType, startTime);
    }

    public CDNNetDiagnoseInfo(Album album, String userCookie, String userId, UserType userType, int startTime) {
        super(userCookie, userId, startTime);
        this.mUserType = userType;
        this.mAlbum = album;
    }

    public CDNNetDiagnoseInfo(Album album, String userCookie, String userId, UserType userType, int startTime, int bid, String rever) {
        super(userCookie, userId, startTime, bid, rever);
        this.mUserType = userType;
        this.mAlbum = album;
    }

    public Album getAlbum() {
        return this.mAlbum;
    }

    public UserType getUserType() {
        return this.mUserType;
    }

    public boolean isVipUser() {
        if (this.mUserType == null) {
            return false;
        }
        if (this.mUserType.isPlatinum() || this.mUserType.isLitchi()) {
            return true;
        }
        return false;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(TAG).append(hashCode()).append("{");
        builder.append("album=").append(DataUtils.albumInfoToString(this.mAlbum));
        builder.append(", userType=").append(this.mUserType);
        builder.append(", netInfo=").append(super.toString());
        builder.append("}");
        return builder.toString();
    }
}

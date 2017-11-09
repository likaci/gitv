package com.gala.video.lib.share.ifmanager.bussnessIF.web;

public interface IJSConfigResult {
    String getNsLookDomains();

    String getPingDomains();

    String getThridSpeedFirstURL();

    String getThridSpeedSecordURL();

    String getTracertDomains();

    String getUrlCoupon();

    String getUrlFAQ();

    String getUrlGetGold();

    String getUrlMemberPackage();

    String getUrlMemberRights();

    String getUrlMultiscreen();

    String getUrlRoleActivity();

    String getUrlSignIn();

    String getUrlSubject();

    String getValueByKey(String str);

    boolean isAccelerateExclude(String str);

    boolean isCrosswalkEnable();

    boolean isOpenHardWardEnable();
}

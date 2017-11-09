package com.qiyi.tv.client.feature.viprights;

import com.qiyi.tv.client.Result;

public interface VipRightsManager {
    public static final int STATE_ACTIVATED = 1;
    public static final int STATE_UNACTIVATED = 0;

    Result<String> checkActivationCode(String str);

    Result<Integer> getActivationState();

    int openActivationPage();
}

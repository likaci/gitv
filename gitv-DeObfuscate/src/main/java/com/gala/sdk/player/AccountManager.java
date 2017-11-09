package com.gala.sdk.player;

public interface AccountManager {

    public interface AccountListener {
        public static final int ACCOUNT_BENEFIT_SKIP_AD_DISABLED = 101;
        public static final int ACCOUNT_BENEFIT_SKIP_AD_ENABLED = 100;
        public static final int ACCOUNT_STATE_LOGIN = 0;
        public static final int ACCOUNT_STATE_LOGOUT = 1;

        void OnBenefitChanged(Account account, int i);

        void OnStateChanged(Account account, int i, boolean z);
    }

    void addAccountListener(AccountListener accountListener);

    boolean isLogin();

    ISdkError login(Account account);

    void logout();

    void removeAccountListener(AccountListener accountListener);
}

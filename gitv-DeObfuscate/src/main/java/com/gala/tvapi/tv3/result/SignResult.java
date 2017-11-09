package com.gala.tvapi.tv3.result;

import com.gala.tvapi.tv3.ApiResult;

public class SignResult extends ApiResult {
    public int currSignDays;
    public int status;

    public boolean isReachLimit() {
        if (this.code == null || !this.code.equals("E352")) {
            return false;
        }
        return true;
    }

    public boolean isHasSign() {
        if (this.status == 1) {
            return true;
        }
        if (this.code == null || !this.code.equals("E353")) {
            return false;
        }
        return true;
    }
}

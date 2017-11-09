package com.qiyi.tv.client.impl.p035a;

import android.content.Context;
import android.os.Bundle;
import com.qiyi.tv.client.impl.Command;
import com.qiyi.tv.client.impl.Log;
import com.qiyi.tv.client.impl.ParamsHelper;
import com.qiyi.tv.client.impl.Utils;
import java.util.ArrayList;

abstract class C2001d<T> extends Command {
    protected int f2108a = 1;
    protected ArrayList<T> f2109a;

    public abstract Bundle mo4355a();

    public C2001d(Context context, int i, int i2) {
        super(context, i, 20003, i2);
    }

    public Bundle process(Bundle params) {
        Bundle bundle = new Bundle();
        Utils.copyBundle(bundle, params);
        ParamsHelper.setOperationTarget(bundle, getTarget());
        ParamsHelper.setOperationType(bundle, getOperationType());
        ParamsHelper.setOperationDataType(bundle, getDataType());
        int parseMaxCount = ParamsHelper.parseMaxCount(bundle);
        int a = C2017q.m1717a();
        Object obj = null;
        Log.m1620d("DataListCommand", "process() maxCount=" + parseMaxCount + ", pageSize=" + a);
        Bundle a2;
        int parseResultCode;
        int i;
        if (parseMaxCount > a) {
            int ceil = (int) Math.ceil(((double) parseMaxCount) / ((double) a));
            int i2 = 1;
            while (i2 <= ceil) {
                Object obj2;
                int i3 = (i2 != ceil || i2 == 1) ? a : parseMaxCount % a;
                ParamsHelper.setPageNo(bundle, i2);
                ParamsHelper.setPageSize(bundle, a);
                ParamsHelper.setMaxCount(bundle, i3);
                try {
                    a2 = C2000c.m1643a().m1663a(bundle);
                    parseResultCode = ParamsHelper.parseResultCode(a2);
                    obj2 = (ArrayList) ParamsHelper.parseResultData(a2);
                    i = parseResultCode;
                } catch (Throwable e) {
                    Log.m1625w("DataListCommand", "process() error!", e);
                    Object obj3 = obj;
                    i = Utils.parseErrorCode(e);
                    obj2 = obj3;
                }
                Log.m1620d("DataListCommand", "process() pageNo=" + i2 + ", pageMaxCount=" + i3 + ", pageCode=" + i);
                if (i == 0) {
                    if (this.f2109a == null) {
                        this.f2109a = obj2;
                        this.f2108a = i;
                        Log.m1620d("DataListCommand", "process() first one mCode=" + this.f2108a + ", mList=" + this.f2109a);
                    } else if (obj2 != null) {
                        this.f2109a.addAll(obj2);
                    }
                    if (obj2 == null || obj2.size() < a) {
                        Log.m1620d("DataListCommand", "process() reach end! pageData=" + obj2);
                        break;
                    }
                    i2++;
                    obj = obj2;
                } else if (this.f2109a == null) {
                    this.f2109a = obj2;
                    this.f2108a = i;
                    Log.m1620d("DataListCommand", "process() fist one error mCode=" + this.f2108a + ", mList=" + this.f2109a);
                }
            }
        } else {
            ArrayList arrayList;
            ParamsHelper.setPageNo(bundle, 1);
            ParamsHelper.setPageSize(bundle, parseMaxCount);
            try {
                a2 = C2000c.m1643a().m1663a(bundle);
                parseResultCode = ParamsHelper.parseResultCode(a2);
                arrayList = (ArrayList) ParamsHelper.parseResultData(a2);
                i = parseResultCode;
            } catch (Throwable e2) {
                Log.m1625w("DataListCommand", "process() error!", e2);
                i = Utils.parseErrorCode(e2);
                arrayList = null;
            }
            Log.m1620d("DataListCommand", "process() once pageCode=" + i + ", pageData=" + arrayList);
            this.f2108a = i;
            this.f2109a = arrayList;
        }
        Log.m1620d("DataListCommand", "process() mCode=" + this.f2108a + ", mList=" + this.f2109a);
        return mo4355a();
    }
}

package com.gala.report.core.multiprocess;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public interface IMultiProcess {
    IBinder onBindMultiProcess(Intent intent);

    void onCreateMultiProcess(Context context);
}

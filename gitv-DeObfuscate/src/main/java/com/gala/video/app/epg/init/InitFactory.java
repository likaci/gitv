package com.gala.video.app.epg.init;

import android.util.Log;
import com.gala.video.app.epg.init.task.AppStoreInitTask;
import com.gala.video.app.epg.init.task.AppendPingbackParamsTask;
import com.gala.video.app.epg.init.task.CommonInitTask;
import com.gala.video.app.epg.init.task.DeleteCollectionTask;
import com.gala.video.app.epg.init.task.FingerPrintInitTask;
import com.gala.video.app.epg.init.task.JSConfigInitTask;
import com.gala.video.app.epg.init.task.LayoutCacheInitTask;
import com.gala.video.app.epg.init.task.LoadFondInitTask;
import com.gala.video.app.epg.init.task.LogRecordInitTask;
import com.gala.video.app.epg.init.task.MultiScreenInitTask;
import com.gala.video.app.epg.init.task.OnlyInMainProcessInitTask;
import com.gala.video.app.epg.init.task.PushServiceInitTask;
import com.gala.video.app.epg.init.task.StartUpAdRequestTask;
import com.gala.video.app.epg.preference.JSInitPreference;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.share.ifmanager.bussnessIF.startup.InitTaskInput;
import java.util.ArrayList;
import java.util.List;

public class InitFactory {
    public static InitTaskInput makeUpCommonInitTask() {
        List<Integer> process = new ArrayList();
        process.add(Integer.valueOf(0));
        process.add(Integer.valueOf(1));
        return new InitTaskInput(new CommonInitTask(), process, 100);
    }

    public static InitTaskInput makeUpOnlyMainProcessInitTask() {
        List<Integer> process = new ArrayList();
        process.add(Integer.valueOf(0));
        return new InitTaskInput(new OnlyInMainProcessInitTask(), process, 100);
    }

    public static InitTaskInput makeUpStartAdRequestTask() {
        List<Integer> process = new ArrayList();
        process.add(Integer.valueOf(0));
        return new InitTaskInput(new StartUpAdRequestTask(), process, 100);
    }

    public static InitTaskInput makeUpMultiScreenInitTask() {
        List<Integer> process = new ArrayList();
        process.add(Integer.valueOf(0));
        process.add(Integer.valueOf(1));
        return new InitTaskInput(new MultiScreenInitTask(), process, 101);
    }

    public static InitTaskInput makeUpAppstoreInitTask() {
        List<Integer> process = new ArrayList();
        process.add(Integer.valueOf(0));
        return new InitTaskInput(new AppStoreInitTask(), process, 101);
    }

    public static InitTaskInput makeUpJSConfigInitTask() {
        List<Integer> process = new ArrayList();
        process.add(Integer.valueOf(0));
        process.add(Integer.valueOf(1));
        Runnable runnable = new JSConfigInitTask();
        long delayTime = 0;
        if (JSInitPreference.isFirstLoadDate(AppRuntimeEnv.get().getApplicationContext())) {
            delayTime = 3000;
            JSInitPreference.saveFirstLoadDate(AppRuntimeEnv.get().getApplicationContext(), false);
        }
        Log.v("InitFactory", "delayTime = " + delayTime);
        return new InitTaskInput(runnable, process, 101, delayTime);
    }

    public static InitTaskInput makeUpLogRecordInitTask() {
        List<Integer> process = new ArrayList();
        process.add(Integer.valueOf(0));
        process.add(Integer.valueOf(1));
        return new InitTaskInput(new LogRecordInitTask(), process, 101);
    }

    public static InitTaskInput makeUpPushServiceInitTask() {
        List<Integer> process = new ArrayList();
        process.add(Integer.valueOf(2));
        return new InitTaskInput(new PushServiceInitTask(AppRuntimeEnv.get().getApplicationContext()), process, 101);
    }

    public static InitTaskInput makeUpLayoutCacheInitTask() {
        List<Integer> process = new ArrayList();
        process.add(Integer.valueOf(0));
        return new InitTaskInput(new LayoutCacheInitTask(), process, 101);
    }

    public static InitTaskInput makeUpDeleteCollectionTask() {
        List<Integer> process = new ArrayList();
        process.add(Integer.valueOf(0));
        return new InitTaskInput(new DeleteCollectionTask(AppRuntimeEnv.get().getApplicationContext()), process, 101);
    }

    public static InitTaskInput makeUpInitFingerPrintTask() {
        List<Integer> process = new ArrayList();
        process.add(Integer.valueOf(0));
        return new InitTaskInput(new FingerPrintInitTask(AppRuntimeEnv.get().getApplicationContext()), process, 101, 20000);
    }

    public static InitTaskInput makeUpAppendPingbackParamsTask() {
        List<Integer> process = new ArrayList();
        process.add(Integer.valueOf(0));
        process.add(Integer.valueOf(1));
        return new InitTaskInput(new AppendPingbackParamsTask(), process, 101);
    }

    public static InitTaskInput makeUpLoadFondTask() {
        List<Integer> process = new ArrayList();
        process.add(Integer.valueOf(0));
        process.add(Integer.valueOf(1));
        return new InitTaskInput(new LoadFondInitTask(), process, 101);
    }
}

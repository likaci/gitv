package com.push.nativeprocess;

import android.text.TextUtils;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class WatchDog extends NativeProcess {
    private final String TAG = NativeProcess.TAG;
    public String cmd = "am startservice -a ";
    public String cmdPackage = "am startservice -n ";
    public String cmdRoot = "am startservice --user 0 -a ";
    public String cmdRootPackage = "am startservice --user 0 -n ";

    public void runOnSubprocess() {
        doSomething();
        Log.d(NativeProcess.TAG, "doSomething() over,I can die!!!");
        System.exit(0);
    }

    private boolean doSomething() {
        Process process;
        BufferedReader br;
        String cmdOut;
        String line;
        String packageName = NativeProcess.getPackageName();
        if (!TextUtils.isEmpty(packageName)) {
            String strCmd = this.cmdRootPackage + packageName + "/" + NativeProcess.getServiceName();
            try {
                process = Runtime.getRuntime().exec(strCmd);
                Log.d(NativeProcess.TAG, "package doSomething strCmd:" + strCmd);
                if (process == null) {
                    return true;
                }
                br = new BufferedReader(new InputStreamReader(process.getInputStream()));
                cmdOut = "";
                while (true) {
                    line = br.readLine();
                    if (line == null) {
                        break;
                    }
                    cmdOut = cmdOut + line;
                }
                Log.d(NativeProcess.TAG, "package doSomething process:" + cmdOut);
                if (!cmdOut.contains("Starting service: null")) {
                    return true;
                }
                strCmd = this.cmdPackage + packageName + "/" + NativeProcess.getServiceName();
                Log.d(NativeProcess.TAG, "package doSomething failure, do it use new way strCmd:" + strCmd);
                Runtime.getRuntime().exec(strCmd);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        String str = this.cmdRoot + NativeProcess.getServiceName();
        try {
            process = Runtime.getRuntime().exec(str);
            Log.d(NativeProcess.TAG, "doSomething cmd:" + str);
            if (process == null) {
                return true;
            }
            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            cmdOut = "";
            while (true) {
                line = br.readLine();
                if (line == null) {
                    break;
                }
                cmdOut = cmdOut + line;
            }
            Log.d(NativeProcess.TAG, "doSomething process:" + cmdOut);
            if (!cmdOut.contains("Starting service: null")) {
                return true;
            }
            str = this.cmd + NativeProcess.getServiceName();
            Log.d(NativeProcess.TAG, "doSomething failure, do it use new way:" + str);
            Runtime.getRuntime().exec(str);
            return true;
        } catch (IOException e3) {
            e3.printStackTrace();
            return false;
        } catch (Exception e22) {
            e22.printStackTrace();
            return false;
        }
    }
}

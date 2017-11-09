package com.gala.video.lib.share.ifimpl.openplay.service;

import android.os.Binder;
import android.os.Bundle;
import android.util.SparseArray;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.feature.reporter.IFavoriteChangedReporter;
import com.gala.video.lib.share.ifimpl.openplay.service.feature.reporter.IHistoryChangedReporter;
import com.gala.video.lib.share.ifimpl.openplay.service.feature.reporter.IVideoPlayStateReporter;
import com.gala.video.lib.share.ifimpl.openplay.service.feature.reporter.NullFavoriteChangedReporter;
import com.gala.video.lib.share.ifimpl.openplay.service.feature.reporter.NullHistoryChangedReporter;
import com.gala.video.lib.share.ifimpl.openplay.service.feature.reporter.NullVideoPlayStateReporter;
import com.gala.video.lib.share.project.Project;
import com.qiyi.tv.client.Version;
import com.qiyi.tv.client.impl.ClientInfo;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class OpenApiManager {
    private static final int AUTH_SUCCESS = 0;
    private static final int AUTH_WRONG_CUSTOMERNAME = 1;
    private static final int AUTH_WRONG_PACKAGE = 2;
    private static final int AUTH_WRONG_UNKNOWN = -1;
    private static final int AUTH_WRONG_VERSION = 3;
    private static final String TAG = "OpenApiManager";
    private static OpenApiManager sManager = new OpenApiManager();
    private final List<ServerCommand<?>> mCommands = new CopyOnWriteArrayList();
    private boolean mExistAuthSuccess;
    private IFavoriteChangedReporter mFavoriteChangedReporter;
    private IHistoryChangedReporter mHistoryChangedReporter;
    private final SparseArray<Holder> mHolders = new SparseArray();
    private boolean mNeedEncrypt = true;
    private IVideoPlayStateReporter mVideoPlayStateReporter;

    private class Holder {
        private ClientInfo mClient;
        private int mCode;

        public Holder(int code, ClientInfo client) {
            this.mCode = code;
            this.mClient = client;
        }

        public int getCode() {
            return this.mCode;
        }

        public int hashCode() {
            return this.mClient.getPackageName().hashCode();
        }

        public boolean equals(Object o) {
            if (o instanceof Holder) {
                return this.mClient.getPackageName().equals(((Holder) o).mClient.getPackageName());
            }
            return false;
        }

        public String toString() {
            return "Holder(code=" + this.mCode + ", client=" + this.mClient + ")";
        }
    }

    private OpenApiManager() {
    }

    public static OpenApiManager instance() {
        return sManager;
    }

    public synchronized void addAllCommands(List<ServerCommand<?>> commands) {
        this.mCommands.addAll(commands);
        if (LogUtils.mIsDebug) {
            int size = commands.size();
            for (int i = 0; i < size; i++) {
                LogUtils.d(TAG, "OpenApiBinder() add[" + i + "]=" + commands.get(i));
            }
        }
    }

    synchronized ServerCommand<?> findCommand(int target, int operation, int data) {
        ServerCommand<?> find;
        find = null;
        for (ServerCommand<?> command : this.mCommands) {
            if (command.getTarget() == target && command.getOperationType() == operation && command.getDataType() == data) {
                find = command;
                break;
            }
        }
        return find;
    }

    public synchronized boolean addCommand(ServerCommand<?> command) {
        boolean z;
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "addCommand(" + command + ")");
        }
        if (command == null || this.mCommands.contains(command)) {
            z = false;
        } else {
            z = this.mCommands.add(command);
        }
        return z;
    }

    public synchronized boolean removeCommand(ServerCommand<?> command) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "removeCommand(" + command + ")");
        }
        return this.mCommands.remove(command);
    }

    public synchronized void clearCommand() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "clear()");
        }
        this.mCommands.clear();
    }

    private synchronized void replaceWatcherProcess(int pid) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "replaceWatcherProcess(" + pid + ")");
        }
        for (ServerCommand<?> command : this.mCommands) {
            command.replaceWatcherProcess(pid);
        }
    }

    void clearAuth() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "clearAuth() mExistAuthSuccess=" + this.mExistAuthSuccess);
        }
        this.mExistAuthSuccess = false;
    }

    public synchronized boolean isAuthSuccess() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "isAuthSuccess() return " + this.mExistAuthSuccess);
        }
        return this.mExistAuthSuccess;
    }

    synchronized boolean isAllowedClient() {
        boolean allowed;
        int pid = Binder.getCallingPid();
        Holder holder = (Holder) this.mHolders.get(pid);
        allowed = false;
        if (holder != null && holder.getCode() == 0) {
            allowed = true;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "isAllowedClient() pid=" + pid + ", holder=" + holder + " return " + allowed);
        }
        return allowed;
    }

    synchronized boolean authLocal(Bundle params) {
        boolean z;
        int authCode;
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "authLocal() begin.");
        }
        String[] localPackageNames = Project.getInstance().getBuild().getCustomerPkgName();
        String localCustomerName = Project.getInstance().getBuild().getCustomerName();
        String localAuthUuid = Project.getInstance().getBuild().getOpenApiOldUuid();
        ClientInfo clientInfo = ServerParamsHelper.parseClientInfo(params);
        String clientPackageName = clientInfo != null ? clientInfo.getPackageName() : null;
        String clientCustomerName = clientInfo != null ? clientInfo.getSignature() : null;
        String clientOldUuid = clientInfo != null ? clientInfo.getUuid() : null;
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "clientCustomerName=" + clientCustomerName);
        }
        clientCustomerName = clientCustomerName == null ? null : OpenApiSignatureHelper.decryptCustomerName(clientCustomerName);
        int clientVersion = clientInfo != null ? clientInfo.getVersionCode() : -1;
        boolean packageSame = false;
        String realLocalName = null;
        for (String localPackageName : localPackageNames) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "authLocal() localPackageName=" + localPackageName);
            }
            if (localPackageName.equals(clientPackageName)) {
                packageSame = true;
                realLocalName = localPackageName;
                break;
            }
        }
        boolean customerNameSame = false;
        if (localCustomerName != null && localCustomerName.equals(clientCustomerName)) {
            customerNameSame = true;
        }
        boolean uuidSame = false;
        if (localAuthUuid != null && localAuthUuid.equals(clientOldUuid)) {
            uuidSame = true;
        }
        if (!customerNameSame && !uuidSame) {
            authCode = 1;
        } else if (!packageSame) {
            authCode = 2;
        } else if (isCompatible(Version.VERSION_CODE, clientVersion)) {
            authCode = 0;
        } else {
            authCode = 3;
        }
        int pid = Binder.getCallingPid();
        Holder holder = new Holder(authCode, clientInfo);
        int size = this.mHolders.size();
        int removed = -1;
        for (int i = 0; i < size; i++) {
            if (((Holder) this.mHolders.valueAt(i)).equals(holder)) {
                removed = this.mHolders.keyAt(i);
                break;
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "auhtLocal() find old process " + removed);
        }
        if (removed > 0 && removed != pid) {
            this.mHolders.remove(removed);
            replaceWatcherProcess(removed);
        }
        this.mHolders.put(pid, holder);
        if (authCode == 0) {
            this.mExistAuthSuccess = true;
        }
        if (authCode != 0) {
            LogUtils.e(TAG, "OpenAPI auth fail: auth code = " + authCode + ", server version = " + Version.VERSION_CODE);
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "authLocal() localCustomerName=" + localCustomerName + ", realLocalName=" + realLocalName + ", pid=" + pid + ", holder=" + holder + " return " + authCode);
        }
        if (authCode == 0) {
            z = true;
        } else {
            z = false;
        }
        return z;
    }

    public IVideoPlayStateReporter getVideoPlayStateReporter() {
        if (this.mVideoPlayStateReporter == null) {
            this.mVideoPlayStateReporter = new NullVideoPlayStateReporter();
        }
        return this.mVideoPlayStateReporter;
    }

    public void setVideoPlayStateReporter(IVideoPlayStateReporter videoPlayStateReporter) {
        this.mVideoPlayStateReporter = videoPlayStateReporter;
    }

    public IFavoriteChangedReporter getFavoriteChangedReporter() {
        if (this.mFavoriteChangedReporter == null) {
            this.mFavoriteChangedReporter = new NullFavoriteChangedReporter();
        }
        return this.mFavoriteChangedReporter;
    }

    public void setFavoriteChangedReporter(IFavoriteChangedReporter favoriteChangedReporter) {
        this.mFavoriteChangedReporter = favoriteChangedReporter;
    }

    public IHistoryChangedReporter getHistoryChangedReporter() {
        if (this.mHistoryChangedReporter == null) {
            this.mHistoryChangedReporter = new NullHistoryChangedReporter();
        }
        return this.mHistoryChangedReporter;
    }

    public void setHistoryChangedReporter(IHistoryChangedReporter historyChangedReporter) {
        this.mHistoryChangedReporter = historyChangedReporter;
    }

    private boolean isCompatible(int serverVersion, int clientVersion) {
        boolean compatibale = serverVersion == clientVersion;
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "isCompatible(" + serverVersion + ", " + clientVersion + ") return " + compatibale);
        }
        return true;
    }

    public void setNeedEncrypt(boolean needEncrypt) {
        this.mNeedEncrypt = needEncrypt;
    }

    public boolean needEncrypt() {
        return this.mNeedEncrypt;
    }
}

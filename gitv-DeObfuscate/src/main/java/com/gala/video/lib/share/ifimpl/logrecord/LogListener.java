package com.gala.video.lib.share.ifimpl.logrecord;

import android.content.Context;
import android.util.Log;
import com.gala.report.core.log.ILogListener;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;

public class LogListener implements ILogListener {
    public static final String MSG_BEFORE_SEND_FEEDBACK = "上传前";
    public static final String MSG_ISRUNNING_RETRYLATER = "请等待本次信息上传后，再反馈故障，谢谢";
    public static final String MSG_LOGSAVED = "报障已完成，正在提交反馈";
    public static final String MSG_LOGSERVICE_START = "手动报障已开启，请重现您的软件故障，然后连按5次菜单键提交报障信息";
    public static final String MSG_LOGSERVICE_STARTUNKNOW = "如果您不小心开启了此功能，可以连按5次菜单键关闭";
    public static final String MSG_LOGSERVICE_TIMEOUT = "日志服务运行时间太长，已自动关闭";
    public static final String MSG_LOGS_CRASH = "logRecord crash";
    public static final String MSG_LOGS_FEEDBACK = "logRecord 反馈一下";
    public static final String MSG_LOGS_HELP = "logRecord 帮助反馈";
    public static final String MSG_LOGS_SECONDNARY_FEEDBACK = "logRecord 二级页面 点击反馈";
    public static final String MSG_LOG_BEGIN_SEND = "正在提交反馈，请稍候";
    public static final String MSG_LOG_MANUAL = "logRecord 手动上传";
    public static final String MSG_LOG_NEWWORKL = "logRecord 网络诊断";
    public static final String MSG_LOG_UNABLESAVED = "无法保存日志，请重新报障";
    public static final String MSG_NO_MEMORY = "空间不足，无法开启手动报障，请删除一些内容";
    private static final String TAG = "Project/LogListener";
    private Context mContext;

    public LogListener(Context context) {
        this.mContext = context;
    }

    public void initSuccess() {
        Log.v(TAG, "initSuccess()");
        GetInterfaceTools.getILogRecordProvider().getLogCore().startRecord();
    }

    public void initFail() {
        Log.v(TAG, "initFail()");
    }

    public void onStartRecordSuccess() {
        Log.v(TAG, "onStartRecordSuccess()");
    }

    public void onStartRecordFail() {
        Log.v(TAG, "onStartRecordFail()");
    }

    public void onStopRecordSuccess() {
        Log.v(TAG, "onStopRecordSuccess()");
    }

    public void onStopRecordFail() {
        Log.v(TAG, "onStopRecordFail()");
    }

    public void releaseSuccess() {
        Log.v(TAG, "releaseSuccess()");
    }

    public void releaseFail(String s) {
        Log.v(TAG, "releaseFail()");
    }
}

package com.gala.android.dlna.sdk.stddmrcontroller.enums;

import com.gala.android.dlna.sdk.stddmrcontroller.Util;
import com.gala.android.dlna.sdk.stddmrcontroller.data.ActionResult;
import java.util.HashSet;

public enum ACTION {
    PLAY(SERVICE.AVTransport, ACTION_TAG.Play, new HashSet<ACTION_ARGUMENT>() {
        {
            add(ACTION_ARGUMENT.InstanceID);
            add(ACTION_ARGUMENT.Speed);
        }
    }, null),
    PAUSE(SERVICE.AVTransport, ACTION_TAG.Pause, new HashSet<ACTION_ARGUMENT>() {
        {
            add(ACTION_ARGUMENT.InstanceID);
        }
    }, null),
    STOP(SERVICE.AVTransport, ACTION_TAG.Stop, new HashSet<ACTION_ARGUMENT>() {
        {
            add(ACTION_ARGUMENT.InstanceID);
        }
    }, null),
    SEEK(SERVICE.AVTransport, ACTION_TAG.Seek, new HashSet<ACTION_ARGUMENT>() {
        {
            add(ACTION_ARGUMENT.InstanceID);
            add(ACTION_ARGUMENT.Unit);
            add(ACTION_ARGUMENT.Target);
        }
    }, null),
    PUSHURL(SERVICE.AVTransport, ACTION_TAG.SetAVTransportURI, new HashSet<ACTION_ARGUMENT>() {
        {
            add(ACTION_ARGUMENT.InstanceID);
            add(ACTION_ARGUMENT.CurrentURI);
            add(ACTION_ARGUMENT.CurrentURIMetaData);
        }
    }, null),
    GETPOSITIONABS(SERVICE.AVTransport, ACTION_TAG.GetPositionInfo, new HashSet<ACTION_ARGUMENT>() {
        {
            add(ACTION_ARGUMENT.InstanceID);
        }
    }, ACTION_ARGUMENT.AbsTime),
    GETPOSITIONREL(SERVICE.AVTransport, ACTION_TAG.GetPositionInfo, new HashSet<ACTION_ARGUMENT>() {
        {
            add(ACTION_ARGUMENT.InstanceID);
        }
    }, ACTION_ARGUMENT.RelTime),
    GETTRANSPORTSTATE(SERVICE.AVTransport, ACTION_TAG.GetTransportInfo, new HashSet<ACTION_ARGUMENT>() {
        {
            add(ACTION_ARGUMENT.InstanceID);
        }
    }, ACTION_ARGUMENT.CurrentTransportState),
    GETMEDIADURATION(SERVICE.AVTransport, ACTION_TAG.GetMediaInfo, new HashSet<ACTION_ARGUMENT>() {
        {
            add(ACTION_ARGUMENT.InstanceID);
        }
    }, ACTION_ARGUMENT.MediaDuration),
    GETTRACKDURATION(SERVICE.AVTransport, ACTION_TAG.GetPositionInfo, new HashSet<ACTION_ARGUMENT>() {
        {
            add(ACTION_ARGUMENT.InstanceID);
        }
    }, ACTION_ARGUMENT.TrackDuration),
    GETMEDIAINFO(SERVICE.AVTransport, ACTION_TAG.GetMediaInfo, new HashSet<ACTION_ARGUMENT>() {
        {
            add(ACTION_ARGUMENT.InstanceID);
        }
    }, ACTION_ARGUMENT.CurrentURI),
    SETVOLUME(SERVICE.RenderingControl, ACTION_TAG.SetVolume, new HashSet<ACTION_ARGUMENT>() {
        {
            add(ACTION_ARGUMENT.InstanceID);
            add(ACTION_ARGUMENT.Channel);
            add(ACTION_ARGUMENT.DesiredVolume);
        }
    }, null),
    GETVOLUME(SERVICE.RenderingControl, ACTION_TAG.GetVolume, new HashSet<ACTION_ARGUMENT>() {
        {
            add(ACTION_ARGUMENT.InstanceID);
            add(ACTION_ARGUMENT.Channel);
        }
    }, ACTION_ARGUMENT.CurrentVolume);
    
    private HashSet<ACTION_ARGUMENT> mArguments;
    private ACTION_ARGUMENT mReturnArgument;
    private SERVICE mService;
    private ACTION_TAG mTag;

    private ACTION(SERVICE service, ACTION_TAG tag, HashSet<ACTION_ARGUMENT> arguments, ACTION_ARGUMENT returnArgument) {
        this.mService = service;
        this.mTag = tag;
        this.mArguments = arguments;
        this.mReturnArgument = returnArgument;
    }

    public String getActionTag() {
        return this.mTag == null ? null : this.mTag.name();
    }

    public String getServiceTag() {
        return this.mService == null ? null : this.mService.getTag();
    }

    public HashSet<ACTION_ARGUMENT> getArguments() {
        return this.mArguments;
    }

    public ACTION_ARGUMENT getReturnArgument() {
        return this.mReturnArgument;
    }

    public static ActionResult checkResult(ACTION action, String result) {
        if (action == null) {
            return new ActionResult(false, "", RESULT_DESCRIPTION.FAIL_PARAMETER_NO_ACTION);
        }
        if (Util.isEmpty(result)) {
            return new ActionResult(false, result, RESULT_DESCRIPTION.FAIL_INVALID_RETURN_VALUE);
        }
        if ("NOT_IMPLEMENTED".equals(result)) {
            return new ActionResult(false, result, RESULT_DESCRIPTION.FAIL_NOT_IMPLEMENTED);
        }
        ActionResult successResult = new ActionResult(true, result, RESULT_DESCRIPTION.SUCCESS);
        switch ($SWITCH_TABLE$com$gala$android$dlna$sdk$stddmrcontroller$enums$ACTION()[action.ordinal()]) {
            case 6:
            case 7:
            case 9:
            case 10:
                if (Util.isValidPositionStr(result)) {
                    return successResult;
                }
                return new ActionResult(false, result, RESULT_DESCRIPTION.FAIL_INVALID_RETURN_VALUE);
            case 8:
                if (TRANSPORT_STATE.isState(result)) {
                    return successResult;
                }
                return new ActionResult(false, result, RESULT_DESCRIPTION.FAIL_INVALID_RETURN_VALUE);
            case 13:
                if (Util.isIntStrInRange(result, 0, 100)) {
                    return successResult;
                }
                return new ActionResult(false, result, RESULT_DESCRIPTION.FAIL_INVALID_RETURN_VALUE);
            default:
                return successResult;
        }
    }
}

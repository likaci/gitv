package com.gala.android.dlna.sdk.stddmrcontroller;

import com.gala.android.dlna.sdk.controlpoint.MediaType;
import com.gala.android.dlna.sdk.stddmrcontroller.data.ActionContent;
import com.gala.android.dlna.sdk.stddmrcontroller.data.ActionResult;
import com.gala.android.dlna.sdk.stddmrcontroller.enums.ACTION;
import com.gala.android.dlna.sdk.stddmrcontroller.enums.FUNCTION;
import com.gala.android.dlna.sdk.stddmrcontroller.enums.RESULT_DESCRIPTION;
import java.util.HashSet;
import org.cybergarage.upnp.Device;
import org.cybergarage.util.Debug;

public class StdDmrController implements IStdDmrController {
    protected static final ActionResult RESULT_UNAVAILABLE_FUNCTION = new ActionResult(false, "", RESULT_DESCRIPTION.FAIL_UNAVAILABLE_FUNCTION);
    private static final String START_POSITION = "00:00:00";
    private static final String TAG = new StringBuilder(String.valueOf(StdDmrController.class.getSimpleName())).append(": ").toString();
    protected HashSet<FUNCTION> mAvailableFunctions = new HashSet();
    protected Device mTargetDmrDevice;

    protected StdDmrController(Device targetDevice) {
        if (isSuitableDevice(targetDevice)) {
            this.mTargetDmrDevice = targetDevice;
            checkAviliableFunctions(targetDevice);
            return;
        }
        this.mTargetDmrDevice = null;
    }

    protected void checkAviliableFunctions(Device targetDevice) {
        this.mAvailableFunctions = new HashSet();
        for (FUNCTION function : FUNCTION.values()) {
            this.mAvailableFunctions.add(function);
        }
    }

    public boolean isFunctionAvailable(FUNCTION function) {
        if (function == null) {
            return false;
        }
        if (this.mAvailableFunctions == null || this.mAvailableFunctions.isEmpty()) {
            return true;
        }
        return this.mAvailableFunctions.contains(function);
    }

    protected boolean isSuitableDevice(Device device) {
        return Util.isStdDmrDevice(device);
    }

    protected boolean isConnectedToDevice() {
        return isSuitableDevice(this.mTargetDmrDevice);
    }

    public HashSet<FUNCTION> getAvailableFunctions() {
        return this.mAvailableFunctions;
    }

    public ActionResult playMedia(String path, String title, MediaType type) {
        if (!isFunctionAvailable(FUNCTION.PLAYMEDIA)) {
            return RESULT_UNAVAILABLE_FUNCTION;
        }
        ActionResult actionResult = pushUrl(path, title, type);
        return (actionResult == null || !actionResult.isSuccessful()) ? actionResult : play();
    }

    public ActionResult pushUrl(String path, String title, MediaType type) {
        Debug.message(TAG + "standDmr push url");
        if (!isFunctionAvailable(FUNCTION.PUSHURL)) {
            return RESULT_UNAVAILABLE_FUNCTION;
        }
        if (Util.isEmpty(path) || type == null || title == null) {
            return new ActionResult(false, "", RESULT_DESCRIPTION.FAIL_INVALID_ARGUMENT);
        }
        return StdDmrActionProcessor.doAction(this.mTargetDmrDevice, new ActionContent(ACTION.PUSHURL, Util.getPushUrlArgumentValues(path, title, type)));
    }

    public ActionResult play() {
        if (!isFunctionAvailable(FUNCTION.PLAY)) {
            return RESULT_UNAVAILABLE_FUNCTION;
        }
        return StdDmrActionProcessor.doAction(this.mTargetDmrDevice, new ActionContent(ACTION.PLAY, Util.getPlayArgumentValues()));
    }

    public ActionResult pause() {
        if (!isFunctionAvailable(FUNCTION.PAUSE)) {
            return RESULT_UNAVAILABLE_FUNCTION;
        }
        return StdDmrActionProcessor.doAction(this.mTargetDmrDevice, new ActionContent(ACTION.PAUSE, Util.getDefaultArgumentValues()));
    }

    public ActionResult stop() {
        if (!isFunctionAvailable(FUNCTION.STOP)) {
            return RESULT_UNAVAILABLE_FUNCTION;
        }
        return StdDmrActionProcessor.doAction(this.mTargetDmrDevice, new ActionContent(ACTION.STOP, Util.getDefaultArgumentValues()));
    }

    public ActionResult getTransportState() {
        if (!isFunctionAvailable(FUNCTION.GETTRANSPORTSTATE)) {
            return RESULT_UNAVAILABLE_FUNCTION;
        }
        return StdDmrActionProcessor.doAction(this.mTargetDmrDevice, new ActionContent(ACTION.GETTRANSPORTSTATE, Util.getDefaultArgumentValues()));
    }

    public ActionResult getPosition() {
        if (!isFunctionAvailable(FUNCTION.GETPOSITION)) {
            return RESULT_UNAVAILABLE_FUNCTION;
        }
        ActionResult actionResult = StdDmrActionProcessor.doAction(this.mTargetDmrDevice, new ActionContent(ACTION.GETPOSITIONABS, Util.getDefaultArgumentValues()));
        if (actionResult != null && actionResult.isSuccessful() && !START_POSITION.equals(actionResult.getResultString())) {
            return actionResult;
        }
        return StdDmrActionProcessor.doAction(this.mTargetDmrDevice, new ActionContent(ACTION.GETPOSITIONREL, Util.getDefaultArgumentValues()));
    }

    public ActionResult getMediaDuration() {
        if (!isFunctionAvailable(FUNCTION.GETMEDIADURATION)) {
            return RESULT_UNAVAILABLE_FUNCTION;
        }
        ActionResult actionResult = StdDmrActionProcessor.doAction(this.mTargetDmrDevice, new ActionContent(ACTION.GETTRACKDURATION, Util.getDefaultArgumentValues()));
        if (actionResult != null && actionResult.isSuccessful() && !START_POSITION.equals(actionResult.getResultString())) {
            return actionResult;
        }
        return StdDmrActionProcessor.doAction(this.mTargetDmrDevice, new ActionContent(ACTION.GETMEDIADURATION, Util.getDefaultArgumentValues()));
    }

    public ActionResult getMediaUri() {
        if (!isFunctionAvailable(FUNCTION.GETMEDIAURI)) {
            return RESULT_UNAVAILABLE_FUNCTION;
        }
        return StdDmrActionProcessor.doAction(this.mTargetDmrDevice, new ActionContent(ACTION.GETMEDIAINFO, Util.getDefaultArgumentValues()));
    }

    public ActionResult seek(String targetPosition) {
        if (!isFunctionAvailable(FUNCTION.SEEK)) {
            return RESULT_UNAVAILABLE_FUNCTION;
        }
        if (!Util.isValidPositionStr(targetPosition)) {
            return new ActionResult(false, targetPosition, RESULT_DESCRIPTION.FAIL_INVALID_ARGUMENT);
        }
        ActionResult actionResult = StdDmrActionProcessor.doAction(this.mTargetDmrDevice, new ActionContent(ACTION.SEEK, Util.getSeekRELArgumentValues(targetPosition)));
        if (actionResult != null && actionResult.isSuccessful()) {
            return actionResult;
        }
        return StdDmrActionProcessor.doAction(this.mTargetDmrDevice, new ActionContent(ACTION.SEEK, Util.getSeekABSArgumentValues(targetPosition)));
    }

    public ActionResult setVolume(int percent) {
        if (!isFunctionAvailable(FUNCTION.SETVOLUME)) {
            return RESULT_UNAVAILABLE_FUNCTION;
        }
        if (percent < 0 || percent > 100) {
            return new ActionResult(false, percent, RESULT_DESCRIPTION.FAIL_INVALID_ARGUMENT);
        }
        return StdDmrActionProcessor.doAction(this.mTargetDmrDevice, new ActionContent(ACTION.SETVOLUME, Util.getSetVolumeArgumentValues(percent)));
    }

    public ActionResult getVolume() {
        if (!isFunctionAvailable(FUNCTION.GETVOLUME)) {
            return RESULT_UNAVAILABLE_FUNCTION;
        }
        return StdDmrActionProcessor.doAction(this.mTargetDmrDevice, new ActionContent(ACTION.GETVOLUME, Util.getSoundArgumentValues()));
    }
}

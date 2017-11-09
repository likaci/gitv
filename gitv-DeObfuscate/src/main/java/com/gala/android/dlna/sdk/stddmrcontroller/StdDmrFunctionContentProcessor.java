package com.gala.android.dlna.sdk.stddmrcontroller;

import com.gala.android.dlna.sdk.stddmrcontroller.data.ActionResult;
import com.gala.android.dlna.sdk.stddmrcontroller.enums.FUNCTION;
import com.gala.android.dlna.sdk.stddmrcontroller.enums.RESULT_DESCRIPTION;
import java.util.Vector;
import org.cybergarage.upnp.Device;
import org.json.JSONObject;

public class StdDmrFunctionContentProcessor {
    private static /* synthetic */ int[] f299xb08d23a3;

    static /* synthetic */ int[] m159xb08d23a3() {
        int[] iArr = f299xb08d23a3;
        if (iArr == null) {
            iArr = new int[FUNCTION.values().length];
            try {
                iArr[FUNCTION.GETMEDIADURATION.ordinal()] = 6;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[FUNCTION.GETMEDIAURI.ordinal()] = 12;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[FUNCTION.GETPOSITION.ordinal()] = 4;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[FUNCTION.GETTRANSPORTSTATE.ordinal()] = 5;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[FUNCTION.GETVOLUME.ordinal()] = 7;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[FUNCTION.PAUSE.ordinal()] = 2;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[FUNCTION.PLAY.ordinal()] = 1;
            } catch (NoSuchFieldError e7) {
            }
            try {
                iArr[FUNCTION.PLAYMEDIA.ordinal()] = 10;
            } catch (NoSuchFieldError e8) {
            }
            try {
                iArr[FUNCTION.PUSHURL.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                iArr[FUNCTION.SEEK.ordinal()] = 8;
            } catch (NoSuchFieldError e10) {
            }
            try {
                iArr[FUNCTION.SETVOLUME.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                iArr[FUNCTION.STOP.ordinal()] = 3;
            } catch (NoSuchFieldError e12) {
            }
            f299xb08d23a3 = iArr;
        }
        return iArr;
    }

    public static boolean isStdDmrCommand(String commandStr) {
        if (Util.isEmpty(commandStr)) {
            return false;
        }
        try {
            JSONObject json = new JSONObject(commandStr);
            if (json == null || !Util.FUNCTION_TAG_DLNA.equalsIgnoreCase(json.optString(Util.FUNCTION_TAG_FLAVOR))) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static ActionResult processFunctionContent(Device targetDevice, String commandStr) {
        ActionResult actionResult = new ActionResult(false, "", RESULT_DESCRIPTION.FAIL_INVALID_FUNCTION_CONTENT);
        if (Util.isEmpty(commandStr)) {
            return actionResult;
        }
        try {
            JSONObject json = new JSONObject(commandStr);
            JSONObject content = json.getJSONObject(Util.FUNCTION_TAG_CONTENT);
            if (content == null || !Util.FUNCTION_TAG_DLNA.equalsIgnoreCase(json.optString(Util.FUNCTION_TAG_FLAVOR))) {
                return actionResult;
            }
            StdDmrController controller = StdDmrControllerFactory.getStdDmrControllerByDevice(targetDevice);
            if (controller == null) {
                return new ActionResult(false, "", RESULT_DESCRIPTION.FAIL_NO_TARGET_DEVICE);
            }
            try {
                FUNCTION function = FUNCTION.valueOf(content.optString(Util.FUNCTION_TAG_ACTION));
                if (function == null) {
                    return new ActionResult(false, "", RESULT_DESCRIPTION.FAIL_INVALID_FUNCTION_NAME);
                }
                String str;
                switch (m159xb08d23a3()[function.ordinal()]) {
                    case 1:
                        actionResult = controller.play();
                        break;
                    case 2:
                        actionResult = controller.pause();
                        break;
                    case 3:
                        actionResult = controller.stop();
                        break;
                    case 4:
                        actionResult = controller.getPosition();
                        break;
                    case 5:
                        actionResult = controller.getTransportState();
                        break;
                    case 6:
                        actionResult = controller.getMediaDuration();
                        break;
                    case 7:
                        actionResult = controller.getVolume();
                        break;
                    case 8:
                        Vector<String> seekArgumentValues = FUNCTION.getArgumentValues(FUNCTION.SEEK, content);
                        if (seekArgumentValues != null) {
                            actionResult = controller.seek(Util.getPositionStringBySecondStr((String) seekArgumentValues.get(0)));
                            break;
                        }
                        actionResult = new ActionResult(false, "", RESULT_DESCRIPTION.FAIL_INVALID_ARGUMENT_VALUES);
                        break;
                    case 9:
                        Vector<String> pushUrlArgumentValues = FUNCTION.getArgumentValues(FUNCTION.PUSHURL, content);
                        if (pushUrlArgumentValues != null) {
                            str = (String) pushUrlArgumentValues.get(0);
                            actionResult = controller.pushUrl(str, (String) pushUrlArgumentValues.get(1), Util.getMediaTypeByName((String) pushUrlArgumentValues.get(2)));
                            break;
                        }
                        actionResult = new ActionResult(false, "", RESULT_DESCRIPTION.FAIL_INVALID_ARGUMENT_VALUES);
                        break;
                    case 10:
                        Vector<String> playMediaArgumentValues = FUNCTION.getArgumentValues(FUNCTION.PLAYMEDIA, content);
                        if (playMediaArgumentValues != null) {
                            str = (String) playMediaArgumentValues.get(0);
                            actionResult = controller.playMedia(str, (String) playMediaArgumentValues.get(1), Util.getMediaTypeByName((String) playMediaArgumentValues.get(2)));
                            break;
                        }
                        actionResult = new ActionResult(false, "", RESULT_DESCRIPTION.FAIL_INVALID_ARGUMENT_VALUES);
                        break;
                    case 11:
                        Vector<String> setVolumeArgumentValues = FUNCTION.getArgumentValues(FUNCTION.SETVOLUME, content);
                        if (setVolumeArgumentValues != null) {
                            actionResult = controller.setVolume(Util.getVolumeByStr((String) setVolumeArgumentValues.get(0)));
                            break;
                        }
                        actionResult = new ActionResult(false, "", RESULT_DESCRIPTION.FAIL_INVALID_ARGUMENT_VALUES);
                        break;
                    case 12:
                        actionResult = controller.getMediaUri();
                        break;
                    default:
                        actionResult = new ActionResult(false, function.name(), RESULT_DESCRIPTION.FAIL_UNKNOW_FUNCTION);
                        break;
                }
                return actionResult;
            } catch (Exception e) {
                return new ActionResult(false, "", RESULT_DESCRIPTION.FAIL_INVALID_FUNCTION_NAME);
            }
        } catch (Exception e2) {
            return actionResult;
        }
    }
}

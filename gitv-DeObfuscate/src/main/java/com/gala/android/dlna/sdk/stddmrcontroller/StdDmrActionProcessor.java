package com.gala.android.dlna.sdk.stddmrcontroller;

import com.gala.android.dlna.sdk.stddmrcontroller.data.ActionContent;
import com.gala.android.dlna.sdk.stddmrcontroller.data.ActionResult;
import com.gala.android.dlna.sdk.stddmrcontroller.enums.ACTION;
import com.gala.android.dlna.sdk.stddmrcontroller.enums.ACTION_ARGUMENT;
import com.gala.android.dlna.sdk.stddmrcontroller.enums.RESULT_DESCRIPTION;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.Service;

public class StdDmrActionProcessor {
    private StdDmrActionProcessor() {
    }

    protected static ActionResult doAction(Device targetDeivce, ActionContent actionData) {
        if (!Util.isStdDmrDevice(targetDeivce)) {
            return new ActionResult(false, "", RESULT_DESCRIPTION.FAIL_NO_TARGET_DEVICE);
        }
        if (actionData == null || actionData.getAction() == null) {
            return new ActionResult(false, "", RESULT_DESCRIPTION.FAIL_PARAMETER_NO_ACTION);
        }
        ACTION action = actionData.getAction();
        if (isActionUnavailable(targetDeivce, action)) {
            return new ActionResult(false, "", RESULT_DESCRIPTION.FAIL_UNAVAILABLE_ACTION);
        }
        String serviceTag = action.getServiceTag();
        if (Util.isEmpty(serviceTag)) {
            return new ActionResult(false, "", RESULT_DESCRIPTION.FAIL_PARAMETER_NO_SERVICE);
        }
        Service localService = targetDeivce.getService(serviceTag);
        if (localService == null) {
            return new ActionResult(false, action.getServiceTag(), RESULT_DESCRIPTION.FAIL_UNAVAILABLE_SERVICE);
        }
        Action localAction = localService.getAction(action.getActionTag());
        if (localAction == null) {
            return new ActionResult(false, action.getActionTag(), RESULT_DESCRIPTION.FAIL_UNAVAILABLE_ACTION);
        }
        HashSet<ACTION_ARGUMENT> arguments = action.getArguments();
        if (!(arguments == null || arguments.isEmpty())) {
            Hashtable<ACTION_ARGUMENT, String> mArgumentValues = actionData.getArgumentValues();
            if (mArgumentValues == null || mArgumentValues.isEmpty()) {
                return new ActionResult(false, "", RESULT_DESCRIPTION.FAIL_INVALID_ARGUMENT_VALUES);
            }
            Iterator it = arguments.iterator();
            while (it.hasNext()) {
                ACTION_ARGUMENT argument = (ACTION_ARGUMENT) it.next();
                if (!mArgumentValues.containsKey(argument)) {
                    return new ActionResult(false, argument.name(), RESULT_DESCRIPTION.FAIL_INVALID_ARGUMENT_VALUES);
                }
                localAction.setArgumentValue(argument.name(), (String) mArgumentValues.get(argument));
            }
        }
        if (!localAction.postControlAction()) {
            return new ActionResult(false, action.getActionTag(), localAction.getPostActionFailedReason());
        }
        if (action.getReturnArgument() == null) {
            return new ActionResult(true, "", RESULT_DESCRIPTION.SUCCESS);
        }
        return ACTION.checkResult(action, localAction.getArgumentValue(action.getReturnArgument().name()));
    }

    private static boolean isActionUnavailable(Device targetDeivce, ACTION action) {
        return false;
    }
}

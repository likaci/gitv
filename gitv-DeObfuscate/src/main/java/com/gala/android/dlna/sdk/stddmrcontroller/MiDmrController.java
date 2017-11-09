package com.gala.android.dlna.sdk.stddmrcontroller;

import com.gala.android.dlna.sdk.controlpoint.MediaType;
import com.gala.android.dlna.sdk.stddmrcontroller.data.ActionResult;
import com.gala.android.dlna.sdk.stddmrcontroller.enums.FUNCTION;
import com.gala.android.dlna.sdk.stddmrcontroller.enums.RESULT_DESCRIPTION;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.cybergarage.upnp.Device;
import org.cybergarage.util.Debug;
import org.cybergarage.util.TimerUtil;

public class MiDmrController extends StdDmrController implements IStdDmrController {
    private static final int MI_PUSH_ERROR_CODE_GET_TRANSPORT_STATE_SUCCESS = 1;
    private static final int MI_PUSH_ERROR_CODE_GET_TRANSPORT_STATE_TIMEOUT = 2;
    private static final int MI_PUSH_ERROR_CODE_STOP_FAILED = 0;
    public static final String STATE_CUSTOM = "CUSTOM";
    public static final String STATE_NO_MEDIA_PRESENT = "NO_MEDIA_PRESENT";
    public static final String STATE_PAUSED_PLAYBACK = "PAUSED_PLAYBACK";
    public static final String STATE_PAUSED_RECORDING = "PAUSED_RECORDING";
    public static final String STATE_PLAYING = "PLAYING";
    public static final String STATE_RECORDING = "RECORDING";
    public static final String STATE_STOPPED = "STOPPED";
    public static final String STATE_TRANSITIONING = "TRANSITIONING";
    public static final String TAG = new StringBuilder(String.valueOf(MiDmrController.class.getSimpleName())).append(": ").toString();
    private boolean isFutureCancel = false;

    class C00791 implements Callable<Integer> {
        C00791() {
        }

        public Integer call() throws Exception {
            MiDmrController.this.isFutureCancel = false;
            ActionResult initialStateResult = MiDmrController.this.getTransportStateNoException();
            if (initialStateResult != null && initialStateResult.isSuccessful()) {
                String initialState = initialStateResult.getResultString();
                if (!("PLAYING".equals(initialState) || "PAUSED_PLAYBACK".equals(initialState))) {
                    return Integer.valueOf(1);
                }
            }
            Debug.message(MiDmrController.TAG + "reset Midmr state start");
            ActionResult stopResult = null;
            try {
                stopResult = MiDmrController.this.stop();
            } catch (Exception ex) {
                Debug.message(MiDmrController.TAG + "stop exception");
                ex.printStackTrace();
            }
            Debug.message(MiDmrController.TAG + "stop result = " + stopResult);
            if (stopResult == null || stopResult.isSuccessful()) {
                return MiDmrController.this.transportStateCheck();
            }
            return Integer.valueOf(0);
        }
    }

    protected MiDmrController(Device targetDevice) {
        super(targetDevice);
    }

    protected boolean isSuitableDevice(Device device) {
        return Util.isMiDmrDevice(device);
    }

    protected void checkAviliableFunctions(Device targetDevice) {
        super.checkAviliableFunctions(targetDevice);
        this.mAvailableFunctions.remove(FUNCTION.GETVOLUME);
    }

    public ActionResult playMedia(String path, String title, MediaType type) {
        Debug.message("MiDmrController play!");
        if (!isFunctionAvailable(FUNCTION.PLAYMEDIA)) {
            return RESULT_UNAVAILABLE_FUNCTION;
        }
        ActionResult actionResult = pushUrl(path, title, type);
        return (actionResult == null || !actionResult.isSuccessful()) ? actionResult : super.play();
    }

    public ActionResult pushUrl(String path, String title, MediaType type) {
        Debug.message(TAG + "Midmr push url");
        ExecutorService executor = Executors.newSingleThreadExecutor();
        FutureTask<Integer> future = new FutureTask(new C00791());
        executor.execute(future);
        Integer result = null;
        executor.shutdown();
        try {
            result = (Integer) future.get(10, TimeUnit.SECONDS);
            Debug.message(TAG + "check state result = " + result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e2) {
            e2.printStackTrace();
        } catch (TimeoutException e3) {
            Debug.message(TAG + "get transportState timeout");
            e3.printStackTrace();
            result = Integer.valueOf(2);
        } finally {
            future.cancel(true);
            executor.shutdown();
            this.isFutureCancel = true;
        }
        if (result.intValue() == 0) {
            return new ActionResult(false, "can not stop mi box", RESULT_DESCRIPTION.PUSH_MI_STOP_ERROR);
        }
        if (result.intValue() == 2) {
            return new ActionResult(false, "transpostSate is not stopped", RESULT_DESCRIPTION.PUSH_MI_GETTRANSTATE_ERROR);
        }
        return super.pushUrl(path, title, type);
    }

    public Integer transportStateCheck() {
        ActionResult getTransStateResult = getTransportStateNoException();
        int checkCount = 1;
        while (!this.isFutureCancel && !isStateCorrect(getTransStateResult)) {
            Debug.message(TAG + "check count = " + checkCount + " state = " + getTransStateResult);
            TimerUtil.wait(1000);
            getTransStateResult = getTransportStateNoException();
            checkCount++;
        }
        return Integer.valueOf(1);
    }

    public ActionResult getTransportStateNoException() {
        ActionResult getTransStateResult = null;
        try {
            getTransStateResult = getTransportState();
        } catch (Exception ex) {
            Debug.message(TAG + "get mi transtate error");
            ex.printStackTrace();
        }
        return getTransStateResult;
    }

    private boolean isStateCorrect(ActionResult statusResult) {
        if (statusResult == null || !statusResult.isSuccessful()) {
            return false;
        }
        String state = statusResult.getResultString();
        if ("PAUSED_PLAYBACK".equals(state) || "PLAYING".equals(state)) {
            return false;
        }
        return true;
    }
}

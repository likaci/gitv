package com.gala.speedrunner.speedrunner;

public interface IRunSpeedCallback extends IFailureCallback {
    void onComplete(float f);

    void onProgress(float f);
}

package com.gala.android.dlna.sdk.stddmrcontroller;

import com.gala.android.dlna.sdk.controlpoint.MediaType;
import com.gala.android.dlna.sdk.stddmrcontroller.data.ActionResult;
import com.gala.android.dlna.sdk.stddmrcontroller.enums.FUNCTION;
import java.util.HashSet;

public interface IStdDmrController {
    HashSet<FUNCTION> getAvailableFunctions();

    ActionResult getMediaDuration();

    ActionResult getMediaUri();

    ActionResult getPosition();

    ActionResult getTransportState();

    ActionResult getVolume();

    boolean isFunctionAvailable(FUNCTION function);

    ActionResult pause();

    ActionResult play();

    ActionResult playMedia(String str, String str2, MediaType mediaType);

    ActionResult pushUrl(String str, String str2, MediaType mediaType);

    ActionResult seek(String str);

    ActionResult setVolume(int i);

    ActionResult stop();
}

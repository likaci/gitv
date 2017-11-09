package com.gala.video.app.epg.feedback;

import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.IFeedbackDialogController;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.IFeedbackKeyProcess;

public class FeedbackCreater {
    public static IFeedbackKeyProcess createKeyProcessor() {
        return new FeedbackKeyProcessor();
    }

    public static IFeedbackDialogController createDialogController() {
        return new FeedBackController();
    }
}

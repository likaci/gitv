package com.gala.video.app.epg.home.widget.actionbar;

import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class MessagePromptDispatcher {
    private Set<IMessageNotification> mMessageNotifications = new CopyOnWriteArraySet();
    private Set<IMessageUpdating> mMessageUpdatings = new CopyOnWriteArraySet();

    public interface IMessageNotification {
        void onMessageReceive(IMsgContent iMsgContent);
    }

    public interface IMessageUpdating {
        void onMessageUpdate();
    }

    private static class InstanceHolder {
        private static final MessagePromptDispatcher INSTANCE = new MessagePromptDispatcher();

        private InstanceHolder() {
        }
    }

    public static MessagePromptDispatcher get() {
        return InstanceHolder.INSTANCE;
    }

    public synchronized void register(IMessageNotification iMessageNotification) {
        this.mMessageNotifications.add(iMessageNotification);
    }

    public synchronized void unregister(IMessageNotification iMessageNotification) {
        this.mMessageNotifications.remove(iMessageNotification);
    }

    public synchronized void register(IMessageUpdating iMessageUpdating) {
        this.mMessageUpdatings.add(iMessageUpdating);
    }

    public synchronized void unregister(IMessageUpdating iMessageUpdating) {
        this.mMessageUpdatings.remove(iMessageUpdating);
    }

    public void onMessageReceive(IMsgContent content) {
        if (this.mMessageNotifications != null && this.mMessageNotifications.size() != 0) {
            for (IMessageNotification iMessageNotification : this.mMessageNotifications) {
                iMessageNotification.onMessageReceive(content);
            }
        }
    }

    public void onMessageUpdate() {
        if (this.mMessageUpdatings != null && this.mMessageUpdatings.size() != 0) {
            for (IMessageUpdating iMessageUpdating : this.mMessageUpdatings) {
                iMessageUpdating.onMessageUpdate();
            }
        }
    }
}

package com.squareup.picasso;

import android.app.Notification;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import com.squareup.picasso.Picasso.LoadedFrom;

abstract class RemoteViewsAction extends Action<RemoteViewsTarget> {
    final RemoteViews remoteViews;
    final int viewId;

    static class AppWidgetAction extends RemoteViewsAction {
        private final int[] appWidgetIds;

        AppWidgetAction(Picasso picasso, Request data, RemoteViews remoteViews, int viewId, int[] appWidgetIds, boolean skipCache, int errorResId, String key) {
            super(picasso, data, remoteViews, viewId, errorResId, skipCache, key);
            this.appWidgetIds = appWidgetIds;
        }

        void update() {
            AppWidgetManager.getInstance(this.picasso.context).updateAppWidget(this.appWidgetIds, this.remoteViews);
        }
    }

    static class NotificationAction extends RemoteViewsAction {
        private final Notification notification;
        private final int notificationId;

        NotificationAction(Picasso picasso, Request data, RemoteViews remoteViews, int viewId, int notificationId, Notification notification, boolean skipCache, int errorResId, String key) {
            super(picasso, data, remoteViews, viewId, errorResId, skipCache, key);
            this.notificationId = notificationId;
            this.notification = notification;
        }

        void update() {
            ((NotificationManager) Utils.getService(this.picasso.context, "notification")).notify(this.notificationId, this.notification);
        }
    }

    static class RemoteViewsTarget {
        final RemoteViews remoteViews;
        final int viewId;

        RemoteViewsTarget(RemoteViews remoteViews, int viewId) {
            this.remoteViews = remoteViews;
            this.viewId = viewId;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            RemoteViewsTarget remoteViewsTarget = (RemoteViewsTarget) o;
            if (this.viewId == remoteViewsTarget.viewId && this.remoteViews.equals(remoteViewsTarget.remoteViews)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return (this.remoteViews.hashCode() * 31) + this.viewId;
        }
    }

    abstract void update();

    RemoteViewsAction(Picasso picasso, Request data, RemoteViews remoteViews, int viewId, int errorResId, boolean skipCache, String key) {
        super(picasso, new RemoteViewsTarget(remoteViews, viewId), data, skipCache, false, errorResId, null, key);
        this.remoteViews = remoteViews;
        this.viewId = viewId;
    }

    void complete(Bitmap result, LoadedFrom from) {
        this.remoteViews.setImageViewBitmap(this.viewId, result);
        update();
    }

    public void error() {
        if (this.errorResId != 0) {
            setImageResource(this.errorResId);
        }
    }

    void setImageResource(int resId) {
        this.remoteViews.setImageViewResource(this.viewId, resId);
        update();
    }
}

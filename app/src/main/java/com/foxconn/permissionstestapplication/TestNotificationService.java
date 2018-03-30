package com.foxconn.permissionstestapplication;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import timber.log.Timber;

public class TestNotificationService extends NotificationListenerService {

    @Override
    public void onListenerConnected() {
        Timber.v("TestNotificationService#onListenerConnected");
    }

    @Override
    public void onListenerDisconnected() {
        Timber.v("TestNotificationService#onListenerDisconnected");
        super.onListenerDisconnected();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Timber.v("TestNotificationService#onNotificationPosted: " + sbn);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Timber.v("TestNotificationService#onNotificationRemoved: " + sbn);
    }
}

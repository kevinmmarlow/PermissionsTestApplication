package com.foxconn.permissionstestapplication;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.res.Configuration;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

import static android.accessibilityservice.AccessibilityServiceInfo.FLAG_REQUEST_FILTER_KEY_EVENTS;
import static android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;

/**
 TODO: Add Class Header
 */
public class TestAccessibilityService extends AccessibilityService implements AccessibilityActionPerformer {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
    }

    @Override
    public void onInterrupt() {
        // Required override, no-op
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        return false;
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        updateServiceInfo();
    }

    private void updateServiceInfo() {
        AccessibilityServiceInfo serviceInfo = getServiceInfo();
        if (serviceInfo == null) {
            return;
        }

        serviceInfo.flags = FLAG_REQUEST_FILTER_KEY_EVENTS;
        serviceInfo.eventTypes = TYPE_WINDOW_STATE_CHANGED;
        setServiceInfo(serviceInfo);
    }

    /**
     use the onConfigurationChange method to detect when external keyboard is attached. Note: at this point, I am not completely confident that this method
     works for both USB and BT keyboards

     @param newConfig the new configuration that is currently live
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}

package com.foxconn.permissionstestapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import java.util.List;

import timber.log.Timber;

/**
 TODO: Add Class Header
 */
public class PermissionManager {
    public static final int PERMISSION_TYPE_OVERLAY = 1;
    static final int PERMISSION_TYPE_LOCATION = 2;
    static final int PERMISSION_TYPE_WRITE_EXTERNAL_STORAGE = 3;
    static final int PERMISSION_TYPE_READ_PHONE_STATE = 4;
    public static final int PERMISSION_TYPE_ACCESSIBILITY = 5;
    public static final int PERMISSION_TYPE_APP_USAGE = 6;
    public static final int PERMISSION_TYPE_NOTIFICATIONS = 7;
    static final int PERMISSION_TYPE_MODIFY_SYSTEM = 8;
    public static final int PERMISSION_TYPE_DEV_OPTIONS = 9;
    public static final int PERMISSION_TYPE_FORCE_RESIZE_ACTIVITIES = 10;

    private static final int ONE_HOUR = 60 * 60 * 1000;
    public static final String PACKAGE_PREFIX = "package:";
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final String DEVELOPMENT_FORCE_RESIZABLE_ACTIVITIES = "force_resizable_activities";
    private static final String DEVELOPMENT_ENABLE_FREEFORM_WINDOWS_SUPPORT = "enable_freeform_support";

    private final Context context;
    private final UsageStatsManager usageStatsManager;

    public PermissionManager(Context context,
                             UsageStatsManager usageStatsManager) {
        this.context = context;
        this.usageStatsManager = usageStatsManager;
    }

    public boolean hasLocationPermission() {
        return !(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED);
    }

    public boolean hasWritePermission() {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean hasPermissions(@NonNull String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public boolean hasReadPhoneStatePermission() {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasOverlayPermission() {
        return Settings.canDrawOverlays(context.getApplicationContext());
    }

    public boolean hasEnabledDeveloperMode() {
        return Settings.Global.getInt(context.getApplicationContext().getContentResolver(), Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0) == 1;
    }

    public boolean hasFreeformSupport() {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_FREEFORM_WINDOW_MANAGEMENT)
                || hasSystemFreeFormSupport()
                || hasResizableEnable();
    }

    private boolean hasResizableEnable() {
        return Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1
                && Settings.Global.getInt(context.getContentResolver(), DEVELOPMENT_FORCE_RESIZABLE_ACTIVITIES, -1) == 1;
    }

    private boolean hasSystemFreeFormSupport() {
        return Settings.Global.getInt(context.getContentResolver(), DEVELOPMENT_ENABLE_FREEFORM_WINDOWS_SUPPORT, -1) == 1;
    }

    public boolean hasAccessibilityPermission() {
        int accessibilityEnabled = 0;
        final String service = context.getPackageName() + "/" + TestAccessibilityService.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    context.getApplicationContext().getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
            Timber.v("accessibilityEnabled = %s", accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            Timber.e("Error finding setting, default accessibility to not found: %s", e.getMessage());
        }
        TextUtils.SimpleStringSplitter stringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {

            String settingValue = Settings.Secure.getString(
                    context.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                stringColonSplitter.setString(settingValue);
                while (stringColonSplitter.hasNext()) {
                    String accessibilityService = stringColonSplitter.next();
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean hasAppUsageDataPermission() {

        long now = System.currentTimeMillis();
        long oneHourAgo = now - ONE_HOUR;
        List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY, oneHourAgo, now);

        return queryUsageStats != null && !queryUsageStats.isEmpty();
    }

    public boolean hasNotificationAccessPermission() {
        String packageName = context.getPackageName();
        ContentResolver contentResolver = context.getContentResolver();

        final String flat = Settings.Secure.getString(contentResolver, ENABLED_NOTIFICATION_LISTENERS);
        if (TextUtils.isEmpty(flat)) {
            return false;
        }
        final String[] names = flat.split(":");
        for (String name : names) {
            final ComponentName cn = ComponentName.unflattenFromString(name);
            if (cn != null && TextUtils.equals(packageName, cn.getPackageName())) {
                return true;
            }
        }

        return false;
    }

    public boolean hasModifySystemPermission() {
        return Settings.System.canWrite(context);
    }

    public boolean hasPermissionForType(@PermissionType int permissionType) {
        switch (permissionType) {
            case PERMISSION_TYPE_ACCESSIBILITY:
                return hasAccessibilityPermission();
            case PERMISSION_TYPE_APP_USAGE:
                return hasAppUsageDataPermission();
            case PERMISSION_TYPE_LOCATION:
                return hasLocationPermission();
            case PERMISSION_TYPE_MODIFY_SYSTEM:
                return hasModifySystemPermission();
            case PERMISSION_TYPE_NOTIFICATIONS:
                return hasNotificationAccessPermission();
            case PERMISSION_TYPE_OVERLAY:
                return hasOverlayPermission();
            case PERMISSION_TYPE_READ_PHONE_STATE:
                return hasReadPhoneStatePermission();
            case PERMISSION_TYPE_WRITE_EXTERNAL_STORAGE:
                return hasWritePermission();
            case PERMISSION_TYPE_DEV_OPTIONS:
                return hasEnabledDeveloperMode();
            case PERMISSION_TYPE_FORCE_RESIZE_ACTIVITIES:
                return hasFreeformSupport();
        }

        return false;
    }
}

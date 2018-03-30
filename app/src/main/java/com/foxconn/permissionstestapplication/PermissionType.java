package com.foxconn.permissionstestapplication;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

@Retention(SOURCE)
@IntDef({PermissionManager.PERMISSION_TYPE_OVERLAY, PermissionManager.PERMISSION_TYPE_LOCATION,
                PermissionManager.PERMISSION_TYPE_WRITE_EXTERNAL_STORAGE, PermissionManager.PERMISSION_TYPE_READ_PHONE_STATE,
                PermissionManager.PERMISSION_TYPE_ACCESSIBILITY, PermissionManager.PERMISSION_TYPE_APP_USAGE,
                PermissionManager.PERMISSION_TYPE_NOTIFICATIONS, PermissionManager.PERMISSION_TYPE_MODIFY_SYSTEM,
                PermissionManager.PERMISSION_TYPE_DEV_OPTIONS, PermissionManager.PERMISSION_TYPE_FORCE_RESIZE_ACTIVITIES})
public @interface PermissionType {}

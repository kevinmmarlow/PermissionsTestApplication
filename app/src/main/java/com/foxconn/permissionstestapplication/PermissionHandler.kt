package com.foxconn.permissionstestapplication

import android.Manifest
import android.content.pm.PackageManager
import android.support.annotation.NonNull
import android.util.SparseArray
import com.foxconn.permissionstestapplication.AskPermissionAction.Companion.BluetoothPermission
import com.foxconn.permissionstestapplication.AskPermissionAction.Companion.CameraPermission
import com.foxconn.permissionstestapplication.AskPermissionAction.Companion.DensityPermission
import com.foxconn.permissionstestapplication.AskPermissionAction.Companion.DesktopImagePreviewPermission
import com.foxconn.permissionstestapplication.AskPermissionAction.Companion.NONE
import com.foxconn.permissionstestapplication.AskPermissionAction.Companion.OpenFilePermission
import com.foxconn.permissionstestapplication.AskPermissionAction.Companion.ScreenRatioPermission
import com.foxconn.permissionstestapplication.AskPermissionAction.Companion.WifiPermission
import java.lang.Exception

/**
TODO: Add Class Header
 */
open class PermissionHandler constructor(private val permissionManager: PermissionManager) {
    private val requestCodeToPermissions = SparseArray<Array<String>>()

    init {
        requestCodeToPermissions.put(DesktopImagePreviewPermission, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
        requestCodeToPermissions.put(BluetoothPermission, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
        requestCodeToPermissions.put(WifiPermission, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
        requestCodeToPermissions.put(OpenFilePermission, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))
        requestCodeToPermissions.put(DensityPermission, arrayOf(Manifest.permission.WRITE_SECURE_SETTINGS))
        requestCodeToPermissions.put(CameraPermission, arrayOf(Manifest.permission.CAMERA))
        requestCodeToPermissions.put(ScreenRatioPermission, arrayOf(Manifest.permission.WRITE_SECURE_SETTINGS))
    }

    open fun checkPermission(requestCode: Int): Boolean {
        val permissions = requestCodeToPermissions[requestCode]
        if (permissions == null) {
            throw EmptyPermissionException(requestCode)
        } else {
            return permissionManager.hasPermissions(permissions)
        }
    }

    open fun handlePermission(requestCode: Int, @NonNull permissions: Array<String>,
                              @NonNull grantResults: IntArray): Int {
        val permissionSet = requestCodeToPermissions[requestCode].toSet()
        val success = grantResults.isNotEmpty() && grantResults
                .mapIndexed { index, result -> checkAndAskPermission(permissionSet, permissions[index], result) }
                .reduce { acc, isSuccess -> acc && isSuccess }
        return if (success) requestCode else NONE
    }

    fun isDpiChangeEnabled(): Boolean {
        return checkPermission(AskPermissionAction.DensityPermission)
    }

    fun isScreenRatioChangeSettingEnabled(): Boolean {
        return checkPermission(AskPermissionAction.ScreenRatioPermission)
    }

    private fun checkAndAskPermission(permissionSet: Set<String>, permission: String, result: Int): Boolean =
            permissionSet.contains(permission) && result == PackageManager.PERMISSION_GRANTED
}

class EmptyPermissionException(private val requestCode: Int) : Exception() {

    override val message: String?
        get() = "Empty permission for request: $requestCode, please check PermissionHandler"
}

class AskPermissionAction(val requestCode: Int, val permissions: Array<String>) {
    companion object {
        val NONE = 0
        val BluetoothPermission = 1
        val WifiPermission = 2
        val DesktopImagePreviewPermission = 3
        val OpenFilePermission = 4
        val DensityPermission = 5
        val CameraPermission = 6
        val ScreenRatioPermission = 7
    }
}
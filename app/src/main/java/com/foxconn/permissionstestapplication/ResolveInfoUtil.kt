package com.foxconn.permissionstestapplication

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

/**
 * TODO: Add Class Header
 */
class ResolveInfoUtil(private val context: Context,
                      private val packageManager: PackageManager) {

    private val currentLauncher: String
        get() {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            val info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
            return if (info == null) "" else info.activityInfo.packageName
        }

    val isCurrentLauncher: Boolean
        get() = context.packageName == currentLauncher
}

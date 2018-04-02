package com.foxconn.permissionstestapplication

import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private val adapter = PermissionsAdapter()

    private val permissionManager: PermissionManager by lazy {
        PermissionManager(this, usageStatsManager)
    }

    private val permissionHandler: PermissionHandler by lazy {
        PermissionHandler(permissionManager)
    }

    private val usageStatsManager: UsageStatsManager by lazy {
        getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    }

    private val resolveInfoUtil: ResolveInfoUtil by lazy {
        ResolveInfoUtil(this, packageManager)
    }

    private val viewModels: List<PermissionViewModel> by lazy {
        val viewModels = mutableListOf<PermissionViewModel>()
        viewModels.add(PermissionViewModel("IS HOME LAUNCHER", resolveInfoUtil.isCurrentLauncher))
        viewModels.add(PermissionViewModel("PERMISSION_TYPE_ACCESSIBILITY", permissionManager.hasAccessibilityPermission()))
        viewModels.add(PermissionViewModel("PERMISSION_TYPE_APP_USAGE", permissionManager.hasAppUsageDataPermission()))
        viewModels.add(PermissionViewModel("PERMISSION_TYPE_DEV_OPTIONS", permissionManager.hasEnabledDeveloperMode()))
        viewModels.add(PermissionViewModel("PERMISSION_TYPE_FORCE_RESIZE_ACTIVITIES", permissionManager.hasFreeformSupport()))
        viewModels.add(PermissionViewModel("PERMISSION_TYPE_LOCATION", permissionManager.hasLocationPermission()))
        viewModels.add(PermissionViewModel("PERMISSION_TYPE_MODIFY_SYSTEM", permissionManager.hasModifySystemPermission()))
        viewModels.add(PermissionViewModel("PERMISSION_TYPE_NOTIFICATIONS", permissionManager.hasNotificationAccessPermission()))
        viewModels.add(PermissionViewModel("PERMISSION_TYPE_OVERLAY", permissionManager.hasOverlayPermission()))
        viewModels.add(PermissionViewModel("PERMISSION_TYPE_READ_PHONE_STATE", permissionManager.hasReadPhoneStatePermission()))
        viewModels.add(PermissionViewModel("PERMISSION_TYPE_WRITE_EXTERNAL_STORAGE", permissionManager.hasWritePermission()))
        viewModels.add(PermissionViewModel("DPI CHANGE (MODIFY SYSTEM SETTINGS)", permissionHandler.isDpiChangeEnabled()))
        viewModels.add(PermissionViewModel("SCREEN RATIO (MODIFY SYSTEM SETTINGS)", permissionHandler.isScreenRatioChangeSettingEnabled()))

        return@lazy viewModels.toList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)

        permissionManager.enableAccessibilityPermission()
        permissionManager.enableNotificationAccessPermission()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        adapter.setItemClickListener {
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.setData(viewModels)
    }

    override fun onBackPressed() {
        if (resolveInfoUtil.isCurrentLauncher) {
            Toast.makeText(this, "You are currently the home launcher, cannot go back. Please change in settings if needed.", Toast.LENGTH_SHORT).show()
        } else {
            super.onBackPressed()
        }
    }
}

package com.cargocarriers.dispatch

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat

class PermissionRequest {
    companion object {

        fun permissions(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val PERMISSION_ALL = 1
                val PERMISSIONS =
                    arrayOf(Manifest.permission.CAMERA)
                for (permission in PERMISSIONS) {
                    if (!hasPermissions(activity, permission)) {
                        ActivityCompat.requestPermissions(activity, PERMISSIONS, PERMISSION_ALL)
                    }
                }
            }
        }

        private fun hasPermissions(
            context: Context?,
            permissions: String?
        ): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
                ActivityCompat.checkSelfPermission(
                    context,
                    permissions
                ) == PackageManager.PERMISSION_GRANTED
            } else true
        }

    }
}
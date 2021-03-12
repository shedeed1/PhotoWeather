package com.robusta.photoweather.utilities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat


object PermissionUtil {
    fun isGalleryPermissionGranted(activity: Activity?): Boolean {
        return ActivityCompat.checkSelfPermission(
            activity!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun isCameraPermissionGranted(activity: Activity?): Boolean {
        return ActivityCompat.checkSelfPermission(
            activity!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestCameraPermission(activity: Activity?, Camera_REQUEST_PERMISSION_CODE: Int) {
        ActivityCompat.requestPermissions(
            activity!!, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ), Camera_REQUEST_PERMISSION_CODE
        )
    }

    fun requestGalleryPermission(activity: Activity?, GALLERY_REQUEST_PERMISSION_CODE: Int) {
        ActivityCompat.requestPermissions(
            activity!!, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), GALLERY_REQUEST_PERMISSION_CODE
        )
    }

    fun isLocationPermissionGranted(activity: Activity?): Boolean {
        return ActivityCompat.checkSelfPermission(
            activity!!,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestLocationPermission(activity: Activity?, LOCATION_REQUEST_CODE: Int) {
        ActivityCompat.requestPermissions(
            activity!!, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            ), LOCATION_REQUEST_CODE
        )
    }
}
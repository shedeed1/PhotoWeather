package com.robusta.photoweather.utilities

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.robusta.photoweather.LOCATION_PERMISSION


object LocationUtil {

    private var mLocationCallback: LocationCallback? = null

    /**
     * This will be used only when provider has request and doing work to draw his marker on the map.
     *
     * It is the responsibility of the caller to call [stopCurrentLocationUpdates] after finishing using
     * this function to prevent memory leak and battery consumption.
     */
    @SuppressLint("MissingPermission")
    fun getCurrentLocation(
        context: AppCompatActivity,
        body: (newLocation: Location?) -> Unit
    ) {
        // Permission Checks.
        if (!PermissionUtil.isLocationPermissionGranted(context)) {
            PermissionUtil.requestLocationPermission(context, LOCATION_PERMISSION)
            return
        }
        val locationRequest = LocationRequest().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY // todo these should be the same as what is used with location service.
            interval = 0
            fastestInterval = interval / 2
        }

        val builder = LocationSettingsRequest.Builder().apply {
            addLocationRequest(locationRequest)
        }

        val settingsClient = LocationServices.getSettingsClient(context)
        settingsClient.checkLocationSettings(builder.build())

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?)
                    = body(locationResult?.lastLocation)
        }

        getFusedLocationProviderClient(context)?.requestLocationUpdates(
            locationRequest,
            mLocationCallback,
            Looper.myLooper()
        )
    }

    fun stopCurrentLocationUpdates(context: AppCompatActivity) =
        mLocationCallback?.let {
            getFusedLocationProviderClient(context)?.removeLocationUpdates(it)
        }
}
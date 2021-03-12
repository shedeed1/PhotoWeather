package com.robusta.photoweather.utilities

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient


object LocationUtil {

    private var mLocationCallback: LocationCallback? = null

    private val TAG = LocationUtil::class.java.simpleName

    const val REQUEST_CODE_LOCATION = 1111

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
            PermissionUtil.requestLocationPermission(context, REQUEST_CODE_LOCATION)
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

        Log.i(TAG,"Current Location Updates STARTED Successfully.")
    }

    fun stopCurrentLocationUpdates(context: AppCompatActivity) =
        mLocationCallback?.let {
            getFusedLocationProviderClient(context)?.removeLocationUpdates(it)
            Log.i(TAG,"Current Location Updates Stopped Successfully.")
        }
}
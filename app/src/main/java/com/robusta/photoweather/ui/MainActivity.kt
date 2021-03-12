package com.robusta.photoweather.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.robusta.photoweather.data.response.WeatherResponse
import com.robusta.photoweather.databinding.ActivityMainBinding
import com.robusta.photoweather.getViewModel
import com.robusta.photoweather.utilities.LocationUtil
import com.robusta.photoweather.utilities.PermissionUtil
import com.robusta.photoweather.viewmodel.WeatherViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        val camera = binding.camera

        camera.setLifecycleOwner(this)

        binding.captureBtn.setOnClickListener {
            requestLocation()
        }
    }

    private fun requestLocation() {
        if (!PermissionUtil.isLocationPermissionGranted(this))
            PermissionUtil.requestLocationPermission(this@MainActivity, 1111)
        else
        {
            LocationUtil.getCurrentLocation(this@MainActivity) {

                it?.run {
                    val vm by lazy {
                        getViewModel {
                            WeatherViewModel(
                                it.latitude,it.longitude
                            )
                        }
                    }

                    vm.getWeather().observe(this@MainActivity, Observer<WeatherResponse>{
                        binding.textView.text = it.list.get(0).weather.get(0).description
                    })

                    LocationUtil.stopCurrentLocationUpdates(this@MainActivity)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1111 -> {
                if (PermissionUtil.isLocationPermissionGranted(this))
                {
                    LocationUtil.getCurrentLocation(this@MainActivity) {
                        it?.run {
                            if (it.isFromMockProvider)
                            //viewModel.displaySnackMsg(getString(R.string.fake_location_not_allowed))
                            else
                                Log.i("Test", it.latitude.toString());
                            //viewModel.newPunch(it.latitude, it.longitude)

                            LocationUtil.stopCurrentLocationUpdates(this@MainActivity)
                        }
                    }
                }
                else {
                    // TODO: Handle failing to get permissions
                }

            }
        }
    }
}
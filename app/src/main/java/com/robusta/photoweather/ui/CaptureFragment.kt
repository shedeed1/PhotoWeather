package com.robusta.photoweather.ui

import android.graphics.Camera
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.otaliastudios.cameraview.BitmapCallback
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.PictureResult
import com.robusta.photoweather.R
import com.robusta.photoweather.data.response.WeatherResponse
import com.robusta.photoweather.databinding.ActivityMainBinding
import com.robusta.photoweather.databinding.FragmentCaptureBinding
import com.robusta.photoweather.getViewModel
import com.robusta.photoweather.utilities.LocationUtil
import com.robusta.photoweather.utilities.PermissionUtil
import com.robusta.photoweather.viewmodel.WeatherViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [CaptureFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CaptureFragment : Fragment() {

    private var _binding: FragmentCaptureBinding? = null
    private val binding get() = _binding!!

    private var camera: CameraView? = null

    private fun requestLocation() {
        if (!PermissionUtil.isLocationPermissionGranted(activity))
            PermissionUtil.requestLocationPermission(activity, 1111)
        else
        {
            LocationUtil.getCurrentLocation(activity as AppCompatActivity) {

                it?.run {
                    val vm by lazy {
                        getViewModel {
                            WeatherViewModel(
                                it.latitude,it.longitude
                            )
                        }
                    }

                    vm.getWeather().observe(viewLifecycleOwner, Observer<WeatherResponse>{
                        // Capture picture

                        binding.cityName.text = it.list.get(0).name
                        binding.temperature.text = (it.list.get(0).main.temp - 273).toString()
                        binding.description.text = it.list.get(0).weather.get(0).description

                        camera?.takePictureSnapshot()
                    })

                    LocationUtil.stopCurrentLocationUpdates(activity as AppCompatActivity)
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
                if (PermissionUtil.isLocationPermissionGranted(activity))
                {
                    LocationUtil.getCurrentLocation(activity as AppCompatActivity) {
                        it?.run {
                            // TODO

                            LocationUtil.stopCurrentLocationUpdates(activity as AppCompatActivity)
                        }
                    }
                }
                else {
                    // TODO: Handle failing to get permissions
                }

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCaptureBinding.inflate(inflater, container, false)
        val view = binding.root

        camera = binding.camera

        camera!!.setLifecycleOwner(viewLifecycleOwner)

        binding.captureBtn.setOnClickListener {
            requestLocation()
        }

        camera?.addCameraListener(mCameraListener)

        return view
    }

    private var mCameraListener = object : CameraListener() {
        override fun onPictureTaken(result: PictureResult) {
            super.onPictureTaken(result)
            result.toBitmap(BitmapCallback {
                val bundle = Bundle()
                bundle.putParcelable("PICTURE",it)
                findNavController(this@CaptureFragment).navigate(R.id.action_captureFragment_to_resultFragment, bundle)
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
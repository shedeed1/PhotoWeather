package com.robusta.photoweather.ui.capture

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.otaliastudios.cameraview.BitmapCallback
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.PictureResult
import com.robusta.photoweather.*
import com.robusta.photoweather.data.response.WeatherResponse
import com.robusta.photoweather.databinding.FragmentCaptureBinding
import com.robusta.photoweather.utilities.LocationUtil
import com.robusta.photoweather.utilities.PermissionUtil
import com.robusta.photoweather.viewmodel.WeatherViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*
import kotlin.math.roundToInt


/**
 * A simple [Fragment] subclass.
 * Use the [CaptureFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CaptureFragment : Fragment() {

    private var _binding: FragmentCaptureBinding? = null
    private val binding get() = _binding!!

    private var camera: CameraView? = null

    @SuppressLint("SetTextI18n")
    private fun requestLocation() {
        if (!PermissionUtil.isLocationPermissionGranted(activity))
            PermissionUtil.requestLocationPermission(activity, LOCATION_PERMISSION)
        else
        {
            sendCoordinates()
        }
    }

    private fun sendCoordinates()
    {
        LocationUtil.getCurrentLocation(activity as AppCompatActivity) {

            it?.run {
                val vm by lazy {
                    getViewModel {
                        WeatherViewModel(
                            it.latitude, it.longitude
                        )
                    }
                }

                vm.getWeather().observe(viewLifecycleOwner, Observer<WeatherResponse> {
                    binding.cityName.text = it.list.get(0).name
                    binding.temperature.text = (it.list.get(0).main.temp - 273).roundToInt().toString() + " Celsius"
                    binding.description.text = it.list.get(0).weather.get(0).description

                    binding.cameraOverlay.visibility = VISIBLE

                    camera?.takePictureSnapshot()
                })

                LocationUtil.stopCurrentLocationUpdates(activity as AppCompatActivity)
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
            LOCATION_PERMISSION -> {
                if (PermissionUtil.isLocationPermissionGranted(activity)) {
                    LocationUtil.getCurrentLocation(activity as AppCompatActivity) {
                        it?.run {
                            sendCoordinates()
                        }
                    }
                } else {
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
            binding.captureBtn.visibility = INVISIBLE
            binding.loadingAnimationn.visibility = VISIBLE
            requestLocation()
        }

        camera?.addCameraListener(mCameraListener)

        return view
    }

    private var mCameraListener = object : CameraListener() {
        override fun onPictureTaken(result: PictureResult) {
            super.onPictureTaken(result)
            result.toBitmap(BitmapCallback {
                val uri = saveImageToInternalStorage(it!!)
                val bundle = Bundle()
                bundle.putParcelable(CAPTURE_TO_RESULT_PICTURE, it)
                bundle.putString(CAPTURE_TO_RESULT_PICTURE_URI, uri.toString())

                findNavController(this@CaptureFragment).navigate(R.id.action_captureFragment_to_resultFragment, bundle)
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun saveImageToInternalStorage(image: Bitmap): Uri {

        var file = context?.cacheDir

        file = File(file, "${UUID.randomUUID()}.jpeg")

        try {
            val stream: OutputStream = FileOutputStream(file)
            image.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException){
            e.printStackTrace()
        }
        return FileProvider.getUriForFile(requireContext(),"com.robusta.photoweather.fileprovider",file)

    }
}
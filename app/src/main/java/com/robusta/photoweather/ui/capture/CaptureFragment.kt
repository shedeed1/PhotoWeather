package com.robusta.photoweather.ui.capture

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
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
import com.robusta.photoweather.CAPTURE_TO_RESULT_PICTURE
import com.robusta.photoweather.CAPTURE_TO_RESULT_PICTURE_URI
import com.robusta.photoweather.R
import com.robusta.photoweather.data.response.WeatherResponse
import com.robusta.photoweather.databinding.FragmentCaptureBinding
import com.robusta.photoweather.getViewModel
import com.robusta.photoweather.utilities.LocationUtil
import com.robusta.photoweather.utilities.PermissionUtil
import com.robusta.photoweather.viewmodel.WeatherViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*


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
                                it.latitude, it.longitude
                            )
                        }
                    }

                    vm.getWeather().observe(viewLifecycleOwner, Observer<WeatherResponse> {
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
                if (PermissionUtil.isLocationPermissionGranted(activity)) {
                    LocationUtil.getCurrentLocation(activity as AppCompatActivity) {
                        it?.run {
                            // TODO

                            LocationUtil.stopCurrentLocationUpdates(activity as AppCompatActivity)
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

//                val intent = Intent()
//                intent.action = Intent.ACTION_SEND
//                intent.putExtra(Intent.EXTRA_STREAM, uri)
//                intent.type = "image/jpeg"
//
//                val shareIntent = Intent.createChooser(intent, "Share File")
//                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION)
//
//
//                startActivity(shareIntent)
                findNavController(this@CaptureFragment).navigate(R.id.action_captureFragment_to_resultFragment, bundle)
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun saveImageToInternalStorage(image: Bitmap): Uri {

        // Initializing a new file
        // The bellow line return a directory in internal storage
        var file = context?.cacheDir

        // Create a file to save the image
        file = File(file, "${UUID.randomUUID()}.jpeg")

        try {
            // Get the file output stream
            val stream: OutputStream = FileOutputStream(file)

            // Compress bitmap
            image.compress(Bitmap.CompressFormat.JPEG, 100, stream)

            // Flush the stream
            stream.flush()

            // Close stream
            stream.close()
        } catch (e: IOException){ // Catch the exception
            e.printStackTrace()
        }

        // Return the saved image uri
//        Log.i("Shedeed", file.absolutePath)
//        var files = File(file.parent)
//
//        for (file in files.listFiles())
//        {
//            Log.i("Shedeed",file.absolutePath)
//        }
        return FileProvider.getUriForFile(requireContext(),"com.robusta.photoweather.fileprovider",file)
        //return Uri.parse(file.absolutePath)
    }
}
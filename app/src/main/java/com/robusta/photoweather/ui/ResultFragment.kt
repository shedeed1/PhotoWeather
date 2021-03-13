package com.robusta.photoweather.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.robusta.photoweather.R
import com.robusta.photoweather.databinding.FragmentCaptureBinding
import com.robusta.photoweather.databinding.FragmentResultBinding

/**
 * A simple [Fragment] subclass.
 * Use the [ResultFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.imageView.setImageBitmap(arguments?.getParcelable("PICTURE"))

        return view
    }
}
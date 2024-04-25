package com.example.movieapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentVideoPlayBinding
import com.example.movieapp.utils.APIConstants

class VideoPlayFragment : Fragment() {

    private lateinit var binding: FragmentVideoPlayBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_video_play, container, false)
        val trailerPath = arguments?.get("videoUrl")
        if (trailerPath != null) {
            binding.webView.settings.javaScriptEnabled = true
            val html =
                "<iframe width=\"100%\" height=\"100%\" src=\"${APIConstants.YOUTUBE_URL}$trailerPath\" frameborder=\"0\" allowfullscreen></iframe>"
            binding.webView.loadData(html, "text/html", "utf-8")
        }
        return binding.root
    }
}
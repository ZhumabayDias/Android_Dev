package com.example.myfinalapp.foreground

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myfinalapp.MusicService
import com.example.myfinalapp.databinding.FragmentForegroundBinding

class ForegroundFragment : Fragment() {

    private var _binding: FragmentForegroundBinding? = null
    private val binding: FragmentForegroundBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForegroundBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnStart.setOnClickListener {
            val intent = Intent(requireContext(), MusicService::class.java).apply {
                action = "START"
            }
            requireActivity().startService(intent)
        }

        binding.btnPause.setOnClickListener {
            val intent = Intent(requireContext(), MusicService::class.java).apply {
                action = "PAUSE"
            }
            requireActivity().startService(intent)
        }

        binding.btnStop.setOnClickListener {
            val intent = Intent(requireContext(), MusicService::class.java).apply {
                action = "STOP"
            }
            requireActivity().startService(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
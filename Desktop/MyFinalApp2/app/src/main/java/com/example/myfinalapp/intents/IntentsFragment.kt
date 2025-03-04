package com.example.myfinalapp.intents

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.myfinalapp.databinding.FragmentIntentsBinding

class IntentsFragment : Fragment() {

    private var _binding: FragmentIntentsBinding? = null
    private val binding: FragmentIntentsBinding get() = _binding!!

    private var selectedImageUri: Uri? = null
    private val INSTAGRAM_PACKAGE_NAME = "com.instagram.android"

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedImageUri = it
                shareImageToInstagramStory(it)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIntentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnShare.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
    }

    private fun shareImageToInstagramStory(imageUri: Uri) {
        val intent = Intent("com.instagram.share.ADD_TO_STORY").apply {
            setDataAndType(imageUri, "image/*")
            putExtra("source_application", requireContext().packageName)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            setPackage(INSTAGRAM_PACKAGE_NAME)
        }

        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(requireContext(), "Не удалось открыть Instagram Stories!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
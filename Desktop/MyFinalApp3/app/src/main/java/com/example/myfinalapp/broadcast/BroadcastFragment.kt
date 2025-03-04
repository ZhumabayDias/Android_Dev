package com.example.myfinalapp.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myfinalapp.R
import com.example.myfinalapp.databinding.FragmentBroadcastBinding
import com.example.myfinalapp.databinding.FragmentForegroundBinding

class BroadcastFragment : Fragment() {

    private var _binding: FragmentBroadcastBinding? = null
    private val binding: FragmentBroadcastBinding get() = _binding!!

    private lateinit var airplaneModeReceiver: BroadcastReceiver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBroadcastBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        airplaneModeReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
                    val isAirplaneModeOn = Settings.Global.getInt(
                        context.contentResolver,
                        Settings.Global.AIRPLANE_MODE_ON, 0
                    ) != 0

                    val message = if (isAirplaneModeOn) "Airplane mode is turned on" else "Airplane mode is turned off"
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
            }
        }
        }
    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        requireContext().registerReceiver(airplaneModeReceiver, filter)
    }

    override fun onPause() {
        super.onPause()
        requireContext().unregisterReceiver(airplaneModeReceiver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
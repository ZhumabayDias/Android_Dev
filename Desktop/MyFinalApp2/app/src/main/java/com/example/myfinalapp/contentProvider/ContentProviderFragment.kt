package com.example.myfinalapp.contentProvider

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfinalapp.R
import com.example.myfinalapp.contentProvider.adapter.CalendarAdapter
import com.example.myfinalapp.contentProvider.model.CalendarEvent
import com.example.myfinalapp.databinding.FragmentBroadcastBinding
import com.example.myfinalapp.databinding.FragmentContentProviderBinding


class ContentProviderFragment : Fragment() {

    private var _binding: FragmentContentProviderBinding? = null
    private val binding: FragmentContentProviderBinding get() = _binding!!

    private val PERMISSIONS_REQUEST_READ_CALENDAR = 100

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContentProviderBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun requestCalendarPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CALENDAR)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.READ_CALENDAR),
                PERMISSIONS_REQUEST_READ_CALENDAR)
        } else {
            fetchCalendarEvents()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_READ_CALENDAR && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            fetchCalendarEvents()
        }
    }

    private fun fetchCalendarEvents() {
        val eventsList = mutableListOf<CalendarEvent>()
        val contentResolver: ContentResolver = requireContext().contentResolver

        val uri: Uri = CalendarContract.Events.CONTENT_URI
        val projection = arrayOf(
            CalendarContract.Events._ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART
        )
        val selection = "${CalendarContract.Events.DTSTART} >= ?"
        val selectionArgs = arrayOf(System.currentTimeMillis().toString())
        val sortOrder = "${CalendarContract.Events.DTSTART} ASC"

        val cursor: Cursor? = contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)

        cursor?.use {
            val idIndex = it.getColumnIndex(CalendarContract.Events._ID)
            val titleIndex = it.getColumnIndex(CalendarContract.Events.TITLE)
            val dateIndex = it.getColumnIndex(CalendarContract.Events.DTSTART)

            while (it.moveToNext()) {
                val eventId = it.getLong(idIndex)
                val eventTitle = it.getString(titleIndex)
                val eventDate = it.getLong(dateIndex)
                eventsList.add(CalendarEvent(eventId, eventTitle, eventDate))
            }
        }

        cursor?.close()
        updateRecyclerView(eventsList)
    }

    private fun updateRecyclerView(events: List<CalendarEvent>) {
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = CalendarAdapter(events)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestCalendarPermission()
    }

}
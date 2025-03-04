package com.example.myfinalapp.contentProvider.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myfinalapp.R
import com.example.myfinalapp.contentProvider.model.CalendarEvent
import java.text.SimpleDateFormat
import java.util.*

class CalendarAdapter(private val events: List<CalendarEvent>) :
    RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_event, parent, false)
        return CalendarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val event = events[position]
        holder.bind(event)
    }

    override fun getItemCount(): Int = events.size

    class CalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.eventTitle)
        private val date: TextView = itemView.findViewById(R.id.eventDate)

        fun bind(event: CalendarEvent) {
            title.text = event.title
            date.text = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()).format(Date(event.date))
        }
    }
}
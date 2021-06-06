package com.viet.mydiary.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.viet.mydiary.model.Events
import com.viet.mydiary.utils.OnItemClickListener
import com.viet.mydiary.R
import java.text.SimpleDateFormat

class EventAdapter(private var eventList: MutableList<Events>, var listener: OnItemClickListener) :
    RecyclerView.Adapter<EventAdapter.ViewHolder>(), Filterable {

    var list: MutableList<Events> = eventList
    var filter: CustomFilter = CustomFilter()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textViewTitle.text = eventList[position].title
        holder.textViewEvent.text = eventList[position].event
        holder.textViewTime.text = SimpleDateFormat("hh:mm").format(eventList[position].date)
        holder.textViewDate.text = SimpleDateFormat("dd/MM/yyyy").format(eventList[position].date)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textViewTitle: TextView = view.findViewById(R.id.textViewTitle)
        var textViewEvent: TextView = view.findViewById(R.id.textViewEvent)
        var textViewTime: TextView = view.findViewById(R.id.textViewTime)
        var textViewDate: TextView = view.findViewById(R.id.textViewDate)

        init {
            view.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun getFilter(): Filter {
        return filter;
    }

    inner class CustomFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results: FilterResults = FilterResults()
            if (constraint != null && constraint.isNotEmpty()) {
                val queryString = constraint.toString().toLowerCase()
                val filters: MutableList<Events> = mutableListOf()

                list.forEach {
                    if (it.event.toLowerCase().contains(queryString) ||
                        it.title.toLowerCase().contains(queryString)
                    )
                        filters.add(it)
                }

                results.count = filters.size
                results.values = filters
            } else {
                results.count = list.size
                results.values = list
            }
            return results
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            if (p1 != null) {
                eventList = p1.values as MutableList<Events>
                notifyDataSetChanged()
            }
        }

    }
}
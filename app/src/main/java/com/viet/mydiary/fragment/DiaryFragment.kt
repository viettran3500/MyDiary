package com.viet.mydiary.fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.viet.mydiary.DBOpenHelper
import com.viet.mydiary.adapter.EventAdapter
import com.viet.mydiary.Events
import com.viet.mydiary.OnItemClickListener
import com.viet.mydiary.R
import kotlinx.android.synthetic.main.add_newevent_layout.view.*
import kotlinx.android.synthetic.main.add_newevent_layout.view.btnEditDate
import kotlinx.android.synthetic.main.add_newevent_layout.view.btnEditTime
import kotlinx.android.synthetic.main.add_newevent_layout.view.editTextEvenet
import kotlinx.android.synthetic.main.add_newevent_layout.view.editTextTitle
import kotlinx.android.synthetic.main.add_newevent_layout.view.textViewDateAdd
import kotlinx.android.synthetic.main.add_newevent_layout.view.textViewTimeAdd
import kotlinx.android.synthetic.main.edit_event_layout.view.*
import kotlinx.android.synthetic.main.fragment_diary.view.*
import java.text.SimpleDateFormat
import java.util.*


class DiaryFragment(date: Date?) : Fragment(), OnItemClickListener {

    lateinit var adapter: EventAdapter
    lateinit var listEvents : MutableList<Events>
    lateinit var alertDialog: AlertDialog
    lateinit var dbOpenHelper: DBOpenHelper
    var day: Date? = date

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_diary, container, false)

        dbOpenHelper = DBOpenHelper(this.context!!)

        var itemTouchHelperCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    TODO("Not yet implemented")
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    dbOpenHelper.deleteEvent(listEvents[viewHolder.adapterPosition])
                    listEvents.removeAt(viewHolder.adapterPosition)
                    adapter.notifyItemRemoved(viewHolder.adapterPosition)
                }

            }

        loadAllEvents()
        view.recyclerViewEvents.layoutManager = LinearLayoutManager(this.context)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(view.recyclerViewEvents)
        view.recyclerViewEvents.adapter = adapter

        view.editSearch.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                adapter.filter.filter(p0)
            }

        })

        view.fab.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this.context!!)
            builder.setCancelable(true)
            val addView: View =
                LayoutInflater.from(this.context).inflate(R.layout.add_newevent_layout, null)
            val calendar: Calendar = Calendar.getInstance()
            addView.textViewTimeAdd.text = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(calendar.time)
            if(day!=null)
                calendar.time = day
            addView.textViewDateAdd.text = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(calendar.time)
            addView.btnEditTime.setOnClickListener {
                val calendar: Calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val min = calendar.get(Calendar.MINUTE)
                val timePickerDialog: TimePickerDialog =
                    TimePickerDialog(addView.context,
                        TimePickerDialog.OnTimeSetListener { timePicker, hourOfDay, minute ->
                            val c: Calendar = Calendar.getInstance()
                            c.set(Calendar.HOUR_OF_DAY, hourOfDay)
                            c.set(Calendar.MINUTE, minute)
                            c.timeZone = TimeZone.getDefault()
                            val hformate: SimpleDateFormat =
                                SimpleDateFormat("HH:mm", Locale.ENGLISH)
                            val eventTime: String = hformate.format(c.time)
                            addView.textViewTimeAdd.text = eventTime
                        }, hour, min, true
                    )
                timePickerDialog.show()
            }
            addView.btnEditDate.setOnClickListener {
                val calendar: Calendar = Calendar.getInstance()
                if(day!=null){
                    calendar.time = day
                }
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val dayy = calendar.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(addView.context,
                DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
                    val c: Calendar = Calendar.getInstance()
                    c.set(Calendar.DAY_OF_MONTH, i3)
                    c.set(Calendar.MONTH, i2)
                    c.set(Calendar.YEAR, i)
                    val hformate: SimpleDateFormat =
                        SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                    addView.textViewDateAdd.text = hformate.format(c.time)
                },year,month,dayy)
                datePickerDialog.show()
            }
            addView.btnAddEvent.setOnClickListener {
                if (addView.editTextTitle.text.isEmpty() || addView.editTextEvenet.text.isEmpty())
                    Toast.makeText(this.context, "Please enter enough", Toast.LENGTH_SHORT).show()
                else {
                    val events = Events(
                        addView.editTextTitle.text.toString(),
                        addView.editTextEvenet.text.toString(),
                        textToDate(addView.textViewDateAdd.text.toString() +" "+ addView.textViewTimeAdd.text.toString())
                    )
                    saveEvent(events)
                    alertDialog.cancel()
                }
            }

            builder.setView(addView)
            alertDialog = builder.create()
            alertDialog.show()
        }
        return view
    }

    override fun onItemClick(position: Int) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this.context!!)
        builder.setCancelable(true)
        val addView: View =
            LayoutInflater.from(this.context).inflate(R.layout.edit_event_layout, null)

        var e : Events = listEvents[position]
        addView.editTextTitle1.setText(e.title)
        addView.editTextEvenet1.setText(e.event)

        val c: Calendar = Calendar.getInstance()
        c.time = e.date
        addView.textViewTimeAdd1.text =  SimpleDateFormat("hh:mm").format(e.date)
        addView.textViewDateAdd1.text =  SimpleDateFormat("dd/MM/yyyy").format(e.date)
        addView.btnEditTime1.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val min = calendar.get(Calendar.MINUTE)
            val timePickerDialog: TimePickerDialog =
                TimePickerDialog(addView.context,
                    TimePickerDialog.OnTimeSetListener { timePicker, hourOfDay, minute ->
                        val c: Calendar = Calendar.getInstance()
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        c.set(Calendar.MINUTE, minute)
                        c.timeZone = TimeZone.getDefault()
                        val hformate: SimpleDateFormat =
                            SimpleDateFormat("HH:mm", Locale.ENGLISH)
                        val eventTime: String = hformate.format(c.time)
                        addView.textViewTimeAdd1.text = eventTime
                    }, hour, min, true
                )
            timePickerDialog.show()
        }
        addView.btnEditDate1.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog: DatePickerDialog = DatePickerDialog(addView.context,
                DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
                    val c: Calendar = Calendar.getInstance()
                    c.set(Calendar.DAY_OF_MONTH, i3)
                    c.set(Calendar.MONTH, i2)
                    c.set(Calendar.YEAR, i)
                    val hformate: SimpleDateFormat =
                        SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                    addView.textViewDateAdd1.text = hformate.format(c.time)
                },year,month,day)
            datePickerDialog.show()
        }
        addView.btnEditEvent1.setOnClickListener {
            if(addView.btnEditEvent1.text == "Edit Event"){
                addView.editTextTitle1.isEnabled = true
                addView.editTextEvenet1.isEnabled = true
                addView.btnEditDate1.isEnabled = true
                addView.btnEditTime1.isEnabled = true
                addView.btnEditEvent1.text = "Save"
            }
            else if(addView.btnEditEvent1.text == "Save"){
                if (addView.editTextTitle1.text.isEmpty() || addView.editTextEvenet1.text.isEmpty())
                    Toast.makeText(this.context, "Please enter enough", Toast.LENGTH_SHORT).show()
                else {
                    val events = Events(
                        e.id,
                        addView.editTextTitle1.text.toString(),
                        addView.editTextEvenet1.text.toString(),
                        textToDate(addView.textViewDateAdd1.text.toString() +" "+ addView.textViewTimeAdd1.text.toString())
                    )
                    editEvent(events, position)
                    alertDialog.cancel()
                }
            }
        }

        builder.setView(addView)
        alertDialog = builder.create()
        alertDialog.show()
    }

    fun editEvent(event: Events, position: Int){
        dbOpenHelper.updateEvent(event)
        listEvents[position] = event
        listEvents.sort()
        adapter.notifyDataSetChanged()
        Toast.makeText(this.context, "Event Saved", Toast.LENGTH_SHORT).show()
    }

    private fun loadAllEvents(){
        listEvents = dbOpenHelper.readEvents(day)
        listEvents.sort()
        adapter = EventAdapter(listEvents, this)
    }

    private fun saveEvent(event: Events) {
        dbOpenHelper.saveEvent(event)
        val list = dbOpenHelper.readEvents(day)
        listEvents.add(list[list.size-1])
        listEvents.sort()
        adapter.notifyDataSetChanged()
        Toast.makeText(this.context, "Event Saved", Toast.LENGTH_SHORT).show()
    }

    fun textToDate(text: String): Date{
        return SimpleDateFormat("dd/MM/yyyy hh:mm").parse(text)
    }

}
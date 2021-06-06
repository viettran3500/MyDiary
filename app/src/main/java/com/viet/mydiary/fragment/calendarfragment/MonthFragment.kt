package com.viet.mydiary.fragment.calendarfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.viet.mydiary.*
import com.viet.mydiary.database.DBOpenHelper
import com.viet.mydiary.fragment.calendarfragment.adapter.CalendarAdapter
import com.viet.mydiary.model.Events
import com.viet.mydiary.ui.MainActivity
import com.viet.mydiary.utils.index
import com.viet.mydiary.utils.startFragment
import kotlinx.android.synthetic.main.fragment_two.view.*
import java.util.*

class MonthFragment(var position: Int) : Fragment() {
    private lateinit var mainActivity: MainActivity
    private val MAX_DAY = 50

    var dates: MutableList<Date> = mutableListOf()
    var calendarAdapter: CalendarAdapter? = null

    lateinit var listEvents: MutableList<Events>
    lateinit var dbOpenHelper: DBOpenHelper
    var dateEventList: MutableList<Date> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_two, container, false)
        mainActivity = activity as MainActivity

        dbOpenHelper = DBOpenHelper(this.context!!)
        listEvents = dbOpenHelper.allEvent()

        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, position - startFragment)

        dates.clear()
        val monthCalendar: Calendar = calendar.clone() as Calendar
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1)
        val firstDayOfMonth: Int =
            monthCalendar.get(Calendar.DAY_OF_WEEK) - index

        monthCalendar.add(Calendar.DAY_OF_MONTH, -firstDayOfMonth)
        if (monthCalendar.get(Calendar.DAY_OF_MONTH) > 7)
            monthCalendar.add(Calendar.DAY_OF_MONTH, -7)
        else
            monthCalendar.add(Calendar.DAY_OF_MONTH, -14)

        while (dates.size < MAX_DAY - 1) {
            dates.add(monthCalendar.time)
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        dates.add(calendar.time)

        listEvents.forEach {
            var c: Calendar = Calendar.getInstance()
            c.time = it.date
            if (c.get(Calendar.MONTH) == calendar.get(Calendar.MONTH))
                dateEventList.add(it.date)
        }

        view.rcvCalendar.layoutManager = GridLayoutManager(this.context, 7)
        calendarAdapter = CalendarAdapter(dates, dateEventList, mainActivity)
        view.rcvCalendar.adapter = calendarAdapter

        calendarAdapter!!.notifyDataSetChanged()
        return view
    }

}
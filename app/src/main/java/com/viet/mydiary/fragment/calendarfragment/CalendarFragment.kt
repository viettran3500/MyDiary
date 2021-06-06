package com.viet.mydiary.fragment.calendarfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.viet.mydiary.*
import com.viet.mydiary.fragment.calendarfragment.adapter.ViewPagerAdapter
import com.viet.mydiary.ui.MainActivity
import com.viet.mydiary.utils.index
import com.viet.mydiary.utils.startFragment
import kotlinx.android.synthetic.main.fragment_calendar.view.*
import java.text.SimpleDateFormat
import java.util.*

class CalendarFragment : Fragment() {
    private lateinit var mainActivity: MainActivity
    lateinit var adapter: ViewPagerAdapter
    var list: MutableList<String> = mutableListOf()
    private var dateFormat: SimpleDateFormat = SimpleDateFormat("MMMM yyyy")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_calendar, container, false)

        mainActivity = activity as MainActivity
        list.add("Sun")
        list.add("Mon")
        list.add("Tue")
        list.add("Wed")
        list.add("Thu")
        list.add("Fri")
        list.add("Sat")
        val sAdapter =
            ArrayAdapter(this.context!!, android.R.layout.simple_spinner_dropdown_item, list)
        view.spinner.adapter = sAdapter

        adapter = ViewPagerAdapter(
            mainActivity.supportFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )

        view.viewPager.adapter = adapter

        view.viewPager.currentItem = startFragment
        view.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val i = view.viewPager.currentItem
                index = p2
                view.viewPager.adapter = adapter
                view.viewPager.currentItem = i
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        val calendar: Calendar = Calendar.getInstance()
        val monthYear: String = dateFormat.format(calendar.time)
        view.tvMonthYear.text = monthYear

        view.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                val calendar: Calendar = Calendar.getInstance()
                calendar.add(Calendar.MONTH, position - startFragment)
                val monthYear: String = dateFormat.format(calendar.time)
                view.tvMonthYear.text = monthYear
            }

        })

        return view
    }

}
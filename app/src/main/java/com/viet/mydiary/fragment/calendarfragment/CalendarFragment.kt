package com.viet.mydiary.fragment.calendarfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.viet.mydiary.*
import com.viet.mydiary.fragment.DiaryFragment
import com.viet.mydiary.fragment.calendarfragment.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_calendar.view.*

class CalendarFragment : Fragment(){
    private lateinit var mainActivity: MainActivity
    lateinit var adapter: ViewPagerAdapter
    var list: MutableList<String> = mutableListOf()

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
        val sAdapter = ArrayAdapter(this.context!!, android.R.layout.simple_spinner_dropdown_item, list)
        view.spinner.adapter = sAdapter

        adapter = ViewPagerAdapter(mainActivity.supportFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)

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

        return view
    }

}
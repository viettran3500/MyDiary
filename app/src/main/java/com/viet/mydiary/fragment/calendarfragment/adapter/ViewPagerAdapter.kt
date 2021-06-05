package com.viet.mydiary.fragment.calendarfragment.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.viet.mydiary.fragment.calendarfragment.*
import java.util.*
import kotlin.math.abs

class ViewPagerAdapter(fm: FragmentManager, behavior: Int) :
    FragmentStatePagerAdapter(fm, behavior) {

    private var listFragment: MutableList<MonthFragment> = mutableListOf()
    private var listPosition: MutableList<Int> = mutableListOf()

    override fun getCount(): Int {
        return maxFragment
    }

    override fun getItem(position: Int): Fragment {
        return MonthFragment(position)
//        return if(listPosition.indexOf(position)>=0){
//            fragmentMonth = listFragment[listPosition.indexOf(position)]
//            val i = checkClick()
//            if(i!=-1){
//                val fragment = listFragment[listPosition.indexOf(i)]
//                fragment.calendarAdapter?.notifyDataSetChanged()
//            }
//            fragmentMonth
//        }else{
//            listFragment.add(fragmentMonth)
//            listPosition.add(position)
//            fragmentMonth
//        }
    }

    private fun checkClick(): Int {
        val now : Calendar = Calendar.getInstance()
        if (dayClick != null && lastDayClick != null) {
            val click: Calendar = Calendar.getInstance()
            click.time = dayClick
            val lastClick: Calendar = Calendar.getInstance()
            lastClick.time = lastDayClick
            if (abs(click.get(Calendar.MONTH) - lastClick.get(Calendar.MONTH)) == 1 &&
                click.get(Calendar.YEAR) == lastClick.get(Calendar.YEAR)
            ) {
                val month = lastClick.get(Calendar.MONTH) - now.get(Calendar.MONTH)
                val year = lastClick.get(Calendar.YEAR) - now.get(Calendar.YEAR)

                return startFragment+ month + 12 * year
            }
        }
        return -1
    }
}